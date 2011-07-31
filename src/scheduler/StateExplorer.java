
package scheduler;

import scheduler.components.Room;
import scheduler.components.Component;
import scheduler.components.Instructor;
import scheduler.components.Course;
import scheduler.components.Slot;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import scheduler.components.RoomSize;

/** The class that handles the input of the initial parameters and finds the optimum program based on a beam search algorithm. */
class StateExplorer extends ArrayList<ScheduleCourses> {

    private ArrayList<Course> courses;
    private ArrayList<Instructor> instructors;
    private ArrayList<Room> rooms;
    private ArrayList<Slot> slots;
    private ArrayList<String> semesters;

    /** Default constructor
     */
    public StateExplorer(){
        super();
        courses = new ArrayList<Course>();
        instructors = new ArrayList<Instructor>();
        rooms = new ArrayList<Room>();
        slots = new ArrayList<Slot>();
        semesters = new ArrayList<String>();
    }

    /** Constructor
     * @param roomFile The file containing the info about the rooms.
     * @param instructorFile The file containing the info about the instructors.
     * @param courseFile The file containing the info about the courses.
     * @param slotFile The file containing the info about the slots.
     */
    public StateExplorer(String roomFile, String instructorFile, String courseFile, String slotFile){
        super();
        this.getRoomsFromXML(roomFile, false);
        this.getInstructorsFromXML(instructorFile, false);
        this.getSlotsFromXML(slotFile, false);
        this.getCoursesFromXML(courseFile, false);
         JOptionPane.showMessageDialog(new JFrame(), "Loaded info succesfully.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Returns an ArrayList with the semesters included in the courses given.
     * @param courses The courses from which the semesters will be extracted
     * @return ArrayList with the semesters.
     */
    public ArrayList<String> getSemesters(ArrayList<Course> courses){
        if(courses!=null){
            String temp;
            ArrayList<String> sems = new ArrayList<String>();
            for(int i=0; i<courses.size();i++){
                if(!sems.contains(temp=courses.get(i).getSemester()) )
                               sems.add(temp);
            }
            return sems;
        }
        return null;
    }

    /** Prints the schedule to an html file.
     * @param sc the given schedule that will be printed
     * @param fileName the name of the file that it will be printed to.
     */
     public void printToHtml(ScheduleCourses sc, String fileName) {
        try {
            File file = new File(fileName);
            if(!file.exists()) file.createNewFile();
            if(!file.canWrite()) {
                JOptionPane.showMessageDialog(new JFrame(), "Can't write to file "+fileName, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);
            ArrayList<Course[][]> sems = buildScheduleMatrix(sc);
            out.write("<!DOCTYPE html>\n<html>\n<head>\n<title>Course Schedule</title>\n" +
                    "<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n");
            out.write("<style>*{font-family:Georgia,Arial,sans-serif;}body{font-size:14px;text-align:center;}h2,p{margin:0;}" +
                    "h2{font-size:110%;}caption{font-size:200%;}table{border:5px solid #eee;margin:10px;}td,th{padding:4px 8px;}th{background:#444;color:white;}" +
                    "td{width:18%;text-align:center;}thead th{font-size:120%;}table tr:nth-child(odd) td{background:#f5f5f5;}" +
                    ".course:not(:only-child){padding:3px 5px;border:1px solid #ddd;margin:2px 0;font-size:90%;}</style>\n");
            out.write("</head>\n<body align=\"center\">\n<h1>Πρόγραμμα Μαθημάτων</h1>\n");
            for (int j = 0;j < sems.size();j++) {
                Course[][] sem  = sems.get(j);
                out.write("<div>\n<table border=\"1\"; cellspacing='0'; >");
                out.write("<tr><th colspan=\"6\">" + semesters.get(j).replace("semester", "Semester ") + "</th><tr>");
                out.write("<tr><td width=\"100px\" height=\"40px\"></td>");
                for (int i = 0; i < 6; i++) {
                    out.write("<td width=\"160px\" height=\"40px\">" + Slot.getDay(i) + "</td>");
                }
                out.write("</tr>");
                for (int hour = 0; hour < Slot.HOUR_19_21 +1; hour++) {
                    out.write("<tr>");
                    out.write("<td width=\"100px\" height=\"70px\">" + Slot.getTimeSlot(hour) + "</td>");
                    for (int day = 0; day < Slot.FRIDAY +1; day++) {
                        out.write("<td width=\"160px\" height=\"70px\">");
                        if (sem[day][hour] == null) {
                            out.write("</td>");
                            continue;
                        }
                        out.write(sem[day][hour].getName());
                        out.write("</td>");
                    }
                    out.write("</tr>\n");
                }
                out.write("</table>\n</div>\n");
                out.write("<br/><br/><br/>");
            }
            out.write("</html>\n</body>\n");
            out.close();
            JOptionPane.showMessageDialog(new JFrame(), "Saved .html file succesfully.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
        }
    }

    /** Prints the schedule to an xml file.
     * @param sc the given schedule that will be printed
     * @param fileName the name of the file that it will be printed to.
     */
    public void printToXml(ScheduleCourses sc, String fileName) {
        try {
            File file = new File(fileName);
            if(!file.exists()) file.createNewFile();
            if(!file.canWrite()) {
                JOptionPane.showMessageDialog(new JFrame(), "Can't write to file "+fileName, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n");
            out.write("<scheduledCourses>\n\n");
            ArrayList<Course[][]> sems = buildScheduleMatrix(sc);
            for (int j = 0;j < sems.size();j++) {
                Course[][] sem  = sems.get(j);
                out.write("<semester semesterId=\"" + semesters.get(j) +  "\">\n");
                HashMap<String,Slot> c = new HashMap<String,Slot>();
                for (int day = 0; day < Slot.FRIDAY +1; day++) {
                    for (int hour = 0; hour < Slot.HOUR_19_21 +1; hour++) {
                        if (sem[day][hour] == null) {
                            continue;
                        }
                        if (c.containsKey(sem[day][hour].getName()))
                            out.write("<scheduledCourse courseId=\"" + sem[day][hour].getName() +  "\" slotId1=\"" + c.get(sem[day][hour].getName()).getSlotId() +  "\" slotId2=\"" + slots.get(slots.indexOf(new Slot(day,hour))).getSlotId() + "\"/>\n");
                        else
                            c.put(sem[day][hour].getName(), slots.get(slots.indexOf(new Slot(day,hour))));
                    }
                }
                out.write("</semester>\n\n");
            }
            out.write("</scheduledCourses>\n\n");
            out.close();
            JOptionPane.showMessageDialog(new JFrame(), "Saved .xml file succesfully.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
        }
    }

    /** Prints the schedule to a txt file.
     * @param sc the given schedule that will be printed
     * @param fileName the name of the file that it will be printed to.
     */
    public void printToTxt(ScheduleCourses sc, String fileName) {
        try {
            File file = new File(fileName);
            if(!file.exists()) file.createNewFile();
            if(!file.canWrite()) {
                JOptionPane.showMessageDialog(new JFrame(), "Can't write to file "+fileName, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("CS Department - Time Schedule\n");
            ArrayList<Course[][]> sems = buildScheduleMatrix(sc);
            for (int j = 0;j < sems.size();j++) {
                Course[][] sem  = sems.get(j);
                out.write("\n Semester " + semesters.get(j).charAt(semesters.get(j).length() - 1)  + "\n");
                for (int day = 0; day < Slot.FRIDAY +1; day++) {
                    out.write(Slot.getDay(day) + "\n");
                    for (int hour = 0; hour < Slot.HOUR_19_21 +1; hour++) {
                        if (sem[day][hour] == null) {
                            continue;
                        }
                        out.write(Slot.getTimeSlot(hour) + " (");
                        Room room = new Room(slots.get(slots.indexOf(new Slot(day, hour))).getRoomId());
                        out.write(rooms.get(rooms.indexOf(room))+ ") ");
                        out.write(sem[day][hour].getName() + " (" + instructors.get(instructors.indexOf(
                                new Instructor(sem[day][hour].getInstructorId(), Component.NULL_NAME))).getName() + ")\n");
                    }
                }
            }
            out.close();
            JOptionPane.showMessageDialog(new JFrame(), "Saved .txt file succesfully.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
        }
    }

    /** Builds an ArrayList that contains all the courses according to their slots.*/
    private ArrayList<Course[][]> buildScheduleMatrix(ScheduleCourses sc) {
        ArrayList<Course[][]> sems;
        sems = new ArrayList<Course[][]>();
        sems.add(new Course[Slot.FRIDAY + 1][Slot.HOUR_19_21 + 1]);
        sems.add(new Course[Slot.FRIDAY + 1][Slot.HOUR_19_21 + 1]);
        sems.add(new Course[Slot.FRIDAY + 1][Slot.HOUR_19_21 + 1]);
        sems.add(new Course[Slot.FRIDAY + 1][Slot.HOUR_19_21 + 1]);
        if(sc == null)
                return sems;
        for (int i = 0; i < slots.size(); i++) {
            int day = slots.get(i).getDay();
            int hour = slots.get(i).getTime();
            String sem = sc.get(i).getSemester();
            Course c = sc.get(i); 
            if (sem.equals(semesters.get(0))) {
                (sems.get(0))[day][hour] = c;
            } else if (sem.equals(semesters.get(1))) {
                (sems.get(1))[day][hour] = c;
            } else if (sem.equals(semesters.get(2))) {
                (sems.get(2))[day][hour] = c;
            } else if (sem.equals(semesters.get(3))) {
                (sems.get(3))[day][hour] = c;
            }
        }
        return sems;
    }

    /** Returns the mutated children of the given parent/schedule.
     * @param schedule The parent schedule.
     * @return An ArrayList<ScheduleCourses> with the mutated children of the parent
     */
    public ArrayList<ScheduleCourses> getChildren(ScheduleCourses schedule) {
        ArrayList<ScheduleCourses> children = new ArrayList<ScheduleCourses>();

        /*Produce 4 children each time*/
        for (int i = 0; i < 4; i++) {
            children.add(this.mutateSchedule(schedule)); 
        }

        return children;
    }

    /** Mutates a schedule so that it produces an altered offspring/schedule. Each mutation is basically a simple course swap between to time slots.
     * @param s The initial schedule to be mutated (the parent).
     * @return The mutated schedule.
     */
    public ScheduleCourses mutateSchedule(ScheduleCourses s){
        ScheduleCourses schedule = new ScheduleCourses(s);
        Random rand = new Random();
        int a, b;
        a = rand.nextInt(schedule.size()-1);
        for (int i = 0; i < schedule.size(); i++) {
            if(!schedule.get(i).isNull()){  //if one of the limitations 2,4 or 6 is broken then replace the course that breaks it with another random one.
                if ( ( schedule.get(a).getInstructorId() == schedule.get(i).getInstructorId() ||
                       schedule.get(i).getSemester().equalsIgnoreCase(schedule.get(a).getSemester()) ||
                       schedule.get(i).getId().equalsIgnoreCase(schedule.get(a).getId()))
                        && slots.get(a).getDay() == slots.get(i).getDay()
                        && slots.get(a).getTime()== slots.get(i).getTime() ) {
                    b = rand.nextInt(schedule.size()-1);
                    Collections.swap(schedule, a, b);
                }
            }
        }
        schedule.setHeuristicScore(this.calcHeuristicScore(schedule)); 
        return schedule;
    }

    /** Calculates the heuristic score of the given schedule.
     * @param schedule The schedule whose heuristic score is to be calculated
     * @return The heuristic score of the given schedule.
     */
    public int calcHeuristicScore(ScheduleCourses schedule) {
        schedule.setHeuristicScore(0);

        //Limitation 1 (2 courses in the same slot) can't happen because of the way we correspond courses/slots.
        //Limitation 3 (every course must be taught twice in a week) can't happen because we add each lesson twice to the initial ArrayList.
        
        for (int i = 0; i < schedule.size() - 1; i++) {
            for (int j = schedule.size() - 1; j > i && !schedule.get(i).isNull(); j--) {
                //Limitation 2 (instructor in the same slot with different courses)
                if (!schedule.get(i).isNull() && schedule.get(i).getInstructorId() == schedule.get(j).getInstructorId()
                        && slots.get(i).getDay()== slots.get(j).getDay()
                        && slots.get(i).getTime() == slots.get(j).getTime()) {
                    schedule.setHeuristicScore(schedule.getHeuristicScore() + 6);
                }
                //Limitation 4 (Two courses of the same semester cannot be taught in the same hour/day)
                if (schedule.get(i).getSemester().equalsIgnoreCase(schedule.get(j).getSemester())
                        && slots.get(i).getDay()== slots.get(j).getDay()
                        && slots.get(i).getTime()== slots.get(j).getTime()) {
                    schedule.setHeuristicScore(schedule.getHeuristicScore() + 4);
                }
                //Limitation 6 (every lesson must be taught in two different days in the week)
                if (schedule.get(i).getId().equalsIgnoreCase(schedule.get(j).getId())
                        && slots.get(i).getDay()== slots.get(j).getDay()) {
                    schedule.setHeuristicScore(schedule.getHeuristicScore() + 2);
                }
            }
        }
        
        for (int i = 0; i < schedule.size(); i++) {
            if(!schedule.get(i).isNull()){
                //Limitation 5 (each course must be taught in a room big enough)
                if (schedule.get(i).getRoomSize() == RoomSize.LARGE && slots.get(i).getRoomSize()== RoomSize.MEDIUM ) {
                    schedule.setHeuristicScore(schedule.getHeuristicScore() + 3);
                }
                //Limitation 7 (an instructor must teach only in days he is available)
                if( !instructors.get(instructors.indexOf(new Instructor(schedule.get(i).getInstructorId(), Component.NULL_NAME) ) ).isAvailable(slots.get(i).getDay(),
                        slots.get(i).getTime()) ){
                            schedule.setHeuristicScore(schedule.getHeuristicScore() + 1);
                }
            }
        }
        return schedule.getHeuristicScore();
    }

    /** Prints the given program to the given OutputStream
     * @param schedule */
    public void print(ScheduleCourses schedule, OutputStream stream) {
        String output = ""; int j=0;
        for (int i = 0; i < schedule.size(); i++) {
            while( i < schedule.size() && schedule.get(i).isNull() ) i++;
            if(i < schedule.size()){
                output += "Slot: " + slots.get(j++).toString() + "\nLesson: " + schedule.get(i).toString() + "\n===========\n";
            }
        }
         try {
            stream.write(output.getBytes());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(new JFrame(), "Error, in printing Program to " + stream.toString() + ". ", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

     /**Reads the availability of each room from an .xml file and add the slots in the appropriate LinkedList.
     * @param fileName The name of the .xml file.*/
    public ArrayList<Slot> getSlotsFromXML(String fileName, boolean notify) {
        NodeList list = XMLElements(fileName);
        int day = Slot.NULL_DAY;
        ArrayList<Slot> slots = new ArrayList<Slot>();
        if(rooms == null ||rooms.isEmpty()){
            JOptionPane.showMessageDialog(new JFrame(), "Error in reading" + fileName+".\n Load rooms first!", "Error", JOptionPane.ERROR_MESSAGE);
            this.slots = new ArrayList<Slot>(slots);
            return slots;
        }
        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            //Get current day
            if (element.getNodeName().equals("daySlots")) {
                day = Slot.getDay(element.getAttribute("day"));
            }
            if (element.getNodeName().equals("slot") && !rooms.isEmpty()) {
                String roomId = element.getAttribute("roomId");
                int time = Slot.getTimeSlot(element.getAttribute("startTime"));
                String slotId = element.getAttribute("id");
                RoomSize roomSize = rooms.get(rooms.indexOf(new Room( roomId, Component.NULL_NAME, RoomSize.NULL)) ).getSize() ;
                Slot slot = new Slot( time, day, slotId, roomId, roomSize);
                slots.add(slot); 
            }
        }
        if(notify) JOptionPane.showMessageDialog(new JFrame(), "Loaded slots succesfully.", "info", JOptionPane.INFORMATION_MESSAGE);
        this.slots = new ArrayList<Slot>(slots);
        return slots;
    }

    /** Reads the instructors from an xml file and adds them to the LinkedList.
     * @param fileName The name of the .xml file.*/
    public ArrayList<Instructor> getInstructorsFromXML(String fileName, boolean notify) {
        NodeList list = this.XMLElements(fileName);
        ArrayList<Instructor> instructors = new ArrayList<Instructor>();
        int id = -1;
        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            if (element.getNodeName().equals("instructor")) {
                try{
                    id = Integer.parseInt( element.getAttribute("id"));
                    String name = element.getAttribute("name");
                    instructors.add(new Instructor(id, name));
                }catch(Exception e){ JOptionPane.showMessageDialog(new JFrame(), "Error in the format of " + fileName+ " .", "Error", JOptionPane.ERROR_MESSAGE);}
            }
            if (element.getNodeName().equals("unavailable")) {
                int day = Slot.getDay(element.getAttribute("day"));
                int time = Slot.getTimeSlot(element.getAttribute("startTime"));
                Instructor instr=instructors.get(instructors.indexOf(new Instructor(id, Component.NULL_NAME)));
                if(instr!=null){
                    instr.setAvailable(day, time, false);
                }
            }
        }
        if(notify) JOptionPane.showMessageDialog(new JFrame(), "Loaded instructors succesfully.", "Info", JOptionPane.INFORMATION_MESSAGE);
        this.instructors = new ArrayList<Instructor>(instructors);
        return instructors;
    }

    /** Reads the courses from an xml file and adds them to the LinkedList.
    * @param fileName The name of the .xml file.*/
    public ArrayList<Course> getCoursesFromXML(String fileName, boolean notify) {
        NodeList list = this.XMLElements(fileName);
        String semester = null;
        ArrayList<Course> courses = new ArrayList<Course>();
        if(slots == null || slots.isEmpty()){
            JOptionPane.showMessageDialog(new JFrame(), "Error in reading" + fileName+".\n Load slots first!", "Error", JOptionPane.ERROR_MESSAGE);
            this.courses = new ArrayList<Course>(courses);
            return courses;
        }
        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            if (element.getNodeName().equals("semester")) {
                semester = element.getAttribute("id");
            }
            if (element.getNodeName().equals("course")) {
                try{
                    String name = element.getAttribute("name");
                    String id = element.getAttribute("id");
                    courses.add(new Course(id, name, semester, Integer.parseInt( element.getAttribute("instructorId")) ,
                               Room.getRoomSize(element.getAttribute("size") )  ));
                }catch(Exception e){ JOptionPane.showMessageDialog(new JFrame(), "Error in the format of " + fileName+ " .", "Error", JOptionPane.ERROR_MESSAGE);}
            }
        }
        while(courses.size()*2 < slots.size())
            courses.add(new Course());
        this.courses = new ArrayList<Course>(courses);
        this.semesters = this.getSemesters(courses);
        if(notify) JOptionPane.showMessageDialog(new JFrame(), "Loaded courses succesfully.", "Info", JOptionPane.INFORMATION_MESSAGE);
        return courses;
    }

    /** Reads the rooms from an xml file and adds them to the LinkedList.
     * @param fileName The name of the .xml file.*/
    public ArrayList<Room> getRoomsFromXML(String fileName, boolean notify) {
        NodeList list = this.XMLElements(fileName);
        ArrayList<Room> rooms = new ArrayList<Room>();
        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            if (element.getNodeName().equals("room")) {
                try{
                    String name = element.getAttribute("name");
                    String id = element.getAttribute("id");
                    String size = element.getAttribute("size");
                    rooms.add(new Room(id, name, Room.getRoomSize( size)  ));
                }catch(Exception e){ JOptionPane.showMessageDialog(new JFrame(), "Error in the format of " + fileName+ " .", "Error", JOptionPane.ERROR_MESSAGE);}
            }
        }
        if(notify) JOptionPane.showMessageDialog(new JFrame(), "Loaded rooms succesfully.", "Info", JOptionPane.INFORMATION_MESSAGE);
        this.rooms = new ArrayList<Room>(rooms);
        return rooms;
    }

    /** Reads the Elements of an xml file and returns them as a Nodelist of Elements.
     * @param fileName The name of the .xml file.*/
    public NodeList XMLElements(String fileName) {
        try {
            File xml = new File(fileName);
            if (xml.exists() && xml.canRead() ) {
                // Create a factory
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                // Use the factory to create a builder
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(fileName);
                // Get a list of all elements in the document
                return doc.getElementsByTagName("*");
            } else {
                JOptionPane.showMessageDialog(new JFrame(),"File "+fileName +" not found or cannot be read!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(),"Error in reading " + fileName+ ".", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    /** Performs the beam search algorithm so that it finds the optimal schedule of courses.
     * @return The optimal schedule.
     */
    public ScheduleCourses beamSearch() {
        this.clear();
        if(courses.isEmpty() || instructors.isEmpty() || rooms.isEmpty() || slots.isEmpty()){
                JOptionPane.showMessageDialog(new JFrame(),"Please load all necessary files first.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return null;
        }
        
        ScheduleCourses initialChild = new ScheduleCourses(courses);

        initialChild.setHeuristicScore(this.calcHeuristicScore(initialChild)) ;
        this.add(initialChild);
        ScheduleCourses child1, child2, child3 ;

        this.addAll( this.getChildren(initialChild));

        Collections.sort(this);
        Collections.reverse(this);

        while(this.size() > 0 ) {
                // Take the 3 best children and if one of those is terminal return it
                child1 = this.get(0);
                if (child1.isTerminal()) return child1;
                child2 = this.get(1);
                if (child2.isTerminal()) return child2;
                child3 = this.get(2);
                if (child3.isTerminal()) return child3;
                //Remove all the other children.
                for (int i=3; i < this.size(); i++){
                    this.remove(i);
                }
                //Produce the next set of children from the 3 best
                this.addAll(this.getChildren(child1));
                this.addAll(this.getChildren(child2));
                this.addAll(this.getChildren(child3));
                //Sort them again
                Collections.sort(this);
                Collections.reverse(this);
        }

        return null;

    }

    /** Returns a proper label with the given text.*/
    private JLabel getLabel(final Course course, final Slot slot, String labelText, Border border, Color color){
        JLabel label;
        if(labelText.length()==0)
             label = new JLabel();
        else
            label = new JLabel(labelText);
        if (course != null && slot != null) {
            label.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    getInfoFrame(course, slot);
                }
                public void mousePressed(MouseEvent e) {}
                public void mouseReleased(MouseEvent e) {}
                public void mouseEntered(MouseEvent e) {}
                public void mouseExited(MouseEvent e) {}
            });
        }
        label.setVerticalTextPosition(JLabel.CENTER);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setForeground(color);
        label.setBorder(border); // new SoftBevelBorder(SoftBevelBorder.RAISED)
        return label;
    }

    /** Prints the given schedule to a JPanel and returns it.*/
    public JPanel printToPanel(ScheduleCourses sc){
        JPanel panel = new JPanel();
        //panel.setSize(500,500);
        panel.setBackground(Color.WHITE);
        panel.setLayout( new GridLayout(0,6));
        ArrayList<Course[][]> sems = buildScheduleMatrix(sc);
        for (int j = 0;j < sems.size();j++) {
            if(sc == null)
                panel.add(this.getLabel(null, null, "", LineBorder.createGrayLineBorder(), Color.GRAY));
            else
                panel.add(this.getLabel(null, null, semesters.get(j), LineBorder.createGrayLineBorder(), Color.decode("#333366")));
                Course[][] sem  = sems.get(j);

                for (int i = 0; i < Slot.FRIDAY +1; i++) {
                    if(sc == null)
                        panel.add(this.getLabel(null, null, "", LineBorder.createGrayLineBorder(), Color.GRAY));
                    else
                        panel.add(this.getLabel(null, null, Slot.getDay(i), new SoftBevelBorder(SoftBevelBorder.RAISED), Color.decode("#330066")));
                }

                for (int hour = 0; hour < Slot.HOUR_19_21 +1 ; hour++) {
                    if(sc == null) 
                        panel.add(this.getLabel(null, null, "", LineBorder.createGrayLineBorder(), Color.GRAY));
                    else
                        panel.add(this.getLabel(null, null, Slot.getTimeSlot(hour), new SoftBevelBorder(SoftBevelBorder.RAISED), Color.decode("#330066")));
                    for (int day = 0; day < Slot.FRIDAY +1 ; day++) {
                        if (sem[day][hour] == null) {
                            panel.add(this.getLabel(null, null, "", LineBorder.createGrayLineBorder(), Color.GRAY));
                            continue;
                        }
                        if(sc == null)
                            panel.add(this.getLabel(null, null, "", LineBorder.createGrayLineBorder(), Color.GRAY));
                        else
                            panel.add(this.getLabel(sem[day][hour], slots.get(slots.indexOf(new Slot(day, hour))) ,sem[day][hour].getName(), new SoftBevelBorder(SoftBevelBorder.RAISED), Color.decode("#000099")));
                    }
                }
            }
        return panel;
    }

    /** Returns a JFrame that presents extra  info about the given course.*/
    private JFrame getInfoFrame(Course c, Slot slot) {
        JFrame window = new JFrame("Info");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        java.net.URL imgURL = getClass().getResource("images/graph.png");
        if (imgURL != null) {
            window.setIconImage(new ImageIcon(imgURL, "Schedule").getImage());
        }
        window.setLayout(new BorderLayout(3, 3));
        TextArea textArea = new TextArea(); 
        textArea.setEditable(false);

        textArea.setText("Μάθημα: " + c.getName() + "\n"+
                textArea.getText() + "Εξάμηνο: " + c.getSemester()+ "\n"+ textArea.getText() +
                "Διδάσκων: " +  instructors.get(instructors.indexOf( new Instructor( c.getInstructorId(), Component.NULL_NAME) )).getName()+ "\n" +
                "Αιθουσα: " + rooms.get(rooms.indexOf(new Room(slot.getRoomId())) ) );
        window.getContentPane().add(textArea, BorderLayout.SOUTH);
        window.setVisible(true);
        window.setBounds(150, 150, 150, 170);
        window.setResizable(false);
        window.pack();
        return window;
    }

}

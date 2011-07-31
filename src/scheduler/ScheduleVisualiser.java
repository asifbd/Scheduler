
package scheduler;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class ScheduleVisualiser {

    /** The gui's components (frame and tabbedpanel)*/
    private JPanel panel;
    private JFrame frame;
    StateExplorer stateExplorer;
    ScheduleCourses finalSchedule;

    public ScheduleVisualiser(){
        stateExplorer = new StateExplorer();
        //Frame setup
        frame= createFrame();
    }

    /**Creates/sets the ToolBar */
    private JToolBar createToolBar(){
        JToolBar toolBar=new JToolBar();
        //Load defaults Menu Item
        JButton loadDefaults= new JButton("Load defaults", createImageIcon("images/open.png", "Load defaults") );
        loadDefaults.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(e.getModifiers()!=4 && e.getModifiers()!=8 && e.getModifiers()!=0){//if it's not because of a right/middle click or a keyboard action key
                    loadDefaults();
                }
            }

            private void loadDefaults() {
                stateExplorer.getRoomsFromXML("rooms.xml", false);
                stateExplorer.getInstructorsFromXML("instructors.xml", false);
                stateExplorer.getSlotsFromXML("slots.xml", false);
                stateExplorer.getCoursesFromXML("courses.xml", false);
                JOptionPane.showMessageDialog(new JFrame(), "Loaded default data files succesfully.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        toolBar.add(loadDefaults);
        //Open Button
        JButton calculate= new JButton("Calculate Schedule", createImageIcon("images/execute.png", "Calculate Schedule") );
        calculate.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {}
            public void mousePressed(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON1)
                    calculateSchedule();
            }
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        toolBar.add(calculate);

        toolBar.setSize(350, 10);
        toolBar.setBorderPainted(true);
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        return toolBar;
    }

    /**Saves the schedule to the target file*/
    private boolean save(File file, String ext) {
        if(!FileChooserFilter.isAcceptableExtension(ext)) return false;
        if(file != null) {
            try {
                if(!file.exists())
                    file.createNewFile();
                if(ext.equalsIgnoreCase(FileChooserFilter.html))
                    stateExplorer.printToHtml(finalSchedule, file.getAbsolutePath());
                else if(ext.equalsIgnoreCase(FileChooserFilter.xml))
                    stateExplorer.printToXml(finalSchedule, file.getAbsolutePath() );
                else if(ext.equalsIgnoreCase(FileChooserFilter.txt))
                    stateExplorer.printToTxt(finalSchedule, file.getAbsolutePath() );
                else
                    JOptionPane.showMessageDialog(frame, "Invalid file extension. ", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error in writing file " + file.getName() + ".\n ", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return true;
    }

    /**Creates/sets the Edit menu and its items*/
    private JMenu createEditMenu(){
        JMenu edit=new JMenu("Edit", true);
        //Find SCC Menu Item
        JMenuItem calcSch= new JMenuItem("Calculate Schedule", createImageIcon("images/execute.png", "Calculate Schedule") );
        calcSch.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                    if(e.getModifiers()!=4 && e.getModifiers()!=8 && e.getModifiers()!=0){ //if it's not because of a right/middle click or a keyboard action key
                        calculateSchedule();
                    }
            }
        });
        calcSch.setMnemonic('H');
        calcSch.setAccelerator(KeyStroke.getKeyStroke("ctrl H"));
        edit.add(calcSch);
        return edit;
    }

    /**Creates/sets the File menu and its items*/
    private JMenu createFileMenu(){
        JMenu file= new JMenu("File", true);
        //Load defaults Menu Item
        JMenuItem loadDefaults= new JMenuItem("Load defaults", createImageIcon("images/open.png", "Load defaults") );
        loadDefaults.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(e.getModifiers()!=4 && e.getModifiers()!=8 && e.getModifiers()!=0){//if it's not because of a right/middle click or a keyboard action key
                    loadDefaults();
                }
            }

            private void loadDefaults() {
                stateExplorer.getRoomsFromXML("rooms.xml", false);
                stateExplorer.getInstructorsFromXML("instructors.xml", false);
                stateExplorer.getSlotsFromXML("slots.xml", false);
                stateExplorer.getCoursesFromXML("courses.xml", false);
                JOptionPane.showMessageDialog(new JFrame(), "Loaded default data files succesfully.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        loadDefaults.setMnemonic('D');
        loadDefaults.setAccelerator(KeyStroke.getKeyStroke("ctrl O D"));
        loadDefaults.setSize(35, 15);
        file.add(loadDefaults);
        //Load rooms Menu Item
        JMenuItem loadRooms= new JMenuItem("Load rooms", createImageIcon("images/open.png", "Load rooms") );
        loadRooms.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(e.getModifiers()!=4 && e.getModifiers()!=8 && e.getModifiers()!=0){//if it's not because of a right/middle click or a keyboard action key
                    loadRooms();
                }
            }

            private void loadRooms() {
                File file = chooseFile("Load Rooms", ".xml");
                if (file != null )
                    stateExplorer.getRoomsFromXML(file.getAbsolutePath(), true);
            }
        });
        loadRooms.setMnemonic('R');
        loadRooms.setAccelerator(KeyStroke.getKeyStroke("ctrl O R"));
        loadRooms.setSize(35, 15);
        file.add(loadRooms);
        //Load instructors Menu Item
        JMenuItem loadInstructors= new JMenuItem("Load instructors", createImageIcon("images/open.png", "Load Instructors") );
        loadInstructors.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(e.getModifiers()!=4 && e.getModifiers()!=8 && e.getModifiers()!=0){//if it's not because of a right/middle click or a keyboard action key
                    loadInstructors();
                }
            }

            private void loadInstructors() {
                File file = chooseFile("Load Instructors", ".xml");
                if (file != null )
                    stateExplorer.getInstructorsFromXML(file.getAbsolutePath(), true);
            }
        });
        loadInstructors.setMnemonic('I');
        loadInstructors.setAccelerator(KeyStroke.getKeyStroke("ctrl O I"));
        loadInstructors.setSize(35, 15);
        file.add(loadInstructors);
        //Load slots Menu Item
        JMenuItem loadSlots= new JMenuItem("Load slots", createImageIcon("images/open.png", "Load slots") );
        loadSlots.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(e.getModifiers()!=4 && e.getModifiers()!=8 && e.getModifiers()!=0){//if it's not because of a right/middle click or a keyboard action key
                    loadSlots();
                }
            }

            private void loadSlots() {
                File file = chooseFile("Load Slots", ".xml");
                if (file != null )
                    stateExplorer.getSlotsFromXML(file.getAbsolutePath(), true);
            }
        });
        loadSlots.setMnemonic('L');
        loadSlots.setAccelerator(KeyStroke.getKeyStroke("ctrl O L"));
        loadSlots.setSize(35, 15);
        file.add(loadSlots);
        //Load courses Menu Item
        JMenuItem loadCourses= new JMenuItem("Load courses", createImageIcon("images/open.png", "Load courses") );
        loadCourses.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(e.getModifiers()!=4 && e.getModifiers()!=8 && e.getModifiers()!=0){//if it's not because of a right/middle click or a keyboard action key
                    loadCourses();
                }
            }

            private void loadCourses() {
                File file = chooseFile("Load Courses", ".xml");
                if (file != null )
                    stateExplorer.getCoursesFromXML(file.getAbsolutePath(), true);
            }
        });
        loadCourses.setMnemonic('C');
        loadCourses.setAccelerator(KeyStroke.getKeyStroke("ctrl O C"));
        loadCourses.setSize(35, 15);
        file.add(loadCourses);
        //Save Menu Item
        JMenuItem save= new JMenuItem("Save", createImageIcon("images/save.png", "Save") );
        save.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(e.getModifiers()!=4 && e.getModifiers()!=8 && e.getModifiers()!=0){//if it's not because of a right/middle click or a keyboard action key
                     save(new File("schedule.xml"), "xml");
                     save(new File("table.txt"), "txt");
                }
            }
        });
        save.setMnemonic('S');
        save.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        file.add(save);
        //Save As Menu Item
        JMenuItem saveas= new JMenuItem("Save as..", createImageIcon("images/saveas.png", "Save-As") );
        saveas.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {}
            public void mousePressed(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON1){
                     File file =chooseFile("Save", ".txt, .xml, .html");
                     if(file!=null)
                          save(file, FileChooserFilter.getExtension(file));
                }
            }
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        file.add(saveas);
        //Quit Menu Item
        JMenuItem quit= new JMenuItem("Quit", createImageIcon("images/quit.png", "Quit") );
        quit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(e.getModifiers()!=4 && e.getModifiers()!=8 && e.getModifiers()!=0)//if it's not because of a right/middle click or a keyboard action key
                    System.exit(0);
            }
        });
        quit.setMnemonic('Q');
        quit.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        file.add(quit);
        return file;
    }

    /** Calculates a new schedule and refreshes the panel.*/
    public void calculateSchedule() {
        try { /* Make a new SwingWorker in another thread to calculate the new schedule and when it's done refresh the gui.*/
            SwingWorker worker = new SwingWorker<Void, Void>() {
                @Override
                    protected Void doInBackground() throws Exception {
                            finalSchedule = stateExplorer.beamSearch();
                            return null;
                    }
                @Override
                     public void done() {
                            frame.getContentPane().remove(panel);
                            panel = stateExplorer.printToPanel(finalSchedule);
                            frame.getContentPane().add(panel, BorderLayout.CENTER);
                            frame.setSize(frame.getWidth() + 1, frame.getHeight() + 1);
                            frame.setSize(frame.getWidth() - 1, frame.getHeight() - 1);
                            frame.repaint();
                     }
            };
            worker.execute();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error in schedule calculation.\n ", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**Creates/sets the MenuBar*/
    private JMenuBar createMenu(){
        JMenuBar menu=new JMenuBar();
        JMenu file= createFileMenu();
        menu.add(file);
        JMenu edit = createEditMenu();
        menu.add(edit);

        return menu;
    }
    
    /**Creates/sets the frame of the window */
    private JFrame createFrame(){
        JFrame frame = new JFrame("Schedule Maker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.WHITE);
        java.net.URL imgURL = getClass().getResource("images/graph.png");
        if (imgURL != null)
            frame.setIconImage(new ImageIcon(imgURL, "Schedule").getImage());
        frame.setBounds(100, 100, 700, 730);
        frame.setBackground(Color.WHITE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        frame = fillFrame(frame);
        return frame;
    }

    /**Fils the frame of the window with components (menubar, toolbar, tabbedpanel*/
    private JFrame fillFrame(JFrame frame){
        if (frame != null) {
            //MenuBar setup
            JMenuBar menu = createMenu();
            //Add menubar to the frame
            frame.setJMenuBar(menu);
            //Tabbed Panel setup
            panel = stateExplorer.printToPanel(finalSchedule);
            //ToolBar
            JToolBar toolBar = createToolBar();
            //Add the Tabbed Panel and the Toolbar to the frame
            frame.getContentPane().add(toolBar, BorderLayout.NORTH);
            frame.getContentPane().add(panel, BorderLayout.CENTER);
            frame.setSize(frame.getWidth() + 1, frame.getHeight() + 1);
            frame.setSize(frame.getWidth() - 1, frame.getHeight() - 1);
            frame.repaint();
        }
        return frame;
    }

    /** Returns an ImageIcon, or null if the path was invalid.
     * @param path
     * @param description
     * @return the created imageicon or null if it wasn't created succesfully
     */
    public static ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = StateExplorer.class.getResource(path);
        if(imgURL != null){
            return new ImageIcon(imgURL , description);
        }
        return null;
    }

    /**Creates a window so that the user can choose a file for saving/loading and
     * then it returns that file
     * @param action
     * @param filterDescription 
     * @return  the file selected by the user or null if none was selected
     */
    public static File chooseFile(String action, String filterDescription){
        JFileChooser fileChooser=new JFileChooser("Choose a file to...");
        //Add a custom file filter and disable the default
        fileChooser.addChoosableFileFilter(new FileChooserFilter(filterDescription));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDragEnabled(true);
        File temp=null;
        if (fileChooser.showDialog(new JFrame(), action) == JFileChooser.APPROVE_OPTION) {
                temp = fileChooser.getSelectedFile() ;
        }
        return temp;
    }
}

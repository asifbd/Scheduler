
package scheduler.components;

/**  Class representing a course for the schedule. */
public class Course extends Component {

    /** Basic elements*/
    private String id;
    private String name;
    private String semester;
    private int instructorId;
    private RoomSize roomSize;

    /**Default constructor.*/
    public Course(){
        this(Course.NULL_NAME, Course.NULL_NAME, Course.NULL_NAME, Course.NULL_ID, RoomSize.NULL);
    }

    /** Constructor.
     * @param id The id of the course.
     * @param name 
     * @param semester The semester in which the course will be taught.
     * @param instructorId The id of the course's instructor.
     * @param roomSize The size of the room required for the course.
     */
    public Course(String id,String name,String semester,int instructorId,RoomSize roomSize) {
        this.setId(id);
        this.setName(name);
        this.setSemester(semester);
        this.setInstructorId(instructorId);
        this.setRoomSize(roomSize);
    }

    public void setId(String id){ this.id = id; }
    public String getId() { return this.id; }

    public void setName(String name){ this.name = name; }
    public String getName() { return this.name; }

    public void setSemester(String semester){ this.semester = semester; }
    public String getSemester() { return this.semester; }

    public void setInstructorId(int instructorId){ this.instructorId = instructorId; }
    public int getInstructorId() { return this.instructorId; }

    public void setRoomSize(RoomSize roomSize){ if(Room.isValidRoomSize(roomSize)) this.roomSize = roomSize; }
    public RoomSize getRoomSize() { return this.roomSize; }

    /** Returns true if the course can fit to the room.
     * @param room The room that will be checked to see if the course can fit there.
     * @return false if (roomSize == LARGE && room.getSize() == MEDIUM), true otherwise
     */
    public boolean fits(Room room){
        if(roomSize == RoomSize.LARGE && room.getSize() == RoomSize.MEDIUM)  return false;
        return true;
    }

    /** Returns true if the course can fit exactly to the room (medium room and medium course or large room and large course).
     * @param room The room that will be checked to see if the course can fit there.
     * @return true if the room sizes are equal, false otherwise
     */
    public boolean fitsExactly(Room room){
        if(roomSize ==  room.getSize())  return true;
        return false;
    }

    /**Returns true if the teacher provided in the arguments teaches the class
     *@param teacher The instructor of the class.
     * @return  true if the teacher provided in the arguments teaches the class, false otherwise
     */
    public boolean isTeachedBy(Instructor teacher){
        return (teacher.getId() == this.instructorId);
    }

    /**Generates the hashcode of each instance.*/
    @Override public int hashCode() {
        int hashcode = 0;
        for(int i=0; i< id.length(); i++)
                hashcode += id.charAt(i);
        return 47 + 5 * hashcode;
    }

    /** Compares the course's id with c's and returns true if they are equal.
     * @param o 
     */
    @Override public boolean equals(Object o) {
        if (o == null)  // Null references are not equal to this instance.
            return false;
        if (!o.getClass ().equals (Course.class)) //Neither are they instances of different classes
            return false;
        return (((Course) o).getId().equalsIgnoreCase(this.id));
    }

    /** Returns the course's id and name as a string. */
    @Override public String toString(){
        return this.id + " " + this.name ;
    }

    /**Returns true if the given elements id  is "Null".*/
    public boolean isNull(){
        return this.getId().equalsIgnoreCase(Course.NULL_NAME);
    }

}

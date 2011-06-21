
package scheduler.components;

/**  Class representing an Instructor. */
public class Instructor extends Component{

    private int id;
    private String name;

    /** Represents the availability of the room in each [time slot] [day]. True means available, false mean's not available.*/
    private boolean available[][];

    /**Default Constructor.*/
    public Instructor(){
        this(Instructor.NULL_ID, Instructor.NULL_NAME);
    }

    /** Constructor.
     * @param id The id of the instructor.
     * @param name The instructor's name.
     */
    public Instructor(int id, String name) {
        this.setId(id);
        this.setName(name);
        //6 time slots per day, 5 days. (+1 because the constants start from 0)
        available = new boolean[Slot.HOUR_19_21+1][Slot.FRIDAY+1];
        for (int i = 0; i <=Slot.HOUR_19_21; i++) {
            for (int j = 0; j <=Slot.FRIDAY; j++) {
                available[i][j] = true;
            }
        }
    }

    public void setId(int id) { this.id = id; }
    public int getId() { return id; }

    public void setName(String name) { this.name = name;}
    public String getName() { return name; }

    public boolean isAvailable(int day, int time) {
        if(time == Slot.NULL_HOUR || day == Slot.NULL_DAY)
            return false;
        return available[time][day];
    }
    public void setAvailable(int day, int time, boolean av) { available[time][day] = av; }

    /**Generates the hashcode of each instance.*/
    @Override public int hashCode() {
        return 47 + 5 * id;
    }

    /** Compares the instructor's id with o's and returns true if they are equal.
     * @param obj 
     */
    @Override public boolean equals(Object obj) {
        if (obj == null)  // Null references are not equal to this instance.
            return false;
        if (!obj.getClass ().equals(Instructor.class)) //Neither are they instances of different classes
            return false;
        return (((Instructor) obj).getId() == this.id);
    }

    /** Returns the Instructor's id and name as a string. */
    @Override public String toString(){
        String string = this.id + " " + this.name +"\n";
        for (int i = 0; i <=Slot.HOUR_19_21; i++) {
            for (int j = 0; j <=Slot.FRIDAY; j++) {
                if(!available[i][j]){
                    string +=  Slot.getDay(j) + " " + Slot.getTimeSlot(i) + "\n";
                }
            }
        }
        return  string;
    }

    /**Returns true if the given elements name is "Null".*/
    public boolean isNull(){
        return this.getName().equalsIgnoreCase(Instructor.NULL_NAME);
    }
}

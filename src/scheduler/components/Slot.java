
package scheduler.components;

/** Represents a filled up time slot complete with Room, Instructor, Course, Semester etc */
public class Slot {

    private int time, day;
    private RoomSize roomSize;
    private String roomId, slotId;

    /** Default constructor.*/
    public Slot(){
        this(Component.NULL_NAME);
    }

    /** Constructor that sets the slot's id.
     * @param slotId The slot's id */
    public Slot(String slotId){
        this(Slot.NULL_HOUR, Slot.NULL_DAY, slotId, Room.NULL_NAME, RoomSize.NULL);
    }

    /** Constructor that sets the slot's day, hour.
     * @param day The slot's day
     * @param hour The slot's hour*/
    public Slot(int day, int hour){
        this(hour, day, Component.NULL_NAME, Room.NULL_NAME, RoomSize.NULL);
    }

    /**Generates the hashcode of each instance.*/
    @Override public int hashCode() {
        return 47 + 5 * day * time;
    }

    /** Compares the slot's day/hour with o's and returns true if they are equal
     * @param o 
     */
    @Override public boolean equals(Object o) {
        if (o == null)  // Null references are not equal to this instance.
            return false;
        if (!o.getClass ().equals (Slot.class)) //Neither are they instances of different classes
            return false;
        return ((Slot) o).getDay() == this.day && ((Slot) o).getTime() == this.time;
    }

    /** Constructor that creates a time slot based on the arguments given.
     * @param timeSlot
     * @param day
     * @param slotId 
     * @param roomId 
     * @param roomSize  
     */
    public Slot( int timeSlot, int day, String slotId, String roomId, RoomSize roomSize){
        this.setRoomId(roomId);
        this.setDay(day);
        this.setSlotId(slotId);
        this.setTime(timeSlot);
        this.setRoomSize(roomSize); 
    }

    public void setSlotId(String slotId){ this.slotId= slotId;}
    public String getSlotId(){ return this.slotId;}

    public void setTime(int timeSlot){ if(Slot.isValidTimeSlot(timeSlot)) this.time= timeSlot;}
    public int getTime(){ return this.time;}

    public void setDay(int day){ if(Slot.isValidDay(day)) this.day= day;}
    public int getDay(){ return this.day;}

    public void setRoomId(String roomId){ this.roomId= roomId;}
    public String getRoomId(){ return this.roomId;}

    public RoomSize getRoomSize() { return roomSize; }
    public void setRoomSize(RoomSize roomSize) { this.roomSize = roomSize; }

    /** Returns the slots id as a string. */
    @Override public String toString(){
        return "SlotId: " + this.slotId +
                 " RoomID: " + this.roomId + " Day: "
                + Slot.getDay(this.day) +" Hour: "+ Slot.getTimeSlot(this.time) + " \n" ;
    }

    /** Day constants */
    public final static int MONDAY = 0;
    public final static int TUESDAY = 1;
    public final static int WEDNESDAY = 2;
    public final static int THURSDAY = 3;
    public final static int FRIDAY = 4;
    public final static int NULL_DAY = -1;
    /** Time constants */
    public final static int HOUR_9_11 = 0;
    public final static int HOUR_11_13 = 1;
    public final static int HOUR_13_15 = 2;
    public final static int HOUR_15_17 = 3;
    public final static int HOUR_17_19 = 4;
    public final static int HOUR_19_21 = 5;
    public final static int NULL_HOUR = -2;

    /** Returns the constant corresponding to the given string/day.
     * @param day The string that represents the day.
     * @return  
     */
    public static int getDay(String day) {
        if (day.equalsIgnoreCase("Monday")) return MONDAY;
        if (day.equalsIgnoreCase("Tuesday")) return TUESDAY;
        if (day.equalsIgnoreCase("Wednesday")) return WEDNESDAY;
        if (day.equalsIgnoreCase("Thursday")) return THURSDAY;
        if (day.equalsIgnoreCase("Friday")) return FRIDAY;
        return NULL_DAY;
    }

    /** Returns the constant corresponding to the given string/time.
     * @param start_time The string that represents the starting time of a course.
     * @return  
     */
    public static int getTimeSlot(String start_time){
        if (start_time.equalsIgnoreCase("9")) return HOUR_9_11;
        if (start_time.equalsIgnoreCase("11")) return HOUR_11_13;
        if (start_time.equalsIgnoreCase("13")) return HOUR_13_15;
        if (start_time.equalsIgnoreCase("15")) return HOUR_15_17;
        if (start_time.equalsIgnoreCase("17")) return HOUR_17_19;
        if (start_time.equalsIgnoreCase("19")) return HOUR_19_21;
        return NULL_HOUR;
    }

    /** Returns the constant corresponding to the given string/day.
     * @param day The constant that represents the day.
     * @return  
     */
    public static String getDay(int day) {
        if (day == Slot.MONDAY) return "Monday";
        if (day == Slot.TUESDAY) return "Tuesday";
        if (day == Slot.WEDNESDAY) return "Wednesday";
        if (day == Slot.THURSDAY) return "Thursday";
        if (day == Slot.FRIDAY) return "Friday";
        return Component.NULL_NAME;
    }

    /** Returns the constant corresponding to the given string/time.
     * @param start_time The constant that represents the starting time of a course.
     * @return  
     */
    public static String getTimeSlot(int start_time){
        if (start_time == Slot.HOUR_9_11) return "9:00-11:00";
        if (start_time == Slot.HOUR_11_13) return "11:00-13:00";
        if (start_time == Slot.HOUR_13_15) return "13:00-15:00";
        if (start_time == Slot.HOUR_15_17) return "15:00-17:00";
        if (start_time == Slot.HOUR_17_19) return "17:00-19:00";
        if (start_time == Slot.HOUR_19_21) return "19:00-21:00";
        return Component.NULL_NAME;
    }

    /**Returns true if the day given in the argument is a valid schedule day.
     * @param day
     * @return  
     */
    public static boolean isValidDay(int day){ return (day>=Slot.MONDAY && day<= Slot.FRIDAY ); }

    /**Returns true if the hour given in the argument is a valid schedule hour(time slot).
     * @param time
     * @return  
     */
    public static boolean isValidTimeSlot(int time){ return (time>=Slot.HOUR_9_11 && time<=Slot.HOUR_19_21); }

}

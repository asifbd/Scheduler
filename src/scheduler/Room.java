
package scheduler;

/**  Class representing a room for teaching. */
public class Room extends Component {

    /** Size constants */
    final static int MEDIUM = 11;
    final static int LARGE = 12;

    private String id;
    private String name;
    private int size;

    /**Default Constructor.*/
    public Room(){
        this(Room.NULL_NAME, Room.NULL_NAME, Room.NULL_ID );
    }

    /** Constructor.
     * @param id The id of the room.
     */
    public Room(String id){
        this(id, Room.NULL_NAME, Room.NULL_ID );
    }

    /** Constructor.
     * @param id The id of the room.
     * @param name The name of the room.
     * @param size The size of the room.
     */
    public Room(String id,String name,int size){
        this.setId(id);
        this.setName(name);
        this.setSize(size);
    }

    public void setId(String id){ this.id = id; }
    public String getId(){ return id; }

    public void setName(String name){ this.name = name; }
    public String getName(){ return name; }

    public void setSize(int size){if(Room.isValidRoomSize(size)) this.size = size; }
    public int getSize(){ return size; }

    /**Compares the two Rooms based on their size. */
    public int compareTo(Room o) {
        if( o.getSize() == this.size   )
                return 0;
        else if( o.getSize() < this.size   )
                return -1;
        return 1;
    }

    /**Generates the hashcode of each instance.*/
    @Override public int hashCode() {
        int hashcode = 0;
        for(int i=0; i< id.length(); i++)
                hashcode += id.charAt(i);
        return 47 + 5 * hashcode;
    }

    /** Compares the vertex's id with o's and returns true if they are equal */
    @Override public boolean equals(Object o) {
        if (o == null)  // Null references are not equal to this instance.
            return false;
        if (!o.getClass ().equals (Room.class)) //Neither are they instances of different classes
            return false;
        return (((Room) o).getId().equalsIgnoreCase(this.id));
    }


    /** Returns the room's id and name as a string. */
    @Override public String toString(){
        return this.name;
    }

    /** Returns the constant corresponding to the given string/size.
     * @param room_size The string that represents the size of the room.
     */
    public static int getRoomSize(String room_size){
        if (room_size.equalsIgnoreCase("large")) return Room.LARGE;
        if (room_size.equalsIgnoreCase("medium")) return Room.MEDIUM;
        return Room.NULL_ID;
    }

    /**Returns true if the room size given in the argument is a valid size.*/
    public static boolean isValidRoomSize(int roomSize){ return (roomSize==Room.LARGE || roomSize==Room.MEDIUM); }

    /**Returns true if the given elements id  is "Null".*/
    public boolean isNull(){
        return this.getId().equalsIgnoreCase(Room.NULL_NAME);
    }
}

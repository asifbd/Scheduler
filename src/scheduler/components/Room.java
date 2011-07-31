
package scheduler.components;

/**  Class representing a room for teaching. */
public class Room extends Component {

    private String id;
    private String name;
    private RoomSize size;

    /**Default Constructor.*/
    public Room(){
        this(Room.NULL_NAME, Room.NULL_NAME, RoomSize.NULL );
    }

    /** Constructor.
     * @param id The id of the room.
     */
    public Room(String id){
        this(id, Room.NULL_NAME, RoomSize.NULL );
    }

    /** Constructor.
     * @param id The id of the room.
     * @param name The name of the room.
     * @param size The size of the room.
     */
    public Room(String id, String name, RoomSize size){
        this.setId(id);
        this.setName(name);
        this.setSize(size);
    }

    public void setId(String id){ this.id = id; }
    public String getId(){ return id; }

    public void setName(String name){ this.name = name; }
    public String getName(){ return name; }

    public void setSize(RoomSize size){if(Room.isValidRoomSize(size)) this.size = size; }
    public RoomSize getSize(){ return size; }

    /**Compares the two Rooms based on their size.
     * @param o
     * @return 0 if they are equal, -1 if the given o room is smaller, 1 if the given o room is larger
     */
    public int compareTo(Room o) {
        if( o.getSize() == this.size   )
                return 0;
        else if( o.getSize() == RoomSize.MEDIUM && this.size == RoomSize.LARGE )
                return -1;
        return 1;
    }

    /**Generates the hashcode of each instance.*/
    @Override public int hashCode() {
        return 47 + 5 * id.hashCode();
    }

    /** Compares the vertex's id with o's and returns true if they are equal
     * @param o 
     */
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
    public static RoomSize getRoomSize(String room_size){
        if (room_size.equalsIgnoreCase(RoomSize.LARGE.name())) return RoomSize.LARGE;
        if (room_size.equalsIgnoreCase("medium")) return RoomSize.MEDIUM;
        return RoomSize.NULL;
    }

    /**Returns true if the room size given in the argument is a valid size. 
     */
    public static boolean isValidRoomSize(RoomSize roomSize){ return (roomSize==RoomSize.LARGE || roomSize==RoomSize.MEDIUM); }

    /**Returns true if the given elements id  is "Null". 
     */
    public boolean isNull(){
        return this.getId().equalsIgnoreCase(Room.NULL_NAME);
    }
}

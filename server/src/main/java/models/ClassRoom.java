package models;

/**
 * Represents the data needed to send an classroom
 *
 * @author Loïc Frueh
 * @author Yohann Meyer
 */
public class ClassRoom {

    /**
     * the classroom name
     */
    private String classRoom = "";

    /**
     * Default constructor
     */
    public ClassRoom(){}

    /**
     * Constructs a Classroom object
     * @param cR    the classroom name
     */
    public ClassRoom(String cR){classRoom = cR;}

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }
    
    public String floor(){
        return classRoom.substring(0,1);
    }
}


package data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"NO_CLASSROOM"})
public class ClassRoom {

    public static final ClassRoom NO_CLASSROOM = new ClassRoom("NOCR");
    private String classRoom = "";


    public ClassRoom(){}

    public ClassRoom(String cR){classRoom = cR;}

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }
}

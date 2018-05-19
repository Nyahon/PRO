package models;

public class ClassRoom {

    private String classRoom = "";

    public ClassRoom(){}

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


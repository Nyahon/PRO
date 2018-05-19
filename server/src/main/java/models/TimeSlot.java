package models;

import java.sql.Date;

public class TimeSlot {

    private ClassRoom classroom = new ClassRoom();
    private Date date;
    private int idPeriod;

    public TimeSlot(){}
    
    public TimeSlot(String classroom, long date) {
        this.classroom.setClassRoom( classroom );
        this.date = new Date(date);
    }
    
    public TimeSlot(String classroom, long date, int idPeriod) {
        this.classroom = new ClassRoom(classroom);
        this.date = new Date(date);
        this.idPeriod = idPeriod;
    }

    public String getClassroom() {
        return classroom.getClassRoom();
    }

    public String floor() {
        return classroom.floor();
    }

    public void setClassroom(String classroom) {
        this.classroom.setClassRoom( classroom );

    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public int getIdPeriod() {
        return idPeriod;
    }
    
    public void setIdPeriod(int idPeriod) {
        this.idPeriod = idPeriod;
    }
}

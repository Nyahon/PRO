package models;

import java.sql.Date;

public class AdvancedRequest {

    /**
     * The building, 0 corresponds to Cheseaux and 1 to St-Roch
     */
    private int building;
    private Date date;
    private int idPeriodBegin;
    private int idPeriodEnd;
    private String floor;
    private ClassRoom classroom;

    public AdvancedRequest(int building, Date date, int idPeriodBegin, int idPeriodEnd,
                           String floor, ClassRoom classroom) {

        this.building = building;
        this.date = date;
        this.idPeriodBegin = idPeriodBegin;
        this.idPeriodEnd = idPeriodEnd;
        this.floor = floor;
        this.classroom = classroom;
    }

    public int getBuilding() {
        return building;
    }

    public void setBuilding(int building) {
        this.building = building;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIdPeriodBegin() {
        return idPeriodBegin;
    }

    public void setIdPeriodBegin(int idPeriodBegin) {
        this.idPeriodBegin = idPeriodBegin;
    }

    public int getIdPeriodEnd() {
        return idPeriodEnd;
    }

    public void setIdPeriodEnd(int idPeriodEnd) {
        this.idPeriodEnd = idPeriodEnd;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getClassroom() {
        return classroom.getClassRoom();
    }

    public void setClassroom(ClassRoom classroom) {
        this.classroom = new ClassRoom(classroom.getClassRoom());
    }
}
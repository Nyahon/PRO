package models;

import utils.PeriodManager;

import java.sql.Date;

public class AdvancedRequest {
    private int building;
    private Date date;
    private int idPeriodBegin;
    private int idPeriodEnd = PeriodManager.PERIODS_END.size()-1;
    private String floor;
    private String classroom;

    public AdvancedRequest(){};

    public AdvancedRequest(int building, Date date, int idPeriodBegin,
                           String floor, String classroom) {

        this.building = building;
        this.date = date;
        this.idPeriodBegin = idPeriodBegin;
        this.floor = floor;
        this.classroom = classroom;
    }

    public AdvancedRequest(int building, Date date, int idPeriodBegin, int idPeriodEnd,
                           String floor, String classroom) {

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
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}
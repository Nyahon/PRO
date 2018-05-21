package models;

import utils.PeriodManager;

import java.sql.Date;

public class AdvancedRequest {
    private int building;
    private Date dateBegin;
    private Date dateEnd;
    private int idPeriodBegin;
    private int idPeriodEnd = PeriodManager.PERIODS_END.size()-1;
    private String floor;
    private String classroom;

    public AdvancedRequest(){};

    public AdvancedRequest(int building, Date begin, Date end, int idPeriodBegin,
                           String floor, String classroom) {

        this.building = building;
        this.dateBegin = begin;
        this.dateEnd = end;
        this.idPeriodBegin = idPeriodBegin;
        this.idPeriodEnd = PeriodManager.PERIODS_START.size();
        this.floor = floor;
        this.classroom = classroom;
    }

    public AdvancedRequest(int building, Date begin, Date end, int idPeriodBegin, int idPeriodEnd,
                           String floor, String classroom) {

        this.building = building;
        this.dateBegin = begin;
        this.dateEnd = end;
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

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date date) {
        this.dateBegin = date;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date date) {
        this.dateEnd = date;
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
package models;

import utils.PeriodManager;

import java.sql.Date;

/**
 * Represents the data needed to send an advanced request
 *
 * @author Loïc Frueh
 * @author Romain Gallay
 */
public class AdvancedRequest {

    /**
     * The building, 0 for Cheaseaux and 1 for St-Roch
     */
    private int building;

    /**
     * The beginning date
     */
    private Date dateBegin;

    /**
     * The ending date
     */
    private Date dateEnd;

    /**
     * The beginning period
     */
    private int idPeriodBegin;

    /**
     * The ending period
     */
    private int idPeriodEnd;

    /**
     * The floor
     */
    private String floor;

    /**
     * The classroom
     */
    private String classroom;

    /**
     * Default constructor
     */
    public AdvancedRequest(){};

    /**
     * Constructs an AdvancedRequest, idPeriodEnd is automatically set to the last period of a day
     * @param building  the building
     * @param begin the beginning date
     * @param end   the ending date
     * @param idPeriodBegin the beginning period
     * @param floor the floor
     * @param classroom the classroom
     */
    public AdvancedRequest(int building, Date begin, Date end, int idPeriodBegin,
                           String floor, String classroom) {

        this.building = building;
        this.dateBegin = begin;
        this.dateEnd = end;
        this.idPeriodBegin = idPeriodBegin;
        this.idPeriodEnd = PeriodManager.PERIODS_START.size()-1;
        this.floor = floor;
        this.classroom = classroom;
    }

    /**
     * Constructs an AdvancedRequest with every parameters
     * @param building  the building
     * @param begin the beginning date
     * @param end   the ending date
     * @param idPeriodBegin the beginning period
     * @param floor the floor
     * @param classroom the classroom
     */
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
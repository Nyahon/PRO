package models;

import java.sql.Date;

/**
 * Represents the data needed to send an classroom
 *
 * @author Loïc Frueh
 * @author Yohann Meyer
 */
public class TimeSlot {

    /**
     * the classroom
     */
    private ClassRoom classroom = new ClassRoom();

    /**
     * the date
     */
    private Date date;

    /**
     * the id period as defined in PeriodManager
     */
    private int idPeriod;

    /**
     * Default constructor
     */
    public TimeSlot(){}

    /**
     * Constructs a TimeSlot with <code>idPeriod</code> set to 0
     * @param classroom the classroom
     * @param date  the date
     */
    public TimeSlot(String classroom, long date) {
        this.classroom.setClassRoom( classroom );
        this.date = new Date(date);
    }

    /**
     * Constructs a TimeSlot with 3 parameters
     */
    public TimeSlot(String classroom, long date, int idPeriod) {
        this.classroom = new ClassRoom(classroom);
        this.date = new Date(date);
        this.idPeriod = idPeriod;
    }

    public String getClassroom() {
        return classroom.getClassRoom();
    }

    /**
     * Return the classroom floor
     * @return the classroom floor
     */
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

package utils;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * A class to map a period with its start and time. Contains only static attributes and methods
 *
 * @author Yohann Meyer
 * @author Romain Gallay
 */
public class PeriodManager {

    static {
        PERIODS_START = Arrays.asList(LocalTime.of(0,00), LocalTime.of(7,00),
                LocalTime.of(7,45), LocalTime.of(8,30), LocalTime.of(9,15),
                LocalTime.of(10,25), LocalTime.of(11,15), LocalTime.of(12,00),
                LocalTime.of(13,15), LocalTime.of(14,00), LocalTime.of(14,55),
                LocalTime.of(15,45), LocalTime.of(16,35), LocalTime.of(17,20),
                LocalTime.of(18,30), LocalTime.of(19,15), LocalTime.of(20,5),
                LocalTime.of(20,50), LocalTime.of(21,35), LocalTime.of(22,20));

        PERIODS_END = Arrays.asList(LocalTime.of(7,00), LocalTime.of(7,45),
                LocalTime.of(8,30), LocalTime.of(9,15), LocalTime.of(10,0),
                LocalTime.of(11,10), LocalTime.of(12,0), LocalTime.of(12,45),
                LocalTime.of(14,0), LocalTime.of(14,45), LocalTime.of(15,40),
                LocalTime.of(16,30), LocalTime.of(17,20), LocalTime.of(18,5),
                LocalTime.of(19,15), LocalTime.of(20,0), LocalTime.of(20,50),
                LocalTime.of(21,35), LocalTime.of(22,20), LocalTime.of(23,59));
    }

    /**
     * An array containing the start time of every period, the index corresponds to the period number
     */
    public static final List<LocalTime> PERIODS_START;

    /**
     * An array containing the end time of every period, the index corresponds to the period number
     */
    public static final List<LocalTime> PERIODS_END;

    public static final int FIRST_PERIOD_ID = 0;
    public static final int FIRST_WORK_PERIOD_ID = 3;
    public static final int LAST_PERIOD_ID = PERIODS_START.size() - 1;


    /**
     * Default constructor, private because there is no need to instantiate the class
     */
    private PeriodManager(){};

    /**
     * A method to compute the period number corresponding to a given time.
     * If the time corresponds to the exact end of a period, then the next
     * period is returned. Note : the first period corresponds to the period 0.
     * @param time   the time one wants to match on a period
     * @return period  the period matching the given time
     */
    public static int currentOrNextPeriod(LocalTime time){

        int period;

        // if start time corresponds to the end of a period, then return the next one
        if(PERIODS_END.contains(time)){
            period = PERIODS_END.indexOf(time) + 1;

        } else {
            period = 0;

            // iterate on PERIODS_END until time is past current entry
            while (time.isAfter(PERIODS_END.get(period))) {
                period++;
            }
        }
        return period;
    }

    /**
     * A method to compute the period number corresponding to a given time.
     * If the time corresponds to the exact start of a period, then the previous
     * period is returned. Note : the first period corresponds to the period 0.
     * @param time   the time one wants to match on a period
     * @return period  the period matching the given time
     */
    public static int currentOrPreviousPeriod(LocalTime time){

        int period;

        // if end time corresponds to the start of a period, then return the previous one
        if(PERIODS_START.contains(time)){
            period = PERIODS_START.indexOf(time) - 1;

        } else {
            period = PERIODS_START.size() - 1;

            while (time.isBefore(PERIODS_START.get(period))) {
                period--;
            }
        }
        return period;
    }
}

package utils;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class PeriodManager {

    static {
        PERIODS_START = Arrays.asList(LocalTime.of(8,30), LocalTime.of(9,15),
                LocalTime.of(10,25), LocalTime.of(11,15), LocalTime.of(12,00),
                LocalTime.of(13,15), LocalTime.of(14,00), LocalTime.of(14,55),
                LocalTime.of(15,45), LocalTime.of(16,35), LocalTime.of(17,20),
                LocalTime.of(18,30), LocalTime.of(19,15), LocalTime.of(20,5),
                LocalTime.of(20,50), LocalTime.of(21,35));

        PERIODS_END = Arrays.asList(LocalTime.of(9,15), LocalTime.of(10,0),
                LocalTime.of(11,10), LocalTime.of(12,0), LocalTime.of(12,45),
                LocalTime.of(14,0), LocalTime.of(14,45), LocalTime.of(15,40),
                LocalTime.of(16,30), LocalTime.of(17,20), LocalTime.of(18,5),
                LocalTime.of(19,15), LocalTime.of(20,0), LocalTime.of(20,50),
                LocalTime.of(21,35), LocalTime.of(22,20));
    }

    public static final List<LocalTime> PERIODS_START;
    public static final List<LocalTime> PERIODS_END;

    private PeriodManager(){};

    /**
     * This method computes the period number corresponding to a given time.
     * If the time corresponds to the exact end of a period, then the next
     * period is returned.
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
     * This method computes the period number corresponding to a given time.
     * If the time corresponds to the exact start of a period, then the previous
     * period is returned.
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

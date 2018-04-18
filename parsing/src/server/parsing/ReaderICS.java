package server.parsing;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.DateEnd;
import biweekly.property.DateStart;
import com.sun.istack.internal.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

public class ReaderICS {

    static {
        listStartPeriods = Arrays.asList(LocalTime.of(8,30), LocalTime.of(9,15),
                LocalTime.of(10,25), LocalTime.of(11,15), LocalTime.of(12,00),
                LocalTime.of(13,15), LocalTime.of(14,00), LocalTime.of(14,55),
                LocalTime.of(15,45), LocalTime.of(16,35), LocalTime.of(17,20),
                LocalTime.of(18,30), LocalTime.of(19,15), LocalTime.of(20,5),
                LocalTime.of(20,50), LocalTime.of(21,35));

        listEndPeriods = Arrays.asList(LocalTime.of(9,15), LocalTime.of(10,0),
                LocalTime.of(11,10), LocalTime.of(12,0), LocalTime.of(12,45),
                LocalTime.of(14,0), LocalTime.of(14,45), LocalTime.of(15,40),
                LocalTime.of(16,30), LocalTime.of(17,20), LocalTime.of(18,5),
                LocalTime.of(19,15), LocalTime.of(20,0), LocalTime.of(20,50),
                LocalTime.of(21,35), LocalTime.of(22,20));
    }

    private static String horaire1 = "test.ics";
    private static String horaire2 = "gaps_global_S2_2017_2018.ics";

    private static List<LocalTime> listStartPeriods;
    private static List<LocalTime> listEndPeriods;

    public void readICSFile(String fileName){
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            // there is only 1 calender so its enough to take the 1st, w/o use .all()
            ICalendar ical = Biweekly.parse(br).first();

            List<VEvent> events = ical.getEvents();

            // each event concerns a course from time X to Y at date Z in room R
            for (VEvent e : events){

                ArrayList<Integer> periods = periodFromSchedule(e.getDateStart(), e.getDateEnd());
                List<String> rooms = Arrays.asList(e.getLocation().getValue().split("\\s*,\\s*"));

                //System.out.println("Course : " + e.getSummary().getValue());
                //System.out.println("Room : " + e.getLocation().getValue());
                System.out.println("Rooms : " + rooms);
                System.out.println("Starting : " + e.getDateStart().getValue());
                System.out.println("Periods : "  + periods);
                System.out.println("Ending : " + e.getDateEnd().getValue());
                System.out.println();


                // TODO : instead of sysout, use classes TransformerICSToMySQL and ToolboxMySQL to pass data
/*
                ToolBoxMySQL tool = new ToolBoxMySQL();

                for(int i = periods.get(0); i < periods.get(periods.size()); i++) {
                    for(String room : rooms) {
                        tool.insertTakePlace(i, room, new Date(e.getDateStart().getValue().getTime()));
                    }
                }
*/
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /*
     * This method return the numbers corresponding to the periods between a DateStart and a DateEnd
     * given in parameter.
     */
    private ArrayList periodFromSchedule(@NotNull DateStart datestart,@NotNull DateEnd dateend){
        ArrayList periods = new ArrayList<Integer>();

        // create a Calendar for both Dates
        Calendar calendarStart = GregorianCalendar.getInstance();
        Calendar calendarEnd = GregorianCalendar.getInstance();
        calendarStart.setTimeInMillis(datestart.getValue().getTime());
        calendarEnd.setTimeInMillis(dateend.getValue().getTime());

        // create LocalTime from both dates
        LocalTime startTime = LocalTime.of(calendarStart.get(Calendar.HOUR_OF_DAY), calendarStart.get(Calendar.MINUTE));
        LocalTime endTime = LocalTime.of(calendarEnd.get(Calendar.HOUR_OF_DAY), calendarEnd.get(Calendar.MINUTE));

        System.out.println("time start : " + startTime);
        System.out.println("time end : " + endTime);

        int startPeriod = currentOrNextPeriod(startTime);
        int endPeriod = currentOrPreviousPeriod(endTime);

        // add periods in ArrayList periods
        for(int i = startPeriod; i <= endPeriod; i++){
            periods.add(i);
        }
        return periods;
    }

    private int currentOrNextPeriod(LocalTime time){

        int period;

        // if start time corresponds to the end of a period, then return the next one
        if(listEndPeriods.contains(time)){
            period = listEndPeriods.indexOf(time) + 1;

        } else {
            period = 0;

            // iterate on listEndPeriods until time is past current entry
            while (time.isAfter(listEndPeriods.get(period))) {
                period++;
            }
        }
        return period + 1;
    }

    private int currentOrPreviousPeriod(LocalTime time){

        int period;

        // if end time corresponds to the start of a period, then return the previous one
        if(listStartPeriods.contains(time)){
            period = listStartPeriods.indexOf(time) - 1;

        } else {
            period = listStartPeriods.size() - 1;

            while (time.isBefore(listStartPeriods.get(period))) {
                period--;
            }
        }
        return period + 1;
    }

    public static void main (String args[]){
        System.out.println("Parsing ics file ...");

        ReaderICS readerICS = new ReaderICS();
        readerICS.readICSFile(horaire2);
        
        System.out.println("Parsing done !");
    }

}

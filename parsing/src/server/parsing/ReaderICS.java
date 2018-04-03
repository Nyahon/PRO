package server.parsing;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.DateEnd;
import biweekly.property.DateStart;
import biweekly.util.ICalDate;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ReaderICS {

    private static List<String> listStartPeriods = Arrays.asList("8:30","9:15", "10:25","11:15",
            "12:00","13:15", "14:00","14:55", "15:45", "16:35", "17:20", "18:30",
            "19:15", "20:5", "20:50", "21:35");

    private static List<String> listEndPeriods = Arrays.asList("9:10","10:0", "11:10", "12:0",
            "13:15", "14:0", "14:45", "15:40", "16:30", "17:20", "18:5", "19:15",
            "20:0", "20:50", "21:35", "22:20");

    public void readICSFile(String fileName){
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            // there is only 1 calender so its enough to take the 1st, w/o use .all()
            ICalendar ical = Biweekly.parse(br).first();

            List<VEvent> events = ical.getEvents();

            // each event concerns a course from time X to Y at date Z in room R
            for (VEvent e : events){

                ArrayList<Integer> periods = getPeriodFromSchedule(e.getDateStart(), e.getDateEnd());

                System.out.println("Course : " + e.getSummary().getValue());
                System.out.println("Room : " + e.getLocation().getValue());
                System.out.println("Starting : " + e.getDateStart().getValue());
                System.out.println("Periods : "  + periods);
                System.out.println("Ending : " + e.getDateEnd().getValue());
                System.out.println();

                // TODO : instead of sysout, use classes TransformerICSToMySQL and ToolboxMySQL to pass data
/*
                ToolBoxMySQL tool = new ToolBoxMySQL();

                for(int i = periods.get(0); i < periods.get(periods.size()); i++) {
                    tool.insertTakePlace(i, e.getLocation().getValue(), new Date(e.getDateStart().getValue().getTime()));
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
    private ArrayList getPeriodFromSchedule(DateStart datestart, DateEnd dateend){
        ArrayList periods = new ArrayList<Integer>();

        // transform DateStart and DateEnd in Date object
        Date start = new Date(datestart.getValue().getTime());
        Date end = new Date(dateend.getValue().getTime());

        // create a Calendar for both Dates
        Calendar calendarStart = GregorianCalendar.getInstance();
        calendarStart.setTime(start);
        Calendar calendarEnd = GregorianCalendar.getInstance();
        calendarEnd.setTime(end);

        // get the period from the time of both dates
        String timeStart = calendarStart.get(Calendar.HOUR_OF_DAY) + ":" + calendarStart.get(Calendar.MINUTE);
        String timeEnd = calendarEnd.get(Calendar.HOUR_OF_DAY) + ":" + calendarEnd.get(Calendar.MINUTE);

        System.out.println("time start : " + timeStart);
        System.out.println("time end : " + timeEnd);
        int periodStart = listStartPeriods.indexOf(timeStart);
        int periodEnd = listEndPeriods.indexOf(timeEnd);

        // add periods in ArrayList periods
        for(int i = periodStart; i <= periodEnd; i++){
            periods.add(i + 1);
        }

        return periods;
    }
    
    public static void main (String args[]){
        System.out.println("let us start");

        ReaderICS readerICS = new ReaderICS();
        readerICS.readICSFile("horaire.ics");
    }

}

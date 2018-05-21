package utils;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.DateEnd;
import biweekly.property.DateStart;
import com.sun.istack.internal.NotNull;
import database.ToolBoxMySQL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalTime;
import java.util.*;

/** A class to read an ICS file, parse its content and send it to a MySQL database.
 *  @author : Dejvid Muaremi, Aurélien Siu, Romain Gallay, Yohann Meyer, Loïc Frueh, Labinot Rashiti
 */

public class ReaderICS {

    private ToolBoxMySQL tool;
    private PrintWriter writer;
    private static String messageUpdateDB = "Updating database : ";

    /**
     * Constructor of the class
     */
    public ReaderICS(ToolBoxMySQL tool, PrintWriter writer){
        this.tool = tool;
        this.writer = writer;
    }
   /**
    * This method reads an ICS file with the name <code>fileRaeder</code> and send
    * its content to the MYSQL databse defined in the class database.ToolBoxMySQL
    * @param fileName the name of the file we need to parse  
    */
    public void readICSFile(String fileName){
        try(BufferedReader br = new BufferedReader(new FileReader(fileName));
            BufferedReader br2 = new BufferedReader(new FileReader(fileName))) {

            // there is only 1 calender so its enough to take the 1st, w/o use .all()
            ICalendar ical = Biweekly.parse(br).first();

            List<VEvent> events = ical.getEvents();
            int numberOfEventsProcessed = 0;
            int lastPercent = 0;
            String progressBar = "";
            String progressBarInverse = new String(new char[50]).replace("\0", " ");
            String formatting = " ";

            // each event concerns a course from time X to Y at date Z in room R
            for (VEvent e : events){
                numberOfEventsProcessed++;

                ArrayList<Integer> periods = periodFromSchedule(e.getDateStart(), e.getDateEnd());
                List<String> rooms = Arrays.asList(e.getLocation().getValue().split("\\s*,\\s*"));

                for(int i : periods) {
                    for(String room : rooms) {
                        tool.insertClassroomWithCheck(room);
                        tool.insertTakePlace(new Date(e.getDateStart().getValue().getTime()), i, room);
                    }
                }
                // send the percentage of the file processed
                int percent = numberOfEventsProcessed * 100 / events.size();
                if (percent != lastPercent) {
                    lastPercent = percent;
                    if(percent > 9){
                        formatting = "";
                    }
                    if(percent%2 == 0){
                        progressBar += "#";
                        progressBarInverse = progressBarInverse.substring(0, progressBarInverse.length()-1);
                    }
                    writer.print(messageUpdateDB + "(" + lastPercent + "%) :" + formatting + " |" + progressBar +
                            progressBarInverse + "|\r");
                    writer.flush();
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This method return the numbers corresponding to the periods between a DateStart and a DateEnd
     * given as parameters.
     * @param datestart   the starting date
     * @param dateend   the ending date
     * @return periods  an ArrayList containing the periods between the given dates
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

        int startPeriod = PeriodManager.currentOrNextPeriod(startTime);
        int endPeriod = PeriodManager.currentOrPreviousPeriod(endTime);

        // add periods in ArrayList periods
        for(int i = startPeriod; i <= endPeriod; i++){
            periods.add(i);
        }
        return periods;
    }

    public static String getMessageUpdateDB() {
        return messageUpdateDB;
    }
}

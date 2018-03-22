import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class Reader {

    public static void main (String args[]){

        try(BufferedReader br = new BufferedReader(new FileReader("horaire.ics"))) {

            // there is only 1 calender so its enough to take the 1st, o/w use .all()
            ICalendar ical = Biweekly.parse(br).first();

            List<VEvent> events = ical.getEvents();

            for (VEvent e : events){
                System.out.println("Course : " + e.getSummary().getValue());
                System.out.println("Room : " + e.getLocation().getValue());
                System.out.println("Starting : " + e.getDateStart().getValue());
                System.out.println("Ending : " + e.getDateEnd().getValue());
                System.out.println();

            }

        } catch (IOException e){
            e.printStackTrace();
        }

    }
}

package controller;

import utils.FloorFreePeriodMap;
import models.AdvancedRequest;
import models.ClassRoom;
import models.TimeSlot;
import network.client.ClientSocket;
import network.protocol.Protocol;
import utils.PeriodManager;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;


public class Controller {

    private static final ClientSocket client = new ClientSocket();

    public static Map<String, Integer> handleClientFloorRequest(TimeSlot data) throws IOException {
        client.connect(Protocol.SERVER_IP, Protocol.DEFAULT_PORT);
        client.askForFloor(data);
        FloorFreePeriodMap freePeriodMap = new FloorFreePeriodMap(data.floor());
        List<TimeSlot> result = client.receiveTimeSlots();
        for (TimeSlot t : result) {
            int numberOfFreePeriod = t.getIdPeriod() - data.getIdPeriod();
            freePeriodMap.insert(t.getClassroom(), numberOfFreePeriod);
        }

        return freePeriodMap.getFreeMap();
    }

    public static List<TimeSlot> handleClientClassroomRequest(ClassRoom data) throws IOException {
        client.connect(Protocol.SERVER_IP, Protocol.DEFAULT_PORT);
        client.askForClassroom(data);
        List<TimeSlot> result = client.receiveTimeSlots();
        createQuickClassroomFile(result, data);
        File file = new File("classroom-request.txt");
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            String cmd = "rundll32 url.dll,FileProtocolHandler " + file.getCanonicalPath();
            Runtime.getRuntime().exec(cmd);
        }
        else {
            Desktop.getDesktop().edit(file);
        }


        return client.receiveTimeSlots();
    }

    public static List<TimeSlot> handleClientAdvancedRequest(List<AdvancedRequest> data) throws IOException {
        client.connect(Protocol.SERVER_IP, Protocol.DEFAULT_PORT);
        PrintWriter writer = new PrintWriter("DARYLL.txt", "UTF-8");
        for (AdvancedRequest request : data) {
            client.askForAdvancedRequest(request);
            List<TimeSlot> result = client.receiveTimeSlots();
            if (request.getClassroom() != null) {
                writeToFileWithClassroomTemplate(result, request, writer);
            }
            else if (request.getFloor() != null) {
                writeToFileWithFloorTemplate(result, request, writer);
            }
            else {
                writeToFileWithAllTemplate(result, request, writer);
            }
        }
        writer.close();
        return client.receiveTimeSlots();
    }

    private static void createQuickClassroomFile(List<TimeSlot> timeSlotList, ClassRoom clientRequest) throws FileNotFoundException, UnsupportedEncodingException {
        LocalDate date = LocalDate.now();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        List<Integer> periods = new ArrayList<>();

        PrintWriter writer = new PrintWriter("DARYLL.txt", "UTF-8");
        writer.println("Salle: " + clientRequest.getClassRoom());
        writer.flush();
        writer.println("-------------------------------------------------");
        writer.println(formatter.format(date) + " - " + date.toString());
        writer.println("-------------------------------------------------");
        writer.flush();

        for (int j = 3; j < PeriodManager.PERIODS_START.size() - 1; ++j) {
            periods.add(j);
        }
        for (TimeSlot t : timeSlotList) {
            periods.remove(t.getIdPeriod());
        }
        for (int period : periods) {
            writer.println(PeriodManager.PERIODS_START.get(period).toString() + " - " + PeriodManager.PERIODS_END.get(period).toString());
            writer.flush();
        }

        writer.close();
    }

    private static void writeToFileWithClassroomTemplate(List<TimeSlot> timeSlotList, AdvancedRequest clientRequest, PrintWriter writer) {
        List<Integer> periods = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");

        writer.println("Salle: " + clientRequest.getClassroom());
        writer.flush();

        LocalDate date = clientRequest.getDateBegin().toLocalDate();
        LocalDate timeSlotDate;
        List<TimeSlot> tmp = new ArrayList<>();
        for (int i = 0; i < timeSlotList.size(); ++i) {
            timeSlotDate = timeSlotList.get(i).getDate().toLocalDate();
            if (i != 0 && !timeSlotDate.toString().equals(timeSlotList.get(i - 1).getDate().toLocalDate().toString() ) ) {
                timeSlotDate = timeSlotList.get(i - 1).getDate().toLocalDate();
                boolean isTimeToGetNextTimeSlot = false;
                do {
                    writer.println("-------------------------------------------------");
                    writer.println(formatter.format(date) + " - " + date.toString());
                    writer.println("-------------------------------------------------");
                    writer.flush();

                    for (int j = 3; j < PeriodManager.PERIODS_START.size() - 1; ++j) {
                        periods.add(j);
                    }
                    if (date.toString().equals(timeSlotDate.toString())) {
                        for (TimeSlot t : tmp) {
                            periods.remove(t.getIdPeriod());
                        }
                        isTimeToGetNextTimeSlot = true;
                    }
                    for (int period : periods) {
                        writer.println(PeriodManager.PERIODS_START.get(period).toString() + " - " + PeriodManager.PERIODS_END.get(period).toString());
                        writer.flush();
                    }

                    date.plusDays(1);
                } while (date.isBefore(clientRequest.getDateEnd().toLocalDate()) && !isTimeToGetNextTimeSlot);

                /*
                for (date = clientRequest.getDateBegin().toLocalDate(); date.isBefore(clientRequest.getDateBegin().toLocalDate()); date.plusDays(1)) {
                    writer.println("-------------------------------------------------");
                    writer.println(formatter.format(date) + " - " + date.toString());
                    writer.println("-------------------------------------------------");
                    writer.flush();
                    for (int j = 3; j < PeriodManager.PERIODS_START.size() - 1; ++j) {
                        periods.add(j);
                    }
                    if (date.toString().equals(timeSlotDate.toString())) {
                        for (TimeSlot t : tmp) {
                            periods.remove(t.getIdPeriod());
                        }
                    }
                    for (int period : periods) {
                        writer.println(PeriodManager.PERIODS_START.get(period).toString() + " - " + PeriodManager.PERIODS_END.get(period).toString());
                        writer.flush();
                    }
                }
                */
                tmp.clear();
            }
            tmp.add(timeSlotList.get(i));
        }

    }

    private static void writeToFileWithFloorTemplate(List<TimeSlot> timeSlotList, AdvancedRequest clientRequest, PrintWriter writer) {
        List<Integer> periods = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");

        writer.println("Etage: " + clientRequest.getFloor());
        writer.println("#############################################################################");
        writer.println();
        writer.flush();
    }

    private static void writeToFileWithAllTemplate(List<TimeSlot> timeSlotList, AdvancedRequest clientRequest, PrintWriter writer) {

    }
}
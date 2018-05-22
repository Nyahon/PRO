package controller;


import utils.ClassroomsByFloor;
import utils.FloorFreePeriodMap;
import models.AdvancedRequest;
import models.ClassRoom;
import models.TimeSlot;
import network.client.ClientSocket;
import network.protocol.Protocol;
import utils.PeriodManager;

import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;


public class Controller {

    private static final ClientSocket client = new ClientSocket();
    private static AdvancedRequest clientRequest;

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

    public static void handleClientClassroomRequest(ClassRoom data) throws IOException {
        client.connect(Protocol.SERVER_IP, Protocol.DEFAULT_PORT);
        client.askForClassroom(data);
        List<TimeSlot> result = client.receiveTimeSlots();
        createQuickClassroomFile(result, data);
        File file = new File("DARYLL.txt");
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            String cmd = "rundll32 url.dll,FileProtocolHandler " + file.getCanonicalPath();
            Runtime.getRuntime().exec(cmd);
        }
        else {
            String cmd = "nano " + file.getCanonicalPath();
            Runtime.getRuntime().exec(cmd);
        }
    }

    public static void handleClientAdvancedRequest(List<AdvancedRequest> data) throws IOException {
        client.connect(Protocol.SERVER_IP, Protocol.DEFAULT_PORT);
        PrintWriter writer = new PrintWriter("DARYLL.txt", "UTF-8");
        for (AdvancedRequest request : data) {
            client.askForAdvancedRequest(request);
            List<TimeSlot> result = client.receiveTimeSlots();
            clientRequest = request;

            if (clientRequest.getClassroom() != null) {
                writeToFileWithClassroomTemplate(result, writer);
            }
            else if (clientRequest.getFloor() != null) {
                writeToFileWithFloorTemplate(result, writer);
            }
            else {
                writeToFileWithAllTemplate(result, writer);
            }
        }
        writer.close();


        File file = new File("DARYLL.txt");
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            String cmd = "rundll32 url.dll,FileProtocolHandler " + file.getCanonicalPath();
            Runtime.getRuntime().exec(cmd);
        }
        else {
            Desktop.getDesktop().edit(file);
        }


    }

    private static void createQuickClassroomFile(List<TimeSlot> timeSlotList, ClassRoom clientRequest) throws FileNotFoundException, UnsupportedEncodingException {
        LocalDate date = LocalDate.now();
        DateTimeFormatter daysOfTheWeekFormatter = DateTimeFormatter.ofPattern("EEEE");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        List<Integer> periods = new ArrayList<>();

        PrintWriter writer = new PrintWriter("DARYLL.txt", "UTF-8");
        writer.println("Salle: " + clientRequest.getClassRoom());
        writer.println("-------------------------------------------------");
        writer.println(date.format(daysOfTheWeekFormatter) + " - " + date.format(dateFormatter));
        writer.println("-------------------------------------------------");
        writer.flush();

        for (int j = 3; j < PeriodManager.PERIODS_START.size() - 1; ++j) {
            periods.add(j);
        }
        for (TimeSlot t : timeSlotList) {
            periods.remove(Integer.valueOf(t.getIdPeriod()));
        }
        for (int period : periods) {
            writer.println(PeriodManager.PERIODS_START.get(period).toString() + " - " + PeriodManager.PERIODS_END.get(period).toString());
            writer.flush();
        }

        writer.close();
    }

    private static void writeToFileWithClassroomTemplate(List<TimeSlot> timeSlotList, PrintWriter writer) {
        List<Integer> periods = new ArrayList<>();
        DateTimeFormatter daysOfTheWeekFormatter = DateTimeFormatter.ofPattern("EEEE");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        writer.println("Salle: " + clientRequest.getClassroom());
        writer.flush();

        List<TimeSlot> tmp = new ArrayList<>();
        LocalDate timeSlotDate;
        // Loop on all dates in the interval wanted by the user
        for (LocalDate date = clientRequest.getDateBegin().toLocalDate(); date.isBefore(clientRequest.getDateEnd().toLocalDate().plusDays(1)); date = date.plusDays(1)) {
            writer.println("-------------------------------------------------");
            writer.println(date.format(daysOfTheWeekFormatter) + " - " + date.format(dateFormatter));
            writer.println("-------------------------------------------------");
            writer.flush();

            int firstPeriod = clientRequest.getIdPeriodBegin() < 3 ? 3 : clientRequest.getIdPeriodBegin();
            // Fill the period tab with all existing periods
            for (int j = firstPeriod; j < PeriodManager.PERIODS_START.size() - 1; ++j) {
                periods.add(j);
            }

            // Compare the current date of the inteval with the date of the first Timeslot in the list
            if (timeSlotList.size() != 0 && date.equals(timeSlotList.get(0).getDate().toLocalDate())) {
                // Loop on all timeslots of the list until the date of one of them is no longer equals to the previous one.
                for (int i = 0; i < timeSlotList.size(); ++i) {
                    timeSlotDate = timeSlotList.get(i).getDate().toLocalDate();
                    if (i != 0 && !timeSlotDate.equals(timeSlotList.get(i - 1).getDate().toLocalDate())) {
                        break;
                    }
                    // Group all timeslots with the same date in a list
                    tmp.add(timeSlotList.get(i));
                }

                // Remove all the occupied periods from the period list
                for (TimeSlot t : tmp) {
                    periods.remove(Integer.valueOf(t.getIdPeriod()));
                }

                // Remove all the processed timeslots from the list
                timeSlotList.removeAll(tmp);
                tmp.clear();
            }

            // Write all the remaining periods in the file
            for (int period : periods) {
                writer.println(PeriodManager.PERIODS_START.get(period).toString() + " - " + PeriodManager.PERIODS_END.get(period).toString());
                writer.flush();
            }
            periods.clear();
        }
    }

    private static void writeToFileWithFloorTemplate(List<TimeSlot> timeSlotList, PrintWriter writer) {
        List<Integer> periods = new ArrayList<>();
        DateTimeFormatter daysOfTheWeekFormatter = DateTimeFormatter.ofPattern("EEEE");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        writer.println("Etage: " + clientRequest.getFloor());
        writer.println("#############################################################################");
        writer.println();
        writer.flush();

        List<TimeSlot> tmp = new ArrayList<>();
        // Loop on all dates in the interval wanted by the user
        for (LocalDate date = clientRequest.getDateBegin().toLocalDate(); date.isBefore(clientRequest.getDateEnd().toLocalDate().plusDays(1)); date = date.plusDays(1)) {
            writer.println(date.format(daysOfTheWeekFormatter) + " - " + date.format(dateFormatter));
            writer.println("*************************************");
            writer.flush();
            // Recuperate classrooms in the requested floor and Loop on all classroom
            List<String> classrooms = ClassroomsByFloor.FloorsMap.get(clientRequest.getFloor());
            writeFreePeriodsForFloor(writer, classrooms, date, timeSlotList, tmp, periods);
        }
    }

    private static void writeToFileWithAllTemplate(List<TimeSlot> timeSlotList, PrintWriter writer) {
        List<Integer> periods = new ArrayList<>();
        List<String> classrooms;
        DateTimeFormatter daysOfTheWeekFormatter = DateTimeFormatter.ofPattern("EEEE");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // Recuperate a list of the floors in fonction of the requested building
        List<String> floors = clientRequest.getBuilding() == 0 ? ClassroomsByFloor.FLOORS_CHESEAUX : ClassroomsByFloor.FLOORS_ST_ROCH;

        List<TimeSlot> tmp = new ArrayList<>();
        // Loop on all dates in the interval wanted by the user
        for (LocalDate date = clientRequest.getDateBegin().toLocalDate(); date.isBefore(clientRequest.getDateEnd().toLocalDate().plusDays(1)); date = date.plusDays(1)) {
            writer.println(date.format(daysOfTheWeekFormatter) + " - " + date.format(dateFormatter));
            writer.println("#############################################################################");
            writer.println();
            writer.flush();

            // Loop on all floors of the building
            for (String floor : floors) {
                writer.println("Etage: " + floor);
                writer.println("*************************");
                writer.println();
                writer.flush();

                // Recuperate classrooms in the current floor and Loop on all classroom
                classrooms = ClassroomsByFloor.FloorsMap.get(floor);
                writeFreePeriodsForFloor(writer, classrooms, date, timeSlotList, tmp, periods);
            }
        }
    }

    private static void writeFreePeriodsForFloor(PrintWriter writer, List<String> classrooms, LocalDate date, List<TimeSlot> timeSlotList, List<TimeSlot> tmp, List<Integer> periods) {
        for (String classroom : classrooms) {
            writer.println("Salle : " + classroom);
            writer.println("--------------");

            int beginPeriod = clientRequest.getIdPeriodBegin() < 3 ? 3 : clientRequest.getIdPeriodBegin();
            // Fill the period tab with all existing periods
            for (int j = beginPeriod; j < PeriodManager.PERIODS_START.size() - 1; ++j) {
                periods.add(j);
            }

            // Compare the current date of the interval with the date of the first Timeslot in the list
            if (timeSlotList.size() != 0 && date.equals(timeSlotList.get(0).getDate().toLocalDate())) {
                // Compare the current classroom of the floor with the classroom of the first Timeslot in the list
                if (classroom.equals(timeSlotList.get(0).getClassroom())) {
                    removeClassroomOccupiedPeriod(timeSlotList, tmp, periods, classroom);
                }
            }

            // Write all the remaining periods in the file
            for (int period : periods) {
                writer.println(PeriodManager.PERIODS_START.get(period).toString() + " - " + PeriodManager.PERIODS_END.get(period).toString());
                writer.flush();
            }

            periods.clear();
            if (classrooms.lastIndexOf(classroom) != classrooms.size() - 1) {
                writer.println("--------------");
                writer.flush();
            }
            else {
                writer.println();
                writer.println();
                writer.flush();
            }
        }
    }

    private static void removeClassroomOccupiedPeriod (List<TimeSlot> timeSlotList, List<TimeSlot> tmp, List<Integer> periods, String classroom) {
        // Add the first TimeSlot of the TimeSlot list in the temp list
        tmp.add(timeSlotList.get(0));
        // Loop on all TimeSlot of the TimeSlot list until it reach a TimeSlot with a different classroom than the previous one.
        for (int i = 1; i < timeSlotList.size(); ++i) {
            if (!timeSlotList.get(i).getClassroom().equals(classroom)) {
                break;
            }
            // Group all TimeSlot of the same classroom in the temp list
            tmp.add(timeSlotList.get(i));
        }
        // Remove all the occupied periods from the period list
        for (TimeSlot t : tmp) {
            periods.remove(Integer.valueOf(t.getIdPeriod()));
        }
        // Remove all the processed TimeSlot from the list
        timeSlotList.removeAll(tmp);
        tmp.clear();
    }
}
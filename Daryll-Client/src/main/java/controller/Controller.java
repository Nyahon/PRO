package controller;


import utils.*;
import models.AdvancedRequest;
import models.ClassRoom;
import models.TimeSlot;
import network.client.ClientSocket;
import network.protocol.Protocol;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * A controller class to handle and format requests between GUI and ClientSocket
 *
 * @author Loïc Frueh
 * @author Romain Gallay
 */

public class Controller {

    /**
     * The client socket that is used to send data to the server
     */
    private static final ClientSocket client = new ClientSocket();

    /**
     * An advanced done by the user in the GUI
     */
    private static AdvancedRequest clientRequest;


    /**
     * Processes user quick request for a floor and transmit the necessary information to the GUI
     * in order to display it on the plans.
     * @param data  the data send by the GUI that contains the floor, the date, and the start time of the free rooms
     *              request
     * @return A Map that show the number of free periods remaining, since the requested start time, per classroom.
     *         Classroom -> number of period remaining for this classroom.
     *         This information is used by the GUI to display rightful colors in the plan.
     */
    public static Map<String, Integer> handleClientFloorRequest(TimeSlot data) throws IOException {
        client.connect(Protocol.SERVER_IP, Protocol.DEFAULT_PORT);
        // Send request to the server
        client.askForFloor(data);
        // Wait for the response
        List<TimeSlot> result = client.receiveTimeSlots();
        client.disconnect();
        // Create a Map that will store the classroom with their remaining number of free periods since the client requested period
        FloorFreePeriodMap freePeriodMap = new FloorFreePeriodMap(data.floor());
        for (TimeSlot t : result) {
            int numberOfFreePeriod = t.getIdPeriod() - data.getIdPeriod();
            freePeriodMap.insert(t.getClassroom(), numberOfFreePeriod);
        }
        return freePeriodMap.getFreeMap();
    }

    /**
     * Processes user quick request for a classroom free period schedule. Create and open a file that store the result.
     * If the file already exist, overwrite it.
     * @param data  the data send by the GUI. It is the user requested classroom
     */
    public static void handleClientClassroomRequest(ClassRoom data) throws IOException {
        client.connect(Protocol.SERVER_IP, Protocol.DEFAULT_PORT);
        // Send request to the server
        client.askForClassroom(data);
        // Wait for the response
        List<TimeSlot> result = client.receiveTimeSlots();
        client.disconnect();
        // Create and open a file with the result of the client request (if the file already exist, it will overwrite it)
        createQuickClassroomFile(result, data);
        File file = new File(ConfigLoader.getOutputFilename());
        openFileInTextEditor(file);
    }

    /**
     * Processes user advanced requests from the GUI advanced request menu. Create and open a file that store the result
     * of all the requests. If the file already exist, overwrite it.
     * @param data  a list of all advanced requests done by the user
     */
    public static void handleClientAdvancedRequest(List<AdvancedRequest> data) throws IOException {
        client.connect(Protocol.SERVER_IP, Protocol.DEFAULT_PORT);
        PrintWriter writer = new PrintWriter(ConfigLoader.getOutputFilename(), ConfigLoader.getFileEncoding());
        // Send the client advanced requests and processed them one by one
        for (int i = 0; i < data.size(); ++i) {
            // Send request to the server
            client.askForAdvancedRequest(data.get(i));
            // Wait for the response
            List<TimeSlot> result = client.receiveTimeSlots();
            clientRequest = data.get(i);

            writer.println("REQUETE no " + (i + 1));
            writer.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            writer.println();
            writer.flush();

            // Check the type of client request, write the result with the correct template in a file and open it
            if (clientRequest.getClassroom() != null) {
                writeToFileWithClassroomTemplate(result, writer);
            }
            else if (clientRequest.getFloor() != null) {
                writeToFileWithFloorTemplate(result, writer);
            }
            else {
                writeToFileWithDefaultTemplate(result, writer);
            }

            if (i != data.size() - 1) {
                writer.println();
                writer.println();
                writer.flush();
            }
        }
        writer.close();
        client.disconnect();

        File file = new File(ConfigLoader.getOutputFilename());
        openFileInTextEditor(file);
    }

    /**
     * Create a file with the result of the user classroom free schedule request.
     * @param timeSlotList  a list of all occupied periods of the classroom for today
     * @param clientRequest  the client asked classroom
     */
    private static void createQuickClassroomFile(List<TimeSlot> timeSlotList, ClassRoom clientRequest) throws FileNotFoundException, UnsupportedEncodingException {
        LocalDate date = LocalDate.now();
        DateTimeFormatter daysOfTheWeekFormatter = DateTimeFormatter.ofPattern(ConfigLoader.getDaysOfWeekFormatter());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(ConfigLoader.getDateFormatter());
        List<Integer> periods = new ArrayList<>();

        PrintWriter writer = new PrintWriter(ConfigLoader.getOutputFilename(), ConfigLoader.getFileEncoding());
        // Write the header in the file
        writer.println(DisplayConstants.FILE_CLASSROOM_TITLE + ": " + clientRequest.getClassRoom());
        writer.println("-------------------------------------------------");
        writer.println(date.format(daysOfTheWeekFormatter) + " - " + date.format(dateFormatter));
        writer.println("-------------------------------------------------");
        writer.flush();

        // Fill the period tab with all existing periods
        for (int j = PeriodManager.FIRST_WORK_PERIOD_ID; j < PeriodManager.LAST_PERIOD_ID; ++j) {
            periods.add(j);
        }
        // Remove all the occupied periods from the period list
        for (TimeSlot t : timeSlotList) {
            periods.remove(Integer.valueOf(t.getIdPeriod()));
        }
        // Write all the remaining periods in the file
        for (int period : periods) {
            writer.println(PeriodManager.PERIODS_START.get(period).toString() + " - " + PeriodManager.PERIODS_END.get(period).toString());
            writer.flush();
        }
        if (periods.isEmpty()) {
            writer.println(DisplayConstants.FILE_ALL_OCCUPIED_MESSAGE);
            writer.flush();
        }
        writer.close();
    }

    /**
     * Write in a file with the classroom template style (free schedule for a particular classroom)
     * @param timeSlotList  a list of all occupied periods of the classroom for the requested interval of dates
     * @param writer the writer that is used to write the information in the file
     */
    private static void writeToFileWithClassroomTemplate(List<TimeSlot> timeSlotList, PrintWriter writer) {
        List<Integer> periods = new ArrayList<>();
        DateTimeFormatter daysOfTheWeekFormatter = DateTimeFormatter.ofPattern(ConfigLoader.getDaysOfWeekFormatter());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(ConfigLoader.getDateFormatter());

        // Write the header in the file
        writer.println(DisplayConstants.FILE_CLASSROOM_TITLE + ": " + clientRequest.getClassroom());
        writer.flush();

        List<TimeSlot> tmp = new ArrayList<>();
        LocalDate timeSlotDate;
        // Loop on all dates in the interval wanted by the user
        for (LocalDate date = clientRequest.getDateBegin().toLocalDate(); date.isBefore(clientRequest.getDateEnd().toLocalDate().plusDays(1)); date = date.plusDays(1)) {
            writer.println("-------------------------------------------------");
            writer.println(date.format(daysOfTheWeekFormatter) + " - " + date.format(dateFormatter));
            writer.println("-------------------------------------------------");
            writer.flush();

            int firstPeriod = clientRequest.getIdPeriodBegin() < PeriodManager.FIRST_WORK_PERIOD_ID ? PeriodManager.FIRST_WORK_PERIOD_ID : clientRequest.getIdPeriodBegin();
            // Fill the period tab with all existing periods
            for (int j = firstPeriod; j < clientRequest.getIdPeriodEnd(); ++j) {
                periods.add(j);
            }

            // Compare the current date of the inteval with the date of the first Timeslot in the list
            if (timeSlotList.size() != 0 && date.equals(timeSlotList.get(0).getDate().toLocalDate())) {
                // Loop on all TimeSlots of the list until the date of one of them is no longer equals to the previous one.
                for (int i = 0; i < timeSlotList.size(); ++i) {
                    timeSlotDate = timeSlotList.get(i).getDate().toLocalDate();
                    // Compare the date of the current TimeSlot with the previous one
                    if (i != 0 && !timeSlotDate.equals(timeSlotList.get(i - 1).getDate().toLocalDate())) {
                        break;
                    }
                    // Group all TimeSlots with the same date in a temp list
                    tmp.add(timeSlotList.get(i));
                }

                // Remove all the occupied periods from the period list
                for (TimeSlot t : tmp) {
                    periods.remove(Integer.valueOf(t.getIdPeriod()));
                }

                // Remove all the processed TimeSlots from the list
                timeSlotList.removeAll(tmp);
                tmp.clear();
            }

            // Write all the remaining periods in the file
            for (int period : periods) {
                writer.println(PeriodManager.PERIODS_START.get(period).toString() + " - " + PeriodManager.PERIODS_END.get(period).toString());
                writer.flush();
            }
            if (periods.isEmpty()) {
                writer.println(DisplayConstants.FILE_ALL_OCCUPIED_MESSAGE);
                writer.flush();
            }
            periods.clear();
        }
    }

    /**
     * Write in a file with the floor template style (free schedule for a particular floor)
     * @param timeSlotList  a list of all occupied periods of each classroom of the floor for the requested interval of dates
     * @param writer the writer that is used to write the information in the file
     */
    private static void writeToFileWithFloorTemplate(List<TimeSlot> timeSlotList, PrintWriter writer) {
        List<Integer> periods = new ArrayList<>();
        DateTimeFormatter daysOfTheWeekFormatter = DateTimeFormatter.ofPattern(ConfigLoader.getDaysOfWeekFormatter());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(ConfigLoader.getDateFormatter());
        // Write the header
        writer.println(DisplayConstants.FILE_FLOOR_TITLE + ": " + clientRequest.getFloor());
        writer.println("##################################################");
        writer.println();
        writer.flush();

        List<TimeSlot> tmp = new ArrayList<>();
        // Loop on all dates in the interval wanted by the user
        for (LocalDate date = clientRequest.getDateBegin().toLocalDate(); date.isBefore(clientRequest.getDateEnd().toLocalDate().plusDays(1)); date = date.plusDays(1)) {
            writer.println(date.format(daysOfTheWeekFormatter) + " - " + date.format(dateFormatter));
            writer.println("*************************************");
            writer.flush();
            // Recuperate classrooms in the requested floor and write the free period of all these classrooms in a file
            List<String> classrooms = ClassroomsByFloor.FLOORS_MAP.get(clientRequest.getFloor());
            writeFreePeriodsForFloor(writer, classrooms, date, timeSlotList, tmp, periods);
        }
    }

    /**
     * Write in a file with the default template style (free schedule for all classroom of all floor)
     * @param timeSlotList  a list of all occupied periods of all classroom in all floor for the requested interval of dates
     * @param writer the writer that is used to write the information in the file
     */
    private static void writeToFileWithDefaultTemplate(List<TimeSlot> timeSlotList, PrintWriter writer) {
        List<Integer> periods = new ArrayList<>();
        List<String> classrooms;
        DateTimeFormatter daysOfTheWeekFormatter = DateTimeFormatter.ofPattern(ConfigLoader.getDaysOfWeekFormatter());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(ConfigLoader.getDateFormatter());

        // Recuperate a list of the floors in function of the requested building
        List<String> floors = clientRequest.getBuilding() == 0 ? ClassroomsByFloor.FLOORS_CHESEAUX : ClassroomsByFloor.FLOORS_ST_ROCH;

        List<TimeSlot> tmp = new ArrayList<>();
        // Loop on all dates in the interval wanted by the user
        for (LocalDate date = clientRequest.getDateBegin().toLocalDate(); date.isBefore(clientRequest.getDateEnd().toLocalDate().plusDays(1)); date = date.plusDays(1)) {
            writer.println(date.format(daysOfTheWeekFormatter) + " - " + date.format(dateFormatter));
            writer.println("########################################");
            writer.println();
            writer.flush();

            // Loop on all floors of the building
            for (String floor : floors) {
                writer.println(DisplayConstants.FILE_FLOOR_TITLE + ": " + floor);
                writer.println("*************************");
                writer.println();
                writer.flush();

                // Recuperate classrooms in the current floor and Loop on all classroom
                classrooms = ClassroomsByFloor.FLOORS_MAP.get(floor);
                writeFreePeriodsForFloor(writer, classrooms, date, timeSlotList, tmp, periods);
            }
        }
    }

    /**
     * Write in a file the free periods of all classroom of a floor (write period in a schedule style like 08h30 - 09h15)
     * @param writer the writer that is used to write the information in the file
     * @param classrooms  a list of all classrooms of the floor
     * @param date the date that is used to show if there is free periods for the classrooms
     * @param timeSlotList  a list of all occupied periods of the classrooms requested by the user
     * @param tmp  an empty temporary list that will be used to store all occupied periods that are linked to the same classroom
     * @param periods  an empty list that will store all useful and interesting periods ids according to the user request
     */
    private static void writeFreePeriodsForFloor(PrintWriter writer, List<String> classrooms, LocalDate date, List<TimeSlot> timeSlotList, List<TimeSlot> tmp, List<Integer> periods) {
        for (String classroom : classrooms) {
            writer.println(DisplayConstants.FILE_CLASSROOM_TITLE + ": " + classroom);
            writer.println("--------------");

            int beginPeriod = clientRequest.getIdPeriodBegin() < PeriodManager.FIRST_WORK_PERIOD_ID ? PeriodManager.FIRST_WORK_PERIOD_ID : clientRequest.getIdPeriodBegin();
            // Fill the period tab with all existing periods
            for (int j = beginPeriod; j < clientRequest.getIdPeriodEnd(); ++j) {
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

            if (periods.isEmpty()) {
                writer.println(DisplayConstants.FILE_ALL_OCCUPIED_MESSAGE);
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

    /**
     * Remove from the periods list all the occupied periods of the classroom that are store in the temporary list
     * @param timeSlotList  a list of all occupied periods of the classrooms requested by the user
     * @param tmp  an empty temporary list that will be used to store all occupied periods that are linked to the same classroom
     * @param periods a list that store all useful and interesting periods ids according to the user request
     */
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

    /**
     * Remove from the periods list all the occupied periods of the classroom that are store in the temporary list
     * @param file  a list of all occupied periods of the classrooms requested by the user
     */
    private static void openFileInTextEditor(File file) throws IOException {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            String cmd = "rundll32 url.dll,FileProtocolHandler " + file.getCanonicalPath();
            Runtime.getRuntime().exec(cmd);
        }
        else {
            String cmd = "nano " + file.getCanonicalPath();
            Runtime.getRuntime().exec(cmd);
        }
    }

    /**Return the closest free room from the user position
     *
     * @param classRoom the current user classroom
     * @return the closest free classroom
     */
    public static ClassRoom closestClassroom(ClassRoom classRoom) {

        ClassRoom closestRoom = null;
        String floor = classRoom.floor();
        int currentPeriod = PeriodManager.currentOrNextPeriod(LocalTime.now());

        // a timeslot representing the given classroom at the current date + period
        TimeSlot ts = new TimeSlot(classRoom.getClassRoom(), System.currentTimeMillis(), currentPeriod);
        Map<String, Integer> mapReceived = null;

        try {
            mapReceived = floorRequestWithFreeRoom(floor, false, ts);
        } catch (IOException e){
            e.printStackTrace();
        }

        Map.Entry<String,Integer> firstEntry = mapReceived.entrySet().iterator().next();
        String currentFloor = firstEntry.getKey().substring(0,1);
        System.out.println("current floor = " +currentFloor);
        double difference = Double.MAX_VALUE;

        // iterate on the official classroom list in the current floor
        List<String> roomList = ClassroomsByFloor.FLOORS_MAP.get(currentFloor);

        for(String room : roomList){
            if(mapReceived.containsKey(room) && mapReceived.get(room) > 0){
                double currentDifference = Math.abs(roomList.indexOf(room)-roomList.indexOf(classRoom.getClassRoom()));
                ClassRoom currentRoom = new ClassRoom(room);
                if(currentDifference < difference){
                    difference = currentDifference;
                    closestRoom = currentRoom;
                }
            }
        }
        return closestRoom;
    }

    /**
     * Find the closest floor from a given classroom, with at least one room free.
     * @param floor : the floor to search
     * @param floorAChecked : if we have checked floor A, we have to check upper floors
     * @return a map containing a key classroom with a value representing the number of free periods
     * @throws IOException if an I/O error occurs
     */
    private static Map<String, Integer> floorRequestWithFreeRoom(String floor, boolean floorAChecked,
                                                                 TimeSlot ts) throws IOException {

        Map<String, Integer> mapReceived = Controller.handleClientFloorRequest(ts);

        if(mapReceived.isEmpty()){
            int currentFloorIndex = ClassroomsByFloor.FLOORS_MAP.get(floor).indexOf(floor);
            if(floorAChecked && floor != "K"){
                // do floor++
                floor = ClassroomsByFloor.FLOORS_MAP.get(floor).get(currentFloorIndex+1);
            } else if (floor != "A") {
                // do floor--
                floor = ClassroomsByFloor.FLOORS_MAP.get(floor).get(currentFloorIndex-1);
            } else {
                floorAChecked = true;
                floor = ClassroomsByFloor.FLOORS_MAP.get(floor).get(currentFloorIndex+1);
            }
            String roomFloorReference = ClassroomsByFloor.FLOORS_MAP.get(floor).get(0);
            ts.setClassroom(roomFloorReference);
            mapReceived = floorRequestWithFreeRoom(floor, floorAChecked, ts);
        }
        return mapReceived;
    }
}

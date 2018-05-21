package controller;

import utils.FloorFreePeriodMap;
import models.AdvancedRequest;
import models.ClassRoom;
import models.TimeSlot;
import network.client.ClientSocket;
import network.protocol.Protocol;
import utils.PeriodManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
        createFile(result, data);


        return client.receiveTimeSlots();
    }


    public static List<TimeSlot> handleClientAdvancedRequest(List<AdvancedRequest> data) throws IOException {
        client.connect(Protocol.SERVER_IP, Protocol.DEFAULT_PORT);
        client.askForAdvancedRequest(data);
        return client.receiveTimeSlots();
    }

    private static void createFile(List<TimeSlot> timeSlotList, ClassRoom clientRequest) throws FileNotFoundException, UnsupportedEncodingException {

        List<Integer> periods = new ArrayList<>();
        for (int i = 3; i < PeriodManager.PERIODS_START.size() - 1; ++i) {
            periods.add(i);
        }

        for (TimeSlot t : timeSlotList) {
            periods.remove(t.getIdPeriod());
        }

        PrintWriter writer = new PrintWriter("classroom-request.txt", "UTF-8");
        writer.println("Classe: " + clientRequest.getClassRoom());
        writer.println("-------------------------------------------------");
        writer.flush();
        for (int period : periods) {
            writer.println(PeriodManager.PERIODS_START.get(period).toString() + " - " + PeriodManager.PERIODS_END.get(period).toString());
            writer.flush();
        }

    }
}

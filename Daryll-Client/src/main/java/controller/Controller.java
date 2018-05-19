package controller;

import Utils.FloorFreePeriodMap;
import models.ClassRoom;
import models.TimeSlot;
import network.client.ClientSocket;
import network.protocol.Protocol;

import java.io.IOException;
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

    public static List<TimeSlot> handleClientClassroomRequest(TimeSlot data) throws IOException {
        client.connect(Protocol.SERVER_IP, Protocol.DEFAULT_PORT);
        client.askForClassroom(new ClassRoom(data.getClassroom()));
        return client.receiveTimeSlots();
    }



}

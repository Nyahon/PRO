package controller;

import utils.FloorFreePeriodMap;
import models.AdvancedRequest;
import models.ClassRoom;
import models.TimeSlot;
import network.client.ClientSocket;
import network.protocol.Protocol;

import java.io.File;
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
        List<TimeSlot> result = client.receiveTimeSlots();


        return client.receiveTimeSlots();
    }


    public static List<TimeSlot> handleClientAdvancedRequest(List<AdvancedRequest> data) throws IOException {
        client.connect(Protocol.SERVER_IP, Protocol.DEFAULT_PORT);
        client.askForAdvancedRequest(data);
        return client.receiveTimeSlots();
    }

    private File createFile(List<TimeSlot> t) {
        File file = new File();


        return file;
    }
}

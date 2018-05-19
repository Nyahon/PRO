package controller;

import models.ClassRoom;
import models.TimeSlot;
import network.client.ClientSocket;
import network.protocol.Protocol;

import java.io.IOException;
import java.util.List;

public class Controller {

    private static final ClientSocket client = new ClientSocket();

    public static List<TimeSlot> handleClientFloorRequest(TimeSlot data) throws IOException {
        client.connect(Protocol.SERVER_IP, Protocol.DEFAULT_PORT);
        client.askForFloor(data);
        return client.receiveTimeSlots();
    }

    public static List<TimeSlot> handleClientClassroomRequest(TimeSlot data) throws IOException {
        client.connect(Protocol.SERVER_IP, Protocol.DEFAULT_PORT);
        client.askForClassroom(new ClassRoom(data.getClassroom()));
        return client.receiveTimeSlots();
    }



}

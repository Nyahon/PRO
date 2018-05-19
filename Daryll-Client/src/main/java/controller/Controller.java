package controller;

import models.TimeSlot;
import network.client.ClientSocket;
import network.protocol.Protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private static final ClientSocket client = new ClientSocket();

    public static List<TimeSlot> handleClientRequest(TimeSlot data) throws IOException {
        client.connect(Protocol.SERVER_IP, Protocol.DEFAULT_PORT);
        client.askForClassRoom(data);
        return client.receiveClassRooms();
    }
}

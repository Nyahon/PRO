package controller;

import models.TimeSlot;
import network.client.ClientSocket;
import network.protocol.Protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    public static void sendGUIdata(TimeSlot data) throws IOException {
        ClientSocket client = new ClientSocket();
        client.connect(Protocol.SERVER_IP, Protocol.DEFAULT_PORT);
        client.askForClassRoom(data);
    }

    public static List<TimeSlot> handleClientRequest(TimeSlot data) throws IOException {
        List<TimeSlot> response = new ArrayList<>();
        sendGUIdata(data);
        return response;
    }
}

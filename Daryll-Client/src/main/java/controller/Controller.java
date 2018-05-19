package controller;

import models.TimeSlot;
import network.client.ClientSocket;
import network.protocol.protocol;
import org.python.netty.handler.ssl.ApplicationProtocolConfig;

import java.io.IOException;

public class Controller {
    public void getGUIData(TimeSlot data) throws IOException {
        ClientSocket client = new ClientSocket();
        client.connect(protocol.SERVER_IP, protocol.DEFAULT_PORT);
        client.askForClassRoom(data);
    }
}

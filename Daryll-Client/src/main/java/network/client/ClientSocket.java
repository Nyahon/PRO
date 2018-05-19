package network.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import models.AdvancedRequest;
import network.protocol.Protocol;
import models.ClassRoom;
import network.serialisation.JsonObjectMapper;
import models.TimeSlot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ClientSocket {


    protected static final Logger LOG = Logger.getLogger(ClientSocket.class.getName());
    protected BufferedReader is;
    protected PrintWriter os;
    protected Socket socket;


    public void connect(String server, int port) throws IOException {

        socket = new Socket(server, port);
        is = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
        os = new PrintWriter( socket.getOutputStream() );
        //   String resp = is.readLine();
        //   System.out.println( resp );
        if( !is.readLine().equals(Protocol.RESPONSE_CONNECTED) )
            throw new IOException("error during connection");

        LOG.info("Connected to ___ serialisation.server " + server + " on port " + port + "." );



    }

    public void disconnect() throws IOException {

        os.println(Protocol.CMD_BYE);
        os.flush();

        is.close();
        os.close();
        socket.close();
        LOG.info("Disconnected from serialisation.server.");
    }

    public boolean isConnected() {

        return socket != null;


    }

    public void askForClassroom(ClassRoom c) throws JsonProcessingException, ConnectException, IOException{
        os.println(Protocol.CMD_CLASSROOM);
        os.flush();
        String rsp;
        if( !(rsp = is.readLine() ).equals(Protocol.RESPONSE_CLASSROOM) )
            throw new ConnectException(rsp);

        os.println(JsonObjectMapper.toJson( c ));
        os.flush();

    }

    public void askForFloor(TimeSlot t) throws IOException {
        os.println(Protocol.CMD_FLOOR);
        os.flush();
        String rsp;
        if( !(rsp = is.readLine() ).equals(Protocol.RESPONSE_TIMESLOT) )
            throw new ConnectException(rsp);
        os.println(JsonObjectMapper.toJson(t));
        os.flush();
    }

    public void askForAdvancedRequest(List<AdvancedRequest> a) throws IOException {
        os.println(Protocol.CMD_ADVANCED);
        os.flush();
        String rsp;
        if( !(rsp = is.readLine() ).equals(Protocol.RESPONSE_ADVANCED) )
            throw new ConnectException(rsp);

        for (AdvancedRequest request : a) {
            os.println(JsonObjectMapper.toJson(request));
            os.flush();
        }
        os.println(Protocol.RESPONSE_OK);
    }

    public List<TimeSlot> receiveTimeSlots() throws IOException {
        List<TimeSlot> response = new ArrayList<>();
        String rsp;
        while (!(rsp = is.readLine()).equals(Protocol.RESPONSE_OK)) {
            response.add(JsonObjectMapper.parseJson(rsp, TimeSlot.class));
        }
        return response;
    }


}

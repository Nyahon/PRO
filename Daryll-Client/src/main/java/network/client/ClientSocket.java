package network.client;

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

/**
 * The class that make the connection between the client and the server
 *
 * @author Loïc Frueh
 * @author Yohann Meyer
 */
public class ClientSocket {

    /**
     * The Logger that shows information about the connection
     */
    protected static final Logger LOG = Logger.getLogger(ClientSocket.class.getName());
    /**
     * A reader that read the data send by the server
     */
    protected BufferedReader is;
    /**
     * A writer that send the data to the server
     */
    protected PrintWriter os;
    /**
     * The client socket that binds with the server socket to establish the connection
     */
    protected Socket socket;


    /**
     * Make the connection between client and server in order to send datas
     * @param server the adresse of the listening server
     * @param port the port on which the server is listening
     */
    public void connect(String server, int port) throws IOException {

        socket = new Socket(server, port);
        is = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
        os = new PrintWriter( socket.getOutputStream() );
        if( !is.readLine().equals(Protocol.RESPONSE_CONNECTED) )
            throw new IOException("error during connection");

        LOG.info("Connected to DARYLL server " + server + " on port " + port + "." );
    }

    /**
     *  Close the connection between the client and the server
     */
    public void disconnect() throws IOException {

        os.println(Protocol.CMD_BYE);
        os.flush();

        is.close();
        os.close();
        socket.close();
        LOG.info("Disconnected from serialisation.server.");
    }

    /**
     * Check if the client is connected with the server or not
     * @return true if it is and false if not
     */
    public boolean isConnected() {

        return socket != null;


    }

    /**
     * Send the protocol command to the server for a classroom request and send the data.
     * @param c the classroom from the request of the user
     */
    public void askForClassroom(ClassRoom c) throws IOException{
        os.println(Protocol.CMD_CLASSROOM);
        os.flush();
        String rsp;
        if( !(rsp = is.readLine() ).equals(Protocol.RESPONSE_CLASSROOM) )
            throw new ConnectException(rsp);

        os.println(JsonObjectMapper.toJson( c ));
        os.flush();

    }

    /**
     * Send the protocol command for a floor request and send the data
     * @param t the floor, date and time of the request in one Object
     */
    public void askForFloor(TimeSlot t) throws IOException {
        os.println(Protocol.CMD_FLOOR);
        os.flush();
        String rsp;
        if( !(rsp = is.readLine() ).equals(Protocol.RESPONSE_TIMESLOT) )
            throw new ConnectException(rsp);
        os.println(JsonObjectMapper.toJson(t));
        os.flush();
    }

    /**
     * Send the protocol command for an advanced equest and send the data
     * @param a the building, floor, classroom, start date, end date, start time and end time of the request in one Object
     */
    public void askForAdvancedRequest(AdvancedRequest a) throws IOException {
        os.println(Protocol.CMD_ADVANCED);
        os.flush();
        String rsp;
        if( !(rsp = is.readLine() ).equals(Protocol.RESPONSE_ADVANCED) )
            throw new ConnectException(rsp);

        os.println(JsonObjectMapper.toJson(a));
        os.flush();
    }

    /**
     * Recieve the result of a request send by the client and processed by the server.
     * @return a list of all occupied periods for all classrooms at the dates and times requested bye the user.
     */
    public List<TimeSlot> receiveTimeSlots() throws IOException {
        List<TimeSlot> response = new ArrayList<>();
        String rsp;
        while (!(rsp = is.readLine()).equals(Protocol.RESPONSE_OK)) {
            System.out.println("response = " +rsp);
            response.add(JsonObjectMapper.parseJson(rsp, TimeSlot.class));
        }
        return response;
    }


}

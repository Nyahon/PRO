package serialisation.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import serialisation.data.ClassRoom;
import serialisation.data.JsonObjectMapper;
import serialisation.data.TimeSlot;
import serialisation.protocol.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
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
        if( !is.readLine().equals(protocol.RESPONSE_CONNECTED) )
            throw new IOException("error during connection");

        LOG.info("Connected to ___ serialisation.server " + server + " on port " + port + "." );



    }

    public void disconnect() throws IOException {

        os.println(protocol.CMD_BYE);
        os.flush();

        is.close();
        os.close();
        socket.close();
        LOG.info("Disconnected from serialisation.server.");
    }

    public boolean isConnected() {

        return socket != null;


    }

    public void askForTimeSlot(ClassRoom c) throws JsonProcessingException, ConnectException, IOException{
        os.println(protocol.CMD_CLASSROOM);
        os.flush();
        String rsp;
        if( !(rsp = is.readLine() ).equals(protocol.RESPONSE_CLASSROOM) )
            throw new ConnectException(rsp);

        os.println(JsonObjectMapper.toJson( c ));
        os.flush();

    }

    public void askForClassRoom(List<TimeSlot> t){

    }



}

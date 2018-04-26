package server;
import data.ClassRoom;
import data.JsonObjectMapper;
import data.TimeSlot;
import protocol.protocol;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class implements the protocol
 *
 * @author Yohann Meyer
 */
public class ServerClientHandler implements IClientHandler {
    final static Logger LOG = Logger.getLogger(ServerClientHandler.class.getName());

    public int numberOfNewStudents = 0;
    public int numberOfCommands = 0;
    private List<TimeSlot> queryTimeSlots;
    private List<ClassRoom> queryClassRooms;



    @Override
    public void handleClientConnection(InputStream is, OutputStream os) throws IOException {

        //TODO check les success comme il faut
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(os));

        writer.println(protocol.RESPONSE_CONNECTED);
        writer.flush();

        String command;
        boolean done = false;
        while (!done && ((command = reader.readLine()) != null)) {

            LOG.log(Level.INFO, "COMMAND: {0}", command);
            switch (command.toUpperCase()) {
                case protocol.CMD_CLASSROOM:

                    try {
                        getClassRoom(reader, writer);
                    }catch (Exception e){ //TODO BETTER
                        LOG.log(Level.SEVERE, "Error trying to get classroom: ", e);
                    }
                    break;
                case protocol.CMD_TIMESLOT:

                    try {
                        getTimeSlots(reader, writer);
                    }catch (Exception e){ //TODO BETTER
                        LOG.log(Level.SEVERE, "Error trying to get free time slots: ", e);
                    }
                    break;

                case protocol.CMD_BYE:

                    writer.println(protocol.RESPONSE_BYE);
                    writer.flush();
                    done = true;
                    break;

                default:

                    writer.println("You absolute spoon, there's a protocol.");
                    writer.flush();
                    break;
            }
            writer.flush();
        }

    }

    private void getClassRoom(BufferedReader reader, PrintWriter writer) throws IOException, ClassNotFoundException {

        writer.println(protocol.RESPONSE_CLASSROOM);
        writer.flush();

        queryTimeSlots = JsonObjectMapper.parseJsonArray( reader.lines().collect(Collectors.joining()),  TimeSlot.class );

        /** CALL DB with query **/
        /** TODO HERE COME DB CONNEXION **/
        /** GET response    **/

        writer.println(protocol.RESPONSE_OK);
        writer.flush();

        /*
        String jsonResponse = createJson(response);
        writer.println(jsonResponse);
        writer.flush();
        */
    }

    private void getTimeSlots(BufferedReader reader, PrintWriter writer) throws IOException, ClassNotFoundException {

        writer.println(protocol.RESPONSE_TIMESLOT);
        writer.flush();

        //queryClassRooms = JsonObjectMapper.parseJson( reader.lines().collect(Collectors.joining()),  ClassRoom.class );

        /** CALL DB with query **/
        /** TODO HERE COME DB CONNEXION **/
        /** GET response    **/

        writer.println(protocol.RESPONSE_OK);
        writer.flush();
        /*
        String jsonResponse = createJson(response);
        writer.println(jsonResponse);
        writer.flush();
        */

    }

}

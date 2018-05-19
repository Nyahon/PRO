package network.server;
import models.ClassRoom;
import network.serialisation.JsonObjectMapper;
import models.TimeSlot;
import database.ToolBoxMySQL;
import network.protocol.protocol;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class implements the serialisation.server.protocol
 *
 * @author Yohann Meyer, Romain Gallay
 */
public class ServerClientHandler implements IClientHandler {
    final static Logger LOG = Logger.getLogger(ServerClientHandler.class.getName());

    public int numberOfNewStudents = 0;
    public int numberOfCommands = 0;
    private List<TimeSlot> queryTimeSlots;
    private List<ClassRoom> queryClassRooms;
    private ToolBoxMySQL toolBoxMySQL;

    public ServerClientHandler(){
        toolBoxMySQL = new ToolBoxMySQL();
    }


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
                case protocol.CMD_FLOOR:

                    try {
                        floorSchedule(reader, writer);
                    }catch (Exception e){ //TODO BETTER
                        LOG.log(Level.SEVERE, "Error trying to get free time slots for a whole floor: ", e);
                    }

                case protocol.CMD_BYE:

                    writer.println(protocol.RESPONSE_BYE);
                    writer.flush();
                    done = true;
                    break;

                default:

                    writer.println("You absolute spoon, there's a serialisation.server.protocol.");
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

    private void floorSchedule(BufferedReader reader, PrintWriter writer) throws IOException{

        writer.println(protocol.RESPONSE_FLOOR);
        writer.flush();

        // get the TimeSlot sent by client and parse it from Json
        TimeSlot timeSlotReferenceFloor = JsonObjectMapper.parseJson(reader.readLine(), TimeSlot.class);
        toolBoxMySQL.initConnection();
        ArrayList<TimeSlot> floorSchedule = null;

        // request the schedule of every classroom in the same floor
        try {
            floorSchedule = toolBoxMySQL.occupiedRoomsAtGivenSchedule(timeSlotReferenceFloor);
        } catch (SQLException e){
            LOG.log(Level.SEVERE, "Error during database query to get schedule for a whole floor", e);
        }

        // send every TimeSlot as Json to client
        for(TimeSlot ts : floorSchedule){
            writer.println(JsonObjectMapper.toJson(ts));
        }

        writer.println(protocol.RESPONSE_OK);
        writer.flush();
    }

    private void classRoomSchedule(BufferedReader reader, PrintWriter writer) throws IOException, ClassNotFoundException {

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

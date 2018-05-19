package network.server;
import models.ClassRoom;
import network.serialisation.JsonObjectMapper;
import models.TimeSlot;
import database.ToolBoxMySQL;
import network.protocol.ProtocolServer;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class implements the serialisation.server.ProtocolServer
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
    private boolean done;

    public ServerClientHandler(){
        toolBoxMySQL = new ToolBoxMySQL();
    }

    @Override
    public void handleClientConnection(InputStream is, OutputStream os) throws IOException {

        //TODO check les success comme il faut
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(os));

        writer.println(ProtocolServer.RESPONSE_CONNECTED);
        writer.flush();

        String command;
        done = false;
        while (!done && ((command = reader.readLine()) != null)) {

            LOG.log(Level.INFO, "COMMAND: {0}", command);
            switch (command.toUpperCase()) {
                case ProtocolServer.CMD_CLASSROOM:

                    try {
                        classRoomSchedule(reader, writer);
                    }catch (Exception e){ //TODO BETTER
                        LOG.log(Level.SEVERE, "Error trying to get a classroom schedule: ", e);
                    }
                    break;
                case ProtocolServer.CMD_TIMESLOT:

                    try {
                        getTimeSlots(reader, writer);
                    }catch (Exception e){ //TODO BETTER
                        LOG.log(Level.SEVERE, "Error trying to get free time slots: ", e);
                    }
                    break;
                case ProtocolServer.CMD_FLOOR:

                    try {
                        floorSchedule(reader, writer);
                    }catch (Exception e){ //TODO BETTER
                        LOG.log(Level.SEVERE, "Error trying to get free time slots for a whole floor: ", e);
                    }
                    break;
                case ProtocolServer.CMD_INITDATABASE:

                    try {
                        initDatabase(reader, writer);
                    }catch (Exception e){ //TODO BETTER
                        LOG.log(Level.SEVERE, "Error trying to init database: ", e);
                    }
                    break;

                case ProtocolServer.CMD_BYE:

                    writer.println(ProtocolServer.RESPONSE_BYE);
                    writer.flush();
                    done = true;
                    break;

                default:

                    writer.println("Wrong command");
                    writer.flush();
                    done = true;
                    break;
            }
            writer.flush();
        }

    }

    private void getTimeSlots(BufferedReader reader, PrintWriter writer) throws IOException, ClassNotFoundException {

        writer.println(ProtocolServer.RESPONSE_TIMESLOT);
        writer.flush();

        //queryClassRooms = JsonObjectMapper.parseJson( reader.lines().collect(Collectors.joining()),  ClassRoom.class );

        /** CALL DB with query **/
        /** TODO HERE COME DB CONNEXION **/
        /** GET response    **/

        writer.println(ProtocolServer.RESPONSE_OK);
        writer.flush();
        /*
        String jsonResponse = createJson(response);
        writer.println(jsonResponse);
        writer.flush();
        */

    }

    private void floorSchedule(BufferedReader reader, PrintWriter writer) throws IOException{

        writer.println(ProtocolServer.RESPONSE_FLOOR);
        writer.flush();

        // get the TimeSlot sent by client and parse it from Json
        TimeSlot timeSlotReferenceFloor = JsonObjectMapper.parseJson(reader.readLine(), TimeSlot.class);
        toolBoxMySQL.initConnection();
        ArrayList<TimeSlot> floorSchedule = null;

        // request the schedule of every classroom in the same floor
        floorSchedule = toolBoxMySQL.occupiedRoomsAtGivenSchedule(timeSlotReferenceFloor);

        // send every TimeSlot as Json to client
        for(TimeSlot ts : floorSchedule){
            writer.println(JsonObjectMapper.toJson(ts));
        }

        writer.println(ProtocolServer.RESPONSE_OK);
        writer.flush();
    }

    private void classRoomSchedule(BufferedReader reader, PrintWriter writer) throws IOException {

        writer.println(ProtocolServer.RESPONSE_CLASSROOM);
        writer.flush();

        // get the ClassRoom sent by client and parse it from Json
        ClassRoom classRoom = JsonObjectMapper.parseJson(reader.readLine(), ClassRoom.class);
        toolBoxMySQL.initConnection();
        ArrayList<TimeSlot> classRoomSchedule = null;

        // query the schedule of the classroom
        classRoomSchedule = toolBoxMySQL.classRoomSchedule(classRoom);

        // send every TimeSlot as Json to client
        for(TimeSlot ts : classRoomSchedule){
            writer.println(JsonObjectMapper.toJson(ts));
        }
        writer.println(ProtocolServer.RESPONSE_OK);
        writer.flush();
    }

    private void initDatabase(BufferedReader reader, PrintWriter writer) throws IOException {
        int tries = 0;
        final int maxTry = 5;
        String password = "root";
        writer.println("Enter password :");
        writer.flush();

        // user has maxTry tries to enter the right password
        while(!reader.readLine().equals(password) && tries < maxTry){
            if(tries < 4) {
                writer.println("Wrong password, " + (maxTry - tries) + " tries left");
            } else {
                writer.println("Wrong password, " + (maxTry - tries) + " try left");
            }
            writer.flush();
            tries++;
        }

        // if user has tried 5 password, then disconnect him
        if(tries == maxTry){
            writer.println("disconnected");
            writer.flush();
            done = true;
            return;
        }
        toolBoxMySQL.initDatabase();
        writer.println("Database initialization successful !");
        writer.flush();
        done = true;
    }
}

package network.server;
import models.AdvancedRequest;
import models.ClassRoom;
import network.serialisation.JsonObjectMapper;
import models.TimeSlot;
import database.ToolBoxMySQL;
import network.protocol.ProtocolServer;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A manager to handle a client connexion, process its commands and pass them to a ToolBoxMySQL object.
 *
 * @author Yohann Meyer
 * @author Romain Gallay
 */
public class ServerClientHandler implements IClientHandler {

    /**
     * A Logger to print messages in console
     */
    private final static Logger LOG = Logger.getLogger(ServerClientHandler.class.getName());

    /**
     * A ToolBoxMySQL object to pass the client requests to a MySQL database.
     */
    private ToolBoxMySQL toolBoxMySQL;

    /**
     * Represents the state of the client connexion.
     */
    private boolean done;

    /**
     * the file name of the ICS to parse
     */
    private static final String fileNameICS = "gaps_global_S2_2017_2018.ics";

    /**
     * Default constructor
     */
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

                case ProtocolServer.CMD_FLOOR:
                    try {
                        floorSchedule(reader, writer);
                    }catch (Exception e){ //TODO BETTER
                        LOG.log(Level.SEVERE, "Error trying to get occupied time slots for a whole floor: ", e);
                    }
                    break;

                case ProtocolServer.CMD_ADVANCED:

                    try {
                        advancedRequest(reader, writer);;
                    }catch (Exception e){ //TODO BETTER
                        LOG.log(Level.SEVERE, "Error trying to get occupied time slots during advanced request: " + e.getMessage());
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
                    writer.println("Unknown command");
                    writer.flush();
                    done = true;
                    break;
            }
            writer.flush();
        }
    }

    /**
     * Handle the protocol to retrieve the schedule of every room in a given floor
     *
     * @param reader    BufferedReader to read commands sent by client
     * @param writer    PrintWriter to send responses back to the client
     * @throws java.io.IOException  if an I/O error occurs, or if the Json parsing fails
     */
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
        toolBoxMySQL.closeConnection();
    }

    /**
     * Handle the protocol to retrieve the schedule of a given room during the current day
     *
     * @param reader    BufferedReader to read commands sent by client
     * @param writer    PrintWriter to send responses back to the client
     * @throws java.io.IOException  if an I/O error occurs, or if the Json parsing fails
     */
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
        toolBoxMySQL.closeConnection();
    }

    /**
     * Handle the protocol to retrieve schedules through an advanced request.
     *
     * @param reader    BufferedReader to read commands sent by client
     * @param writer    PrintWriter to send responses back to the client
     * @throws java.io.IOException  if an I/O error occurs, or if the Json parsing fails
     */
    private void advancedRequest(BufferedReader reader, PrintWriter writer) throws IOException{
        writer.println(ProtocolServer.RESPONSE_ADVANCED);
        writer.flush();

        AdvancedRequest advancedRequest = JsonObjectMapper.parseJson(reader.readLine(), AdvancedRequest.class);
        System.out.println(advancedRequest);

        ArrayList<TimeSlot> answer = new ArrayList<>();
        toolBoxMySQL.initConnection();

        // if the advancedRequest contains a classroom
        if(advancedRequest.getClassroom() != null){
            answer.addAll(toolBoxMySQL.classroomAdvancedSchedule(advancedRequest));

            // if the advancedRequest contains a floor
        } else if(advancedRequest.getFloor() != null){
            answer.addAll(toolBoxMySQL.floorAdvancedSchedule(advancedRequest));

            // if the advancedRequest only contains the building
        } else {
            answer.addAll(toolBoxMySQL.buildingAdvancedSchedule(advancedRequest));
        }

        // send every TimeSlot as Json to client
        for(TimeSlot ts : answer){
            writer.println(JsonObjectMapper.toJson(ts));
        }
        LOG.log(Level.INFO, "sent !");
        toolBoxMySQL.closeConnection();

        writer.println(ProtocolServer.RESPONSE_OK);
        writer.flush();
    }

    /**
     * Handle a ssh or telnet connexion to populate the database with the help of a ToolBoxMySQL object
     *
     * @param reader    BufferedReader to read commands sent by client
     * @param writer    PrintWriter to send responses back to the client
     * @throws java.io.IOException  if an I/O error occurs
     */
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

        // if user has tried 5 passwords, then disconnect him
        if(tries == maxTry){
            writer.println("disconnected");
            writer.flush();
            done = true;
            return;
        }
        toolBoxMySQL.populateDatabase(writer, fileNameICS);
        writer.println("Updating database : success !" +
                "                                                                     ");
        writer.flush();
        done = true;
        toolBoxMySQL.closeConnection();
    }
}

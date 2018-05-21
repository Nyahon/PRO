package network.server;
import models.AdvancedRequest;
import models.ClassRoom;
import network.serialisation.JsonObjectMapper;
import models.TimeSlot;
import database.ToolBoxMySQL;
import network.protocol.ProtocolServer;
import utils.ReaderICS;

import java.io.*;
import java.sql.SQLException;
import java.sql.Time;
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

                case ProtocolServer.CMD_FLOOR:
                    try {
                        floorSchedule(reader, writer);
                    }catch (Exception e){ //TODO BETTER
                        LOG.log(Level.SEVERE, "Error trying to get occupied time slots for a whole floor: ", e);
                    }
                    break;

                case ProtocolServer.CMD_ADVANCED:

                    try {
                        advancedRequest(reader, writer);
                    }catch (Exception e){ //TODO BETTER
                        LOG.log(Level.SEVERE, "Error trying to get occupied time slots during advanced request: ", e);
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

    private void advancedRequest(BufferedReader reader, PrintWriter writer) throws IOException{
        writer.println(ProtocolServer.RESPONSE_ADVANCED);
        writer.flush();

        String answerJson;
        ArrayList<AdvancedRequest> advancedRequests = new ArrayList<>();

        while((answerJson = reader.readLine()) != ProtocolServer.RESPONSE_OK){
            advancedRequests.add(JsonObjectMapper.parseJson(answerJson, AdvancedRequest.class));
        }

        ArrayList<TimeSlot> answer = new ArrayList<>();

        // query the database to get the occupied timeslot
        for(AdvancedRequest ar : advancedRequests){

            // if the advancedRequest contains a classroom
            if(!ar.getClassroom().equals(null)){
                answer.addAll(toolBoxMySQL.classroomAdvancedSchedule(ar));

                // if the advancedRequest contains a floor
            } else if(!ar.getFloor().equals(null)){
                answer.addAll(toolBoxMySQL.floorAdvancedSchedule(ar));

                // if the advancedRequest only contains the building
            } else {
                answer.addAll(toolBoxMySQL.buildingAdvancedSchedule(ar));
            }
        }

        // send every TimeSlot as Json to client
        for(TimeSlot ts : answer){
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

        // if user has tried 5 passwords, then disconnect him
        if(tries == maxTry){
            writer.println("disconnected");
            writer.flush();
            done = true;
            return;
        }
        toolBoxMySQL.initDatabase(writer);
        writer.println(ReaderICS.getMessageUpdateDB() + "success !" +
                "                                                                     ");
        writer.flush();
        done = true;
    }
}

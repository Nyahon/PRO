package database;

import models.AdvancedRequest;
import models.ClassRoom;
import models.TimeSlot;
import utils.PeriodManager;
import utils.ReaderICS;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;

/**
 * A class to connect and perform actions on a MySQL database
 *
 * @author Dejvid Muaremi
 * @author Romain Gallay
 */
public class ToolBoxMySQL  {

    /**
     * Logger to deliver information or report errors
     */
    private static final Logger LOG = Logger.getLogger(ToolBoxMySQL.class.getName());

    /**
     * the database name
     */
    private static final String database = "daryll";

    /**
     * a MySQL account to access the database
     */
    private static final String account = "root";

    /**
     * the password of the MySQL account
     */
    private static final String password = "root";

    /**
     * the connexion to perform action on the MySQL database
     */
    private Connection connection;

    /**
     * a string containing a MySQL request
     */
    private String sql;

    /**
     * Default constructor
     */
    public ToolBoxMySQL(){}

    /**
     * A method initialize the connexion to the database
     */
    public void initConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/" + database + "?user=" + account + "&password=" + password;
            connection = DriverManager.getConnection(url);
        } catch(SQLException e){
            LOG.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
            closeConnection();
        }
    }

    /**
     * A method to populate the database with basic information and the information of an ICS file
     * @param writer    a PrintWriter to update the client on the progress of database populating
     * @param fileNameICS   the name of the ICS file to parse
     */
    public void populateDatabase(PrintWriter writer, String fileNameICS) {
        initConnection();
        insertPeriods();
        // insert default classrooomequipment
        insertClassroomEquipment(1, true,true,false,true);
        ReaderICS readerICS = new ReaderICS(this, writer);
        readerICS.readICSFile(fileNameICS);
        closeConnection();
    }

    /**
     * This method close the connection to the database
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LOG.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    /**
     * This method inserts the periods list into the database
     */
    public void insertPeriods() {
        LOG.info("Insertion of the periods...");
        ResultSet result;
        PreparedStatement ps;

        try (Statement statement = connection.createStatement()) {

            // Retrieve periods
            sql = "SELECT idPeriod " +
                    "FROM Period";
            result = statement.executeQuery(sql);

            sql = "call addPeriod(?,?,?)";
            ps = connection.prepareCall(sql);
            for (int i = 0; i < PeriodManager.PERIODS_START.size(); i += 1) {
                if(!result.next()){
                    ps.setInt(1, i); // idPeriod (0 to n)
                    ps.setTime(2, Time.valueOf(PeriodManager.PERIODS_START.get(i)));
                    ps.setTime(3, Time.valueOf(PeriodManager.PERIODS_END.get(i)));

                    ps.executeUpdate();
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            LOG.log(Level.SEVERE, e.getMessage());
            closeConnection();
        }
    }

    /**
     * This method inserts a new Classroom element into the database
     * @param classroomName defines the name of the new Classroom
     * @param isLocked defines if the room is locked or not
     * @param idClassroomEquipment defines the reference to an element of the ClassroomEquipment table
     *
     */
    private void insertClassroom(String classroomName, boolean isLocked, int place, int idClassroomEquipment) {
        LOG.info("Insertion Classroom...");
        ResultSet result;
        PreparedStatement ps;

        try (Statement statement = connection.createStatement()) {

            // Insert Floor (do nothing if it already exists)
            insertFloor(classroomName.substring(0, 1), place);

            // Check if the classroom already exists
            sql = "SELECT classroomName " +
                    "FROM Classroom AS CR " +
                    "WHERE CR.classroomName=?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, classroomName);
            result = ps.executeQuery();

            // Add the classroom if it doesn't exists
            if (!result.next()) {
                sql = "call addClassroom(?,?,?,?)";
                ps = connection.prepareCall(sql);
                ps.setString(1, classroomName);
                ps.setBoolean(2, isLocked);
                ps.setString(3, classroomName.substring(0, 1));
                ps.setInt(4, idClassroomEquipment);
                ps.executeUpdate();
            }
        } catch (SQLException e){
            e.printStackTrace();
            LOG.log(Level.SEVERE, e.getMessage());
            closeConnection();
        }
    }

    /**
     * This method inserts new Classroom elements according to the list
     * @param listClassrooms list of classrooms to add
     */
    public void insertClassrooms(String[] listClassrooms, int place) {
        LOG.info("Insertion Classrooms...");

        try (Statement statement = connection.createStatement()) {

            // Insert defaut ClassRoomEquipment if it doesn't exists
            insertClassroomEquipment(1, true,true,false,true);

            for (int i = 0; i < listClassrooms.length; ++i) {
                insertClassroom(listClassrooms[i], false, place, 1);
            }
        } catch (SQLException e){
            e.printStackTrace();
            LOG.log(Level.SEVERE, e.getMessage());
            closeConnection();
        }
    }

    /**
     * This method inserts a classroom with its corresponding floor level
     * @param classname the name of the classroom to add
     */
    public void insertClassroomWithCheck(String classname){
        char levelChar = classname.charAt(0);
        int levelInt;

        if(Character.toLowerCase(levelChar) > 'k'){
            levelInt = 1;
        } else {
            levelInt = 0;
        }

        insertClassroom(classname, false, levelInt, 1);

    }
    /**
     * This method inserts a new Classroom element into the database
     * @param idClassroomEquipment id of the ClassroomEquipment
     * @param beamer provided?
     * @param electricalSocket provided?
     * @param computers provided?
     * @param board provided?
     *
     */
    public void insertClassroomEquipment(int idClassroomEquipment, boolean beamer, boolean electricalSocket,
                                          boolean computers, boolean board) {
        LOG.info("insert new ClassroomEquipment...");
        ResultSet result = null;

        try (Statement statement = connection.createStatement()) {

            // Test if default ClassRoomEquipment exists
            sql = "SELECT *" +
                    "FROM ClassroomEquipment AS CE " +
                    "WHERE CE.idClassroomEquipment=" + idClassroomEquipment;
            result = statement.executeQuery(sql);

            if (!result.next()) {
                PreparedStatement ps;
                sql = "call addClassroomEquipment (?,?,?,?);";
                ps = connection.prepareCall(sql);

                //ps.setInt(1, idClassroomEquipment);
                ps.setBoolean(1, beamer);
                ps.setBoolean(2, electricalSocket);
                ps.setBoolean(3, computers);
                ps.setBoolean(4, board);

                ps.executeUpdate();
            }
        } catch (SQLException e){
            e.printStackTrace();
            LOG.log(Level.SEVERE, e.getMessage());
            closeConnection();
        }
    }

    /**
     * This method inserts a new floor element into the database
     * @param floorName name of the floor
     * @param place defines the place (number)
     */
    private void insertFloor(String floorName, int place) throws SQLException {
        LOG.info("insert new Floor...");
        ResultSet result;
        PreparedStatement ps;

        try (Statement statement = connection.createStatement()) {

            // Check if the floor is already in the database
            sql = "SELECT floorName " +
                    "FROM Floor AS F " +
                    "WHERE F.floorName=?";

            ps = connection.prepareStatement(sql);
            ps.setString(1, floorName);
            result = ps.executeQuery();

            if (!result.next()) {
                sql = "call addFloor(?,?);";
                ps = connection.prepareCall(sql);
                ps.setString(1, floorName);
                ps.setInt(2, place);

                ps.executeUpdate();
            }
        }
    }

    /**
     * This method inserts a new floor equipment into the database
     * @param toiletM define if men toilets are available on the floor
     * @param toiletF define if women toilets are available
     * @param coffeeMachine define if a selecta is available
     * @param selecta define if a selecta is available
     * @param wayOut define if there is an exit.
     *
     */
    public void insertFloorEquipment(boolean toiletM, boolean toiletF, boolean coffeeMachine, boolean selecta, boolean wayOut) {
        LOG.info("insert new FloorEquipment...");
        PreparedStatement ps;

        try (Statement statement = connection.createStatement()) {

            sql = "call addFloorEquipment(?,?,?,?,?);";
            ps = connection.prepareCall(sql);

            ps.setBoolean(1, toiletM);
            ps.setBoolean(2, toiletF);
            ps.setBoolean(3, coffeeMachine);
            ps.setBoolean(4, selecta);
            ps.setBoolean(5, wayOut);

            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            LOG.log(Level.SEVERE, e.getMessage());
            closeConnection();
        }
    }

    /**
     * This method inserts a new couple of Period and Classroom (TakePlace table) into the database
     * @param date name of the floor
     * @param idPeriod id of the linked element of the Period table
     * @param classroomName the classroom name
     * */
    public void insertTakePlace(Date date, int idPeriod, String classroomName) {
        LOG.info("Insertion of a new TakePlace element...");

        try (Statement statement = connection.createStatement()) {
            ResultSet result;
            PreparedStatement ps;
            // Test if the TakePlace given already exists
            sql = "SELECT * " +
                    "FROM TakePlace AS TP " +
                    "WHERE TP.date=? AND TP.idPeriod=? AND TP.classroomName=?";

            ps = connection.prepareStatement(sql);
            ps.setDate(1, date);
            ps.setInt(2, idPeriod);
            ps.setString(3, classroomName);
            result = ps.executeQuery();


            if (!result.next()) {
                sql = "call addTakePlace(?,?,?)";
                ps = connection.prepareCall(sql);
                ps.setDate(1, date);
                ps.setInt(2, idPeriod);
                ps.setString(3, classroomName);
                ps.executeUpdate();
            }
        } catch (SQLException e){
            e.printStackTrace();
            LOG.log(Level.SEVERE, e.getMessage());
            closeConnection();
        }
    }
	/**
     * This method updates the lock status of a classroom which already exist.
     * @param classroomName the name of the classroom to update. Must be in the database.
     * @param isLocked the new status of the classroom
     */
	public void updateClassroomLock(String classroomName, boolean isLocked){
		LOG.info("Update of the state of the classroom...");

        try(Statement statement = connection.createStatement()){
            ResultSet result;
            PreparedStatement ps;
            // Test if the classroom given exists
            sql = "SELECT * " +
                    "FROM classroom AS CR " +
                    "WHERE CR.classroomName=?";
             ps = connection.prepareStatement(sql);
             ps.setString(1, classroomName);
             result = ps.executeQuery();

             if (!result.next()) {
                 LOG.log(Level.SEVERE, "Can't find the classroom...");
                 return;
             }
             sql = "call updateClassroomLock(?,?)";
             ps = connection.prepareCall(sql);
             ps.setString(1, classroomName);
             ps.setBoolean(2,isLocked);
             ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            LOG.log(Level.SEVERE, e.getMessage());
            closeConnection();
        }
    }
    /**
     * This method updates the classroom equipment for the specified classroom equipment.
     * You have to set all the models.
     * @param idClassroomEquipment must exist in the database, used to find the equipment to update.
     * @param beamer does the classroom has a beamer ?
     * @param electricalSocket does the classroom has some electrical socket ?
     * @param computers does the classroom has computers ?
     * @param board does the classroom has a board ?
     */
    public void updateClassroomEquipment(int idClassroomEquipment, boolean beamer, boolean electricalSocket, boolean computers, boolean board){
		LOG.info("Update of the state of the classroom equipment...");

        try(Statement statement = connection.createStatement()){
            ResultSet result;
            PreparedStatement ps;
            // Test if the classroom equipment given exists
            sql = "SELECT * " +
                    "FROM classroomequipment AS CE " +
                    "WHERE CE.idClassroomEquipment=?";
             ps = connection.prepareStatement(sql);
             ps.setInt(1, idClassroomEquipment);
             result = ps.executeQuery();

             if (!result.next()) {
                 LOG.log(Level.SEVERE, "Can't find the classroom equipment...");
                 return;
             }
             sql = "call updateClassroomEquipment(?,?,?,?,?)";
             ps = connection.prepareCall(sql);
             ps.setInt(1, idClassroomEquipment);
             ps.setBoolean(2,beamer);
             ps.setBoolean(3,electricalSocket);
             ps.setBoolean(4,computers);
             ps.setBoolean(5,board);
             ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            LOG.log(Level.SEVERE, e.getMessage());
            closeConnection();
        }
	}

    /**
     * This method updates the floor equipment for the specified floor equipment.
     * You have to set all the models.
     * @param idFloorEquipment must exist in the database, used to find the equipment to update.
     * @param toiletM does the floor has toilet for the male student ?
     * @param toiletF does the floor has toilet for the female student ?
     * @param coffeeMachine does the floor has a coffee machine ?
     * @param selecta does the floor has a selecta ?
     * @param wayOut does the floor has access to a way out ?
     */
    public void updateFloorEquipment(int idFloorEquipment, boolean toiletM, boolean toiletF, boolean coffeeMachine, boolean selecta, boolean wayOut){
		LOG.info("Update of the state of the floor equipment...");

        try(Statement statement = connection.createStatement()){
            ResultSet result;
            PreparedStatement ps;
            // Test if the floor equipment given exists
            sql = "SELECT * " +
                    "FROM floorequipment AS FE " +
                    "WHERE FE.idFloorEquipment=?";
             ps = connection.prepareStatement(sql);
             ps.setInt(1, idFloorEquipment);
             result = ps.executeQuery();

             if (!result.next()) {
                 LOG.log(Level.SEVERE, "Can't find the floor equipment...");
                 return;
             }
             sql = "call updateFloorEquipment(?,?,?,?,?,?)";
             ps = connection.prepareCall(sql);
             ps.setInt(1, idFloorEquipment);
             ps.setBoolean(2,toiletM);
             ps.setBoolean(3,toiletF);
             ps.setBoolean(4,coffeeMachine);
             ps.setBoolean(5,selecta);
             ps.setBoolean(6,wayOut);
             ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            LOG.log(Level.SEVERE, e.getMessage());
            closeConnection();
        }
    }
    /**
     * This method is used to clear the table before we add the new course
     */
    void clearTakePlace(){
        LOG.info("Update of the state of the floor equipment...");

        try(Statement statement = connection.createStatement()){
            ResultSet result;
            PreparedStatement ps;
            // Test if the floor equipment given exists
            sql = "truncate takeplace";
            ps = connection.prepareStatement(sql);
            result = ps.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            LOG.log(Level.SEVERE, e.getMessage());
            closeConnection();
        }
    }

    /**
     * Receives a ClassRoom object, query the database to return the full schedule (aka multiple timetables) of a given room.
     * @param c the classroom to get it's time table
     * @return A full schedule of the given room, empty if it'a always free, an SQLException if something bad happens.
     */
    public ArrayList<TimeSlot> classRoomSchedule(ClassRoom c) {
        ArrayList<TimeSlot> timeTable = new ArrayList<>();
        LOG.info("Getting the timetable of a classroom");

        try {
            Statement statement = connection.createStatement();
            ResultSet result;
            PreparedStatement ps;
            sql =   "call fullTimeTableFromRoom(?)";

            ps = connection.prepareStatement(sql);
            ps.setString(1, c.getClassRoom());
            result = ps.executeQuery();

            timeTable.addAll(receiveDataFromDB(result));

        } catch (SQLException e){
            e.printStackTrace();
        }
        return timeTable;
    }

    /**
     * Receives a TimeTable object containing the current room of a user with a requested schedule,
     * query the database to return all rooms that are occupied during this schedule.
     * @param t a TimeSlot containing the date and the starting and ending time.
     * @return an ArrayList of TimeSlot containing the classroom occupied at a given time,
     *         empty if it'a always free, an SQLException if something bad happens.
     */
    public ArrayList<TimeSlot> occupiedRoomsAtGivenSchedule(TimeSlot t) {
      ArrayList<TimeSlot> timeTable = new ArrayList<TimeSlot>();
      LOG.info("Getting the classrooms that are occupied during this schedule");

      try {
          Statement statement = connection.createStatement();
          ResultSet result;
          PreparedStatement ps;
          sql = "call occupiedRoomsAtGivenSchedule(?,?,?)";

          ps = connection.prepareStatement(sql);

          ps.setString(1, t.floor());
          ps.setDate(2, t.getDate());
          ps.setInt(3, t.getIdPeriod());

          result = ps.executeQuery();

          timeTable.addAll(receiveDataFromDB(result));

      } catch (SQLException e){
          e.printStackTrace();
      }
      return timeTable;
    }

    /**
     * Query the database to return a TimeSlot array containing all occupied periods
     * during the interval and in the classroom defined in the given <code>advancedRequest</code>.
     * @param   advancedRequest a TimeSlot containing the date and the starting and ending time.
     * @return  an ArrayList of TimeSlot containing the classroom occupied at a given time,
     *         empty if it'a always free, an SQLException if something bad happens.
     */
    public ArrayList<TimeSlot> classroomAdvancedSchedule(AdvancedRequest advancedRequest){
        ArrayList<TimeSlot> timeTable = new ArrayList<TimeSlot>();
        LOG.info("Advanced request : classroom");

        try {
            Statement statement = connection.createStatement();
            ResultSet result;
            PreparedStatement ps;
            sql = "call classroomAdvancedSchedule(?,?,?,?,?,?)";
            //place, date, idPeriodBegin, idPeriodEnd, classroomName

            ps = connection.prepareStatement(sql);

            ps.setInt(1, advancedRequest.getBuilding());
            ps.setDate(2, advancedRequest.getDateBegin());
            ps.setDate(3, advancedRequest.getDateEnd());
            ps.setInt(4, advancedRequest.getIdPeriodBegin());
            ps.setInt(5, advancedRequest.getIdPeriodEnd());
            ps.setString(6, advancedRequest.getClassroom());

            result = ps.executeQuery();

            timeTable.addAll(receiveDataFromDB(result));

        } catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("database : " + timeTable);
        return timeTable;
    }

    /**
     * Query the database to return a TimeSlot array containing all occupied periods
     * during the interval and in the floor defined in the given <code>advancedRequest</code>.
     * @param   advancedRequest a TimeSlot containing the date and the starting and ending time.
     * @return  an ArrayList of TimeSlot containing the classroom occupied at a given time,
     *         empty if it'a always free, an SQLException if something bad happens.
     */
    public ArrayList<TimeSlot> floorAdvancedSchedule(AdvancedRequest advancedRequest){
        ArrayList<TimeSlot> timeTable = new ArrayList<TimeSlot>();
        LOG.info("Advanced request : classroom");

        try {
            Statement statement = connection.createStatement();
            ResultSet result;
            PreparedStatement ps;
            sql = "call floorAdvancedSchedule(?,?,?,?,?,?)";
            //place, date, idPeriodBegin, idPeriodEnd, classroomName

            ps = connection.prepareStatement(sql);

            ps.setInt(1, advancedRequest.getBuilding());
            ps.setDate(2, advancedRequest.getDateBegin());
            ps.setDate(3, advancedRequest.getDateEnd());
            ps.setInt(4, advancedRequest.getIdPeriodBegin());
            ps.setInt(5, advancedRequest.getIdPeriodEnd());
            ps.setString(6, advancedRequest.getFloor());

            result = ps.executeQuery();

            timeTable.addAll(receiveDataFromDB(result));

        } catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("database : " + timeTable);
        return timeTable;
    }

    /**
     * Query the database to return a TimeSlot array containing all occupied periods
     * during the interval and in the building defined in the given <code>advancedRequest</code>.
     * @param   advancedRequest a TimeSlot containing the date and the starting and ending time.
     * @return  an ArrayList of TimeSlot containing the classroom occupied at a given time,
     *         empty if it'a always free, an SQLException if something bad happens.
     */
    public ArrayList<TimeSlot> buildingAdvancedSchedule(AdvancedRequest advancedRequest){
        ArrayList<TimeSlot> timeTable = new ArrayList<TimeSlot>();
        LOG.info("Advanced request : classroom");

        try {
            Statement statement = connection.createStatement();
            ResultSet result;
            PreparedStatement ps;
            sql = "call buildingAdvancedSchedule(?,?,?,?,?)";
            //place, date, idPeriodBegin, idPeriodEnd, classroomName

            ps = connection.prepareStatement(sql);
        
            ps.setInt(1, advancedRequest.getBuilding());
            ps.setDate(2, advancedRequest.getDateBegin());
            ps.setDate(3, advancedRequest.getDateEnd());
            ps.setInt(4, advancedRequest.getIdPeriodBegin());
            ps.setInt(5, advancedRequest.getIdPeriodEnd());
        
            result = ps.executeQuery();
        
            timeTable.addAll(receiveDataFromDB(result));
        
        } catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("database : " + timeTable);
        return timeTable;
    }
    
    /**
     * Take the result of the database and return a timetable.
     * @param result the result of the database
     * @return A timetable when there's a result and empty when the class are always free
     * @throws SQLException when something bad happens while reading the result set.
     */
    private ArrayList<TimeSlot> receiveDataFromDB(ResultSet result) throws SQLException {
        ArrayList<TimeSlot> timeTable = new ArrayList<>();
        if(result == null){
            return timeTable;
        }
        while (result.next()) {
            String classroom = result.getString("classroomName");
            Date date = result.getDate("date");
            int idPeriod = result.getInt("idPeriod");
            TimeSlot tmp = new TimeSlot(classroom,date.getTime(), idPeriod);
            timeTable.add(tmp);
        }
        return timeTable;
    }
}




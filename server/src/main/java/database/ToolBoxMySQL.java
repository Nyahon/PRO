package database;
/*
 * Project DARYLL
 * File     : database.ToolBoxMySQL.java
 * Author   : Siu Aurélien
 * Created on : 27.03.2018
 * Edited by : Muaremi Dejvid, Romain Gallay
 * Last edit  : 12.04.2018
 * Description : Contains the basic tools to connect to the database and modify its content.
 *
 */

import serialisation.data.ClassRoom;
import serialisation.data.TimeSlot;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;

public class ToolBoxMySQL  {

    // Logger to deliver information or report errors
    private static final Logger LOG = Logger.getLogger(ToolBoxMySQL.class.getName());

    // Login informations for the connection to the database
    private static final String database = "daryll";
    private static final String account = "root";
    private static final String password = "root";

    private Connection connection;
    private String sql;

    private static final String[][] periods = {{"08:25:00", "09:10:00"}, {"09:15:00", "10:00:00"},
            {"10:25:00", "11:10:00"}, {"11:15:00", "12:00:00"}, {"12:00:00", "13:15:00"}, {"13:15:00","14:00:00"},
            {"14:00:00", "14:45:00"}, {"14:55:00", "15:40:00"}, {"15:45:00", "16:30:00"}, {"16:35:00", "17:20:00"},
            {"17:20:00", "18:05:00"}, {"18:30:00", "19:15:00"}, {"19:15:00", "20:00:00"}, {"20:05:00", "20:50:00"},
            {"20:50:00", "21:35:00"}, {"21:35:00", "22:20:00"}};

    public ToolBoxMySQL(){
        initConnection();
        insertPeriods(periods);
        // insert default classrooomequipment
        insertClassroomEquipment(1, true,true,false,true);
        closeConnection();
    }

    /**
     * @brief This method initialize the connection to the database
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
     * @brief This method close the connection to the database
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
     * @brief This method insert the periods list into the database
     */
    public void insertPeriods(String[][] listOfPeriods) {
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
            for (int i = 0; i < listOfPeriods.length; i += 1) {
                if(!result.next()){
                    ps.setInt(1, i); // idPeriod (0 to n)
                    ps.setTime(2, Time.valueOf(listOfPeriods[i][0]));
                    ps.setTime(3, Time.valueOf(listOfPeriods[i][1]));

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
     * @brief This method insert a new Classroom element into the database
     * @param classroomName It defines the name of the new Classroom
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
     * @brief This method insert new Classroom elements according to the list
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

    public void insertClassroomWithCheck(String classname){
        char levelChar = classname.charAt(0);
        int levelInt;

        if(levelChar > 'k'){
            levelInt = 1;
        } else {
            levelInt = 0;
        }
        insertClassroom(classname, false, levelInt, 1);

    }
    /**
     * @brief This method insert a new Classroom element into the database
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
     * @brief This method insert a new floor element into the database
     * @param floorName name of the floor
     * @param place defines the place (number)
     *
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
     * @brief This method insert a new floor equipment into the database
     * @param toiletM define if men toilets are available on the floor
     * @param toiletF define if women toilets are available
     * @param coffeeMachine define if a selecta is available
     * @param selecta define if a selecta is available
     * @param wayOut define if there is an exit.
     *
     */
    public void insertFloorEquipment(boolean toiletM, boolean toiletF, boolean coffeeMachine, boolean selecta, boolean wayOut) {
        LOG.info("insert new FloorEquipment...");
        ResultSet result;
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
     * @brief This method insert a new couple of Period and Classroom (TakePlace table) into the database
     * @param date name of the floor
     * @param idPeriod id of the linked element of the Period table
     * @param classroomName
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
     * @brief This method update the lock status of a classroom which already exist.
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
     * @brief update the classroom equipment for the specified classroom equipment.
     * You have to set all the data.
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
     * @brief update the floor equipment for the specified floor equipment.
     * You have to set all the data.
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
     * @brief clear the take place table.
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
     * @return A full schedule of the given room, null if it'a always free, an SQLException if something bad happens.
     */
    ArrayList<TimeSlot> fullTimeTableFromRoom(ClassRoom c) throws SQLException{
        ArrayList<TimeSlot> timeTable = new ArrayList<TimeSlot>();
        LOG.info("Getting the timetable of a classroom");
    
        Statement statement = connection.createStatement();
        ResultSet result;
        PreparedStatement ps;
        sql =   "SELECT *" +
                "FROM takeplace AS T" +
                "INNER JOIN period AS P" +
                "ON T.idPeriod = P.idPeriod" +
                "WHERE T.classroomName=?";
        
        ps = connection.prepareStatement(sql);
        ps.setString(1, c.getClassRoom());
        result = ps.executeQuery();
    
        if (!result.next()) {
          LOG.log(Level.SEVERE, "The classroom is always free...");
          return null;
        }
        
        while (result.next()){
          TimeSlot tmp = new TimeSlot(result.getString("classroomName"), result.getLong("date"), result.getLong("startingTime"), result.getLong("finishingTime"));
          timeTable.add(tmp);
        }
        return timeTable;
    }
    
    /**
     * Receives a TimeTable object containing the current room of a user with a requested schedule,
     * query the database to return all rooms that are occupied during this schedule.
     * @param t a TimeSlot containing the date and the starting and ending time.
     * @return an ArrayList of TimeSlot containing the classroom occupied at a given time,
     *         null if it'a always free, an SQLException if something bad happens.
     */
    ArrayList<TimeSlot> occupiedRoomsAtGivenSchedule(TimeSlot t) throws SQLException{
      ArrayList<TimeSlot> timeTable = new ArrayList<TimeSlot>();
      LOG.info("Getting the classrooms that are occupied during this schedule");
  
      Statement statement = connection.createStatement();
      ResultSet result;
      PreparedStatement ps;
      sql =   "SELECT *" +
              "FROM takeplace AS T" +
              "INNER JOIN period AS P" +
              "ON T.idPeriod = P.idPeriod" +
              "WHERE T.date = ? AND P.startingTime = ? AND P.finishingTime = ?;";
  
      ps = connection.prepareStatement(sql);
      ps.setLong(1, t.getDate());
      ps.setLong(2,t.getTime_start());
      ps.setLong(3,t.getTime_end());
      result = ps.executeQuery();
  
      if (!result.next()) {
        LOG.log(Level.SEVERE, "The time slot is always free...");
        return null;
      }
  
      while (result.next()){
        TimeSlot tmp = new TimeSlot(result.getString("classroomName"), result.getLong("date"), result.getLong("startingTime"), result.getLong("finishingTime"));
        timeTable.add(tmp);
      }
      return timeTable;
    }
}




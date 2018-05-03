/*
 * Project DARYLL
 * File     : ToolBoxMySQL.java
 * Author   : Siu Aurélien
 * Created on : 27.03.2018
 * Edited by : Muaremi Dejvid
 * Last edit  : 12.04.2018
 * Description : Contains the basic tools to connect to the database and modify its content.
 *
 */

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;

public class ToolBoxMySQL implements Runnable {

    // Logger to deliver information or report errors
    private static final Logger LOG = Logger.getLogger(ToolBoxMySQL.class.getName());

    // Login informations for the connection to the database
    private static final String database = "daryll";
    private static final String password = "mly.48ODR-51";

    private Connection connection;
    private String sql;

    public static void main(String... args) {}

    /**
     * @brief This method is called when we start this Runnable class.
     * It initialize the connection to the database
     */
    @Override
    public void run() {
        try {
            initConnection();

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, e.getMessage());
            closeConnection();
        }
    }

    /**
     * @brief This method initialize the connection to the database
     */
    private void initConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/" + database + "?user=root&password=" + password;
        connection = DriverManager.getConnection(url);
    }

    /**
     * @brief This method close the connection to the database
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
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
        } catch (SQLException e) {
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
    private void insertClassroom(String classroomName, boolean isLocked, int place, int idClassroomEquipment) throws SQLException {
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
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, e.getMessage());
            closeConnection();
        }
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
            LOG.log(Level.SEVERE, e.getMessage());
            closeConnection();
        }
    }
	
	
}




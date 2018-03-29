/*
 * Project DARYLL
 * File     : ToolBoxMySQL.java
 * Author   : Siu Aurélien
 * Created on : 27.03.2018
 * Last edit  : 29.03.2018
 * Description : Java class that contains tools to insert or update element in the database
 *
 */

import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ToolBoxMySQL extends Thread {

    private static final Logger LOG = Logger.getLogger(ToolBoxMySQL.class.getName());
    private static final Scanner mScanner = new Scanner(System.in);
    private Connection mConnection;
    private String sql;

    private static final String[][] hoursStartEnd = {{"08:25:00", "09:10:00"}, {"09:15:00", "10:00:00"},
            {"10:25:00", "11:10:00"}, {"11:15:00", "12:00:00"}, {"12:00:00", "13:15:00"}, {"13:15:00","14:00:00"},
            {"14:00:00", "14:45:00"}, {"14:55:00", "15:40:00"}, {"15:45:00", "16:30:00"}, {"16:35:00", "17:20:00"},
            {"17:20:00", "18:05:00"}, {"18:30:00", "19:15:00"}, {"19:15:00", "20:00:00"}, {"20:05:00", "20:50:00"},
            {"20:50:00", "21:35:00"}, {"21:35:00", "22:20:00"}};

    public static void main(String... args) {
        new ToolBoxMySQL().start();
    }

    @Override
    public void run() {
        try {
            initConnection();
            insertPeriods();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        } finally {
            if (mConnection != null) {
                try {
                    mConnection.close();
                } catch (SQLException e) {
                    LOG.log(Level.SEVERE, e.getMessage());
                }
            }
        }
    }

    private void initConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/daryll?user=root&password=mly.48ODR-51";
        mConnection = DriverManager.getConnection(url);
    }

    // Prepare the database insertion
    private void initInsertion() throws SQLException {
        // Insertion of the different periods
        insertPeriods();
    }

    private void insertPeriods() throws SQLException {
        System.out.print("Insertion of the periods");
        try (Statement statement = mConnection.createStatement()) {
            PreparedStatement ps;
            String sql;
            sql = "INSERT INTO Period(idPeriod, startingTime, finishingTime) VALUES (?,?,?)";
            ps = mConnection.prepareStatement(sql);
            for (int i = 0; i < hoursStartEnd.length; i += 1) {
                ps.setInt(1, i); // idPeriod (0 to n)
                ps.setTime(2, Time.valueOf(hoursStartEnd[i][0]) );
                ps.setTime(3, Time.valueOf(hoursStartEnd[i][1]) );

                ps.executeUpdate();
            }
        }
    }

    // Insertion a classroom in the database
    private void insertClassroom(String roomName, boolean isLocked) throws SQLException {
        System.out.print("Insertion Classroom...");
        try (Statement statement = mConnection.createStatement()) {

            PreparedStatement ps;
            sql = "INSERT INTO Classroom(roomName, isLocked, floorName) VALUES (?,?,?)";
            ps = mConnection.prepareStatement(sql);
            ps.setString(1, roomName);
            ps.setBoolean(2, isLocked);
            ps.setString(3, roomName.substring(0, 0));
            ps.executeUpdate();
        }
    }

    // Insertion of Classrooms in the database
    // InitInsert must have been called before
    public void insertClassrooms(String[] listClassrooms) {
        System.out.print("Insertion Classrooms...");
        ResultSet result = null;

        try (Statement statement = mConnection.createStatement()) {

            // Insert defaut ClassRoomEquipment if it doesn't exists
            insertClassroomEquipment(0, true,true,false,true);

            for (int i = 0; i < listClassrooms.length; ++i) {
                insertClassroom(listClassrooms[i], false);
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        } finally {
            if (mConnection != null) {
                try {
                    mConnection.close();
                } catch (SQLException e) {
                    LOG.log(Level.SEVERE, e.getMessage());
                }
            }
        }
    }

    // Insertion of a new ClassroomEquipment in the database
    private void insertClassroomEquipment(int idClassroomEquipment, boolean beamer, boolean electricalSocket,
                                          boolean computers, boolean board) throws SQLException {
        System.out.print("insert new ClassroomEquipment...");
        ResultSet result = null;

        try (Statement statement = mConnection.createStatement()) {

            // Test if default ClassRoomEquipment exists
            sql = "SELECT *" +
                    "FROM ClassroomEquipment AS CE " +
                    "WHERE CE.idClassroomEquipment=" + idClassroomEquipment;
            result = statement.executeQuery(sql);

            if (result == null) {
                PreparedStatement ps;
                sql = "INSERT INTO ClassroomEquipment (idClassroomEquipment, beamer, electricalSocket, computers, board) VALUES (?,?,?,?,?);";
                ps = mConnection.prepareStatement(sql);

                ps.setInt(1, idClassroomEquipment);
                ps.setBoolean(2, beamer);
                ps.setBoolean(3, electricalSocket);
                ps.setBoolean(4, computers);
                ps.setBoolean(5, board);

                ps.executeUpdate();
            }
        }
    }

    // Insertion of a new Floor in the database
    // InitInsert must have been called before
    private void insertFloor(String floorName, int place) throws SQLException {
        System.out.print("insert new Floor...");
        ResultSet result = null;

        try (Statement statement = mConnection.createStatement()) {

            // Test if default ClassRoomEquipment exists
            sql = "SELECT *" +
                    "FROM Floor AS F " +
                    "WHERE F.floorName=" + floorName;
            result = statement.executeQuery(sql);

            if (result == null) {
                PreparedStatement ps;
                sql = "INSERT INTO Floor (floorName, place) VALUES (?,?);";
                ps = mConnection.prepareStatement(sql);
                ps.setString(1, floorName);
                ps.setInt(2, place);

                ps.executeUpdate();
            }
        }
    }

    // Insertion of a new Floor in the database
    // InitInsert must have been called before
    private void insertFloorEquipment(boolean toiletM, boolean toiletF, boolean coffeeMachine, boolean selecta, boolean wayOut) throws SQLException {
        System.out.print("insert new FloorEquipment...");
        ResultSet result = null;

        try (Statement statement = mConnection.createStatement()) {

            // Test if default FloorEquipment exists
            sql = "SELECT *" +
                    "FROM FloorEquipment AS FE " +
                    "WHERE FE.floorName=0";
            result = statement.executeQuery(sql);

            if (result == null) {
                PreparedStatement ps;
                sql = "INSERT INTO FloorEquipment (toiletM, toiletF, coffeeMachine, selecta, wayOut) VALUES (?,?,?,?,?);";
                ps = mConnection.prepareStatement(sql);

                ps.setBoolean(1, toiletM);
                ps.setBoolean(2, toiletF);
                ps.setBoolean(3, coffeeMachine);
                ps.setBoolean(4, selecta);
                ps.setBoolean(5, wayOut);

                ps.executeUpdate();
            }
        }
    }

    // Insertion of Classrooms in the database
    private void insertTakePlace(int idPeriod, String roomName, Date date) throws SQLException {
        System.out.print("Insertion of the periods");
        try (Statement statement = mConnection.createStatement()) {
            PreparedStatement ps;
            sql = "INSERT INTO Period(idPeriod, startingTime, finishingTime) VALUES (?,?,?)";
            ps = mConnection.prepareStatement(sql);
            for (int i = 0; i < hoursStartEnd.length - 1; i += 2) {
                ps.setInt(1, idPeriod);
                ps.setTime(2, Time.valueOf(hoursStartEnd[i][0]));
                ps.setTime(3, Time.valueOf(hoursStartEnd[i][1]));
                ps.executeUpdate();
                ++idPeriod;
            }
        }
    }
}

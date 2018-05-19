package database;

import utils.ReaderICS;

public class TransformerICStoMySQL {

    private static enum Place {CHESEAUX, ST_ROCH}; // not used for the moment
    private static String[] place = {"Cheseaux", "St-Roch"}; // not used for the moment

    // 2 dimensional tab containing all the periods ( couple of starting and ending time of a period)
    private static final String[][] periods = {{"08:25:00", "09:10:00"}, {"09:15:00", "10:00:00"},
            {"10:25:00", "11:10:00"}, {"11:15:00", "12:00:00"}, {"12:00:00", "13:15:00"}, {"13:15:00","14:00:00"},
            {"14:00:00", "14:45:00"}, {"14:55:00", "15:40:00"}, {"15:45:00", "16:30:00"}, {"16:35:00", "17:20:00"},
            {"17:20:00", "18:05:00"}, {"18:30:00", "19:15:00"}, {"19:15:00", "20:00:00"}, {"20:05:00", "20:50:00"},
            {"20:50:00", "21:35:00"}, {"21:35:00", "22:20:00"}};


    private String[] classroomsCheseaux = {"G01", "G07", "H02", "K04", "A01", "A02", "A03", "B23"};
    private String[] classroomsStRoch = {"R102", "S104", "T106", "S101"};



    // test function for the class database.ToolBoxMySQL
    public void testToolBoxMySQL() {
        ToolBoxMySQL tbs = new ToolBoxMySQL();
        tbs.initConnection();
        tbs.insertPeriods(periods);

        tbs.insertClassrooms(classroomsCheseaux, 0);
        tbs.insertClassrooms(classroomsStRoch, 1);

        tbs.insertTakePlace(java.sql.Date.valueOf("2018-10-15"), 0, "G07");
        tbs.insertClassroomEquipment(2, false, true,false, true);
        tbs.insertFloorEquipment(true,true,false, false, false);
        tbs.closeConnection();
    }

}

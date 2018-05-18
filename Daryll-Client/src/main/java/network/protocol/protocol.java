package network.protocol;

/**
 * This class defines constants for the Roulette Protocol (version 2)
 *
 * @author Olivier Liechti
 */
public class protocol {

    public final static String VERSION = "1.0";

    public final static int DEFAULT_PORT = 2613;

    public final static String CMD_CLASSROOM = "CLASSROOM";
    public final static String CMD_TIMESLOT = "TIMESLOT";
    public final static String CMD_BYE = "BYE";


    public final static String RESPONSE_OK = "OK";
    public final static String RESPONSE_CONNECTED = "CONNECTED";

    public final static String RESPONSE_CLASSROOM = "READY";
    public final static String RESPONSE_TIMESLOT = "READY";
    public final static String RESPONSE_BYE = "BYE_BYE";



    public final static String[] SUPPORTED_COMMANDS = new String[]{CMD_CLASSROOM, CMD_TIMESLOT};

}

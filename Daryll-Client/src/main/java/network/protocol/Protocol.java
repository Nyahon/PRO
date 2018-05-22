package network.protocol;

import utils.ConfigLoader;

import java.util.Properties;

/**
 * This class defines constants for the DARYLL protocol
 *
 * @author Yohann Meyer, Loïc Frueh, Romain Gallay
 */
public class Protocol {

    static {
        ConfigLoader config = new ConfigLoader();
        DEFAULT_PORT = config.getServerPort();
        SERVER_IP = config.getServerAddress();
    }

    /**
     * the software version
     */
    public final static String VERSION = "1.0";

    /**
     * the default port used in the client-server communication
     */
    public final static int DEFAULT_PORT;

    /**
     * the server ip
     */
    public final static String SERVER_IP;

    /**
     * the command used in classroom request
     */
    public final static String CMD_CLASSROOM = "CLASSROOM";

    /**
     * the command use in a floor request
     */
    public final static String CMD_FLOOR = "FLOOR";

    /**
     * the command used in an advanced request
     */
    public final static String CMD_ADVANCED = "ADVANCED";

    /**
     * the command to end a client-server communication
     */
    public final static String CMD_BYE = "BYE";

    /**
     * the command to acknowledge a communication
     */
    public final static String RESPONSE_OK = "OK";

    /**
     * the command to acknowledge a connexion
     */
    public final static String RESPONSE_CONNECTED = "CONNECTED";
    public final static String RESPONSE_CLASSROOM = "READY";
    public final static String RESPONSE_TIMESLOT = "READY";
    public final static String RESPONSE_FLOOR = "READY";
    public final static String RESPONSE_ADVANCED = "READY";
    /**
     * the command to disconnect
     */
    public final static String RESPONSE_BYE = "BYE_BYE";
}

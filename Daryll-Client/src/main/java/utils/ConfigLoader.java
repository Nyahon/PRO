package utils;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class to load a properties file and access its properties
 * @author Romain Gallay
 */
public class ConfigLoader {

    public static final String MAIN_FOLDER = "DARYLL";
    public static final String PLAN_PATH = MAIN_FOLDER + "/plans";
    public static final String GUI_RESOURCES_PATH = MAIN_FOLDER + "/guiResources";
    private static final String CONFIG_FILENAME = MAIN_FOLDER + "/daryll.properties";

    private static final Logger LOG = Logger.getLogger(ConfigLoader.class.getName());
    private static final String EQ = "=";

    private static final String SERVER_ADDRESS_DEFAULT = "daryll.lan.iict.ch";
    private static final String SERVER_PORT_DEFAULT = "2613";
    private static final String OUTPUT_FILENAME_DEFAULT = "DARYLL.txt";
    private static final String DAYS_OF_WEEK_FORMATTER_DEFAULT = "EEEE";
    private static final String DATE_FORMATTER_DEFAULT = "dd.MM.yyyy";
    private static final String FILE_ENCODING_DEFAULT = "UTF-8";

    private static final String SERVER_ADDRESS_ID = "serverAddress";
    private static final String SERVER_PORT_ID = "serverPort";
    private static final String OUTPUT_FILENAME_ID = "outputFilename";
    private static final String DAYS_OF_WEEK_FORMATTER_ID = "daysOfWeekFormatter";
    private static final String DATE_FORMATTER_ID = "dateFormatter";
    private static final String FILE_ENCODING_ID = "encodageFile";

    private static String serverAddress;
    private static int serverPort;
    private static String outputFilename;
    private static String daysOfWeekFormatter;
    private static String dateFormatter;
    private static String fileEncoding;

    private static Properties properties;


    static {
        properties = new Properties();
        createFileIfNotExists();
        loadConfig();
    }

    /**
     * Private default constructor
     */
    private ConfigLoader(){}

    /**
     * Loads the properties defined in the configuration file.
     */
    private static void loadConfig(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CONFIG_FILENAME),
                    "UTF-8"));
            properties.load(reader);
        } catch (IOException e){
            LOG.log(Level.WARNING, "File " + CONFIG_FILENAME + " not found, loading default configuration.");
        }
        serverAddress = properties.getProperty(SERVER_ADDRESS_ID, SERVER_ADDRESS_DEFAULT);
        serverPort = Integer.valueOf(properties.getProperty(SERVER_PORT_ID, SERVER_PORT_DEFAULT));
        outputFilename = properties.getProperty(OUTPUT_FILENAME_ID, OUTPUT_FILENAME_DEFAULT);
        daysOfWeekFormatter = properties.getProperty(DAYS_OF_WEEK_FORMATTER_ID, DAYS_OF_WEEK_FORMATTER_DEFAULT);
        dateFormatter = properties.getProperty(DATE_FORMATTER_ID, DATE_FORMATTER_DEFAULT);
        fileEncoding = properties.getProperty(FILE_ENCODING_ID, FILE_ENCODING_DEFAULT);
    }

    /**
     * Creates a new configuration file if it does not exist yet
     */
    private static void createFileIfNotExists(){
        File configFile = new File(CONFIG_FILENAME);

        try {
            if(configFile.createNewFile()){

                try(PrintWriter writer = new PrintWriter(CONFIG_FILENAME, "UTF-8")){

                    writer.println(SERVER_ADDRESS_ID + EQ + SERVER_ADDRESS_DEFAULT);
                    writer.println(SERVER_PORT_ID + EQ + SERVER_PORT_DEFAULT);
                    writer.println(OUTPUT_FILENAME_ID + EQ + OUTPUT_FILENAME_DEFAULT);
                    writer.println(DAYS_OF_WEEK_FORMATTER_ID + EQ + DAYS_OF_WEEK_FORMATTER_DEFAULT);
                    writer.println(DATE_FORMATTER_ID + EQ + DATE_FORMATTER_DEFAULT);
                    writer.println(FILE_ENCODING_ID + EQ + FILE_ENCODING_DEFAULT);
                } catch (IOException e){
                    LOG.log(Level.SEVERE, "An I/O error occurred during config file filling.");
                }
            }
        } catch (IOException e){
            LOG.log(Level.SEVERE, "An I/O error occurred during configuration file creation.");
        }
    }

    /**
     * Retrieve the server address
     * @return the server address from the properties file, or the default server address if there is an issue
     * with the properties
     */
    public static String getServerAddress() {
        return serverAddress;
    }


    public static void setServerAddress(String serverAddress) {
        ConfigLoader.serverAddress = serverAddress;
    }

    /**
     * Retrieve the server port
     * @return the server port from the properties file, or the default server port if there is an issue
     * with the properties
     */
    public static int getServerPort() {
        return serverPort;
    }

    public static void setServerPort(int serverPort) {
        ConfigLoader.serverPort = serverPort;
    }

    /**
     * Retrieve the output filename to generate a file after an advanced research
     * @return the output filename from the properties file, or the default output filename if there is an issue
     * with the properties
     */
    public static String getOutputFilename() {
        return outputFilename;
    }

    public static void setOutputFilename(String outputFilename) {
        ConfigLoader.outputFilename = outputFilename;
    }

    /**
     * Retrieve the formatter to represent the days of week in the file generated after an advanced research
     * @return the formatter to represent the days of week from the properties file,
     * or the default formatter if there is an issue with the properties
     */
    public static String getDaysOfWeekFormatter() {
        return daysOfWeekFormatter;
    }

    public static void setDaysOfWeekFormatter(String daysOfWeekFormatter) {
        ConfigLoader.daysOfWeekFormatter = daysOfWeekFormatter;
    }

    /**
     * Retrieve the formatter to represent the date in the file generated after an advanced research
     * @return the formatter to represent the date from the properties file,
     * or the default formatter if there is an issue with the properties
     */
    public static String getDateFormatter() {
        return dateFormatter;
    }

    public static void setDateFormatter(String dateFormatter) {
        ConfigLoader.dateFormatter = dateFormatter;
    }

    /**
     * Retrieve the encoder used to generated a file after an advanced research
     * @return the encoder used to generated a file after an advanced research,
     * or the default encoder if there is an issue with the properties
     */
    public static String getFileEncoding() {
        return fileEncoding;
    }

    public static void setFileEncoding(String fileEncoding) {
        ConfigLoader.fileEncoding = fileEncoding;
    }
}

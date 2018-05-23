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

    private static final String CONFIG_FILENAME = "daryll.properties";
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
    public static String serverAddress() {
        return properties.getProperty(SERVER_ADDRESS_ID, SERVER_ADDRESS_DEFAULT);
    }

    /**
     * Retrieve the server port
     * @return the server port from the properties file, or the default server port if there is an issue
     * with the properties
     */
    public static int serverPort() {
        return Integer.valueOf(properties.getProperty(SERVER_PORT_ID, SERVER_PORT_DEFAULT));
    }

    /**
     * Retrieve the output filename to generate a file after an advanced research
     * @return the output filename from the properties file, or the default output filename if there is an issue
     * with the properties
     */
    public static String outputFilename() {
        return properties.getProperty(OUTPUT_FILENAME_ID, OUTPUT_FILENAME_DEFAULT);
    }

    /**
     * Retrieve the formatter to represent the days of week in the file generated after an advanced research
     * @return the formatter to represent the days of week from the properties file,
     * or the default formatter if there is an issue with the properties
     */
    public static String daysOfWeekFormatter() {
        return properties.getProperty(DAYS_OF_WEEK_FORMATTER_ID, DAYS_OF_WEEK_FORMATTER_DEFAULT);
    }

    /**
     * Retrieve the formatter to represent the date in the file generated after an advanced research
     * @return the formatter to represent the date from the properties file,
     * or the default formatter if there is an issue with the properties
     */
    public static String dateFormatter() {
        return properties.getProperty(DATE_FORMATTER_ID, DATE_FORMATTER_DEFAULT);
    }

    /**
     * Retrieve the encoder used to generated a file after an advanced research
     * @return the encoder used to generated a file after an advanced research,
     * or the default encoder if there is an issue with the properties
     */
    public static String encodageFile() {
        return properties.getProperty(FILE_ENCODING_ID, FILE_ENCODING_DEFAULT);
    }
}

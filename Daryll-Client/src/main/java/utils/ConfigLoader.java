package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private static final String DEFAULT_SERVER_ADDRESS = "localhost"; //"daryll.lan.iict.ch";
    private static final String DEFAULT_SERVER_PORT = "2613";
    private static final String DEFAULT_OUTPUT_FILENAME = "DARYLL.txt";
    private static final String DEFAULT_DAYS_OF_WEEK_FORMATTER = "EEEE";
    private static final String DEFAULT_DATE_FORMATTER = "DD.MM.YYYY";
    private static final String DEFAULT_ENCODAGE_FILE = "UTF-8";

    private static final String SERVER_ADDRESS_ID = "serverAddress";
    private static final String SERVER_PORT_ID = "serverPort";
    private static final String OUTPUT_FILENAME_ID = "outputFilename";
    private static final String DAYS_OF_WEEK_FORMATTER_ID = "daysOfWeekFormatter";
    private static final String DATE_FORMATTER_ID = "dateFormatter";
    private static final String ENCODE_FILE_ID = "encodageFile";

    private static Properties properties;

    static {
        properties = new Properties();
        loadConfig();
    }

    private ConfigLoader(){}

    private static void loadConfig(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CONFIG_FILENAME), "UTF-8"));
            properties.load(reader);
        } catch (IOException e){
            LOG.log(Level.WARNING, "File " + CONFIG_FILENAME + " not found, loading default configuration.");
        }
    }

    public static String serverAddress() {
        return properties.getProperty(SERVER_ADDRESS_ID, DEFAULT_SERVER_ADDRESS);
    }

    public static int serverPort() {
        return Integer.valueOf(properties.getProperty(SERVER_PORT_ID, DEFAULT_SERVER_PORT));
    }

    public static String outputFilename() {
        return properties.getProperty(OUTPUT_FILENAME_ID, DEFAULT_OUTPUT_FILENAME);
    }

    public static String daysOfWeekFormatter() {
        return properties.getProperty(DAYS_OF_WEEK_FORMATTER_ID, DEFAULT_DAYS_OF_WEEK_FORMATTER);
    }

    public static String dateFormatter() {
        return properties.getProperty(DATE_FORMATTER_ID, DEFAULT_DATE_FORMATTER);
    }

    public static String encodageFile() {
        return properties.getProperty(ENCODE_FILE_ID, DEFAULT_ENCODAGE_FILE);
    }
}

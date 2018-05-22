package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConfigLoader {

    private static final String DEFAULT_SERVER_ADDRESS = "daryll.lan.iict.ch";
    private static final String DEFAULT_SERVER_PORT = "2614";
    private static final String fileName = "darryl.properties";
    private static final String serverAddressId = "serverAddress";
    private static final String serverPortId = "serverPort";
    private String serverAddress;
    private int serverPort;

    public ConfigLoader(){
        loadConfig();
    }

    private Properties loadConfig(){

        Properties properties = null;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            properties = new Properties();
            properties.load(reader);
        } catch (IOException e){
            e.printStackTrace();
        }
        serverAddress = properties.getProperty(serverAddressId, DEFAULT_SERVER_ADDRESS);
        serverPort = Integer.valueOf(properties.getProperty(serverPortId, DEFAULT_SERVER_PORT));

        return properties;
    }

    public void initLocalConfig(){
        serverAddress = "localhost";
    }
    public static String getFileName() {
        return fileName;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}

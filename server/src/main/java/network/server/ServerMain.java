package network.server;

import network.protocol.ProtocolServer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides the main() method for starting the application. It creates an
 * instance of ServerDaryll and starts it (it will bind on the default port specified
 * in the serialisation.server.protocol).
 *
 * @author Yohann Meyer
 */
public class ServerMain {

    /**
     * The main method creates a new Daryll serialisation.server, which will accept TCP connection
     * requests on the default port defined in the Protocol specification.
     *
     * @param args the command line arguments
     * @throws java.io.IOException  if an I/O error occurs
     */
    public static void main(String[] args) throws IOException {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s %n");
        ServerDaryll server = new ServerDaryll( ProtocolServer.DEFAULT_PORT );
        try {
            server.startServer();
        } catch (IOException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

}

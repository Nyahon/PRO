
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import network.client.ClientSocket;
import org.junit.rules.ExternalResource;
import network.protocol.ProtocolServer;
import network.server.ServerDaryll;

/**
 *
 * @author Olivier Liechti
 */
public class EphemeralClientServerPair extends ExternalResource {

    ServerDaryll server;
    ClientSocket client;


    @Override
    protected void before() throws Throwable {
        server = new ServerDaryll(ProtocolServer.DEFAULT_PORT);
        server.startServer();

        client = new ClientSocket();

        client.connect("localhost", server.getPort());
    }

    @Override
    protected void after() {
        try {
            client.disconnect();
        } catch (IOException ex) {
            Logger.getLogger(EphemeralClientServerPair.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            server.stopServer();
        } catch (IOException ex) {
            Logger.getLogger(EphemeralClientServerPair.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ServerDaryll getServer() {
        return server;
    }

    public ClientSocket getClient() {
        return client;
    }

}

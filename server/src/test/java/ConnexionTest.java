

import java.io.IOException;

import network.client.ClientSocket;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * This class contains automated tests to validate the serialisation.client and the serialisation.server
 * implementation of the  Daryll Protocol
 *
 * @author Yohann Meyer
 */
public class ConnexionTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public EphemeralClientServerPair pair = new EphemeralClientServerPair();

    @Test
    public void theTestRouletteServerShouldRunDuringTests() throws IOException {
        assertTrue(pair.getServer().isRunning());
    }

    @Test
    public void theTestRouletteClientShouldBeConnectedWhenATestStarts() throws IOException {
        assertTrue(pair.getClient().isConnected());
    }

    @Test
    public void itShouldBePossibleForARouletteClientToConnectToARouletteServer() throws Exception {
        int port = pair.getServer().getPort();
        ClientSocket client = new ClientSocket();
        assertFalse(client.isConnected());
        client.connect("localhost", port);
        assertTrue(client.isConnected());
    }


}

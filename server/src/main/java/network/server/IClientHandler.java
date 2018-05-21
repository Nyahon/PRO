package network.server;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This interface is implemented by application-level serialisation.server.protocol serialisation.server components.
 * The main serialisation.server accepts incoming TCP connections. When a new connection is made,
 * the serialisation.server gets a new socket, spawns a new Thread and creates an instance of a
 * class implementing this interface.
 *
 * @author Yohann Meyer
 */
public interface IClientHandler {

    /**
     * Handles the interaction with a serialisation.client, by reading commands on the input stream
     * and sending responses on the output stream. For text-oriented protocols, the
     * implementation is likely to wrap is and os with an InputStreamReader and an
     * OutputStreamWriter.
     *
     * @param is input stream to read commands sent by the serialisation.client
     * @param os output stream to send responses back to the serialisation.client
     * @throws java.io.IOException if an I/O error occurs
     */
    public void handleClientConnection(InputStream is, OutputStream os) throws IOException;

}

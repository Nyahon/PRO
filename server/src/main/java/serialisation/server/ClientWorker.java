package serialisation.server;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Instances of this class are created whenever a serialisation.client has arrived and a
 * connection has been established (the instances are created by the TCPServer
 * class). The class is responsible for setting up and cleaning up the
 * communication streams, but delegates the hard work (i.e. the implementation
 * of our own application serialisation.server.protocol) to a class that implements the
 * IClientHandler interface.
 *
 * This means that we could reuse this class, develop a new class that
 * implements the IClientHandler interface and implement another application
 * serialisation.server.protocol.
 *
 * @author Olivier Liechti
 */
public class ClientWorker implements Runnable {

    static final Logger LOG = Logger.getLogger(ClientWorker.class.getName());

    private IClientHandler handler = null;
    private Socket clientSocket = null;
    private InputStream is = null;
    private OutputStream os = null;
    private boolean done = false;
    private ServerDaryll server = null;

    public ClientWorker(Socket clientSocket, IClientHandler handler, ServerDaryll server) throws IOException {
        this.clientSocket = clientSocket;
        this.handler = handler;
        this.server = server;
        is = clientSocket.getInputStream();
        os = clientSocket.getOutputStream();
    }

    public void run() {
        try {
            handler.handleClientConnection(is, os);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Exception in serialisation.client handler: {0}", ex.getMessage());
        } finally {
            done = true;
            server.notifyClientWorkerDone(this);
            try {
                clientSocket.close();
            } catch (IOException ex) {
                LOG.log(Level.INFO, ex.getMessage());
            }
            try {
                is.close();
            } catch (IOException ex) {
                LOG.log(Level.INFO, ex.getMessage());
            }
            try {
                os.close();
            } catch (IOException ex) {
                LOG.log(Level.INFO, ex.getMessage());
            }
        }
    }

    public boolean isDone() {
        return done;
    }

    public void notifyServerShutdown() {
        try {
            is.close();
        } catch (IOException ex) {
            LOG.log(Level.INFO, "Exception while closing input stream on the serialisation.server: {0}", ex.getMessage());
        }

        try {
            os.close();
        } catch (IOException ex) {
            LOG.log(Level.INFO, "Exception while closing output stream on the serialisation.server: {0}", ex.getMessage());
        }

        try {
            clientSocket.close();
        } catch (IOException ex) {
            LOG.log(Level.INFO, "Exception while closing socket on the serialisation.server: {0}", ex.getMessage());
        }
    }

}

package network.server;





import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the main logic of the Server. It starts a loop
 * to accept incoming connections on a TCP port. When a new connection request
 * is made by a serialisation.client, the serialisation.server spawns a new thread. It instantiates a serialisation.client
 * handler, to which it delegates the processing of the session (the serialisation.client
 * handler executes on the thread).

 *
 * @author Olivier Liechti & Yohann Meyer
 */
public class ServerDaryll {

    final static Logger LOG = Logger.getLogger(ServerDaryll.class.getName());

    /*
     * The TCP port where serialisation.client connection requests are accepted. -1 indicates that
     * we want to use an ephemeral port number, assigned by the OS
     */
    private int listenPort = -1;

    /*
     * The serialisation.server socket, used to accept serialisation.client connection requests
     */
    private ServerSocket serverSocket;

    /*
     * The serialisation.server maintains a list of serialisation.client workers, so that they can be notified
     * when the serialisation.server shuts down
     */
    List<ClientWorker> clientWorkers = new CopyOnWriteArrayList<>();

    /*
     * A flag that indicates whether the serialisation.server should continue to run (or whether
     * a shutdown is in progress)
     */
    private boolean shouldRun = false;


    /**
     * Constructor used to create a serialisation.server that will accept connections on a known
     * TCP port
     *
     * @param listenPort the TCP port on which connection requests are accepted
     */
    public ServerDaryll(int listenPort) {
        this.listenPort = listenPort;
    }


    public void startServer() throws IOException {
        if (serverSocket == null || serverSocket.isBound() == false)
                bindOnKnownPort(listenPort);



        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                shouldRun = true;
                while (shouldRun) {
                    try {
                        LOG.log(Level.INFO, "Listening for serialisation.client connection on {0}", serverSocket.getLocalSocketAddress());
                        Socket clientSocket = serverSocket.accept();
                        LOG.info("New serialisation.client has arrived...");
                        ClientWorker worker = new ClientWorker(clientSocket, getClientHandler(), ServerDaryll.this);
                        clientWorkers.add(worker);
                        LOG.info("Delegating work to serialisation.client worker...");
                        Thread clientThread = new Thread(worker);
                        clientThread.start();
                    } catch (IOException ex) {
                        LOG.log(Level.SEVERE, "IOException in main serialisation.server thread, exit: {0}", ex.getMessage());
                        shouldRun = false;
                    }
                }
            }
        });
        serverThread.start();
    }


    /** Easy switch of Handler if necessary is possible
     *
     * @return the handler for the connection
     */
    private IClientHandler getClientHandler() {

        return new ServerClientHandler();
    }

    /**
     * Indicates whether the serialisation.server is accepting connection requests, by checking
     * the state of the serialisation.server socket
     *
     * @return true if the serialisation.server accepts serialisation.client connection requests
     */
    public boolean isRunning() {
        return (serverSocket.isBound());
    }

    /**
     * Getter for the TCP port number used by the serialisation.server socket.
     *
     * @return the port on which serialisation.client connection requests are accepted
     */
    public int getPort() {
        return serverSocket.getLocalPort();
    }

    /**
     * Requests a serialisation.server shutdown. This will close the serialisation.server socket and notify
     * all serialisation.client workers.
     *
     * @throws IOException
     */
    public void stopServer() throws IOException {
        shouldRun = false;
        serverSocket.close();
        for (ClientWorker clientWorker : clientWorkers) {
            clientWorker.notifyServerShutdown();
        }
    }

    private void bindOnKnownPort(int port) throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));
    }


    /**
     * This method is invoked by the serialisation.client worker when it has completed its
     * interaction with the serialisation.server (e.g. the user has issued the BYE command, the
     * connection has been closed, etc.)
     *
     * @param worker the worker which has completed its work
     */
    public void notifyClientWorkerDone(ClientWorker worker) {
        clientWorkers.remove(worker);
    }

}

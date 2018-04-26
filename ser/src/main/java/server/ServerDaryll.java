package server;





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
 * is made by a client, the server spawns a new thread. It instantiates a client
 * handler, to which it delegates the processing of the session (the client
 * handler executes on the thread).

 *
 * @author Olivier Liechti & Yohann Meyer
 */
public class ServerDaryll {

    final static Logger LOG = Logger.getLogger(ServerDaryll.class.getName());

    /*
     * The TCP port where client connection requests are accepted. -1 indicates that
     * we want to use an ephemeral port number, assigned by the OS
     */
    private int listenPort = -1;

    /*
     * The server socket, used to accept client connection requests
     */
    private ServerSocket serverSocket;

    /*
     * The server maintains a list of client workers, so that they can be notified
     * when the server shuts down
     */
    List<ClientWorker> clientWorkers = new CopyOnWriteArrayList<>();

    /*
     * A flag that indicates whether the server should continue to run (or whether
     * a shutdown is in progress)
     */
    private boolean shouldRun = false;


    /**
     * Constructor used to create a server that will accept connections on a known
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
                        LOG.log(Level.INFO, "Listening for client connection on {0}", serverSocket.getLocalSocketAddress());
                        Socket clientSocket = serverSocket.accept();
                        LOG.info("New client has arrived...");
                        ClientWorker worker = new ClientWorker(clientSocket, getClientHandler(), ServerDaryll.this);
                        clientWorkers.add(worker);
                        LOG.info("Delegating work to client worker...");
                        Thread clientThread = new Thread(worker);
                        clientThread.start();
                    } catch (IOException ex) {
                        LOG.log(Level.SEVERE, "IOException in main server thread, exit: {0}", ex.getMessage());
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
     * Indicates whether the server is accepting connection requests, by checking
     * the state of the server socket
     *
     * @return true if the server accepts client connection requests
     */
    public boolean isRunning() {
        return (serverSocket.isBound());
    }

    /**
     * Getter for the TCP port number used by the server socket.
     *
     * @return the port on which client connection requests are accepted
     */
    public int getPort() {
        return serverSocket.getLocalPort();
    }

    /**
     * Requests a server shutdown. This will close the server socket and notify
     * all client workers.
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
     * This method is invoked by the client worker when it has completed its
     * interaction with the server (e.g. the user has issued the BYE command, the
     * connection has been closed, etc.)
     *
     * @param worker the worker which has completed its work
     */
    public void notifyClientWorkerDone(ClientWorker worker) {
        clientWorkers.remove(worker);
    }

}

package tcpserver.src.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * The type Server.
 */
public class Server implements Runnable{
    private static final Logger logger = Logger.getLogger("Server");

    /**
     * Instantiates a new Server.
     */
    Server(){
        logger.setLevel(Level.INFO);
    }
    private void runClientThread(ServerSocket server){
        try{
            Socket socket = server.accept();
            logger.log(Level.INFO, "Accepted connection from {0}.",
                    new Object[]{socket.getRemoteSocketAddress()});
            ClientThread clientThread = new ClientThread(socket);
            if(socket.isClosed()){
                logger.log(Level.INFO, "Socket already closed returning");
                return;
            }
            Thread t = new Thread(clientThread);
            t.start();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error accepting client connection", e);
        }
    }
    public void run() {

        try(ServerSocket server = new ServerSocket(ServerConstants.PORT)){
            logger.log(Level.INFO,"Server started on port {0}", ServerConstants.PORT);
            while(!Thread.currentThread().isInterrupted()){
                runClientThread(server);
            }
        } catch(IOException ex) {
            logger.log(Level.SEVERE, String.format("Exception caught while trying to listen on port %d Error: %s", ServerConstants.PORT, ex.getMessage()));

        }
        logger.log(Level.INFO,"Server closed");
    }

}

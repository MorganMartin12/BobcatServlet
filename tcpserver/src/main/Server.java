package tcpserver.src.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
    public void run() {

        try(ServerSocket server = new ServerSocket(ServerConstants.PORT);
            Socket socket = server.accept();
            PrintWriter output = new PrintWriter(socket.getOutputStream());
            BufferedReader input =  new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            logger.log(Level.INFO,"Server started on port {0}", ServerConstants.PORT);
            String inputLine;
            while ((inputLine = input.readLine()) != null) {
                logger.log(Level.INFO, "Server received {0}",inputLine);
                output.println(inputLine);
                output.flush();
            }
        } catch(IOException ex) {
            logger.log(Level.SEVERE, String.format("Exception caught while trying to listen on port %d Error: %s", ServerConstants.PORT, ex.getMessage()));

        }
        logger.log(Level.INFO,"Server closed");
    }

}

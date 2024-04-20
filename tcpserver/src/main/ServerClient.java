package tcpserver.src.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The type Server client.
 */
public class ServerClient {
    private static final Logger logger =Logger.getLogger("ServerClient");

    /**
     * Instantiates a new Server client.
     */
    ServerClient(){
        logger.setLevel(Level.INFO);
    }

    /**
     * Start client socket connection, reads input to send to server.
     */
    public void startClient() {
        try(    Socket socket = new Socket("localhost", ServerConstants.PORT);
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            System.out.println("Please enter message: ");
            while ((userInput = stdIn.readLine()) != null) {
                output.println(userInput);
                logger.log(Level.INFO, "Client received: {0}",input.readLine());
                output.flush();
                System.out.println("Please enter message: ");
            }
        }
        catch(IOException e){
            logger.log(Level.INFO,String.format("Io connection error : %s",e));
        }
    }
}

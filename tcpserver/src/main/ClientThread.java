package tcpserver.src.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread implements Runnable {
    private static final Logger logger = Logger.getLogger("Server");
    private final Socket socket;

    ClientThread(Socket socket) {
        this.socket = socket;
        logger.setLevel(Level.INFO);
    }

    public void run() {
        if (this.socket.isClosed()) {
            logger.log(Level.SEVERE, "Socket is already closed before obtaining streams.");
            return;
        }
        try (PrintWriter output = new PrintWriter(this.socket.getOutputStream());
             BufferedReader input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()))) {
            String inputLine;
            while ((inputLine = input.readLine()) != null) {
                logger.log(Level.INFO, "Server received {0}",inputLine);
                output.println(inputLine);
                output.flush();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, String.format("Exception caught while trying to listen on port %d Error: %s", ServerConstants.PORT, e.getMessage()));
        } finally{
            try {
                socket.close();
            }catch (IOException e) {
                logger.log(Level.SEVERE, String.format("Exception caught while trying close socket %d : %s", socket.getPort(), e.getMessage()));
            }
        }
    }
}

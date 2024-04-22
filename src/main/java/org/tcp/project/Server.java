package org.tcp.project;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * The type Server.
 */
public class Server implements Runnable{
    private static final Logger logger = Logger.getLogger("Server");
    private ExecutorService executorService;
    private ServerSocket serverSocket;
    /**
     * Instantiates a new Server.
     */
    Server(){
        logger.setLevel(Level.INFO);
        this.executorService = Executors.newCachedThreadPool();
    }
    public void run() {

        try(ServerSocket serverSocketResource= new ServerSocket(ServerConstants.PORT);){
            serverSocket = serverSocketResource;
            logger.log(Level.INFO,"Server started on port {0}", ServerConstants.PORT);
            while(!Thread.currentThread().isInterrupted()){
                Socket socket = serverSocket.accept();
                logger.log(Level.INFO, "Accepted connection from {0}.",
                        new Object[]{socket.getRemoteSocketAddress()});
                executorService.submit(new ClientThread(socket));
            }
        } catch(IOException e) {
            logger.log(Level.SEVERE, "Severe Server error, shutting down", e);

        }
        finally {
            stop();
        }
    }
    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error closing server socket", e);
        }
        shutdownAndAwaitTermination(executorService);
    }

    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(4, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(4, TimeUnit.SECONDS)) {
                    logger.warning("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

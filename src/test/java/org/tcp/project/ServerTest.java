package org.tcp.project;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ServerTest {
    private Server server;
    private Thread serverThread;

    @BeforeEach
    void setUp() {
        server = new Server();
        serverThread = new Thread(server);
        serverThread.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
        try {
            serverThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    @Test
    public void recievesSuccessfulMessage () {
        String echoedMessage = null;
        try(Socket socket = new Socket("localhost", ServerConstants.PORT);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            output.println("test-message");
            echoedMessage= input.readLine();
        }catch(IOException e){
            e.printStackTrace();
        }
        Assertions.assertEquals(echoedMessage, "test-message");
    }
    @Test
    public void recievesSuccessfulMessageWithMultipleClients ()  throws Exception {
        ArrayList<Future> echoedMessages = new ArrayList<Future>();
        for(int i=0;i<10;i++) {
            echoedMessages.add( Executors.newCachedThreadPool().submit(() -> {
                try (Socket socket = new Socket("localhost", ServerConstants.PORT);
                     PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    output.println("test-message");
                    return input.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }));
        }
        for(int i=0;i<10;i++) {
            Future future = echoedMessages.get(i);
            future.get();
        }
        Assertions.assertEquals(echoedMessages.size(), 10);
    }
}

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


class ServerTest {
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
    void receivesSuccessfulMessage() {
        String echoedMessage = null;
        try(Socket socket = new Socket("localhost", ServerConstants.PORT);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            output.println("test-message");
            echoedMessage= input.readLine();
        }catch(IOException e){
        }
        Assertions.assertEquals("test-message", echoedMessage);
    }
    @Test
    void receivesSuccessfulMessageWithMultipleClients ()  throws Exception {
        ArrayList<Future<String>> echoedMessages = new ArrayList<>();
        for(int i=0;i<10;i++) {
            echoedMessages.add( Executors.newCachedThreadPool().submit(() -> {
                try (Socket socket = new Socket("localhost", ServerConstants.PORT);
                     PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    output.println("test-message");
                    return input.readLine();
                } catch (IOException e) {
                }
                return null;
            }));
        }
        for(int i=0;i<10;i++) {
            Future<String> future = echoedMessages.get(i);
            future.get();
        }
        Assertions.assertEquals(10,echoedMessages.size());
    }
    @Test
    void serverClosedBeforeMessageSent()  throws Exception {
        Socket socket = new Socket("localhost", ServerConstants.PORT);
        try(PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            Thread.sleep(500);
            server.stop();
            serverThread.join();
            output.println("test-message");
        }catch(IOException e){
            Assertions.assertTrue(e.getMessage().contains("An established connection was aborted by the software in your host machine"), "Expected socket to be closed.");
        }
    }

    @Test
    void veryLargeMessage()  throws Exception {
        try( Socket socket = new Socket("localhost", ServerConstants.PORT);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            String largeMessage = generateLargeMessage(2048);
            output.println(largeMessage);
            String response = input.readLine();
            Assertions.assertEquals(largeMessage, response);
        }catch(IOException e){
        }

    }
    @Test
    void specialCharactersTest()  throws Exception {
        try( Socket socket = new Socket("localhost", ServerConstants.PORT);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            output.println("\0\0\0\0\0");
            String response = input.readLine();
            Assertions.assertEquals("\0\0\0\0\0", response);
        }catch(IOException e){
        }

    }

    private String generateLargeMessage(int sizeInKilobytes) {
        StringBuilder largeMessage = new StringBuilder();
        String baseString = "1234567890";  // 10 bytes long
        int iterations = sizeInKilobytes * 102;  // 102 repetitions per kilobyte approximately
        for (int i = 0; i < iterations; i++) {
            largeMessage.append(baseString);
        }
        return largeMessage.toString();
    }
}

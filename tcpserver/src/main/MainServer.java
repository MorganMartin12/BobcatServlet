package tcpserver.src.main;

/**
 * The type Main server.
 */
public class MainServer{
    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String[] args){
        Server server = new Server();
        ServerClient client = new ServerClient();
        Thread t = new Thread(server);
        t.start();
        client.startClient();
    }

}

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

        Thread t = new Thread(server);
        t.start();
    }

}

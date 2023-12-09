import java.net.*;

public class Server {
    private ServerSocket SerSock;

    public Server(ServerSocket SerSock) {
        this.SerSock = SerSock;
    }

    public static void main(String args[]) {
        try {
            ServerSocket serverSocket = new ServerSocket(50000);
            Server ser = new Server(serverSocket);
            System.out.println("Server in ascolto...");
            ser.AvvioServer();
        } catch (Exception e) {
            System.out.println("Errore server!:" + e);
        }
    }

    public void AvvioServer() {
        try {
            while (!SerSock.isClosed()) {
                Socket client = SerSock.accept();
                System.out.println("Un nuovo client si e' collegato! Ecco le info: " + client.getInetAddress());
                ClientHandler gestioneclient = new ClientHandler(client);
                Thread t = new Thread(gestioneclient);
                t.start();
            }

        } catch (Exception e) {
            System.out.println("Errore server!:" + e);
        }
    }

    public void ChiudiServer() {
        try {
            if (SerSock != null) {
                SerSock.close();
            }
        } catch (Exception e) {
            System.out.println("Errore server!:" + e);
        }
    }
}

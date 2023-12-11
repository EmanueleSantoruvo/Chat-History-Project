import java.io.IOException;
import java.net.*;

public class Server{
    private ServerSocket ss;

    public Server(ServerSocket ss){
        this.ss=ss;
    }

    public static void main(String[] args) throws IOException{
        ServerSocket servsock=new ServerSocket(25000);
        Server serv=new Server(servsock);
        serv.AvvioServer();
    }

    public void AvvioServer(){
        try {
            while(!ss.isClosed()){
                Socket s=ss.accept();
                System.out.println("Si e' collegato un nuovo client!:"+s.getRemoteSocketAddress()+s.getPort());
                ClientHandler ch=new ClientHandler(s);
                Thread t=new Thread(ch);
                t.start();
            }
        } catch (Exception e) {
            System.out.println("Errore apertura server:"+e);
        }
    }

    public void ChiudiServerSocket(){
        try {
            if(ss != null){
                ss.close();
            }
        } catch (Exception e) {
            System.out.println("Errore chiusura server socket:"+e);
        }
    }
}
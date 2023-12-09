import java.net.*;
/* CLASSE DEL SERVER
 * 
 * Lo scopo del server è quello di stare sempre in ascolto per le connessioni sul socket che gli abbiamo creato,in questo caso quello sulla porta 50000
 * gestendo autonomamente i client,con una classe secondaria chiamata ClientHandler
*/
public class server {
    public void main(String args[]){
        try {
            ServerSocket serverSocket=new ServerSocket(50000);
            server ser=new server(serverSocket);
            ser.AvvioServer();
        }catch (Exception e) {
            System.out.println("Errore!:"+e);
        }
    }
    private ServerSocket SerSock;

    public server (ServerSocket SerSock){
        this.SerSock = SerSock;
    }

    public void AvvioServer(){
        try {
            while(!SerSock.isClosed()){
                Socket client= SerSock.accept();
                System.out.println("Un nuovo client si e' collegato! Ecco le info: "+client.getInetAddress());
                ClientHandler gestioneclient = new ClientHandler(client);
                Thread t = new Thread(gestioneclient);
                t.start();
            }
    
        }catch (Exception e) {
            System.out.println("Errore!:"+e);
        }
    }

    public void ChiudiServer(){
        try{
            if(SerSock != null){
                SerSock.close();
            }
        }catch (Exception e) {
            System.out.println("Errore!:"+e);
        }
    }
}

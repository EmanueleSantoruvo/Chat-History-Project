import java.io.*;
import java.net.*;
import java.util.*;
public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket s;
    private BufferedReader br;
    private BufferedWriter bw;
    private String clientname;

    public ClientHandler(Socket s){
        try {
            this.s=s;
            this.br=new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.bw=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            this.clientname=br.readLine();
            clientHandlers.add(this);
            Broadcast("Server:"+clientname+" si e' unito");
        } catch (IOException e) {
            ChiudiTutto(s,br,bw);
        }
    }


    @Override
    public void run(){
        String messaggio;
        while(s.isConnected()){
            try {
                messaggio=">"+br.readLine();
                Broadcast(messaggio);
                System.out.println(messaggio);
            } catch (IOException e) {
                  ChiudiTutto(s,br,bw);
                  break;
            }
        }
    }

    public void Broadcast(String messaggio){
        for(ClientHandler ch : clientHandlers){
            try {
                if(!ch.clientname.equals(clientname)){
                    ch.bw.write(messaggio);
                    ch.bw.newLine();
                    ch.bw.flush();
                }
            } catch (IOException e) {
                ChiudiTutto(s,br,bw);
                break;
            }
        }
    }

    public void RimuoviClient(){
        clientHandlers.remove(this);
        Broadcast("Server:"+clientname+" si e' disconnesso");
    }

    public void ChiudiTutto(Socket s,BufferedReader br,BufferedWriter bw){
        RimuoviClient();
        try {
            if(br!=null){
                br.close();
            }
            if(bw!=null){
                bw.close();
            }
            if(s!=null){
                s.close();
            }
        } catch (IOException e) {
            System.out.println("Errore nella funzione di chiusura del ClientHandler:"+e);
        }
    }


















}   


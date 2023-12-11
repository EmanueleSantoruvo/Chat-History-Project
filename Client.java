import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    
    private Socket s;
    private BufferedReader br;
    private BufferedWriter bw;
    private String username;

    public Client(Socket s,String username){
        try {
            this.s=s;
            this.br=new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.bw=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            this.username=username;
        } catch (IOException e) {
            ChiudiTutto(s,br,bw);
        }
    }

    public static void main(String[] args) throws IOException{
        Socket s=new Socket("127.0.0.1",25000);
        Scanner sc=new Scanner(System.in);
        System.out.print("Inserisci un username:");
        String username=sc.nextLine();
        Client c=new Client(s,username);
        c.RiceviMessaggio();
        c.MandaMessaggio();
    }

    public void MandaMessaggio(){
        try {
            bw.write(username);
            bw.newLine();
            bw.flush();
            Scanner sc=new Scanner(System.in);
            while(s.isConnected()){
                String messaggio=sc.nextLine();
                bw.write(username+":"+messaggio);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            System.out.println("Errore invio messaggio client:"+e);
            ChiudiTutto(s,br,bw);
        }    
    }

    public void RiceviMessaggio(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                String messricevuto;
                while(s.isConnected()){
                    try {
                        messricevuto=br.readLine();
                        System.out.println(messricevuto);
                    } catch (Exception e) {
                        ChiudiTutto(s,br,bw);
                    }
                }
            }
        }).start();
    }

    public void ChiudiTutto(Socket s,BufferedReader br,BufferedWriter bw){
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
            System.out.println("Errore nella funzione di chiusura del Client:"+e);
        }
    }
}

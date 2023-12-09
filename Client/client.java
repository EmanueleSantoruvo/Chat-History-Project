import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String nomeutente;

    public Client(Socket socket, String nomeutente) {
        try {
            this.socket = socket;
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.nomeutente = nomeutente;
        } catch (Exception e) {
            ChiudiTuttoClient(socket, br, bw);
        }
    }

    public static void main(String[] args) {
        String nome;
        try {
            Scanner s = new Scanner(System.in);
            Socket socket = new Socket("127.0.0.1", 50000);
            System.out.print("Inserire nome utente:");
            nome = s.next();
            Client c = new Client(socket, nome);
            c.MandaRiceviMess();
        } catch (Exception e) {
            System.out.println("Errore!:" + e);
        }
    }

    public void MandaRiceviMess() {
        try {
            bw.write(nomeutente);
            bw.newLine();
            bw.flush();
            Scanner s = new Scanner(System.in);
            // Avvia un thread separato per ricevere i messaggi dal server
            Thread RiceviThread = new Thread(this::RiceviMess);
            RiceviThread.start();
            while (socket.isConnected()) {
                String messaggio = s.nextLine();
                bw.write(messaggio);
                bw.newLine();
                bw.flush();
                if (messaggio.equalsIgnoreCase("fine"))
                    break;
            }
        } catch (Exception e) {
            System.out.println("Errore:" + e);
            ChiudiTuttoClient(socket, br, bw);
        }
    }

    public void RiceviMess() {
        try {
            String messaggio;
            while (socket.isConnected()) {
                messaggio = br.readLine();
                System.out.println(messaggio);
            }
        } catch (IOException e) {
            System.out.println("Errore nella ricezione dei messaggi: " + e);
            ChiudiTuttoClient(socket, br, bw);
        }
    }

    public void ChiudiTuttoClient(Socket socket, BufferedReader br, BufferedWriter bw) {
        try {
            if (socket != null) {
                socket.close();
            }
            if (br != null) {
                br.close();
            }
            if (bw != null) {
                bw.close();
            }
        } catch (Exception e) {
            System.out.println("Errore!:" + e);
        }
    }
}


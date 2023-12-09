import java.io.*;
import java.util.*;
import java.net.*;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> ch = new ArrayList<>();
    private static ArrayList<String> cronologia = new ArrayList<>();
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String nomeutente;

    public ClientHandler(Socket socket) {
        try {
            ch.add(this);
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.nomeutente = br.readLine();
            InviaCronologia();
            Broadcast("Server: " + nomeutente + " si è unito");
        } catch (Exception e) {
            ChiudiTuttoServer(socket, br, bw);
        }
    }

    @Override
    public void run() {
        String messaggio;
        while (socket.isConnected()) {
            try {
                messaggio = br.readLine();
                cronologia.add(nomeutente + ": " + messaggio);
                Broadcast(messaggio);
            } catch (Exception e) {
                ChiudiTuttoServer(socket, br, bw);
            }
        }
    }

    public void Broadcast(String messaggio) {
        for (int i = 0; i < ch.size(); i++) {
            ClientHandler client = ch.get(i);
            try {
                if (!client.nomeutente.equals(nomeutente)) {
                    client.bw.write(messaggio + "\n");
                    client.bw.flush();
                }
            } catch (Exception e) {
                ChiudiTuttoServer(socket, br, bw);
            }
        }
        // Rimuovi il messaggio dalla cronologia dopo l'invio
        cronologia.remove(nomeutente + ": " + messaggio);
    }
    

    public void RimuoviClient() {
        ch.remove(this);
        Broadcast(nomeutente + " ha lasciato la chat");
    }

    public void ChiudiTuttoServer(Socket socket, BufferedReader br, BufferedWriter bw) {
        SalvaCronologia();
        RimuoviClient();
        try {
            if (br != null) {
                br.close();
            }
            if (bw != null) {
                bw.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            System.out.println("Errore handler!:" + e);
        }
    }

    private void SalvaCronologia() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("cronologia.txt", true), true)) {
            for (int i = 0; i < cronologia.size(); i++) {
                String messaggio = cronologia.get(i);
                writer.println(messaggio);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private void InviaCronologia() {
        for (int i = 0; i < cronologia.size(); i++) {
            String messaggio = cronologia.get(i);
            InviaMessaggio(messaggio);
        }
    }

    private void InviaMessaggio(String messaggio) {
        try {
            bw.write(messaggio + "\n");
            bw.flush();
        } catch (IOException e) {
            System.out.println("Errore handler!:" + e);
        }
    }
}

import java.io.*;
import java.net.*;
import java.util.*;
/*Il codice implementa la gestione dei client in un server di chat. Ciascun client è gestito da un'istanza di ClientHandler che gestisce la comunicazione, la ricezione e l'invio di messaggi, la gestione di una cronologia dei messaggi e la registrazione nel file di log. 
La classe mantiene una lista statica di tutti gli oggetti ClientHandler attivi per consentire la comunicazione tra i vari client*/
public class ClientHandler implements Runnable {

    // Lista statica per mantenere traccia degli oggetti ClientHandler attivi
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    // Riferimento al Socket per la connessione
    private Socket s;
    // Lettore per leggere dati dal client
    private BufferedReader br;
    // Scrittore per inviare dati al client
    private BufferedWriter bw;
    // Nome del client
    private String clientname;
    // Costruttore della classe ClientHandler
    public ClientHandler(Socket s) {
        try {
            // Inizializzazione delle variabili di istanza
            this.s = s;
            this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            // Lettura del nome del client
            this.clientname = br.readLine();
            // Aggiunta dell'istanza corrente alla lista degli oggetti ClientHandler attivi
            clientHandlers.add(this);
            // Messaggio di broadcast informando che il client si è unito
            Broadcast("Server:" + clientname + " si e' unito");
            // Leggi la cronologia e invia al nuovo client
            LeggiCronologia(bw);
        } catch (IOException e) {
            // Gestione dell'eccezione in caso di errore durante l'inizializzazione
            ChiudiTutto(s, br, bw);
        }
    }

    // Metodo run, implementato dall'interfaccia Runnable
    @Override
    public void run() {
        String messaggio;
        // Ciclo per gestire la ricezione dei messaggi finché la connessione è attiva
        while (s.isConnected()) {
            try {
                // Leggi un messaggio dal client
                messaggio = ">" + br.readLine();
                // Scrivi il messaggio nel file di log
                ScriviMessaggio(messaggio);
                // Invia il messaggio a tutti gli altri client
                Broadcast(messaggio);
                // Stampa il messaggio a livello di server
                System.out.println(messaggio);
            } catch (IOException e) {
                // Gestione dell'eccezione in caso di errore durante la lettura del messaggio
                ChiudiTutto(s, br, bw);
                break;
            }
        }
    }

    // Metodo per inviare un messaggio a tutti gli altri client
    public void Broadcast(String messaggio) {
        for (ClientHandler ch : clientHandlers) {
            try {
                // Verifica se il client corrente è diverso da quello che sta inviando il messaggio
                if (!ch.clientname.equals(clientname)) {
                    // Invia il messaggio al client corrente
                    ch.bw.write(messaggio);
                    ch.bw.newLine();
                    ch.bw.flush();
                }
            } catch (IOException e) {
                // Gestione dell'eccezione in caso di errore durante l'invio del messaggio
                ChiudiTutto(s, br, bw);
                break;
            }
        }
    }

    // Metodo statico per scrivere un messaggio nel file di log
    public static void ScriviMessaggio(String messaggio) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("log.txt", true));
            pw.println(messaggio);
            pw.close();
        } catch (Exception e) {
            // Gestione dell'eccezione in caso di errore durante la scrittura nel file di log
            System.out.println("Errore nella scrittura del messaggio nel file:" + e);
        }
    }

    // Metodo statico per leggere la cronologia dal file di log e inviarla al client
    public static void LeggiCronologia(BufferedWriter bw) {
        try {
            BufferedReader BR = new BufferedReader(new FileReader("log.txt"));
            String mess = BR.readLine();
            // Invia un delimitatore di inizio cronologia al client
            bw.write("\n--INIZIO CRONOLOGIA--\n");
            if(mess==null){
                bw.write("Non c'è nessun messaggio nella cronologia");
            }
            // Invia la cronologia al client
            while (mess != null){    
                bw.write(mess);
                bw.newLine();
                bw.flush();
                mess = BR.readLine();
            }
            // Invia un delimitatore di fine cronologia al client
            if(mess==null){
                bw.write("\n--FINE CRONOLOGIA--\n");
                bw.newLine();
                bw.flush();
                BR.close(); 
            }
            // Chiudi il lettore del file di log
        } catch (Exception e) {
            // Gestione dell'eccezione in caso di errore durante la lettura del file di log
            System.out.println("Un utente deve cominciare a scrivere per far creare la cronologia");
        }
    }
    // Metodo per rimuovere il client dalla lista degli oggetti ClientHandler attivi e inviare un messaggio di disconnessione
    public void RimuoviClient() {
        clientHandlers.remove(this);
        Broadcast("Server:" + clientname + " si e' disconnesso");
    }

    // Metodo per chiudere tutti i canali di comunicazione e rimuovere il client dalla lista attiva
    public void ChiudiTutto(Socket s, BufferedReader br, BufferedWriter bw) {
        RimuoviClient();
        try {
            // Chiudi il lettore se non è nullo
            if (br != null) {
                br.close();
            }
            // Chiudi lo scrittore se non è nullo
            if (bw != null) {
                bw.close();
            }
            // Chiudi il socket se non è nullo
            if (s != null) {
                System.out.println("Client scollegato:"+s.getRemoteSocketAddress());
                s.close();
            }
        } catch (IOException e) {
            // Gestione dell'eccezione in caso di errore nella chiusura dei canali di comunicazione
            System.out.println("Errore nella funzione di chiusura del ClientHandler:" + e);
        }
    }
}

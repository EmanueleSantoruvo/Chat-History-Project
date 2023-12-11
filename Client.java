import java.io.*;
import java.net.*;
import java.util.*;
/*Client di chat che si connette a un server su 127.0.0.1 (localhost) sulla porta 25000. Il client permette all'utente di inserire un username, invia l'username al server, e successivamente consente all'utente di inviare e ricevere messaggi. 
La classe Client gestisce l'apertura e la chiusura dei canali di comunicazione, nonché la creazione di thread separati per la ricezione e l'invio dei messaggi. */
public class Client {
    // Riferimento al socket per la connessione
    private Socket s;
    // Lettore per leggere dati dal server
    private BufferedReader br;
    // Scrittore per inviare dati al server
    private BufferedWriter bw;
    // Nome utente del client
    private String username;

    // Costruttore della classe Client
    public Client(Socket s, String username) {
        try {
            // Inizializzazione delle variabili di istanza
            this.s = s;
            this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            this.username = username;
        } catch (IOException e) {
            // Gestione dell'eccezione in caso di errore durante l'inizializzazione
            ChiudiTutto(s, br, bw);
        }
    }

    // Metodo principale per l'esecuzione del client
    public static void main(String[] args) throws IOException {
        // Creazione di un socket per la connessione al server sulla porta 25000
        Socket s = new Socket("127.0.0.1", 25000);
        // Creazione di un oggetto Scanner per leggere l'input dell'utente
        Scanner sc = new Scanner(System.in);
        System.out.print("Inserisci un username:");
        // Lettura dell'username da input
        String username = sc.nextLine();
        // Creazione di un oggetto Client passando il socket e l'username
        Client c = new Client(s, username);
        // Avvio di due thread per gestire la ricezione e l'invio di messaggi
        c.RiceviMessaggio();
        c.MandaMessaggio();
    }

    // Metodo per inviare messaggi al server
    public void MandaMessaggio() {
        try {
            // Invio dell'username al server
            bw.write(username);
            bw.newLine();
            bw.flush();
            // Creazione di un oggetto Scanner per leggere l'input dell'utente
            Scanner sc = new Scanner(System.in);
            // Ciclo per inviare messaggi al server finché la connessione è attiva
            while (s.isConnected()) {
                // Lettura del messaggio da input
                String messaggio = sc.nextLine();
                // Invio del messaggio al server
                bw.write(username + ":" + messaggio);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            // Gestione dell'eccezione in caso di errore durante l'invio del messaggio
            System.out.println("Errore invio messaggio client:" + e);
            // Chiusura di tutti i canali di comunicazione
            ChiudiTutto(s, br, bw);
        }
    }

    // Metodo per ricevere messaggi dal server
    public void RiceviMessaggio() {
        // Creazione di un nuovo thread per la gestione della ricezione dei messaggi
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Stringa per memorizzare il messaggio ricevuto
                String messaggioRicevuto;
                // Ciclo per ricevere messaggi finché la connessione è attiva
                while (s.isConnected()) {
                    try {
                        // Lettura del messaggio dal server
                        messaggioRicevuto = br.readLine();
                        // Stampa del messaggio ricevuto
                        System.out.println(messaggioRicevuto);
                    } catch (Exception e) {
                        // Gestione dell'eccezione in caso di errore durante la lettura del messaggio
                        ChiudiTutto(s, br, bw);
                    }
                }
            }
        }).start();
    }

    // Metodo per chiudere tutti i canali di comunicazione
    public void ChiudiTutto(Socket s, BufferedReader br, BufferedWriter bw) {
        try {
            // Chiusura del lettore se non è nullo
            if (br != null) {
                br.close();
            }
            // Chiusura dello scrittore se non è nullo
            if (bw != null) {
                bw.close();
            }
            // Chiusura del socket se non è nullo
            if (s != null) {
                s.close();
            }
        } catch (IOException e) {
            // Gestione dell'eccezione in caso di errore nella chiusura dei canali di comunicazione
            System.out.println("Errore nella funzione di chiusura del Client:" + e);
        }
    }
}


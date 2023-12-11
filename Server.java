import java.io.*;
import java.net.*;
//il server rimane in ascolto sulla porta 25000,accetta connessioni da client,crea un gestore per ciascuna connessione e gestisce ciascuna connessione in un thread separato.
public class Server {
    
    // Dichiarazione di un oggetto ServerSocket
    private ServerSocket ss;
    // Costruttore della classe Server
    public Server(ServerSocket ss) {
        this.ss = ss;
    }

    // Metodo principale della classe Server
    public static void main(String[] args) throws IOException {
        // Creazione di un oggetto ServerSocket sulla porta 25000
        ServerSocket servsock = new ServerSocket(25000);
        // Creazione di un oggetto Server e passaggio dell'oggetto ServerSocket
        Server serv = new Server(servsock);
        // Stampa di un messaggio di avvio
        System.out.println("Server in ascolto...");
        // Chiamata al metodo AvvioServer
        serv.AvvioServer();
    }

    // Metodo per avviare il server
    public void AvvioServer() {
        try {
            // Ciclo di attesa per nuove connessioni finché il server è aperto
            while (!ss.isClosed()) {
                // Accettazione di una nuova connessione e ottenimento del Socket associato
                Socket s = ss.accept();
                // Stampa delle informazioni relative al client appena connesso
                System.out.println("Si e' collegato un nuovo client, ecco le info:" + s.getRemoteSocketAddress());
                // Creazione di un gestore client (ClientHandler) per gestire la connessione con questi ultimi
                ClientHandler ch = new ClientHandler(s);
                // Creazione e avvio di un nuovo thread per gestire la connessione 
                Thread t = new Thread(ch);
                t.start();
            }
        } catch (IOException e) {
            // Gestione dell'eccezione in caso di errore nell'apertura del server
            System.out.println("Errore apertura server:" + e);
        }
    }

    // Metodo per chiudere il ServerSocket
    public void ChiudiServerSocket() {
        try {
            // Verifica se il ServerSocket è diverso da null
            if (ss != null) {
                // Stampa di un messaggio indicante la disconnessione di un client
                System.out.println("Si e' scollegato un client");
                // Chiusura del ServerSocket
                ss.close();
            }
        } catch (IOException e) {
            // Gestione dell'eccezione in caso di errore nella chiusura del ServerSocket
            System.out.println("Errore chiusura server socket:" + e);
        }
    }
}

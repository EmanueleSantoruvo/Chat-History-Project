import java.io.*;
import java.net.*;
import java.util.*;
public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> ch = new ArrayList<>();
    private Socket s;
    private BufferedReader br;
    private BufferedWriter bw;
    private String username;

    public ClientHandler(Socket s){
        
    }


    @Override
    public void run(){

    }
}

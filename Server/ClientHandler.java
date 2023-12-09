import java.io.*;
import java.util.*;
import java.net.*;
public class ClientHandler implements Runnable{
    private Socket socket;

    public ClientHandler(Socket socket){
		this.socket = socket;
	}
    
    @Override
    public void run() {

    }
    
}

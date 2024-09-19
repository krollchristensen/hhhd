import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Første besked er klientens brugernavn
            out.println("Skriv dit brugernavn:");
            username = in.readLine();
            ChatServer.broadcastMessage(username + " har sluttet sig til chatten", this);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Modtaget fra " + username + ": " + message);
                ChatServer.broadcastMessage(username + ": " + message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ChatServer.removeClient(this);
            ChatServer.broadcastMessage(username + " har forladt chatten.", this);
        }
    }

    // Send besked til klienten
    public void sendMessage(String message) {
        out.println(message);
    }
}

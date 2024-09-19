import java.io.*;
import java.net.*;

public class ChatClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader userInput;

    public ChatClient(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            userInput = new BufferedReader(new InputStreamReader(System.in));

            new Thread(new MessageListener()).start();
            
            System.out.println("Tilsluttet til chat-serveren. Skriv din besked nedenfor:");
            while (true) {
                String message = userInput.readLine();
                out.println(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Lytter til serverens beskeder
    private class MessageListener implements Runnable {
        public void run() {
            try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                    System.out.println(serverMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new ChatClient("localhost", 1234);
    }
}

import java.io.*;
import java.net.*;
import javax.swing.*; // Import for Swing components

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 12345; // Port number for the server
        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(port)) { 
            System.out.println("Server is running...");
            while (listening) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            System.exit(-1);
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine, outputLine;
            while ((inputLine = in.readLine()) != null) {
                showPopup("Client Message: " + inputLine); // Show popup for client message
                out.println("Message received"); // Send a simple response back to the client
                System.out.println("Server sent: Message received");
            }
        } catch (IOException e) {
            System.err.println("Problem with communication.");
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Could not close the socket.");
                e.printStackTrace();
            }
        }
    }

    private void showPopup(String message) {
        JOptionPane.showMessageDialog(null, message); // Show a popup window with the provided message
    }
}
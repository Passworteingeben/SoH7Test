import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

public class Client {
    public static void main(String[] args) throws IOException {
        String hostName = "localhost"; // Server's hostname or IP address
        // int serverPort = 12345; // Port number used by the server
        int clientPort = 12345; // Different port for this client

        try (Socket socket = new Socket(hostName, clientPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            System.out.println("Connected to Server on port " + clientPort);

            // Send a static message "Greetings from Client"
            String staticMessage = "Greetings from Client";
            out.println(staticMessage);
            showPopup("Message Sent"); // Show popup for message sent

            // Read the response from the server and print it to the console
            System.out.println("Echo from server: " + in.readLine());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            e.printStackTrace();
        }
    }

    private static void showPopup(String title) {
        JOptionPane.showMessageDialog(null, title + " from Server"); // Show a popup window
    }
}
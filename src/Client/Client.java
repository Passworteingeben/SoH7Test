import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

public class Client {
    public static void main(String[] args) throws IOException {
        String hostName = "localhost"; // Server's hostname or IP address
        int port = 12345; // Port number used by the server

        try (Socket socket = new Socket(hostName, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Connected to Server");

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                showPopup("Message Sent"); // Show popup for message sent
                System.out.println("Echo from server: " + in.readLine());
            }
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
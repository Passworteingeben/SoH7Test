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
               new ClientHandler(serverSocket.accept()).start();
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
                // Display the message received from the client in a popup window
                JOptionPane.showMessageDialog(null, "Client Message: " + inputLine);

               // Send a simple "Message received" back to the client
               out.println("Message received");
               System.out.println("Server sent: Message received");
           }
       } catch (IOException e) {
           System.err.println("Problem with communication.");
           e.printStackTrace();
       }
   }
}
import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

public class Server {

    public static void main(String[] args) throws IOException {
        int port = 12345; // Port number for the server
        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket()) { 
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress(port));
			shutdownHook(serverSocket);

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

	private static void shutdownHook(ServerSocket serverSocket) {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("ServerSocket closed by shutdown hook.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }));
}
}

class ClientHandler extends Thread {
    private static final char START_OF_BLOCK = 0x0b;
	private static final char END_OF_BLOCK = 0x1c;
	private static final char CARRIAGE_RETURN = 0x0d;

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream(), "UTF-8");
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            StringBuilder messageBuilder = new StringBuilder();
            int currentByte;
            boolean check_EoB = false;

            while ((currentByte = reader.read()) != -1) {
                char currentChar = (char) currentByte;
                if (currentChar == START_OF_BLOCK) { // Check for START_OF_BLOCK
                    messageBuilder.append(currentChar);
                } else if (currentChar == END_OF_BLOCK) { // Check for END_OF_BLOCK
                    messageBuilder.append(currentChar);
                    check_EoB = true;
                } else if (check_EoB) {
                    if (currentChar == CARRIAGE_RETURN) { // Check for CARRIAGE_RETURN
                        messageBuilder.append(currentChar);
                        break; // Exit loop after reading the end-of-block character
                    } else {
                        check_EoB = false;
                    }
                } else {
                    messageBuilder.append(currentChar);
                }
            }

            String receivedMessage = messageBuilder.toString();
            // showPopup("Received HL7 Message: " + receivedMessage); // Show popup for client message

            System.out.println("Received HL7 Message: " + receivedMessage);

            // Send an ACK response back to the client
            try (OutputStream outputStream = socket.getOutputStream();
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                 BufferedWriter writer = new BufferedWriter(outputStreamWriter)) {
                writer.write(START_OF_BLOCK + "ACK\n" + END_OF_BLOCK + CARRIAGE_RETURN); // Send "ACK" followed by a newline to indicate end of message
                writer.flush();
            } catch (IOException e) {
                System.err.println("Error while sending ACK: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Problem with communication.");
            e.printStackTrace();
        } finally {
            try {
                if (!socket.isClosed()) {
                    socket.close();
                    System.out.println("Socket closed.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showPopup(String message) {
        JOptionPane.showMessageDialog(null, message); // Show a popup window with the provided message
    }
}
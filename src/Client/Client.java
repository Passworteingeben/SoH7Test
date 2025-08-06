import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

public class Client {
   private static final char START_OF_BLOCK = 0x0b;
   private static final char END_OF_BLOCK = 0x1c;
   private static final char CARRIAGE_RETURN = 0x0d;

   public static void main(String[] args) throws IOException {
   	String host = "localhost"; // Server's host or IP address
   	int port = 12345; // Different port for this client

   	String message = testMessage();
   	try (Socket socket = new Socket(host, port)) {
   		shutdownHook(socket);
   		System.out.println("Connected to Server on port " + port);
   		socket.setSoTimeout(5000); // Set a timeout of 5 seconds for reading from the socket

   		//sending message
   		try {
   			OutputStream out = socket.getOutputStream();
   			System.out.println("Sending message: ");
   			out.write(message.getBytes("UTF-8"));
   			out.flush();
   		} catch (Exception e) {
   			System.err.println("Error while sending msg: ");
   			e.printStackTrace();
   		}

   		//showPopup("Message Sent"); // Show popup for message sent
   		System.out.println("message send");

   		try {
   			// Receive the ack message from the server
   			String ackMessage = receiveAck(socket);
   			System.out.println("ACK from server: " + ackMessage);
   		} catch (SocketTimeoutException e) {
   			System.err.println("Timed out waiting for ACK");
   			e.printStackTrace();
   		} catch (Exception e) {
   			System.err.println("Error while receiving ack: ");
   			e.printStackTrace();
   		}
   	} catch (IOException e) {
   		System.err.println("Error occurred during communication with server.");
   		e.printStackTrace();
   	} 
   }

   private static void shutdownHook(Socket socket) {
   	Runtime.getRuntime().addShutdownHook(new Thread(() -> {
   		if (socket != null && !socket.isClosed()) {
   			try {
   				socket.close();
   				System.out.println("Socket closed by shutdown hook.");
   			} catch (IOException e) {
   				e.printStackTrace();
   			}
   		}
   	}));
   }

   private static String testMessage() {
   	// Generate a sample HL7 message
   	String msg1 = START_OF_BLOCK + "MSH|^~\\&|SomeHL7App|SENDER|RECEIVER|201806251430||ADT^A01|1234567890|D|2.5\n" + END_OF_BLOCK + CARRIAGE_RETURN; 
   	return msg1;
   }

   private static String receiveAck(Socket socket) throws IOException {
   	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
   	StringBuilder ackMessageBuilder = new StringBuilder();
   	int currentByte;
   	boolean check_EoB = false;
   	while ((currentByte = in.read()) != -1) {
   		// System.out.println(currentByte);
   		
   		char currentChar = (char) currentByte;
   		if (currentChar == START_OF_BLOCK) { // Check for START_OF_BLOCK
   			ackMessageBuilder.append(currentChar);
   		} else if (currentChar == END_OF_BLOCK) { // Check for END_OF_BLOCK
   			ackMessageBuilder.append(currentChar);
   			check_EoB = true;
   		} else if (check_EoB) {
   			if (currentChar == CARRIAGE_RETURN) {
   				ackMessageBuilder.append(currentChar);
   				break;
   			} else {
   				check_EoB = false;
   			}
   		} else {
   			ackMessageBuilder.append(currentChar);
   		}
   		// System.out.println(currentChar);
   	}

   	return ackMessageBuilder.toString();
   }

   private static void showPopup(String title) {
   	JOptionPane.showMessageDialog(null, title + " from Server"); // Show a popup window
   }
}
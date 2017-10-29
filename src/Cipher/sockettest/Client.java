package sockettest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Client {

    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Client");


    public Client() {}
    
    /**
     * Once the client is connected, retrieve keyspace and begin 
     * running the algorithm.
     */
    private void start() {
    	String keyspace = getResponse("key");
    	System.out.println("Keyspace: " + keyspace);
    	
    	// TODO here: run algorithm in 5 threads over keyspace
    	// if key is found: getResponse("found");
    	
    	
    	// Ask server if key has been found
    	// Run every x minutes
    	String found = getResponse("check");
    	if (found.equals("true")) {
    		// Stop everything because key is found
    		getResponse(".");
    		System.exit(0);
    	} else {
    		System.out.println("Key has not been found yet. "
    				+ "Response: " + found);
    	}
    }
    
    /**
     * Send a message to the server and read the response
     * 
     * Values for msg:
     * 'check': will return true/false depending on if key has been found
     * 'key': will return keyspace for this client to use
     * '.': will end connection with server
     * 'found': will tell server that this client found key
     */
    private String getResponse(String msg) {
    	out.println(msg);
    	System.out.println("Sent to server: " + msg);
    	String response;
    	try {
    		response = in.readLine();
    		if (response == null) {
    			System.exit(0);
    		}
    	} catch(IOException ex) {
    		response = "Error: " + ex;
    	}
    	return response;
    }

    /**
     * Prompt the end user for the server's IP address, connecting, 
     * setting up streams, and consuming the welcome messages from the server.
     */
    public void connectToServer() throws IOException {

        // Get the server address from a dialog box.
        String serverAddress = JOptionPane.showInputDialog(
            frame,
            "Enter IP Address of the Server:",
            "connect to IP",
            JOptionPane.QUESTION_MESSAGE);

        // Make connection and initialize streams
        Socket socket = new Socket(serverAddress, 9898);
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Consume the initial welcoming messages from the server
        for (int i = 0; i < 3; i++) {
            System.out.println(in.readLine());
        }
        start();
    }

    /**
     * Runs the client application.
     */
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.pack();
        client.frame.setVisible(true);
        client.connectToServer();
    }
}
package sockettest;

// used for socket reference: http://cs.lmu.edu/~ray/notes/javanetexamples

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	
	// Indicates whether the key has been found
	private static boolean found = false;

    /**
     * Run in an infinite loop listening on port 9898. When a connection is 
     * requested, it spawns a new thread to do the servicing and immediately 
     * returns to listening.  The server keeps a unique client number for each
     * client that connects.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("The server is running.");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9898);
        try {
            while (true) {
                new RequestHandler(listener.accept(), clientNumber++).start();
            }
        } finally {
            System.out.println("closed server");
            listener.close();
        }
    }

    /**
     * A private thread to handle requests on a particular
     * socket.  The client terminates the dialogue by sending a single line
     * containing only a period.
     */
    private static class RequestHandler extends Thread {
        private Socket socket;
        private int clientNumber;
        
        public RequestHandler(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            log("New connection with client #" + clientNumber + " at " + socket);
        }

        /**
         * Services this thread's client by first sending the
         * client a welcome message then repeatedly reading strings
         * and sending back the an appropriate response.
         */
        public void run() {
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Send a welcome message to the client.
                out.println("You are client #" + clientNumber + ".");
                out.println("Send a line with only a period to quit\n");

                // Get messages from the client, line by line
                while (true) {
                    String input = in.readLine();
                    if (input == null || input.equals(".")) {
                        break;
                    }
                    
                    // Client requesting their keyspace
                    if (input.equals("key")) {
                    	out.println("keyspace" + clientNumber);
                    	//out.println(getKeySpace(clientNumber));
                    	//TODO: implement getKeySpace
                    }
                    
                    // Client says they found the key
                    if (input.equals("found")) {
                    	found = true;
                    	out.println("marked as found.");
                    }
                    
                    // Client wants to know if key has been found
                    if (input.equals("check")) {
                    	out.println(found);
                    }
                }
            } catch (IOException e) {
                log("Error handling client #" + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log("Couldn't close a socket");
                }
                log("Connection with client #" + clientNumber + " closed");
            }
        }

        private void log(String message) {
            System.out.println(message);
        }
    }
}
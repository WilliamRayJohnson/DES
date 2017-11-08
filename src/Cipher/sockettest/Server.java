package Cipher.sockettest;

// used for socket reference: http://cs.lmu.edu/~ray/notes/javanetexamples

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {

   final private static BigInteger keySpaceSize = BigInteger.valueOf(1000000000);

   // Highest key that has been sent to a client
   private static BigInteger highestKey = BigInteger.ZERO;

   // List containing lower bounds of keyspaces that are being used
   private static ArrayList<BigInteger> runningKeySpaces = new ArrayList<BigInteger>();

   // List containing lower bounds of keyspaces which were abandoned by a client
   private static ArrayList<BigInteger> incompleteKeySpaces = new ArrayList<BigInteger>();

   // Indicates whether the key has been found
   private static boolean found = false;

   // Value of key if it is found
   private static BigInteger key = null;

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
           String clientKeySpace = "";
           try {
               BufferedReader in = new BufferedReader(
                       new InputStreamReader(socket.getInputStream()));
               PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

               // Send a welcome message to the client.
               out.println("You are client #" + clientNumber + ".");
               out.println("Send a line with only a period to quit\n");

               // Send plaintext and ciphertext to client
               out.println("councill");
               out.println("130504865423CAAE");

               // Get messages from the client, line by line
               while (true) {
                   String input = in.readLine();
                   if (input == null || input.equals(".")) {
                       break;
                   }

                   // Client requesting their keyspace
                   if (input.equals("key")) {
                       clientKeySpace = getKeySpace();
                       out.println(clientKeySpace);
                   }

                   // Client indicating they've finished their keyspace
                   if (input.equals("complete")) {
                       runningKeySpaces.remove(runningKeySpaces.indexOf(new BigInteger(clientKeySpace.split(",")[0])));
                       clientKeySpace = "";
                       out.println("completed keyspace.");
                   }

                   // Client says they found the key
                   if (input.contains("found")) {
                       found = true;
                       out.println("marked as found.");
                       key = new BigInteger(input.split(",")[1]);
                       out.println("key: " + key);
                   }

                   if (input.equals("getKey")) {
                       out.println(getKey());
                   }

                   // Client wants to know if key has been found
                   if (input.equals("check")) {
                       out.println(found);
                   }
               }
           } catch (IOException e) {
               log("Error handling client #" + clientNumber + "x: " + e);
           } finally {
               try {
                   socket.close();
               } catch (IOException e) {
                   log("Couldn't close a socket");
               }
               log("Connection with client #" + clientNumber + " closed");

               // Mark client's keyspace as incomplete
               if (clientKeySpace.length() > 0) {
                   markIncomplete(clientKeySpace);
               }
           }
       }

       /**
        * Mark a keyspace as incomplete if a client disconnects unexpectedly
        * @param clientKeySpace keyspace formatted firstkey,lastkey that the
        * client presumably did not complete
        */
       private void markIncomplete(String clientKeySpace) {
           BigInteger firstKey = new BigInteger(clientKeySpace.split(",")[0]);
           runningKeySpaces.remove(runningKeySpaces.indexOf(firstKey));
           incompleteKeySpaces.add(firstKey);
       }

       /**
        * If key is null, tell client keys has still not been found
        * Otherwise, return the key
        */
       private String getKey() {
           if (key != null)
           {
               return key.toString();
           }
           else
           {
               return "The key has not been found";
           }
       }

       /**
        * If there are any incomplete keyspaces, return the first one.
        * Otherwise, return the next highest keyspace.
        * @return next keyspace to run as string formatted firstkey,lastkey
        */
       private String getKeySpace() {
           String firstKey;
           String lastKey;
           if (incompleteKeySpaces.isEmpty()) {
               runningKeySpaces.add(highestKey);
               firstKey = highestKey.toString();
               highestKey = highestKey.add(keySpaceSize);
               lastKey = highestKey.toString();
           } else {
               runningKeySpaces.add(incompleteKeySpaces.get(0));
               firstKey = incompleteKeySpaces.get(0).toString();
               lastKey = incompleteKeySpaces.get(0).add(keySpaceSize).toString();
               incompleteKeySpaces.remove(0);
           }
           log("Assigning keyspace: " + firstKey);
           return firstKey + "," + lastKey;
       }

       private void log(String message) {
           System.out.println(message);
       }

   }
}

package Cipher.sockettest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Cipher.BruteForce.DESBruteForceNode;


public class Client {

   private BufferedReader in;
   private PrintWriter out;

   public Client() {}

   /**
    * Once the client is connected, retrieve keyspace and begin
    * running the algorithm.
    */
   private void start(String plaintext, String ciphertext) {
       byte[] plainTextBytes = plaintext.getBytes();
       byte[] cipherTextBytes = ciphertext.getBytes();
       int[] plainTextInt = new int[8];
       int[] cipherTextInt = new int[8];
       for (int aByte = 0; aByte < 8; aByte++){
           plainTextInt[aByte] = Byte.toUnsignedInt(plainTextBytes[aByte]);
           cipherTextInt[aByte] = Byte.toUnsignedInt(cipherTextBytes[aByte]);
       }
       DESBruteForceNode node = new DESBruteForceNode(cipherTextInt, plainTextInt, 5);
       boolean keyFound = false;
       BigInteger keySpaceBegin;
       BigInteger keySpaceEnd;
       while (!serverFoundKey()) {
           String keyspace = getResponse("key");
           System.out.println("Keyspace: " + keyspace);

           String[] beginAndEnd = keyspace.split(",");
           keySpaceBegin = new BigInteger(beginAndEnd[0]);
           keySpaceEnd = new BigInteger(beginAndEnd[1]);
           node.setKeyspace(keySpaceBegin, keySpaceEnd);

           keyFound = node.run();
           if (keyFound) {
               // Tell server that key was found
               getResponse("found," + node.getFoundKey());
               break;
           }

           // Mark keyspace as complete
           getResponse("complete");
       }

       // Stop everything because key is found
       getResponse(".");
       System.exit(0);

   }

   /**
    * Ask server if anyone has found the key yet
    */
   private boolean serverFoundKey() {
       String found = getResponse("check");
       if (found.equals("true")) {
           System.out.println("Key: " + getResponse("getKey"));
           return true;
       } else {
           System.out.println("Key has not been found yet. "
                   + "Response: " + found);
           return false;
       }
   }

   /**
    * Send a message to the server and read the response
    *
    * Values for msg:
    * 'check': will return true/false depending on if key has been found
    * 'key': will return new keyspace for this client to use
    * '.': will end connection with server
    * 'found': will tell server that this client found key
    * 'complete': will tell server that client completed assigned keyspace
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
       String serverAddress = "13.58.224.85";

       // Make connection and initialize streams
       Socket socket = new Socket(serverAddress, 9898);
       in = new BufferedReader(
               new InputStreamReader(socket.getInputStream()));
       out = new PrintWriter(socket.getOutputStream(), true);

       // Consume the initial welcoming messages from the server
       for (int i = 0; i < 3; i++) {
           System.out.println(in.readLine());
       }

       String plaintext = in.readLine();
       System.out.println("plaintext: " + plaintext);
       String ciphertext = in.readLine();
       System.out.println("ciphertext: " + ciphertext);

       start(plaintext, ciphertext);
   }

   /**
    * Runs the client application.
    */
   public static void main(String[] args) throws Exception {
       Client client = new Client();
       client.connectToServer();
   }
}

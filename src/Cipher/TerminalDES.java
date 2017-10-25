package Cipher;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Scanner;

public class TerminalDES
{
    private static final Charset UTF8 = Charset.forName("UTF-8");
    
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        

        System.out.print("What is the plain/cipher text's file name?: ");
        String plainTextFileName = in.nextLine();
        System.out.print("What is the key's file name?: ");
        String keyFileName = in.nextLine();
        System.out.print("Would you like to encrypt or decrypt?: ");
        String encyptDecryptOption = in.nextLine();
        in.close();
        
        
        DES cipher = new DES(16, keyFileName);
        
        try
        {
            byte[] plainTextBytes = readFromFile(new File(plainTextFileName), UTF8);

            int[] currentBlock = new int[8];
            int[] cipherTextBlock = null;
            StringBuilder cipherText = new StringBuilder(plainTextBytes.length);
            for (int block = 0; block < plainTextBytes.length; block += 8)
            {
                for (int aByte = 0; aByte < 8; aByte++)
                {
                    if(block + aByte >= plainTextBytes.length){
                       currentBlock[aByte] = 88;
                    }
                    else{
                        currentBlock[aByte] = Byte.toUnsignedInt(plainTextBytes[block + aByte]);
                    }
                }
                
                
                if(encyptDecryptOption.equals("encrypt")){
                    cipherTextBlock = cipher.encrypt(currentBlock);
                }
                else if(encyptDecryptOption.equals("decrypt")){
                    cipherTextBlock = cipher.decrypt(currentBlock);
                }
                else{
                    cipherTextBlock = cipher.encrypt(currentBlock);
                }
                
                
                byte[] output = new byte[8];
                for(int ascii = 0; ascii < 8; ascii++)
                    output[ascii] = (byte) cipherTextBlock[ascii];
                    
                    
                cipherText.append(new String(output));
            }
            
            writeToFile(new File("cipherText.txt"), UTF8, cipherText.toString());
            
        } catch (Exception e)
        {
            e.printStackTrace();

        }

    }
    
    /**
     * Write data to a file given a particular character set.
     * Code found at http://illegalargumentexception.blogspot.com/2009/05/java-rough-guide-to-character-encoding.html
     * @param file The file to write to
     * @param charset The character set to use
     * @param data The data to write
     * @throws IOException
     */
    public static void writeToFile(File file, Charset charset, String data) throws IOException
    {
        OutputStream out = new FileOutputStream(file);
        Closeable stream = out;
        try
        {
            Writer writer = new OutputStreamWriter(out, charset);
            stream = writer;
            writer.write(data);
        } finally
        {
            stream.close();
        }
    }

    /**
     * Reads data from a file using a particular character set
     * Code found at http://illegalargumentexception.blogspot.com/2009/05/java-rough-guide-to-character-encoding.html
     * @param file The file to read from
     * @param charset The character set to use
     * @return An array of bytes representing the character's value
     * @throws IOException
     */
    public static byte[] readFromFile(File file, Charset charset) throws IOException
    {
        InputStream in = new FileInputStream(file);
        Closeable stream = in;
        try
        {
            Reader reader = new InputStreamReader(in, charset);
            stream = reader;
            StringBuilder inputBuilder = new StringBuilder();
            char[] buffer = new char[1024];
            while (true)
            {
                int readCount = reader.read(buffer);
                if (readCount < 0)
                    break;
                inputBuilder.append(buffer, 0, readCount);
            }
            return inputBuilder.toString().getBytes();
        } finally
        {
            stream.close();
        }
    }

}

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class TerminalDES
{

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
        ArrayList<Byte> text = new ArrayList<Byte>();
        
        try
        {
            String content = new String(Files.readAllBytes(Paths.get(plainTextFileName)));
            byte[] plainTextBytes = content.getBytes();
            
            
            FileWriter fw = new FileWriter("cipherText.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);

            int[] currentBlock = new int[8];
            int[] cipherTextBlock = null;
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
                    
                    
                out.print(new String(output));

            }
            
            out.flush();
            out.close();
            bw.close();
            fw.close();
            
        } catch (Exception e)
        {
            e.printStackTrace();

        }

    }

}

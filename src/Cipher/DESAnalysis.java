package Cipher;

import java.util.Random;


public class DESAnalysis
{
    public static void main(String[] args)
    {
        DES cipher = new DES(8);
        cipher.setKey("0001001100110100010101110111100110011011101111001101111111110001");

        int size = 8 * 10000;
        byte[] plainTextBytes = new byte[size];
        new Random().nextBytes(plainTextBytes);
        
        int[] cipherText;
        int[] plainText;
        int[] currentBlock = new int[8];
        long startTime = System.currentTimeMillis();
        for (int block = 0; block < plainTextBytes.length; block += 8)
        {
            for (int aByte = 0; aByte < 8; aByte++)
            {
                if (block + aByte >= plainTextBytes.length)
                {
                    currentBlock[aByte] = 88;
                } else
                {
                    currentBlock[aByte] = Byte.toUnsignedInt(plainTextBytes[block + aByte]);
                }
            }

            cipherText = cipher.encrypt(currentBlock);
            plainText = cipher.decrypt(cipherText);
        }
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        System.out.println("It took: " + time);

    }
}

import java.math.BigInteger;
import java.util.Random;

public class DESAnalysis {
    public static void main(String[] args) {
        DES cipher = new DES(16);
        cipher.setKeyBI(new BigInteger("1383827165325090801"));

        int size = 8;
        byte[] plainTextBytes = new byte[size];
        new Random().nextBytes(plainTextBytes);

        BigInteger cipherText;
        BigInteger plainText;
        byte[] currentBlock = new byte[8];
        long startTime = System.currentTimeMillis();
        for (int block = 0; block < plainTextBytes.length; block += 8) {
            for (int aByte = 0; aByte < 8; aByte++) {
                if (block + aByte >= plainTextBytes.length) {
                    currentBlock[aByte] = 88;
                } else {
                    currentBlock[aByte] = plainTextBytes[block + aByte];
                }
            }

            cipherText = cipher.encrypt(new BigInteger(currentBlock));
            plainText = cipher.decrypt(cipherText);
        }
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        System.out.println("It took: " + time);
    }
}

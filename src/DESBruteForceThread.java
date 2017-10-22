import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A thread that will run DES over a given key space.
 */
public class DESBruteForceThread implements Runnable {
    private BigInteger keySpaceBegin;
    private BigInteger keySpaceEnd;
    private int[] cipherText;
    private int[] plainText;
    private DES cipher;
    private AtomicBoolean keyFound;

    /**
     * Constructs a DES force thread for a given key space
     * @param keySpaceBegin the beginning of the key space, the first key to check
     * @param keySpaceEnd the end of the key space, the last key to check
     * @param cipherText the cipher text trying to be cracked
     * @param plainText the corresponding plain text
     */
    public DESBruteForceThread(BigInteger keySpaceBegin, BigInteger keySpaceEnd, int[] cipherText, 
    		int[] plainText, AtomicBoolean keyFound) {
        this.keySpaceBegin = keySpaceBegin;
        this.keySpaceEnd = keySpaceEnd;
        this.cipherText = cipherText;
        this.plainText = plainText;
        this.keyFound = keyFound;
        this.cipher = new DES(16);
    }

    @Override
    public void run() {
    	BigInteger currentKey = keySpaceBegin;
    	int[] cipherTextOfCurrentKey = new int[8];
        while(!keyFound.get() && !keySpaceEnd.equals(currentKey)) {
            cipher.setKey(currentKey.toString(2));
            cipherTextOfCurrentKey = cipher.encrypt(plainText);
            if(cipherTextOfCurrentKey == cipherText)
            	keyFound.set(true);
            currentKey = currentKey.add(BigInteger.ONE);
        }
    }
}

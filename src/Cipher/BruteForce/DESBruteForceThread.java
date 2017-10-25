package Cipher.BruteForce;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import Cipher.DES;

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
    	String currentKeyString;
    	int[] cipherTextOfCurrentKey = new int[8];
    	
        while(!keyFound.get() && !keySpaceEnd.equals(currentKey)) {
        	currentKeyString = currentKey.toString(2);
        	while (currentKeyString.length() < 64)
                currentKeyString = "0" + currentKeyString;
            cipher.setKey(currentKeyString);
            cipherTextOfCurrentKey = cipher.encrypt(plainText);
            if(Arrays.equals(cipherTextOfCurrentKey, cipherText))
            	keyFound.set(true);
            currentKey = currentKey.add(BigInteger.ONE);
        }
    }
}

package Cipher.BruteForce;

import java.util.concurrent.atomic.*;
import java.math.BigInteger;
import java.util.concurrent.*;

/**
 * A DES brute force node that will run n threads of DES. Broadcast a signal to other nodes if
 * key is found for a given pair of plain and cipher text.
 * 
 * ExecutorService example found at: https://stackoverflow.com/questions/11839881/how-to-stop-a-thread-by-another-thread
 */
public class DESBruteForceNode {
	private int threadCount;
	private BigInteger keySpaceBegin;
	private BigInteger keySpaceEnd;
	private int[] cipherText; 
	private int[] plainText;
	private String foundKey;

	public DESBruteForceNode(int[] cipherText, int[] plainText, int threadCount) {
		this.cipherText = cipherText;
		this.plainText = plainText;
		this.threadCount = threadCount;
	}
	
	/**
	 * Runs 5 threads of DES that will brute force a given key space and a cipher plain
	 * text pair.
	 * @return true if the key has been found
	 */
    public boolean run() {
        final AtomicBoolean keyFound = new AtomicBoolean(false);
        final AtomicReference<String> correctKey = new AtomicReference<String>();
        final AtomicInteger threadsCompleted = new AtomicInteger();
        final ExecutorService DESThreads = Executors.newFixedThreadPool(threadCount);
        
        for(int thread = 0; thread < threadCount; thread++)
        	DESThreads.submit(new DESBruteForceThread(null, null, 
        			cipherText, plainText, keyFound, correctKey, threadsCompleted)); 
        
        final ScheduledExecutorService node = Executors.newSingleThreadScheduledExecutor();
        node.schedule(new Runnable() {
        	public void run() {
        		while(!keyFound.get() && threadsCompleted.intValue() < threadCount) {}
        		DESThreads.shutdown();
        		node.shutdown(); 
        	}
        }, 500, TimeUnit.MILLISECONDS);
        
        if(keyFound.get())
        	foundKey = correctKey.get();
        
        return keyFound.get();
    }
    
    public String getFoundKey() {
    	return foundKey;
    }
    
    public void setKeyspace(BigInteger keySpaceBegin, BigInteger keySpaceEnd) {
    	this.keySpaceBegin = keySpaceBegin;
    	this.keySpaceEnd = keySpaceEnd;
    }
}

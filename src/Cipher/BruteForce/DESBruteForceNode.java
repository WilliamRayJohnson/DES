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
        BigInteger[][] threadRanges = divideUpKeys(keySpaceBegin, keySpaceEnd);
        
        for(int thread = 0; thread < threadCount; thread++)
        	DESThreads.submit(new DESBruteForceThread(threadRanges[thread][0], threadRanges[thread][1], 
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
    
    public BigInteger[][] divideUpKeys(BigInteger keySpaceBegin, BigInteger keySpaceEnd) {
        BigInteger range = keySpaceEnd.subtract(keySpaceBegin).divide(BigInteger.valueOf(threadCount));
        BigInteger[][] dividedUpKeys = new BigInteger[threadCount][2];
        BigInteger threadBeginKey = keySpaceBegin;
        BigInteger threadEndKey = keySpaceBegin.add(range.add(BigInteger.ONE));
        for(int thread = 0; thread < threadCount; thread++){
            dividedUpKeys[thread][0] = threadBeginKey;
            if(thread == threadCount - 1){
                dividedUpKeys[thread][1] = keySpaceEnd.add(BigInteger.ONE);
            }
            else{
                dividedUpKeys[thread][1] = threadEndKey;
            }
            threadBeginKey = threadEndKey;
            threadEndKey = threadEndKey.add(range);
        }
        return dividedUpKeys;
    }
    
    public String getFoundKey() {
    	return foundKey;
    }
    
    public void setKeyspace(BigInteger keySpaceBegin, BigInteger keySpaceEnd) {
    	this.keySpaceBegin = keySpaceBegin;
    	this.keySpaceEnd = keySpaceEnd;
    }
}

package Cipher.BruteForce;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A DES brute force node that will run n threads of DES. Broadcast a signal to other nodes if
 * key is found for a given pair of plain and cipher text.
 * 
 * ExecutorService example found at: https://stackoverflow.com/questions/11839881/how-to-stop-a-thread-by-another-thread
 */
public class DESBruteForceNode {
	final static int threadCount = 5;

    public static void main(String[] args) {
        final AtomicBoolean keyFound = new AtomicBoolean(false);
        final ExecutorService DESThreads = Executors.newFixedThreadPool(threadCount);
        
        for(int thread = 0; thread < threadCount; thread++)
        	DESThreads.submit(new DESBruteForceThread(null, null, null, null, keyFound)); //Thread parameters should come from command line
        
        final ScheduledExecutorService node = Executors.newSingleThreadScheduledExecutor();
        node.schedule(new Runnable() {
        	public void run() {
        		while(!keyFound.get()) {}
        		DESThreads.shutdown();
        		node.shutdown(); 
        	}
        }, 500, TimeUnit.MILLISECONDS);
    }
}

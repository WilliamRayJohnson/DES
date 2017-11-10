package Cipher.BruteForce;

import java.math.BigInteger;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * A DES brute force node that will run n threads of DES. Broadcast a signal to other nodes if key is found for a given pair of plain and cipher text.
 *
 * ExecutorService example found at: https://stackoverflow.com/questions/11839881/how-to-stop-a-thread-by-another-thread
 */
public class DESBruteForceNode {


	private int threadCount;
	private BigInteger keySpaceBegin;
	private BigInteger keySpaceEnd;
	private int[] cipherText;
	private int[] plainText;
	private int[] parityBits;
	private String foundKey;

	/**
	 * A node that will brute force DES over a given key space using a given number of threads
	 *
	 * @param cipherText
	 *            the cipher text
	 * @param plainText
	 *            the plain text
	 * @param threadCount
	 *            the number of threads to use
	 */
	public DESBruteForceNode(int[] cipherText, int[] plainText, int[] parityBits, int threadCount)
	{
		this.cipherText = cipherText;
		this.plainText = plainText;
		this.parityBits = parityBits;
		this.threadCount = threadCount;
	}

	/**
	 * Divides the key spaced based on the number of threads
	 *
	 * @param keySpaceBegin
	 *            the first key to be used in the space
	 * @param keySpaceEnd
	 *            the last key to be used in the space
	 * @return the range of keys for each thread as [begin:end)
	 */
	public BigInteger[][] divideUpKeys(BigInteger keySpaceBegin, BigInteger keySpaceEnd)
	{
		BigInteger range = keySpaceEnd.subtract(keySpaceBegin).divide(BigInteger.valueOf(threadCount));
		BigInteger[][] dividedUpKeys = new BigInteger[threadCount][2];
		BigInteger threadBeginKey = keySpaceBegin;
		BigInteger threadEndKey = keySpaceBegin.add(range.add(BigInteger.ONE));
		for (int thread = 0; thread < threadCount; thread++)
		{
			dividedUpKeys[thread][0] = threadBeginKey;
			if (thread == threadCount - 1)
			{
				dividedUpKeys[thread][1] = keySpaceEnd.add(BigInteger.ONE);
			}
			else
			{
				dividedUpKeys[thread][1] = threadEndKey;
			}
			threadBeginKey = threadEndKey;
			threadEndKey = threadEndKey.add(range);
		}
		return dividedUpKeys;
	}

	public String getFoundKey()
	{
		return foundKey;
	}

	/**
	 * Runs 5 threads of DES that will brute force a given key space and a cipher plain text pair.
	 *
	 * @return true if the key has been found
	 */
	public boolean run()
	{
		final AtomicBoolean keyFound = new AtomicBoolean(false);
		final AtomicReference<String> correctKey = new AtomicReference<String>();
		final AtomicInteger threadsCompleted = new AtomicInteger();
		final ExecutorService DESThreads = Executors.newFixedThreadPool(threadCount);
		Future threadStatus = null;
		BigInteger[][] threadRanges = divideUpKeys(keySpaceBegin, keySpaceEnd);

		for (int thread = 0; thread < threadCount; thread++)
			threadStatus = DESThreads.submit(new DESBruteForceThread(threadRanges[thread][0], threadRanges[thread][1], cipherText, plainText, parityBits, keyFound, correctKey, threadsCompleted));

		while (!threadStatus.isDone())
		{
		}
		DESThreads.shutdown();

		if (keyFound.get()) foundKey = correctKey.get();

		return keyFound.get();
	}

	/**
	 * Assigns key space variables
	 *
	 * @param keySpaceBegin
	 *            the first key to use in the brute force
	 * @param keySpaceEnd
	 *            the last key to use in the brute force
	 */
	public void setKeyspace(BigInteger keySpaceBegin, BigInteger keySpaceEnd)
	{
		this.keySpaceBegin = keySpaceBegin;
		this.keySpaceEnd = keySpaceEnd;
	}
}
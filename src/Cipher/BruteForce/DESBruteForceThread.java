package Cipher.BruteForce;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.atomic.*;

import Cipher.DES;

/**
 * A thread that will run DES over a given key space.
 */
public class DESBruteForceThread implements Runnable {


	private BigInteger keySpaceBegin;
	private BigInteger keySpaceEnd;
	private int[] cipherText;
	private int[] plainText;
	private int[] parityBits;
	private DES cipher;
	private AtomicBoolean keyFound;
	AtomicReference<String> correctKey;
	AtomicInteger threadsCompleted;

	/**
	 * Constructs a DES force thread for a given key space
	 *
	 * @param keySpaceBegin
	 *            the beginning of the key space, the first key to check
	 * @param keySpaceEnd
	 *            the boundary of the key space, the key before this one is the last to be checked
	 * @param cipherText
	 *            the cipher text trying to be cracked
	 * @param plainText
	 *            the corresponding plain text
	 * @param keyFound
	 *            thread safe boolean that will signal the other threads if key is found
	 * @param correctKey
	 *            the variable to assign the correct key if it is found
	 * @param threadsCompleted
	 *            variable thread will increment once thread has finished executing
	 */
	public DESBruteForceThread(BigInteger keySpaceBegin, BigInteger keySpaceEnd, int[] cipherText, int[] plainText, int[] parityBits, AtomicBoolean keyFound, AtomicReference<String> correctKey, AtomicInteger threadsCompleted)
	{
		this.keySpaceBegin = keySpaceBegin;
		this.keySpaceEnd = keySpaceEnd;
		this.cipherText = cipherText;
		this.plainText = plainText;
		this.parityBits = parityBits;
		this.keyFound = keyFound;
		this.correctKey = correctKey;
		this.threadsCompleted = threadsCompleted;
		this.cipher = new DES(16);
	}

	public Boolean checkKey(String key)
	{
		for (int i = 0; i < 8; i++)
		{
			int num = 0;
			for (int j = 0; j < 7; j++)
			{
				if (key.charAt(i * 7 + j) == '1')
				{
					num++;
				}
			}
			int parity = num % 2;
			if (parity != parityBits[i])
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public void run()
	{
		BigInteger currentKey = keySpaceBegin;
		String currentKeyString;
		int[] cipherTextOfCurrentKey = new int[8];

		while (!keyFound.get() && !keySpaceEnd.equals(currentKey))
		{
			currentKeyString = currentKey.toString(2);
			while (currentKeyString.length() < 56)
			{
				currentKeyString = "0" + currentKeyString;
				if (checkKey(currentKeyString))
				{
					cipher.setKey(currentKeyString);
					cipherTextOfCurrentKey = cipher.encrypt(plainText);
					if (Arrays.equals(cipherTextOfCurrentKey, cipherText))
					{
						keyFound.set(true);
						correctKey.set(currentKeyString);
					}
				}
			}
			currentKey = currentKey.add(BigInteger.ONE);
		}
		threadsCompleted.incrementAndGet();
	}
}

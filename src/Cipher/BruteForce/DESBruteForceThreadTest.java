package Cipher.BruteForce;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.concurrent.atomic.*;

import org.junit.*;

public class DESBruteForceThreadTest {
	private int[] plainText = {68, 69, 83, 105, 110, 103, 10, 88};
	private int[] cipherText = {248, 225, 225, 247, 58, 210, 44, 197};
	BigInteger beginKeySpace = new BigInteger("8387236824869660444");
	BigInteger endKeySpace = new BigInteger("8387236824869660452");
	
	@Test
	public void testBruteForceKey() {
		final AtomicBoolean keyFound = new AtomicBoolean(false);
		final AtomicReference<String> foundKey = new AtomicReference<String>();
		final AtomicInteger threadsCompleted = new AtomicInteger();
		Thread DESThread = new Thread(new DESBruteForceThread(beginKeySpace, 
				endKeySpace, cipherText, plainText, keyFound, foundKey, threadsCompleted));
		DESThread.run();
		assertTrue(keyFound.get());
		assertEquals(1, threadsCompleted.get());
	}
}

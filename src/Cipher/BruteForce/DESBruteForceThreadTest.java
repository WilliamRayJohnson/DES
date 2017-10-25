package Cipher.BruteForce;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.*;

public class DESBruteForceThreadTest {
	private int[] plainText = {68, 69, 83, 105, 110, 103, 10, 88};
	private int[] cipherText = {248, 225, 225, 247, 58, 210, 44, 197};
	BigInteger beginKeySpace = new BigInteger("8387236824869660444");
	BigInteger endKeySpace = new BigInteger("8387236824869660452");
	
	@Test
	public void testBruteForceKey() {
		final AtomicBoolean keyFound = new AtomicBoolean(false);
		Thread DESThread = new Thread(new DESBruteForceThread(beginKeySpace, endKeySpace, cipherText, plainText, keyFound));
		DESThread.run();
		assertTrue(keyFound.get());
	}
}

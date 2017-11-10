package Cipher.BruteForce;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class DESBruteForceNodeTest {
    private int[] plainText = {68, 69, 83, 105, 110, 103, 10, 88};
    private int[] cipherText = {248, 225, 225, 247, 58, 210, 44, 197};
    BigInteger beginKeySpace = new BigInteger("8387236824869660444");
    BigInteger endKeySpace = new BigInteger("8387236824869660452");

	@Test
	public void testDivideUpKeys() {
	    DESBruteForceNode node = new DESBruteForceNode(null, null, 3);
	    BigInteger[][] expectedRanges = {{new BigInteger("0"), new BigInteger("6")},
	                                        {new BigInteger("6"), new BigInteger("11")},
	                                        {new BigInteger("11"), new BigInteger("18")}};
	    BigInteger[][] actualRanges = node.divideUpKeys(new BigInteger("0"), new BigInteger("17"));
		assertEquals(expectedRanges[0][0], actualRanges[0][0]);
		assertEquals(expectedRanges[0][1], actualRanges[0][1]);
		assertEquals(expectedRanges[1][0], actualRanges[1][0]);
        assertEquals(expectedRanges[1][1], actualRanges[1][1]);
        assertEquals(expectedRanges[2][0], actualRanges[2][0]);
        assertEquals(expectedRanges[2][1], actualRanges[2][1]);
	}
	
	@Test
	public void testFindKey(){
	    DESBruteForceNode node = new DESBruteForceNode(cipherText, plainText, 2);
	    node.setKeyspace(beginKeySpace, endKeySpace);
	    String expectedKey = "0111010001100101011100110111010001101001011011100110011100100000";
	    boolean keyFound = node.run();
	    
	    assertEquals(expectedKey, node.getFoundKey()); 
	    assertTrue(keyFound);
	}
	
    @Test
    public void testSpeedOfBruteForce() {
        int[] plainText = { 68, 69, 83, 105, 110, 103, 10, 88 };
        int[] cipherText = { 248, 225, 225, 247, 58, 210, 44, 197 };
        BigInteger beginKeySpace = new BigInteger("0");
        BigInteger endKeySpace = new BigInteger("1000000");
        DESBruteForceNode node = new DESBruteForceNode(cipherText, plainText, 5);
        node.setKeyspace(beginKeySpace, endKeySpace);
        long startTime = System.currentTimeMillis();
        node.run();
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        System.out.printf("It took: %f seconds", time * 0.001);
    }

}

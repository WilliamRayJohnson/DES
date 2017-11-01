package Cipher.BruteForce;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class DESBruteForceNodeTest {

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

}

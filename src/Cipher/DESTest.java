package Cipher;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class DESTest {


	DES cipher;
	byte[] plainTextBlock;
	ByteArrayOutputStream converter;
	String plainText;

	@Before
	public void setup() throws IOException
	{
		this.cipher = new DES(16);
		this.plainText = "GreatDES";
		cipher.setKey("00010010011010010101101111001001101101111011011111111000");
		this.converter = new ByteArrayOutputStream();
		this.converter.write(plainText.getBytes());
		this.plainTextBlock = this.converter.toByteArray();
	}

	@Test
	public void testAssignFirstLastBitsForBlocks()
	{
		ArrayList<Integer> nibbles = new ArrayList<>(Arrays.asList(20, 20, 0, 30, 20, 20, 0, 30));
		ArrayList<Integer> expectedOutput = new ArrayList<>(Arrays.asList(21, 21, 33, 30, 21, 21, 33, 30));
		ArrayList<Integer> actualOutput = cipher.assignFirstLastBitsForBlocks(nibbles);
		assertEquals(expectedOutput.get(0), actualOutput.get(0));
		assertEquals(expectedOutput.get(1), actualOutput.get(1));
		assertEquals(expectedOutput.get(2), actualOutput.get(2));
		assertEquals(expectedOutput.get(3), actualOutput.get(3));
		assertEquals(expectedOutput.get(4), actualOutput.get(4));
		assertEquals(expectedOutput.get(5), actualOutput.get(5));
		assertEquals(expectedOutput.get(6), actualOutput.get(6));
		assertEquals(expectedOutput.get(7), actualOutput.get(7));
	}

	@Test
	public void testCipherTextToInt()
	{
		String cipherText = "130504865423CAAE";
		int[] expectedInts = { 19, 5, 4, 134, 84, 35, 202, 174 };
		byte[] CTbytes = cipherText.getBytes();
		int[] actualInts = new int[8];
		for (int aByte = 0; aByte < 8; aByte++)
		{
			actualInts[aByte] = Integer.parseInt(cipherText.substring(aByte * 2, (aByte * 2) + 2), 16);
		}
		assertEquals(expectedInts[0], actualInts[0]);
		assertEquals(expectedInts[1], actualInts[1]);
		assertEquals(expectedInts[2], actualInts[2]);
		assertEquals(expectedInts[3], actualInts[3]);
		assertEquals(expectedInts[4], actualInts[4]);
		assertEquals(expectedInts[5], actualInts[5]);
		assertEquals(expectedInts[6], actualInts[6]);
		assertEquals(expectedInts[7], actualInts[7]);
	}

	@Test
	public void testDecrypt()
	{
		int[] cipherText = { 5, 180, 10, 15, 84, 19, 232, 133 };
		int[] expectedPlainText = { 239, 205, 171, 137, 103, 69, 35, 1 };
		int[] actualPlainText = cipher.decrypt(cipherText);
		assertEquals(expectedPlainText[0], actualPlainText[0]);
		assertEquals(expectedPlainText[1], actualPlainText[1]);
		assertEquals(expectedPlainText[2], actualPlainText[2]);
		assertEquals(expectedPlainText[3], actualPlainText[3]);
		assertEquals(expectedPlainText[4], actualPlainText[4]);
		assertEquals(expectedPlainText[5], actualPlainText[5]);
		assertEquals(expectedPlainText[6], actualPlainText[6]);
		assertEquals(expectedPlainText[7], actualPlainText[7]);
	}

	@Test
	public void testDESAlgorithm()
	{
		int[] plainText = { 239, 205, 171, 137, 103, 69, 35, 1 };
		int[][] keys = { { 114, 112, 252, 239, 2, 27 }, { 229, 201, 219, 217, 174, 121 }, { 153, 207, 66, 138, 252, 85 }, { 29, 53, 219, 214, 173, 114 }, { 168, 83, 235, 7, 236, 124 }, { 47, 123, 80, 62, 165, 99 }, { 188, 24, 246, 183, 132, 236 },
				{ 251, 59, 193, 58, 138, 247 }, { 129, 231, 237, 235, 219, 224 }, { 79, 70, 186, 71, 243, 177 }, { 134, 211, 222, 211, 95, 33 }, { 233, 103, 148, 245, 113, 117 }, { 65, 186, 250, 209, 197, 151 }, { 58, 231, 242, 183, 67, 95 },
				{ 10, 63, 61, 141, 145, 191 }, { 245, 23, 14, 139, 61, 203 } };
		int[] expectedCipherText = { 5, 180, 10, 15, 84, 19, 232, 133 };
		int[] actualCipherText = cipher.DESAlgorithm(keys, plainText);
		assertEquals(expectedCipherText[0], actualCipherText[0]);
		assertEquals(expectedCipherText[1], actualCipherText[1]);
		assertEquals(expectedCipherText[2], actualCipherText[2]);
		assertEquals(expectedCipherText[3], actualCipherText[3]);
		assertEquals(expectedCipherText[4], actualCipherText[4]);
		assertEquals(expectedCipherText[5], actualCipherText[5]);
		assertEquals(expectedCipherText[6], actualCipherText[6]);
		assertEquals(expectedCipherText[7], actualCipherText[7]);
	}

	@Test
	public void testEncrypt()
	{
		int[] plainText = { 239, 205, 171, 137, 103, 69, 35, 1 };
		int[] expectedCipherText = { 5, 180, 10, 15, 84, 19, 232, 133 };
		int[] actualCipherText = cipher.encrypt(plainText);
		assertEquals(expectedCipherText[0], actualCipherText[0]);
		assertEquals(expectedCipherText[1], actualCipherText[1]);
		assertEquals(expectedCipherText[2], actualCipherText[2]);
		assertEquals(expectedCipherText[3], actualCipherText[3]);
		assertEquals(expectedCipherText[4], actualCipherText[4]);
		assertEquals(expectedCipherText[5], actualCipherText[5]);
		assertEquals(expectedCipherText[6], actualCipherText[6]);
		assertEquals(expectedCipherText[7], actualCipherText[7]);
	}

	@Test
	public void testEncryptDecrypt()
	{
		DES localCipher = new DES(16);
		localCipher.setKey("010101010101010101");
		int[] expectedOutput = new int[8];
		for (int aByte = 0; aByte < 8; aByte++)
		{
			expectedOutput[aByte] = Byte.toUnsignedInt(this.plainTextBlock[aByte]);
		}
		int[] cipherText = localCipher.encrypt(expectedOutput);
		int[] actualOutput = localCipher.decrypt(cipherText);
		assertEquals(expectedOutput[0], actualOutput[0]);
		assertEquals(expectedOutput[1], actualOutput[1]);
		assertEquals(expectedOutput[2], actualOutput[2]);
		assertEquals(expectedOutput[3], actualOutput[3]);
		assertEquals(expectedOutput[4], actualOutput[4]);
		assertEquals(expectedOutput[5], actualOutput[5]);
		assertEquals(expectedOutput[6], actualOutput[6]);
		assertEquals(expectedOutput[7], actualOutput[7]);
	}

	@Test
	public void testExpandBytes()
	{
		int[] rightBlock = { 170, 240, 170, 240 };
		int[] expectedOutput = { 85, 21, 122, 85, 21, 122 };
		int[] actualOutput = cipher.expandBytes(rightBlock);
		assertEquals(expectedOutput[0], actualOutput[0]);
		assertEquals(expectedOutput[1], actualOutput[1]);
		assertEquals(expectedOutput[2], actualOutput[2]);
		assertEquals(expectedOutput[3], actualOutput[3]);
		assertEquals(expectedOutput[4], actualOutput[4]);
		assertEquals(expectedOutput[5], actualOutput[5]);
	}

	@Test
	public void testF()
	{
		int[] rightBlock = { 170, 240, 170, 240 };
		int[] key = { 114, 112, 252, 239, 2, 27 };
		int[] expectedOutput = { 187, 169, 74, 35 };
		int[] actualOutput = cipher.f(rightBlock, key);
		assertEquals(expectedOutput[0], actualOutput[0]);
		assertEquals(expectedOutput[1], actualOutput[1]);
		assertEquals(expectedOutput[2], actualOutput[2]);
		assertEquals(expectedOutput[3], actualOutput[3]);
	}

	@Test
	public void testGenerateKeys()
	{
		int[][] expectedOutput = { { 114, 112, 252, 239, 2, 27 }, { 229, 201, 219, 217, 174, 121 }, { 153, 207, 66, 138, 252, 85 }, { 29, 53, 219, 214, 173, 114 }, { 168, 83, 235, 7, 236, 124 }, { 47, 123, 80, 62, 165, 99 },
				{ 188, 24, 246, 183, 132, 236 }, { 251, 59, 193, 58, 138, 247 }, { 129, 231, 237, 235, 219, 224 }, { 79, 70, 186, 71, 243, 177 }, { 134, 211, 222, 211, 95, 33 }, { 233, 103, 148, 245, 113, 117 }, { 65, 186, 250, 209, 197, 151 },
				{ 58, 231, 242, 183, 67, 95 }, { 10, 63, 61, 141, 145, 191 }, { 245, 23, 14, 139, 61, 203 } };
		int[][] actualOutput = cipher.generateKeys();
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				assertEquals(expectedOutput[i][j], actualOutput[i][j]);
			}
		}
	}

	@Test
	public void testGetNibbles()
	{
		int[] rightBlock = { 170, 240, 170, 240 };
		ArrayList<Integer> expectedOutput = new ArrayList<>(Arrays.asList(20, 20, 0, 30, 20, 20, 0, 30));
		ArrayList<Integer> actualOutput = cipher.getNibbles(rightBlock);
		assertEquals(expectedOutput.get(0), actualOutput.get(0));
		assertEquals(expectedOutput.get(1), actualOutput.get(1));
		assertEquals(expectedOutput.get(2), actualOutput.get(2));
		assertEquals(expectedOutput.get(3), actualOutput.get(3));
		assertEquals(expectedOutput.get(4), actualOutput.get(4));
		assertEquals(expectedOutput.get(5), actualOutput.get(5));
		assertEquals(expectedOutput.get(6), actualOutput.get(6));
		assertEquals(expectedOutput.get(7), actualOutput.get(7));
	}

	@Test
	public void testInvIP()
	{
		String rightAndLeft = "0000101001001100110110011001010101000011010000100011001000110100";
		String expectedOutput = "1000010111101000000100110101010000001111000010101011010000000101";
		String actualOutput = cipher.InvIP(rightAndLeft);
		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	public void testPassThroughIPTableDESExample()
	{
		int[] plainText = { 239, 205, 171, 137, 103, 69, 35, 1 };
		int[] expectedOutput = { 170, 240, 170, 240, 255, 204, 0, 204 };
		int[] actaulOutput = cipher.passThroughIPTable(plainText);
		assertEquals(expectedOutput[0], actaulOutput[0]);
		assertEquals(expectedOutput[1], actaulOutput[1]);
		assertEquals(expectedOutput[2], actaulOutput[2]);
		assertEquals(expectedOutput[3], actaulOutput[3]);
		assertEquals(expectedOutput[4], actaulOutput[4]);
		assertEquals(expectedOutput[5], actaulOutput[5]);
		assertEquals(expectedOutput[6], actaulOutput[6]);
		assertEquals(expectedOutput[7], actaulOutput[7]);
	}

	@Test
	public void testPTable()
	{
		String pTableInput = "01011100100000101011010110010111";
		String expectedOutput = "00100011010010101010100110111011";
		String actualOutput = new String(cipher.pTable(pTableInput.toCharArray()));
		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	public void testSbox()
	{
		int[] xoredBytes = { 39, 101, 134, 186, 23, 97 };
		String expectedOutput = "01011100100000101011010110010111";
		String actualOutput = new String(cipher.passThroughSBox(xoredBytes));
		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	public void testXor()
	{
		int[] expandedBytes = { 85, 21, 122, 85, 21, 122 };
		int[] key = { 114, 112, 252, 239, 2, 27 };
		int[] expectedOutput = { 39, 101, 134, 186, 23, 97 };
		int[] actualOutput = cipher.xor(expandedBytes, key);
		assertEquals(expectedOutput[0], actualOutput[0]);
		assertEquals(expectedOutput[1], actualOutput[1]);
		assertEquals(expectedOutput[2], actualOutput[2]);
		assertEquals(expectedOutput[3], actualOutput[3]);
		assertEquals(expectedOutput[4], actualOutput[4]);
		assertEquals(expectedOutput[5], actualOutput[5]);
	}
}

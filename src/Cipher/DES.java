package Cipher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.*;

public class DES {


	private final Integer[][][] sBoxes = {
			{ { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 }, { 1, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 }, { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 }, { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } },
			{ { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 }, { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 }, { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 }, { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } },
			{ { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 }, { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 }, { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 }, { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } },
			{ { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 }, { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 }, { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 }, { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } },
			{ { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 }, { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 }, { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 }, { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } },
			{ { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 }, { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 }, { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 }, { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } },
			{ { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 }, { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 }, { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 }, { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } },
			{ { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 }, { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 }, { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 }, { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } } };

	private int rounds;

	private String key;

	/**
	 *
	 * @param rounds
	 */
	public DES(int rounds)
	{
		this.rounds = rounds;
	}

	/**
	 *
	 * @param rounds
	 * @param keyFileName
	 */
	public DES(int rounds, String keyFileName)
	{
		try
		{
			BufferedReader key = new BufferedReader(new FileReader(keyFileName));
			this.key = new BigInteger(key.readLine().getBytes()).toString(2);
			while (this.key.length() < 56)
				this.key = "0" + this.key;
			key.close();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		this.rounds = rounds;
	}

	/**
	 *
	 * @param shifts
	 *            (Number of bits to shift left)
	 * @param round
	 *            (Base round to shift)
	 * @return (Size 16 shifted string)
	 */
	public static String leftShift(int shifts, String round)
	{
		String temp = round.substring(0, shifts);
		return round.substring(shifts) + temp;
	}

	/**
	 *
	 * @param roundL
	 *            (String size 16)
	 * @param roundR
	 *            (String size 16)
	 * @return temp (String size 32)
	 */
	public static String pc2(String roundL, String roundR)
	{
		String pc2 = roundL + roundR;
		// String temp = "";
		int[] table = { 13, 16, 10, 23, 0, 4, 2, 27, 14, 5, 20, 9, 22, 18, 11, 3, 25, 7, 15, 6, 26, 19, 12, 1, 40, 51, 30, 36, 46, 54, 29, 39, 50, 44, 32, 47, 43, 48, 38, 55, 33, 52, 45, 41, 49, 35, 28, 31 };
		char[] tmp = new char[56];
		for (int x = 0; x < table.length; x++)
			tmp[x] = pc2.charAt(table[x]);
		return new String(tmp);
	}

	/**
	 * Assign the values of the first and last bit of each 6 bit block of the expansion table
	 *
	 * @param nibbles
	 *            An array of 8 integers
	 * @return
	 */
	public ArrayList<Integer> assignFirstLastBitsForBlocks(ArrayList<Integer> nibbles)
	{
		int lastBitMask = 2;
		int firstBitMask = 16;
		int currentNibble;
		int nextNibble;

		for (int nibble = 0; nibble <= 7; nibble++)
		{
			currentNibble = nibbles.get(nibble);
			nextNibble = nibbles.get((nibble + 1) % 8);

			nibbles.set(nibble, currentNibble | ((nextNibble & lastBitMask) << 4));
			nibbles.set((nibble + 1) % 8, nextNibble | ((currentNibble & firstBitMask) >> 4));
		}
		return nibbles;
	}

	/**
	 * Decrypts a single 64-bit block using DES
	 *
	 * @param cipherText
	 * @return the An array of integers representing the 8 bytes of plain text
	 */
	public int[] decrypt(int[] cipherText)
	{
		int[][] keys = generateKeys();
		Collections.reverse(Arrays.asList(keys));
		int[] plainText = DESAlgorithm(keys, cipherText);

		return plainText;
	}

	/**
	 * The DES algorithm
	 *
	 * @param keys
	 *            the keys to be used
	 * @param currentBlock
	 *            the block of data to run through the algorithm
	 * @return the result of the block being run through n number of rounds
	 */
	public int[] DESAlgorithm(int[][] keys, int[] currentBlock)
	{
		int[] fOutput;
		int[] xoredLeftHalf;
		int[] thirtyTwoBitSwapTemp;

		int[] passedIp = passThroughIPTable(currentBlock);
		int[] leftHalf = Arrays.copyOfRange(passedIp, 4, 8);
		int[] rightHalf = Arrays.copyOfRange(passedIp, 0, 4);

		for (int round = 0; round < this.rounds; round++)
		{
			fOutput = f(rightHalf, keys[round]);
			xoredLeftHalf = xor(leftHalf, fOutput);
			leftHalf = rightHalf;
			rightHalf = xoredLeftHalf;
		}
		thirtyTwoBitSwapTemp = rightHalf;
		rightHalf = leftHalf;
		leftHalf = thirtyTwoBitSwapTemp;
		String rightHalfString = "";
		String leftHalfString = "";
		for (int aByte = 0; aByte < leftHalf.length; aByte++)
		{
			String leftNumAsByte = Integer.toBinaryString(leftHalf[aByte]);
			String rightNumAsByte = Integer.toBinaryString(rightHalf[aByte]);
			int leftZerosToAdd = 8 - leftNumAsByte.length();
			int rightZerosToAdd = 8 - rightNumAsByte.length();
			for (int j = 0; j < leftZerosToAdd; j++)
				leftNumAsByte = "0" + leftNumAsByte;
			for (int j = 0; j < rightZerosToAdd; j++)
				rightNumAsByte = "0" + rightNumAsByte;
			leftHalfString = leftNumAsByte + leftHalfString;
			rightHalfString = rightNumAsByte + rightHalfString;
		}
		String bitsToInvert = leftHalfString + rightHalfString;
		String processedTextString = InvIP(bitsToInvert);
		int[] processedText = new int[8];

		for (int k = 0; k < processedText.length; k++)
		{
			processedText[processedText.length - 1 - k] = Integer.parseInt(processedTextString.substring(k * 8, k * 8 + 8), 2);
		}

		return processedText;
	}

	/**
	 * Encrypts a single 64-bit block using DES
	 *
	 * @param plainText
	 *            An array of integers representing the 8 bytes of plain text
	 * @return An array of integers representing the 8 bytes of cipher text
	 */
	public int[] encrypt(int[] plainText)
	{
		int[][] keys = generateKeys();
		int[] cipherText = DESAlgorithm(keys, plainText);

		return cipherText;
	}

	/**
	 * Expand a 32 bit number to a 48 bit number
	 *
	 * @param rightBlock
	 *            An array of 4 integers representing the 32 bit right block
	 * @return the expanded integer array of 6 integers
	 */
	public int[] expandBytes(int[] rightBlock)
	{
		ArrayList<Integer> nibbles;
		ArrayList<Integer> blocks;
		int[] expandedBytes = { 0, 0, 0, 0, 0, 0 };
		int expandedLowerBytes = 0;
		int expandedUpperBytes = 0;

		nibbles = getNibbles(rightBlock);
		blocks = assignFirstLastBitsForBlocks(nibbles);

		for (int block = 0; block <= 3; block++)
		{
			expandedLowerBytes = expandedLowerBytes | blocks.get(block) << (6 * block);
			expandedUpperBytes = expandedUpperBytes | blocks.get(block + 4) << (6 * block);
		}
		for (int aByte = 0; aByte <= 2; aByte++)
		{
			expandedBytes[aByte] = (expandedBytes[aByte] | (expandedLowerBytes & (255 << (aByte * 8)))) >> (aByte * 8);
			expandedBytes[aByte + 3] = (expandedBytes[aByte + 3] | (expandedUpperBytes & (255 << (aByte * 8)))) >> (aByte * 8);
		}

		return expandedBytes;
	}

	public int[] f(int[] rightHalf, int[] key)
	{
		String afterPTable = pTable(passThroughSBox(xor(expandBytes(rightHalf), key)));
		int[] afterPTableInt = new int[4];
		for (int aByte = 0; aByte < 4; aByte++)
		{
			afterPTableInt[3 - aByte] = Integer.parseInt(afterPTable.substring(aByte * 8, aByte * 8 + 8), 2);
		}
		return afterPTableInt;
	}

	/**
	 * Generates keys given what is in the key field
	 *
	 * @return the keys to be used in the 16 rounds of DES
	 */
	public int[][] generateKeys()
	{
		// PC-1
		char[][] tableL = new char[4][7];
		char[][] tableR = new char[4][7];
		int i = 0;
		tableL[1][0] = this.key.charAt(i++);
		tableL[2][1] = this.key.charAt(i++);
		tableL[3][2] = this.key.charAt(i++);
		tableR[3][6] = this.key.charAt(i++);
		tableR[3][2] = this.key.charAt(i++);
		tableR[2][1] = this.key.charAt(i++);
		tableR[1][0] = this.key.charAt(i++);
		tableL[0][6] = this.key.charAt(i++);
		tableL[2][0] = this.key.charAt(i++);
		tableL[3][1] = this.key.charAt(i++);
		tableR[3][5] = this.key.charAt(i++);
		tableR[3][1] = this.key.charAt(i++);
		tableR[2][0] = this.key.charAt(i++);
		tableR[0][6] = this.key.charAt(i++);
		tableL[0][5] = this.key.charAt(i++);
		tableL[1][6] = this.key.charAt(i++);
		tableL[3][0] = this.key.charAt(i++);
		tableR[3][4] = this.key.charAt(i++);
		tableR[3][0] = this.key.charAt(i++);
		tableR[1][6] = this.key.charAt(i++);
		tableR[0][5] = this.key.charAt(i++);
		tableL[0][4] = this.key.charAt(i++);
		tableL[1][5] = this.key.charAt(i++);
		tableL[2][6] = this.key.charAt(i++);
		tableR[3][3] = this.key.charAt(i++);
		tableR[2][6] = this.key.charAt(i++);
		tableR[1][5] = this.key.charAt(i++);
		tableR[0][4] = this.key.charAt(i++);
		tableL[0][3] = this.key.charAt(i++);
		tableL[1][4] = this.key.charAt(i++);
		tableL[2][5] = this.key.charAt(i++);
		tableL[3][6] = this.key.charAt(i++);
		tableR[2][5] = this.key.charAt(i++);
		tableR[1][4] = this.key.charAt(i++);
		tableR[0][3] = this.key.charAt(i++);
		tableL[0][2] = this.key.charAt(i++);
		tableL[1][3] = this.key.charAt(i++);
		tableL[2][4] = this.key.charAt(i++);
		tableL[3][5] = this.key.charAt(i++);
		tableR[2][4] = this.key.charAt(i++);
		tableR[1][3] = this.key.charAt(i++);
		tableR[0][2] = this.key.charAt(i++);
		tableL[0][1] = this.key.charAt(i++);
		tableL[1][2] = this.key.charAt(i++);
		tableL[2][3] = this.key.charAt(i++);
		tableL[3][4] = this.key.charAt(i++);
		tableR[2][3] = this.key.charAt(i++);
		tableR[1][2] = this.key.charAt(i++);
		tableR[0][1] = this.key.charAt(i++);
		tableL[0][0] = this.key.charAt(i++);
		tableL[1][1] = this.key.charAt(i++);
		tableL[2][2] = this.key.charAt(i++);
		tableL[3][3] = this.key.charAt(i++);
		tableR[2][2] = this.key.charAt(i++);
		tableR[1][1] = this.key.charAt(i++);
		tableR[0][0] = this.key.charAt(i++);

		// Building Left and Right strings
		i = 0;
		char[] res1 = new char[28];
		char[] res2 = new char[28];
		for (int j = 0; j < 4; j++)
		{
			for (int k = 0; k < 7; k++)
			{
				res1[j * 7 + k] = tableL[j][k];
				res2[j * 7 + k] = tableR[j][k];
				// result += tableL[j][k];
				// result2 += tableR[j][k];
			}
		}

		// PC-2 leftShifting for 16 rounds
		String result = new String(res1);
		String result2 = new String(res2);
		String[] leftShifts = new String[16];
		i = 0;
		leftShifts[i++] = leftShift(1, result);
		leftShifts[i++] = leftShift(1, leftShifts[i - 2]);
		leftShifts[i++] = leftShift(2, leftShifts[i - 2]);
		leftShifts[i++] = leftShift(2, leftShifts[i - 2]);
		leftShifts[i++] = leftShift(2, leftShifts[i - 2]);
		leftShifts[i++] = leftShift(2, leftShifts[i - 2]);
		leftShifts[i++] = leftShift(2, leftShifts[i - 2]);
		leftShifts[i++] = leftShift(2, leftShifts[i - 2]);
		leftShifts[i++] = leftShift(1, leftShifts[i - 2]);
		leftShifts[i++] = leftShift(2, leftShifts[i - 2]);
		leftShifts[i++] = leftShift(2, leftShifts[i - 2]);
		leftShifts[i++] = leftShift(2, leftShifts[i - 2]);
		leftShifts[i++] = leftShift(2, leftShifts[i - 2]);
		leftShifts[i++] = leftShift(2, leftShifts[i - 2]);
		leftShifts[i++] = leftShift(2, leftShifts[i - 2]);
		leftShifts[i++] = leftShift(1, leftShifts[i - 2]);

		String[] rightShifts = new String[16];
		i = 0;
		rightShifts[i++] = leftShift(1, result2);
		rightShifts[i++] = leftShift(1, rightShifts[i - 2]);
		rightShifts[i++] = leftShift(2, rightShifts[i - 2]);
		rightShifts[i++] = leftShift(2, rightShifts[i - 2]);
		rightShifts[i++] = leftShift(2, rightShifts[i - 2]);
		rightShifts[i++] = leftShift(2, rightShifts[i - 2]);
		rightShifts[i++] = leftShift(2, rightShifts[i - 2]);
		rightShifts[i++] = leftShift(2, rightShifts[i - 2]);
		rightShifts[i++] = leftShift(1, rightShifts[i - 2]);
		rightShifts[i++] = leftShift(2, rightShifts[i - 2]);
		rightShifts[i++] = leftShift(2, rightShifts[i - 2]);
		rightShifts[i++] = leftShift(2, rightShifts[i - 2]);
		rightShifts[i++] = leftShift(2, rightShifts[i - 2]);
		rightShifts[i++] = leftShift(2, rightShifts[i - 2]);
		rightShifts[i++] = leftShift(2, rightShifts[i - 2]);
		rightShifts[i++] = leftShift(1, rightShifts[i - 2]);

		String[] pc2String = new String[16];
		int[][] keys = new int[16][6];
		for (int j = 0; j < 16; j++)
		{
			pc2String[j] = pc2(leftShifts[j], rightShifts[j]);
			for (int k = 0; k < 6; k++)
			{
				keys[j][5 - k] = Integer.parseInt(pc2String[j].substring(k * 8, k * 8 + 8), 2);
			}
		}

		return keys;
	}

	/**
	 * Splits up an array of bytes into their upper and lower nibbles
	 *
	 * @param rightBlock
	 *            An array of 4 integers representing the 32 bit right block
	 * @return An array of 8 integers representing the upper and lower nibbles of the input
	 */
	public ArrayList<Integer> getNibbles(int[] rightBlock)
	{
		ArrayList<Integer> nibbles = new ArrayList<Integer>();

		for (int nibble = 0; nibble <= 3; nibble++)
		{
			nibbles.add(((rightBlock[nibble] & 15) << 1));
			nibbles.add(((rightBlock[nibble] & 240) >> 3));
		}

		return nibbles;
	}

	/**
	 * Pass 64-bit value through the inverse initial permutation table
	 *
	 * @param binary
	 *            The binary represented as a string
	 * @return the value passed through the inverse IP table
	 */
	public String InvIP(String binary)
	{
		int[] invIPTable = new int[] { 40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18,
				58, 26, 33, 1, 41, 9, 49, 17, 57, 25 };

		char[] chb = new char[64];

		for (int i = 0; i < 64; i++)
		{
			chb[i] = binary.charAt(64 - invIPTable[i]);
		}

		return new String(chb);
	}

	/**
	 * Pass 64-bit plain text through the initial permutation table
	 *
	 * @param plainText
	 *            An array of 8 integers representing the plain text
	 * @return The result of IP table as an array of 8 integers
	 */
	public int[] passThroughIPTable(int[] text)
	{
		int shiftAmount;
		int mask;
		int maskedByte;
		int[] IPPassed = { 0, 0, 0, 0, 0, 0, 0, 0 };
		int[] byteOrder = { 4, 0, 5, 1, 6, 2, 7, 3 };

		for (int bite = 0; bite <= 7; bite++)
		{
			shiftAmount = 7 - bite;
			mask = 1;
			for (int bit = 0; bit <= 7; bit++)
			{
				maskedByte = (text[bite] & mask);
				if (shiftAmount >= 0)
					maskedByte = (maskedByte << shiftAmount);
				else if (shiftAmount < 0) maskedByte = (maskedByte >> Math.abs(shiftAmount));
				IPPassed[byteOrder[bit]] = (IPPassed[byteOrder[bit]] | maskedByte);
				shiftAmount--;
				mask = (mask << 1);
			}
		}
		return IPPassed;
	}

	/**
	 * This method converts the array of integers (which is the 48 bit sequence preceding the s-box into a String of 32 bits.
	 *
	 * @param arr
	 *            The array of integers from the previous XOR
	 * @return String of 32 bits
	 */
	public char[] passThroughSBox(int[] arr)
	{
		char[] bef = new char[48];
		for (int i = 0; i < arr.length; i++)
		{
			char[] numAsByte = Integer.toBinaryString(arr[i]).toCharArray();
			int numBits = numAsByte.length - 1;
			for (int j = 7; j > 8 - numAsByte.length; j--)
			{
				bef[i * 8 + j] = numAsByte[numBits];
				numBits--;
			}
			for (int j = 0; j < 8 - numAsByte.length; j++)
			{
				bef[i * 8 + j] = '0';
			}

		}
		char[] aft = new char[32];
		for (int i = 0; i < 8; i++)
		{
			char[] seq = new char[6];
			for (int j = 0; j < 6; j++)
			{
				seq[j] = bef[i * 6 + j];
			}
			int r = 0;
			int c = 0;
			if (seq[5] == '1')
			{
				r += 1;
			}
			if (seq[0] == '1')
			{
				r += 2;
			}
			if (seq[4] == '1')
			{
				c += 1;
			}
			if (seq[3] == '1')
			{
				c += 2;
			}
			if (seq[2] == '1')
			{
				c += 4;
			}
			if (seq[1] == '1')
			{
				c += 8;
			}
			int newInt = convert(i, r, c);
			if (newInt > 7)
			{
				aft[i * 4 + 0] = '1';
				newInt -= 8;
			}
			else
			{
				aft[i * 4 + 0] = '0';
			}
			if (newInt > 3)
			{
				aft[i * 4 + 1] = '1';
				newInt -= 4;
			}
			else
			{
				aft[i * 4 + 1] = '0';
			}
			if (newInt > 1)
			{
				aft[i * 4 + 2] = '1';
				newInt -= 2;
			}
			else
			{
				aft[i * 4 + 2] = '0';
			}
			if (newInt == 1)
			{
				aft[i * 4 + 3] = '1';
			}
			else
			{
				aft[i * 4 + 3] = '0';
			}
		}

		return aft;
	}

	/**
	 * Converts the S box output to a certain permutation
	 *
	 * @param sBox
	 *            String of size 32
	 * @return finalString String of size 32
	 */
	public String pTable(char[] sBox)
	{
		char[] fin = new char[32];
		fin[0] = sBox[15];
		fin[1] = sBox[6];
		fin[2] = sBox[19];
		fin[3] = sBox[20];
		fin[4] = sBox[28];
		fin[5] = sBox[11];
		fin[6] = sBox[27];
		fin[7] = sBox[16];
		fin[8] = sBox[0];
		fin[9] = sBox[14];
		fin[10] = sBox[22];
		fin[11] = sBox[25];
		fin[12] = sBox[4];
		fin[13] = sBox[17];
		fin[14] = sBox[30];
		fin[15] = sBox[9];
		fin[16] = sBox[1];
		fin[17] = sBox[7];
		fin[18] = sBox[23];
		fin[19] = sBox[13];
		fin[20] = sBox[31];
		fin[21] = sBox[26];
		fin[22] = sBox[2];
		fin[23] = sBox[8];
		fin[24] = sBox[18];
		fin[25] = sBox[12];
		fin[26] = sBox[29];
		fin[27] = sBox[5];
		fin[28] = sBox[21];
		fin[29] = sBox[10];
		fin[30] = sBox[3];
		fin[31] = sBox[24];
		return new String(fin);
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	/**
	 * Performs the xor operation on an array of integers of n length. Each operand must be of the same length
	 *
	 * @param leftOperand
	 * @param rightOperand
	 * @return An array of xored integers
	 */
	public int[] xor(int[] leftOperand, int[] rightOperand)
	{
		int[] xoredValue = new int[leftOperand.length];
		for (int aByte = 0; aByte < leftOperand.length; aByte++)
			xoredValue[aByte] = leftOperand[aByte] ^ rightOperand[aByte];
		return xoredValue;
	}

	private int convert(int box, int row, int col)
	{
		return sBoxes[box][row][col];
	}
}
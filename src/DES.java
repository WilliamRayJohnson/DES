import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.math.BigInteger;

public class DES
{
    // This is the array of s-boxes
    private Integer[][][] s =
    {
            {
                    { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
                    { 1, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
                    { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
                    { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } },
            {
                    { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
                    { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
                    { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
                    { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } },
            {
                    { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
                    { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
                    { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
                    { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } },
            {
                    { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
                    { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
                    { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
                    { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } },
            {
                    { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
                    { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
                    { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
                    { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } },
            {
                    { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
                    { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
                    { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
                    { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } },
            {
                    { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
                    { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
                    { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
                    { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } },
            {
                    { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
                    { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
                    { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
                    { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } } };
    
    String key;
    
    public DES(String keyFileName)
    {
        try
        {
            BufferedReader key = new BufferedReader(new FileReader(keyFileName));
            this.key = new BigInteger(key.readLine().getBytes()).toString(2);
            key.close();
        } catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
    
    public void encrypt(String plainTextFileName)
    {
        String binary = "";

        // If leading 0 is cut off, add it back on
        while (binary.length() < 64)
            binary = "0" + binary;

        // PC-1
        int[][] tableL = new int[4][7];
        int[][] tableR = new int[4][7];
        int i = 0;
        tableL[1][0] = Character.getNumericValue(binary.charAt(i++));
        tableL[2][1] = Character.getNumericValue(binary.charAt(i++));
        tableL[3][2] = Character.getNumericValue(binary.charAt(i++));
        tableR[3][6] = Character.getNumericValue(binary.charAt(i++));
        tableR[3][2] = Character.getNumericValue(binary.charAt(i++));
        tableR[2][1] = Character.getNumericValue(binary.charAt(i++));
        tableR[1][0] = Character.getNumericValue(binary.charAt(i++));
        i++; // 8
        tableL[0][6] = Character.getNumericValue(binary.charAt(i++));
        tableL[2][0] = Character.getNumericValue(binary.charAt(i++));
        tableL[3][1] = Character.getNumericValue(binary.charAt(i++));
        tableR[3][5] = Character.getNumericValue(binary.charAt(i++));
        tableR[3][1] = Character.getNumericValue(binary.charAt(i++));
        tableR[2][0] = Character.getNumericValue(binary.charAt(i++));
        tableR[0][6] = Character.getNumericValue(binary.charAt(i++));
        i++;// 16
        tableL[0][5] = Character.getNumericValue(binary.charAt(i++));
        tableL[1][6] = Character.getNumericValue(binary.charAt(i++));
        tableL[3][0] = Character.getNumericValue(binary.charAt(i++));
        tableR[3][4] = Character.getNumericValue(binary.charAt(i++));
        tableR[3][0] = Character.getNumericValue(binary.charAt(i++));
        tableR[1][6] = Character.getNumericValue(binary.charAt(i++));
        tableR[0][5] = Character.getNumericValue(binary.charAt(i++));
        i++;// 24
        tableL[0][4] = Character.getNumericValue(binary.charAt(i++));
        tableL[1][5] = Character.getNumericValue(binary.charAt(i++));
        tableL[2][6] = Character.getNumericValue(binary.charAt(i++));
        tableR[3][3] = Character.getNumericValue(binary.charAt(i++));
        tableR[2][6] = Character.getNumericValue(binary.charAt(i++));
        tableR[1][5] = Character.getNumericValue(binary.charAt(i++));
        tableR[0][4] = Character.getNumericValue(binary.charAt(i++));
        i++;// 32
        tableL[0][3] = Character.getNumericValue(binary.charAt(i++));
        tableL[1][4] = Character.getNumericValue(binary.charAt(i++));
        tableL[2][5] = Character.getNumericValue(binary.charAt(i++));
        tableL[3][6] = Character.getNumericValue(binary.charAt(i++));
        tableR[2][5] = Character.getNumericValue(binary.charAt(i++));
        tableR[1][4] = Character.getNumericValue(binary.charAt(i++));
        tableR[0][3] = Character.getNumericValue(binary.charAt(i++));
        i++;// 40
        tableL[0][2] = Character.getNumericValue(binary.charAt(i++));
        tableL[1][3] = Character.getNumericValue(binary.charAt(i++));
        tableL[2][4] = Character.getNumericValue(binary.charAt(i++));
        tableL[3][5] = Character.getNumericValue(binary.charAt(i++));
        tableR[2][4] = Character.getNumericValue(binary.charAt(i++));
        tableR[1][3] = Character.getNumericValue(binary.charAt(i++));
        tableR[0][2] = Character.getNumericValue(binary.charAt(i++));
        i++;// 48
        tableL[0][1] = Character.getNumericValue(binary.charAt(i++));
        tableL[1][2] = Character.getNumericValue(binary.charAt(i++));
        tableL[2][3] = Character.getNumericValue(binary.charAt(i++));
        tableL[3][4] = Character.getNumericValue(binary.charAt(i++));
        tableR[2][3] = Character.getNumericValue(binary.charAt(i++));
        tableR[1][2] = Character.getNumericValue(binary.charAt(i++));
        tableR[0][1] = Character.getNumericValue(binary.charAt(i++));
        i++;// 56
        tableL[0][0] = Character.getNumericValue(binary.charAt(i++));
        tableL[1][1] = Character.getNumericValue(binary.charAt(i++));
        tableL[2][2] = Character.getNumericValue(binary.charAt(i++));
        tableL[3][3] = Character.getNumericValue(binary.charAt(i++));
        tableR[2][2] = Character.getNumericValue(binary.charAt(i++));
        tableR[1][1] = Character.getNumericValue(binary.charAt(i++));
        tableR[0][0] = Character.getNumericValue(binary.charAt(i++));

        // Building Left and Right strings
        i = 0;
        String result = "";
        String result2 = "";
        for (int j = 0; j < 4; j++)
        {
            for (int k = 0; k < 7; k++)
            {
                result += tableL[j][k];
                result2 += tableR[j][k];
            }
        }

        // PC-2 leftShifting for 16 rounds
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
                keys[j][k] = Integer.parseInt(pc2String[j].substring(k * 8, k * 8 + 8), 2);
            }
        }
        
        try
        {
            // get plain text
            FileInputStream plainText = new FileInputStream(new File(plainTextFileName));
            byte[] plainTextBytes = new byte[plainText.available()];
            plainText.read(plainTextBytes);
            
            int[] currentBlock = new int[8];
            for(int block = 0; block < plainTextBytes.length; block += 8)
            {
                for(int aByte = 0; aByte < 8; aByte++)
                {
                    currentBlock[aByte] = Byte.toUnsignedInt(plainTextBytes[block + aByte]);
                }
                
                int[] passedIp = passThroughIPTable(currentBlock);
                int[] leftHalf = Arrays.copyOfRange(passedIp, 4, 7);
                int[] rightHalf = Arrays.copyOfRange(passedIp, 0, 3);
                
                for(int round = 0; round < 16; round++)
                {
                    int[] fOutput = f(rightHalf, keys[round]);
                }
                
                
            }
        } catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
    
    public int[] f(int[] rightHalf, int[] key)
    {
        int[] expandedHalf = expandBytes(rightHalf);
        int[] xoredKeyAndHalf = xor(expandedHalf, key);
        String passedSBox = passThroughSBox(xoredKeyAndHalf);
        String afterPTable = pTable(passedSBox);
        int[] afterPTableInt = new int[4];
        for(int aByte = 0; aByte < 4; aByte++)
        {
            afterPTableInt[aByte] = Integer.parseInt(afterPTable.substring(aByte * 8, aByte * 8 + 8), 2);
        }
        return afterPTableInt;
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
     * @param roundL (String size 16)
     * @param roundR (String size 16)
     * @return temp (String size 32)
     */
    public static String pc2(String roundL, String roundR)
    {
        String temp = "";
        temp += roundL.charAt(13);
        temp += roundL.charAt(16);
        temp += roundL.charAt(10);
        temp += roundL.charAt(23);
        temp += roundL.charAt(0);
        temp += roundL.charAt(4);
        temp += roundL.charAt(2);
        temp += roundL.charAt(27);
        temp += roundL.charAt(14);
        temp += roundL.charAt(5);
        temp += roundL.charAt(20);
        temp += roundL.charAt(9);
        temp += roundL.charAt(22);
        temp += roundL.charAt(18);
        temp += roundL.charAt(11);
        temp += roundL.charAt(3);
        temp += roundL.charAt(25);
        temp += roundL.charAt(7);
        temp += roundL.charAt(15);
        temp += roundL.charAt(6);
        temp += roundL.charAt(26);
        temp += roundL.charAt(19);
        temp += roundL.charAt(12);
        temp += roundL.charAt(1);
        temp += roundR.charAt(12);
        temp += roundR.charAt(23);
        temp += roundR.charAt(3);
        temp += roundR.charAt(8);
        temp += roundR.charAt(18);
        temp += roundR.charAt(26);
        temp += roundR.charAt(1);
        temp += roundR.charAt(22);
        temp += roundR.charAt(24);
        temp += roundR.charAt(16);
        temp += roundR.charAt(4);
        temp += roundR.charAt(19);
        temp += roundR.charAt(15);
        temp += roundR.charAt(20);
        temp += roundR.charAt(10);
        temp += roundR.charAt(27);
        temp += roundR.charAt(5);
        temp += roundR.charAt(24);
        temp += roundR.charAt(17);
        temp += roundR.charAt(13);
        temp += roundR.charAt(21);
        temp += roundR.charAt(7);
        temp += roundR.charAt(0);
        temp += roundR.charAt(3);
        return temp;
    }

    /**
     *
     * @param sBox
     *            String of size 32
     * @return finalString String of size 32
     */
    public String pTable(String sBox)
    {
        char[] sBoxArray = sBox.toCharArray();
        String finalString = "";
        finalString += sBoxArray[15];
        finalString += sBoxArray[6];
        finalString += sBoxArray[19];
        finalString += sBoxArray[20];
        finalString += sBoxArray[28];
        finalString += sBoxArray[11];
        finalString += sBoxArray[27];
        finalString += sBoxArray[16];
        finalString += sBoxArray[0];
        finalString += sBoxArray[14];
        finalString += sBoxArray[22];
        finalString += sBoxArray[25];
        finalString += sBoxArray[4];
        finalString += sBoxArray[17];
        finalString += sBoxArray[30];
        finalString += sBoxArray[9];
        finalString += sBoxArray[1];
        finalString += sBoxArray[7];
        finalString += sBoxArray[23];
        finalString += sBoxArray[13];
        finalString += sBoxArray[31];
        finalString += sBoxArray[26];
        finalString += sBoxArray[2];
        finalString += sBoxArray[8];
        finalString += sBoxArray[18];
        finalString += sBoxArray[12];
        finalString += sBoxArray[29];
        finalString += sBoxArray[5];
        finalString += sBoxArray[21];
        finalString += sBoxArray[10];
        finalString += sBoxArray[3];
        finalString += sBoxArray[24];
        return finalString;
    }
    
    public static String InvIP(String binary)
    {
        int[] invIPTable = new int[]
        { 40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13,
                53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58,
                26, 33, 1, 41, 9, 49, 17, 57, 25 };

        String builder = "";

        for (int i = 0; i < invIPTable.length; i++)
            builder += binary.charAt(64 - invIPTable[i]);

        String binaryResult = new StringBuilder(builder).reverse().toString();

        return binaryResult;
    }
    
    private int convert(int box, int row, int col)
    {
        return s[box][row][col];
    }

    /**
     * This method converts the array of integers (which is the 48 bit sequence
     * preceding the s-box into a String of 32 bits.
     *
     * @param arr
     *            The array of integers from the previous XOR
     * @return String of 32 bits
     */
    public String passThroughSBox(int[] arr)
    {
        String before = "";
        for (int i = 0; i < arr.length; i++)
        {
            String numAsByte = Integer.toBinaryString(arr[i]);
            int moreZeros = 8 - numAsByte.length();
            for (int j = 0; j < moreZeros; j++)
            {
                numAsByte = "0" + numAsByte;
            }
            before = before + numAsByte;
        }
        String after = "";
        for (int i = 0; i < 8; i++)
        {
            String seq = before.substring(i * 6, (i * 6) + 6);
            String rowAsBinary = String.valueOf(seq.charAt(0)) + String.valueOf(seq.charAt(5));
            String colAsBinary = String.valueOf(seq.charAt(1)) + String.valueOf(seq.charAt(2))
                    + String.valueOf(seq.charAt(3)) + String.valueOf(seq.charAt(4));
            int newInt = convert(i, Integer.parseInt(rowAsBinary, 2), Integer.parseInt(colAsBinary, 2));
            String newSeq = Integer.toBinaryString(newInt);
            int moreZeros = 4 - newSeq.length();
            for (int j = 0; j < moreZeros; j++)
            {
                newSeq = "0" + newSeq;
            }
            System.out.println(seq + " --> " + newSeq);
            after = after + newSeq;
        }
        
        
        return after;
    }
    
    /**
     * Pass 64-bit plain text through the initial permutation table
     * @param plainText An array of 8 integers representing the plain text
     * @return The result of IP table as an array of 8 integers
     */
    public int[] passThroughIPTable(int[] plainText)
    {
        int shiftAmount;
        int mask;
        int maskedByte;
        int[] IPPassed = {0, 0, 0, 0, 0, 0, 0, 0};
        int[] byteOrder = {4, 0, 5, 1, 6, 2, 7, 3};
        
        for(int bite = 0; bite <= 7; bite++)
        {
            shiftAmount = 7 - bite;
            mask = 1;
            for(int bit = 0; bit <= 7; bit++)
            {
                maskedByte = (plainText[bite] & mask);
                if(shiftAmount >= 0)
                    maskedByte = (maskedByte << shiftAmount);
                else if(shiftAmount < 0)
                    maskedByte = (maskedByte >> Math.abs(shiftAmount));
                IPPassed[byteOrder[bit]] = (IPPassed[byteOrder[bit]] | maskedByte);
                shiftAmount--;
                mask = (mask << 1);
            }
        }
        return IPPassed;
    }
    
    /**
     * Expand a 32 bit number to a 48 bit number
     * @param rightBlock An array of 4 integers representing the 32 bit right block
     * @return the expanded integer array of 6 integers
     */
    public int[] expandBytes(int[] rightBlock)
    {
        ArrayList<Integer> nibbles;
        ArrayList<Integer> blocks;
        int[] expandedBytes = {0, 0, 0, 0, 0, 0};
        int expandedLowerBytes = 0;
        int expandedUpperBytes = 0;

        nibbles = getNibbles(rightBlock);
        blocks = assignFirstLastBitsForBlocks(nibbles);
        
        for(int block = 0; block <= 3; block++)
        {
            expandedLowerBytes = expandedLowerBytes | blocks.get(block) << (6 * block);
            expandedUpperBytes = expandedUpperBytes | blocks.get(block + 4) << (6 * block);
        }
        for(int aByte = 0; aByte <= 2; aByte++)
        {
            expandedBytes[aByte] = (expandedBytes[aByte] | (expandedLowerBytes & (255 << (aByte * 8)))) >> (aByte * 8);
            expandedBytes[aByte + 3] = (expandedBytes[aByte + 3] | (expandedUpperBytes & (255 << (aByte * 8)))) >> (aByte * 8); 
        }
            
        
        return expandedBytes;
    }
    
    /**
     * Splits up an array of bytes into their upper and lower nibbles
     * @param rightBlock An array of 4 integers representing the 32 bit right block
     * @return An array of 8 integers representing the upper and lower nibbles of the input
     */
    public ArrayList<Integer> getNibbles(int[] rightBlock)
    {
        ArrayList<Integer> nibbles = new ArrayList<Integer>();
        
        for(int nibble = 0; nibble <= 3; nibble++)
        {
            nibbles.add(((rightBlock[nibble] & 15) << 1));
            nibbles.add(((rightBlock[nibble] & 240) >> 3));
        }
        
        return nibbles;
    }
    
    /**
     * Assign the values of the first and last bit of each 6 bit block of the expansion table
     * @param nibbles An array of 8 integers
     * @return 
     */
    public ArrayList<Integer> assignFirstLastBitsForBlocks(ArrayList<Integer> nibbles)
    {
        int lastBitMask = 2;
        int firstBitMask = 16;
        int currentNibble;
        int nextNibble;
        
        for(int nibble = 0; nibble <= 7; nibble++)
        {   
            currentNibble = nibbles.get(nibble);
            nextNibble = nibbles.get((nibble + 1) % 8);
            
            nibbles.set(nibble, currentNibble | ((nextNibble & lastBitMask) << 4));
            nibbles.set((nibble + 1) % 8, nextNibble | ((currentNibble & firstBitMask) >> 4));
        }
        return nibbles;
    }
    
    /**
     * Performs the xor operation on an array of integers of n length.
     * Each operand must be of the same length
     * @param leftOperand
     * @param rightOperand
     * @return An array of xored integers
     */
    public int[] xor(int[] leftOperand, int[] rightOperand)
    {
    	int[] xoredValue = new int[leftOperand.length];
    	for(int aByte = 0; aByte < leftOperand.length; aByte++)
    	{
    		xoredValue[aByte] = leftOperand[aByte] ^ rightOperand[aByte]; 
    	}
    	return xoredValue;
    }
}

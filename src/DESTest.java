import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class DESTest
{
    DES cipher;
    byte[] plainTextBlock;
    ByteArrayOutputStream converter;
    String plainText;
    
    @Before
    public void setup() throws IOException
    {
        this.cipher = new DES(16);
        this.plainText = "GreatDES";
        cipher.setKey("0001001100110100010101110111100110011011101111001101111111110001");
        this.converter = new ByteArrayOutputStream();
        this.converter.write(plainText.getBytes());
        this.plainTextBlock = this.converter.toByteArray();
    }
    
    @Test
    public void testEncryptDecrypt()
    {
        int[] expectedOutput = new int[8];
        for(int aByte = 0; aByte < 8; aByte++)
            expectedOutput[aByte] = Byte.toUnsignedInt(this.plainTextBlock[aByte]);
        int[] cipherText = cipher.encrypt(expectedOutput);
        int[] actaulOutput = cipher.decrypt(cipherText);
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
    public void testGenerateKeys()
    {
        int[][] expectedOutput = {{114, 112, 252, 239, 2, 27}, {229, 201, 219, 217, 174, 121},
                                    {153, 207, 66, 138, 252, 85}, {29, 53, 219, 214, 173, 114},           
                                    {168, 83, 235, 7, 236, 124}, {47, 123, 80, 62, 165, 99},            
                                    {188, 24, 246, 183, 132, 236}, {251, 59, 193, 58, 138, 247},           
                                    {129, 231, 237, 235, 219, 224}, {79, 70, 186, 71, 243, 177},
                                    {134, 211, 222, 211, 95, 33}, {233, 103, 148, 245, 113, 117},
                                    {65, 186, 250, 209, 197, 151}, {58, 231, 242, 183, 67, 95},
                                    {10, 63, 61, 141, 145, 191}, {245, 23, 14, 139, 61, 203}};
        int[][] actualOutput = cipher.generateKeys();
        assertEquals(expectedOutput[0][0], actualOutput[0][0]);
        assertEquals(expectedOutput[0][1], actualOutput[0][1]);
        assertEquals(expectedOutput[0][2], actualOutput[0][2]);
        assertEquals(expectedOutput[0][3], actualOutput[0][3]);
        assertEquals(expectedOutput[0][4], actualOutput[0][4]);
        assertEquals(expectedOutput[0][5], actualOutput[0][5]);
        assertEquals(expectedOutput[1][0], actualOutput[1][0]);
        assertEquals(expectedOutput[1][1], actualOutput[1][1]);
        assertEquals(expectedOutput[1][2], actualOutput[1][2]);
        assertEquals(expectedOutput[1][3], actualOutput[1][3]);
        assertEquals(expectedOutput[1][4], actualOutput[1][4]);
        assertEquals(expectedOutput[1][5], actualOutput[1][5]);
        assertEquals(expectedOutput[2][0], actualOutput[2][0]);
        assertEquals(expectedOutput[2][1], actualOutput[2][1]);
        assertEquals(expectedOutput[2][2], actualOutput[2][2]);
        assertEquals(expectedOutput[2][3], actualOutput[2][3]);
        assertEquals(expectedOutput[2][4], actualOutput[2][4]);
        assertEquals(expectedOutput[2][5], actualOutput[2][5]);
        assertEquals(expectedOutput[3][0], actualOutput[3][0]);
        assertEquals(expectedOutput[3][1], actualOutput[3][1]);
        assertEquals(expectedOutput[3][2], actualOutput[3][2]);
        assertEquals(expectedOutput[3][3], actualOutput[3][3]);
        assertEquals(expectedOutput[3][4], actualOutput[3][4]);
        assertEquals(expectedOutput[3][5], actualOutput[3][5]);
        assertEquals(expectedOutput[4][0], actualOutput[4][0]);
        assertEquals(expectedOutput[4][1], actualOutput[4][1]);
        assertEquals(expectedOutput[4][2], actualOutput[4][2]);
        assertEquals(expectedOutput[4][3], actualOutput[4][3]);
        assertEquals(expectedOutput[4][4], actualOutput[4][4]);
        assertEquals(expectedOutput[4][5], actualOutput[4][5]);
        assertEquals(expectedOutput[5][0], actualOutput[5][0]);
        assertEquals(expectedOutput[5][1], actualOutput[5][1]);
        assertEquals(expectedOutput[5][2], actualOutput[5][2]);
        assertEquals(expectedOutput[5][3], actualOutput[5][3]);
        assertEquals(expectedOutput[5][4], actualOutput[5][4]);
        assertEquals(expectedOutput[5][5], actualOutput[5][5]);
        assertEquals(expectedOutput[6][0], actualOutput[6][0]);
        assertEquals(expectedOutput[6][1], actualOutput[6][1]);
        assertEquals(expectedOutput[6][2], actualOutput[6][2]);
        assertEquals(expectedOutput[6][3], actualOutput[6][3]);
        assertEquals(expectedOutput[6][4], actualOutput[6][4]);
        assertEquals(expectedOutput[6][5], actualOutput[6][5]);
        assertEquals(expectedOutput[7][0], actualOutput[7][0]);
        assertEquals(expectedOutput[7][1], actualOutput[7][1]);
        assertEquals(expectedOutput[7][2], actualOutput[7][2]);
        assertEquals(expectedOutput[7][3], actualOutput[7][3]);
        assertEquals(expectedOutput[7][4], actualOutput[7][4]);
        assertEquals(expectedOutput[7][5], actualOutput[7][5]);
    }

    @Test
    public void testPassThroughIPTableDESExample()
    {   
        byte[] plainTextByteArray = {1, 35, 69, 103, (byte) 137, (byte) 171, (byte) 205, (byte) 239};
        BigInteger plainText = new BigInteger(plainTextByteArray);
        byte[] expectedOutputByteArray = {0, (byte) 204, 0, (byte) 204, (byte) 255, (byte) 240, (byte) 170, (byte) 240, (byte) 170};
        BigInteger expectedOutput = new BigInteger(expectedOutputByteArray);
        BigInteger actaulOutput = cipher.passThroughIPTable(plainText);
        assertEquals(expectedOutput, actaulOutput);
    }
    
    @Test
    public void testIPWithBigEndian()
    {
        BigInteger plainText = new BigInteger("81985529216486895");
        BigInteger expectedOutput = new BigInteger("14699974583363760298");
        BigInteger actaulOutput = cipher.passThroughIPTable(plainText);
        assertEquals(expectedOutput, actaulOutput);
    }
    
    @Test
    public void testIPWithLittleEndian()
    {
        BigInteger plainText = new BigInteger("17848844570815808640");
        BigInteger expectedOutput = new BigInteger("6129211145068937267");
        BigInteger actaulOutput = cipher.passThroughIPTable(plainText);
        assertEquals(expectedOutput, actaulOutput);
    }
    
    @Test
    public void testExpandBytes()
    {
        int[] rightBlock = {170, 240, 170, 240};
        int[] expectedOutput = {85, 21, 122, 85, 21, 122};
        int[] actualOutput = cipher.expandBytes(rightBlock);
        assertEquals(expectedOutput[0], actualOutput[0]);
        assertEquals(expectedOutput[1], actualOutput[1]);
        assertEquals(expectedOutput[2], actualOutput[2]);
        assertEquals(expectedOutput[3], actualOutput[3]);
        assertEquals(expectedOutput[4], actualOutput[4]);
        assertEquals(expectedOutput[5], actualOutput[5]);
    }
    
    @Test
    public void testExpandedBytesBigInteger(){
        byte[] rightBlockByteArray = {(byte) 240, (byte) 170, (byte) 240, (byte) 170};
        byte[] expectedOutputByteArray = {122, 21, 85, 122, 21, 85};
        BigInteger rightBlock = new BigInteger(rightBlockByteArray);
        BigInteger expectedOutput = new BigInteger(expectedOutputByteArray);
        BigInteger actualOutput = cipher.expandBytes(rightBlock);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testGetNibbles()
    {
        int[] rightBlock = {170, 240, 170, 240};
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
    public void testXor()
    {
    	int[] expandedBytes = {85, 21, 122, 85, 21, 122};
    	int[] key = {114, 112, 252, 239, 2, 27};
    	int[] expectedOutput = {39, 101, 134, 186, 23, 97};
    	int[] actualOutput = cipher.xor(expandedBytes, key);
    	assertEquals(expectedOutput[0], actualOutput[0]);
    	assertEquals(expectedOutput[1], actualOutput[1]);
    	assertEquals(expectedOutput[2], actualOutput[2]);
    	assertEquals(expectedOutput[3], actualOutput[3]);
    	assertEquals(expectedOutput[4], actualOutput[4]);
    	assertEquals(expectedOutput[5], actualOutput[5]);
    }
    
    @Test
    public void testSbox()
    {
        int[] xoredBytes = {39, 101, 134, 186, 23, 97};
        String expectedOutput = "01011100100000101011010110010111";
        String actualOutput = cipher.passThroughSBox(xoredBytes);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testPTable()
    {
        String pTableInput = "01011100100000101011010110010111";
        String expectedOutput = "00100011010010101010100110111011";
        String actualOutput = cipher.pTable(pTableInput);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testPTableWithBigInteger()
    {
        byte[] pTableInputByteArray = {92, (byte) 130, (byte) 181, (byte) 151};
        byte[] expectedOutputByteArray ={35, 74, (byte) 169, (byte) 187};
        BigInteger pTableInput = new BigInteger(pTableInputByteArray);
        BigInteger expectedOutput = new BigInteger(expectedOutputByteArray);
        BigInteger actualOutput = cipher.pTable(pTableInput);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testPTableWithBigEndian()
    {
        BigInteger pTableInput = new BigInteger("1552070039");
        BigInteger expectedOutput = new BigInteger("592095675");
        BigInteger actualOutput = cipher.pTable(pTableInput);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testF()
    {
        int[] rightBlock = {170, 240, 170, 240};
        int[] key = {114, 112, 252, 239, 2, 27};
        int[] expectedOutput = {187, 169, 74, 35};
        int[] actualOutput = cipher.f(rightBlock, key);
        assertEquals(expectedOutput[0], actualOutput[0]);
        assertEquals(expectedOutput[1], actualOutput[1]);
        assertEquals(expectedOutput[2], actualOutput[2]);
        assertEquals(expectedOutput[3], actualOutput[3]);
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
    public void testEncrypt()
    {
        int[] plainText = {239, 205, 171, 137, 103, 69, 35, 1};
        int[] expectedCipherText = {5, 180, 10, 15, 84, 19, 232, 133};
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
    public void testDESAlgorithm()
    {
        int[] plainText = {239, 205, 171, 137, 103, 69, 35, 1};
        int[][] keys = {{114, 112, 252, 239, 2, 27}, {229, 201, 219, 217, 174, 121},
                        {153, 207, 66, 138, 252, 85}, {29, 53, 219, 214, 173, 114},           
                        {168, 83, 235, 7, 236, 124}, {47, 123, 80, 62, 165, 99},            
                        {188, 24, 246, 183, 132, 236}, {251, 59, 193, 58, 138, 247},           
                        {129, 231, 237, 235, 219, 224}, {79, 70, 186, 71, 243, 177},
                        {134, 211, 222, 211, 95, 33}, {233, 103, 148, 245, 113, 117},
                        {65, 186, 250, 209, 197, 151}, {58, 231, 242, 183, 67, 95},
                        {10, 63, 61, 141, 145, 191}, {245, 23, 14, 139, 61, 203}};
        int[] expectedCipherText = {5, 180, 10, 15, 84, 19, 232, 133};
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
    public void testDecrypt()
    {
        int[] cipherText = {5, 180, 10, 15, 84, 19, 232, 133};
        int[] expectedPlainText = {239, 205, 171, 137, 103, 69, 35, 1};
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
}

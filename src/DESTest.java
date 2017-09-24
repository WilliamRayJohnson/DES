import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        this.cipher = new DES();
        this.plainText = "GreatDES";
        this.converter = new ByteArrayOutputStream();
        this.converter.write(plainText.getBytes());
        this.plainTextBlock = this.converter.toByteArray();
    }
    

    @Test
    public void testPassThroughIPTableDESExample()
    {   
        int[] plainText = {239, 205, 171, 137, 103, 69, 35, 1};
        int[] expectedOutput = {170, 240, 170, 240, 255, 204, 0, 204};
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
}

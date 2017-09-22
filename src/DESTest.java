import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
        byte[] plainText = {(byte) 239, (byte) 205, (byte) 171, (byte) 137, 103, 69, 35, 1};
        byte[] expectedOutput = {(byte) 170, (byte) 240, (byte) 170, (byte) 240, (byte) 255, (byte) 204, 0, (byte) 204};
        byte[] actaulOutput = cipher.passThroughIPTable(plainText);
        assertEquals(expectedOutput[0], actaulOutput[0]);
        assertEquals(expectedOutput[1], actaulOutput[1]);
        assertEquals(expectedOutput[2], actaulOutput[2]);
        assertEquals(expectedOutput[3], actaulOutput[3]);
        assertEquals(expectedOutput[4], actaulOutput[4]);
        assertEquals(expectedOutput[5], actaulOutput[5]);
        assertEquals(expectedOutput[6], actaulOutput[6]);
        assertEquals(expectedOutput[7], actaulOutput[7]);
    }

}

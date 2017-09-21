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
    public void testPassThroughIPTable()
    {   
        fail("Not yet implemented");
    }

}

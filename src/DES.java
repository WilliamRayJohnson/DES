import java.util.ArrayList;

public class DES
{
    public DES()
    {
        
    }
    
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
    
    public int[] expandBytes(int[] rightBlock)
    {
        ArrayList<Byte> nibbles;
        int[] expandedBytes = {0, 0, 0, 0, 0, 0};
        
        
        return null;
    }
    
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
}

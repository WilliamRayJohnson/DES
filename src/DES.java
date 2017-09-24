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
}

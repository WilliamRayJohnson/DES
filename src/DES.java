
public class DES
{
    public DES()
    {
        
    }
    
    public byte[] passThroughIPTable(byte[] plainText)
    {
        int shiftAmount;
        byte mask;
        byte maskedByte;
        byte[] IPPassed = {0, 0, 0, 0, 0, 0, 0, 0};
        int[] byteOrder = {4, 0, 5, 1, 6, 2, 7, 3};
        
        for(int bite = 0; bite <= 7; bite++)
        {
            shiftAmount = 7 - bite;
            mask = 1;
            for(int bit = 0; bit <= 7; bit++)
            {
                maskedByte = (byte) (plainText[bite] & mask);
                if(shiftAmount >= 0)
                    maskedByte = (byte) (maskedByte << shiftAmount);
                else if(shiftAmount < 0)
                    maskedByte = (byte) (maskedByte >> Math.abs(shiftAmount));
                IPPassed[byteOrder[bit]] = (byte) (IPPassed[byteOrder[bit]] | maskedByte);
                shiftAmount--;
                mask = (byte) (mask << 1);
            }
        }
        return IPPassed;
    }
}

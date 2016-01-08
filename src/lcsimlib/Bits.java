package lcsimlib;

//The Bits class has methods to isolate bits in a given number
public class Bits 
{
    /*
     * 'data' is the number you want to isolate bits from
     * 'low' is the lowest bit you want to keep
     * 'high' is the highest bit you want to keep
     */
    
    public static byte isoz(byte data, int low, int high)
    {
        int output = data;
        output &= 0xFFFF;
        output = (output << 7-high)&0xFF;
        output = (output >>> low+7-high)&0xFF;
        return (byte)(output&0xFF);
    }
    public static short isoz(short data, int low, int high)
    {
        int output = data;
        output &= 0xFFFF;
        output = (output << 15-high)&0xFFFF;
        output = (output >>> low+15-high)&0xFFFF;
        return (short)(output&0xFFFF);
    }
    public static int isoz(int data, int low, int high)
    {
        int output = data;
        output <<= 31-high;
        output >>>= low+31-high;
        return output;
    }
    public static long isoz(long data, int low, int high)
    {
        long output = data;
        output <<= 63-high;
        output >>>= low+63-high;
        return output;
    }
    
    public static byte isos(byte data, int low, int high)
    {
        byte output = data;
        output <<= 7-high;
        output >>= low+7-high;
        return output;
    }
    public static short isos(short data, int low, int high)
    {
        short output = data;
        output <<= 15-high;
        output >>= low+15-high;
        return output;
    }
    public static int isos(int data, int low, int high)
    {
        int output = data;
        output <<= 31-high;
        output >>= low+31-high;
        return output;
    }
    public static long isos(long data, int low, int high)
    {
        long output = data;
        output <<= 63-high;
        output >>= low+63-high;
        return output;
    }
    
    
    
    public static byte set(byte data, int bit)
    {
        return (byte)(data | (1<<bit));
    }
    public static short set(short data, int bit)
    {
        return (short)(data | (1<<bit));
    }
    public static int set(int data, int bit)
    {
        return (int)(data | (1<<bit));
    }
    public static long set(long data, int bit)
    {
        return (long)(data | (1<<bit));
    }
    
    public static byte clr(byte data, int bit)
    {
        return (byte)(data & ~(1<<bit));
    }
    public static short clr(short data, int bit)
    {
        return (short)(data & ~(1<<bit));
    }
    public static int clr(int data, int bit)
    {
        return (int)(data & ~(1<<bit));
    }
    public static long clr(long data, int bit)
    {
        return (long)(data & ~(1<<bit));
    }
    
    public static byte get(byte data, int bit)
    {
        return (byte) ((data>>>bit)&0x1);
    }
    public static short get(short data, int bit)
    {
        return (short) ((data>>>bit)&0x1);
    }
    public static int get(int data, int bit)
    {
        return (int) ((data>>>bit)&0x1);
    }
    public static long get(long data, int bit)
    {
        return (long) ((data>>>bit)&0x1);
    }
    
    
    
    
}

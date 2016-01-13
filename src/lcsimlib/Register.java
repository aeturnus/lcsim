package lcsimlib;

public class Register 
{
    private byte[] data;
    private boolean r;      //flags to detect if you've read or written
    private boolean w;      //
    private String name;
    //Size in bytes
    public Register(int size)
    {
        data = new byte[size];
        r = false;
        w = false;
        name = "";
        for(int i = 0; i < size; i++)
        {
            data[i] = 0;
        }
    }
    public Register(int size, String setName)
    {
        data = new byte[size];
        r = false;
        w = false;
        name = setName;
        for(int i = 0; i < size; i++)
        {
            data[i] = 0;
        }
    }
    public int getSize()
    {
        return data.length;
    }
    public String getName()
    {
        return name;
    }
    
    //No size checking: I'm trusting the programmer
    public void write1Bytes(byte in)
    {
        data[0] = in;
        w = true;
    }
    public void write2Bytes(short in)
    {
        data[0] = (byte)in;
        data[1] = (byte)( (in >>> 8) & 0xFF );
        w = true;
    }
    public void write4Bytes(int in)
    {
        data[0] = (byte)in;
        data[1] = (byte)( (in >>> 8) & 0xFF );
        data[2] = (byte)( (in >>> 16) & 0xFF );
        data[3] = (byte)( (in >>> 24) & 0xFF );
        w = true;
    }
    public void write8Bytes(long in)
    {
        data[0] = (byte)in;
        data[1] = (byte)( (in >>> 8) & 0xFF );
        data[2] = (byte)( (in >>> 16) & 0xFF );
        data[3] = (byte)( (in >>> 24) & 0xFF );
        data[4] = (byte)( (in >>> 32) & 0xFF );
        data[5] = (byte)( (in >>> 40) & 0xFF );
        data[6] = (byte)( (in >>> 48) & 0xFF );
        data[7] = (byte)( (in >>> 56) & 0xFF );
        w = true;
    }
    
    
    
    public byte read1Bytes()
    {
        r = true;
        return peek1Bytes();
    }
    public short read2Bytes()
    {
        r = true;
        return peek2Bytes();
    }
    public int read4Bytes()
    {
        r = true;
        return peek4Bytes();
    }
    public long read8Bytes()
    {
        r = true;
        return peek8Bytes();
    }
    public byte peek1Bytes()
    {
        return data[0];
    }
    public short peek2Bytes()
    {
        short output = 0;
        output |= data[1]&0xFF;
        output <<= 8;
        output |= data[0]&0xFF;
        return output;
    }
    public int peek4Bytes()
    {
        int output = 0;
        output |= data[3]&0xFF;
        output <<= 8;
        output |= data[2]&0xFF;
        output <<= 8;
        output |= data[1]&0xFF;
        output <<= 8;
        output |= data[0]&0xFF;
        return output;
    }
    public long peek8Bytes()
    {
        int output = 0;
        output |= data[7]&0xFF;
        output <<= 8;
        output |= data[6]&0xFF;
        output <<= 8;
        output |= data[5]&0xFF;
        output <<= 8;
        output |= data[4]&0xFF;
        output <<= 8;
        output |= data[3]&0xFF;
        output <<= 8;
        output |= data[2]&0xFF;
        output <<= 8;
        output |= data[1]&0xFF;
        output <<= 8;
        output |= data[0]&0xFF;
        return output;
    }
    public boolean read() {return r;}       //getters
    public boolean writ() {return w;}
    public void readAck() {r = false;}      //acknowledge: clears flag
    public void writAck() {w = false;}
}

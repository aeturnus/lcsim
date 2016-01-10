package lcsimlib;

import java.util.Vector;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

/*
 * A CodeLoader is able to load code from a particular filename
 */

public abstract class CodeLoader
{
    //Include the dot: add  ".obj", not "obj"
    protected String[] extensions;
    protected LCSystem system;
    
    
    public abstract void init(LCSystem sys);
    
    public abstract boolean load(String filePath);
    public abstract boolean load(File file);
    
    //Returns true if a valid extension is posed
    public boolean checkExtension(String filePath)
    {
        int dotIndex = filePath.lastIndexOf('.');
        String extension = filePath.substring(dotIndex);
        for(int i = 0; i < extensions.length; i++)
        {
            if(extension.equals(extensions[i]))
            {
                return true;
            }
        }
        return false;
    }
    
    public String[] getExtensions()
    {
        if(extensions == null)
        {
            return new String[0];
        }
        return extensions.clone();
    }
    
    protected static void handleIOException(IOException e)
    {
        e.printStackTrace();
    }
    
    protected void handleWrongExtension(String filePath)
    {
        System.out.println("\""+filePath + "\" has incorrect extension");
    }
    
    public static byte read1BytesLE(InputStream f)
    {
        byte output;
        try
        {
            output = (byte) (f.read()&0xFF);
            return output;
        }
        catch (IOException e)
        {
            handleIOException(e);
        }
        return 0;
    }
    public static byte read1BytesBE(InputStream f)
    {
        byte output;
        try
        {
            output = (byte) (f.read()&0xFF);
            return output;
        }
        catch (IOException e)
        {
            handleIOException(e);
        }
        return 0;
    }
    
    public static short read2BytesLE(InputStream f)
    {
        short output;
        try
        {
            output = (short) (f.read()&0xFF);
            output |= (short) (((f.read()&0xFF))<<8)&0xFF00;
            return output;
        }
        catch (IOException e)
        {
            handleIOException(e);
        }
        return 0;
    }
    public static short read2BytesBE(InputStream f)
    {
        short output = 0;
        try
        {
            output |= (short) (f.read()&0xFF);
            output <<= 8;
            output |= (short) (f.read()&0xFF);
            return output;
        }
        catch (IOException e)
        {
            handleIOException(e);
        }
        return 0;
    }
    
    public static int read4BytesLE(InputStream f)
    {
        int output = 0;
        try
        {
            output = (int) (f.read()&0xFF);
            output |= (int) (((f.read()&0xFF))<<8)&0xFF00;
            output |= (int) (((f.read()&0xFF))<<16)&0xFF0000;
            output |= (int) (((f.read()&0xFF))<<24)&0xFF000000;
            return output;
        }
        catch (IOException e)
        {
            handleIOException(e);
        }
        return 0;
    }
    public static int read4BytesBE(InputStream f)
    {
        int output = 0;
        try
        {
            output |= (int) (f.read()&0xFF);
            output <<= 8;
            output |= (int) (f.read()&0xFF);
            output <<= 8;
            output |= (int) (f.read()&0xFF);
            output <<= 8;
            output |= (int) (f.read()&0xFF);

            return output;
        }
        catch (IOException e)
        {
            handleIOException(e);
        }
        return 0;
    }
    
    
    
}

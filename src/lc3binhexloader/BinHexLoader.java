package lc3binhexloader;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import lcsimlib.CodeLoader;
import lcsimlib.Core;
import lcsimlib.LCSystem;
import lcsimlib.RegEnum;

public class BinHexLoader extends CodeLoader
{
    public BinHexLoader()
    {
        // TODO Auto-generated constructor stub
        
    }
    
    public void init(LCSystem sys)
    {
        extensions = new String[]{".bin",".hex"};
        system = sys;
    }
    
    public boolean load(String filePath)
    {
        File f = new File(filePath);
        return load(f);
    }
    
    public boolean load(File file)
    {
        Core core = system.getCore();
        if(!checkExtension(file.getAbsolutePath()))
        {
            return false;
        }
        
        int radix = 2;
        
        if(file.getName().endsWith(".hex"))
        {
            radix = 16;
        }
        
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            
            int addr,orig;
            short data;
            String line;
            
            orig = addr = Integer.parseInt(reader.readLine(),radix);
            while( (line = reader.readLine()) != null)
            {
                data = (short) ( Integer.parseInt(line, radix) & 0xFFFF);
                addr++;
                core.writeMem2Bytes(addr, data);
            }
            reader.close();
            
            core.getRegister(RegEnum.PC).write2Bytes((short)(orig & 0xFFFF));
            
            return true;
        }
        catch (IOException e)
        {
            handleIOException(e);
        }
        return false;
    }

}

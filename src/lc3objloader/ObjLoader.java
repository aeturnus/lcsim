package lc3objloader;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

import lcsimlib.CodeLoader;
import lcsimlib.Core;
import lcsimlib.LCSystem;
import lcsimlib.RegEnum;

public class ObjLoader extends CodeLoader
{

    public ObjLoader()
    {
        // TODO Auto-generated constructor stub
        
    }
    
    public void init(LCSystem sys)
    {
        extensions = new String[]{".obj"};
        system = sys;
    }
    
    public boolean load(String filePath)
    {
        File file = new File(filePath);
        return load(file);
    }
    
    public boolean load(File file)
    {
        Core core = system.getCore();
        if(!checkExtension(file.getAbsolutePath()))
        {
            return false;
        }
        try
        {
            InputStream raw = new FileInputStream(file);
            BufferedInputStream stream = new BufferedInputStream(raw);
            
            int entry = read2BytesBE(stream);
            int addr = entry;
            short data;
            
            //Marks spot, reads, resets, then marks for next iteration
            for(stream.mark(1); stream.read() != -1; stream.mark(1),addr++)
            {
                stream.reset();
                data = read2BytesBE(stream);
                core.writeMem2Bytes(addr, data);
            }
            stream.close();
            core.getRegister(RegEnum.PC).write2Bytes((short)(entry & 0xFFFF));
            return true;
        }
        catch (IOException e)
        {
            handleIOException(e);
        }
        return false;
    }

}

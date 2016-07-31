package lc3ldsloader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import lcsimlib.CodeLoader;
import lcsimlib.Core;
import lcsimlib.LCSystem;
import lcsimlib.RegEnum;

/**
 * Loadscript loader
 * Loadscripts are simple files that
 * detail start address and the files to load
 * @author brandon
 *
 */
public class LdsLoader extends CodeLoader
{

    @Override
    public void init(LCSystem sys)
    {
        // TODO Auto-generated method stub
        extensions = new String[]{".lds"};
        system = sys;
    }

    @Override
    public boolean load(String filePath)
    {
        // TODO Auto-generated method stub
        File file = new File(filePath);
        load(file);
        return false;
    }

    @Override
    public boolean load(File file)
    {
        // TODO Auto-generated method stub
        if(!checkExtension(file.getAbsolutePath()))
        {
            return false;
        }
        try
        {
            InputStream raw = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(raw);
            BufferedReader reader = new BufferedReader(streamReader);
            
            String absPath = file.getCanonicalPath();
            String dir = absPath.substring(0, absPath.lastIndexOf(File.separatorChar));
            
            String entryLine = reader.readLine();
            entryLine = entryLine.substring(entryLine.indexOf('x')+1);
            int entry = Integer.parseInt(entryLine,16);
            
            Core core = system.getCore();
            // read lines until done
            String fileName;
            String path;
            for( fileName = reader.readLine(); fileName != null; fileName = reader.readLine() )
            {
                path = dir + File.separator + fileName;
                if(system.load(path))
                {
                }
                else
                {
                    System.out.println(path + " could not be loaded");
                }
            }
            
            core.getRegister(RegEnum.PC).write2Bytes((short)(entry & 0xFFFF));
            
            reader.close();
            streamReader.close();
            raw.close();
            return true;
        }
        catch (IOException e)
        {
            handleIOException(e);
        }
        catch (NumberFormatException nfe)
        {
            System.err.println("Bad entry point");
        }
        return false;
    }

}

package lcsimlib;

import java.io.File;
import java.util.Vector;

/*
 * The LCSystem class acts as the entire Little Computer system, with the CPU, memory, bus, debugger, and devices
 */

public class LCSystem extends Component
{
    public Core core;
    public Vector<Device> devices;
    public Vector<CodeLoader> loaders;
    public Register mcr;    //machine control register
    private boolean profile;
    public LCSystem()
    {
        super();
        devices = new Vector<Device>();
        loaders = new Vector<CodeLoader>();
        name = "Little Computer";
        profile = true;
    }
    
    public void init(LCSystem system)
    {
        //Do nothing; init is not something this needs to run
    }
    public void openConfig()
    {
    }
    public void show()
    {
    }
    public boolean getProfile(){return profile;}
    public void setProfile(){profile = true;}
    public void unsetProfile(){profile = false;}
    
    public void cycle()
    {
        /*
         * TODO: Module performance profiling
         */
        if(profile)
        {
            if((mcr.read2Bytes() & 0x8000 ) == 0)
            {
                return;
            }
            core.profileCycle();
            for(int i = 0; i < devices.size(); ++i)
            {
                if(!devices.get(i).hasOwnClock())
                {
                    //Device dev = devices.get(i);
                    ///dev.profileCycle();
                    devices.get(i).profileCycle();
                }
            }
        }
        else
        {
            if((mcr.read2Bytes() & 0x8000 ) == 0)
            {
                return;
            }
            core.cycle();
            for(int i = 0; i < devices.size(); ++i)
            {
                if(!devices.get(i).hasOwnClock())
                {
                    //Device dev = devices.get(i);
                    ///dev.profileCycle();
                    devices.get(i).cycle();
                }
            }
        }
    }
    public void enableProfiling()
    {
        profile = true;
    }
    public void disableProfiling()
    {
        profile = false;
    }
    
    public void setCore(Core assignedCore)
    {
        core = assignedCore;
        core.init(this);
        mcr = core.getIORegister(0xFFFE,2); //set up machine control register
        mcr.write2Bytes((short)0xFFFF);
    }
    public void addDevice(Device dev)
    {
        devices.add(dev);
        dev.init(this);
        dev.clearTotalTime();
    }
    public void addLoader(CodeLoader loader)
    {
        loaders.add(loader);
        loader.init(this);
    }
    
    public boolean load(String pathName)
    {
        CodeLoader loader;
        for(int i = 0; i < loaders.size(); i++)
        {
            loader = loaders.get(i);
            if(loader.checkExtension(pathName))
            {
                return loader.load(pathName);
            }
        }
        return false;
    }
    public boolean load(File file)
    {
        CodeLoader loader;
        for(int i = 0; i < loaders.size(); i++)
        {
            loader = loaders.get(i);
            if(loader.checkExtension(file.getAbsolutePath()))
            {
                return loader.load(file);
            }
        }
        return false;
    }
    
    public void printProfile()
    {
        /*
         * TODO: How will we handle other bus clocks?
         */
        int overheadP = 100;
        long overheadT = getAverageTime();
        System.out.println(getName() + ": " + getAverageTime() + " nsec");
        System.out.println(core.getName() + ": " + core.getAverageTime() + " nsec (" + core.getAverageTime() * 100 / getAverageTime()+ "%)");
        overheadT -= core.getAverageTime();
        overheadP -= core.getAverageTime() * 100 / getAverageTime();
        for(int i = 0; i < devices.size(); ++i)
        {
            Device dev = devices.get(i);
            System.out.println(dev.getName() + ": " + dev.getAverageTime() + " nsec (" + dev.getAverageTime() * 100 / getAverageTime()+ "%)");
            overheadT -= dev.getAverageTime();
            overheadP -= dev.getAverageTime() * 100 / getAverageTime();
        }
        System.out.println(getName() + " overhead: " + overheadT + " nsec (" + overheadP + "%)");
    }
    
    public boolean isRunning()
    {
        if( (mcr.read2Bytes() & 0x8000) == 0)
        {
            return false;
        }
        return true;
    }
}

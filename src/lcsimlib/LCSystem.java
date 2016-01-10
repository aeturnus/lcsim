package lcsimlib;

import java.io.File;
import java.util.Vector;
import java.util.Hashtable;

/*
 * The LCSystem class acts as the entire Little Computer system, with the CPU, memory, bus, debugger, and devices
 */

public class LCSystem extends Component
{
    public Core core = null;
    public Vector<Device> devices = null;
    public Vector<CodeLoader> loaders = null ;
    public Register mcr = null;    //machine control register
    private boolean profile;
    private Symbol[] symbolTable;
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
    
    public void cycleCheck()
    {
        if(!isRunning()){return;}
        cycle();
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
    public void unsetCore()
    {
        core = null;
        mcr = null;
    }
    
    public void addDevice(Device dev)
    {
        devices.add(dev);
        dev.init(this);
        dev.clearTotalTime();
    }
    public void removeDevice(Device dev)
    {
        
    }
    
    public void addLoader(CodeLoader loader)
    {
        loaders.add(loader);
        loader.init(this);
    }
    public void removeLoader(CodeLoader loader)
    {
    }
    
    public String[] getExtensions()
    {
        String[][] extsArray = new String[loaders.size()][];
        int size = 0;
        for(int i = 0; i < loaders.size(); i++)
        {
            extsArray[i] = loaders.get(i).getExtensions();
            size += extsArray[i].length;
        }
        String[] output = new String[size];
        int spot = 0;
        for(int i = 0; i < extsArray.length; i++)
        {
            for(int j = 0; j < extsArray[i].length; j++,spot++)
            {
                output[spot] = extsArray[i][j];//.substring(1);
                System.out.println(output[spot]);
            }
        }
        return output;
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
    
    public void addSymbol(String name, int address)
    {
        addSymbol(new Symbol(name,address));
    }
    public void addSymbol(Symbol symbol)
    {
        symbolTable[symbol.getAddress()] = symbol;
    }
    public Symbol getSymbol(int address)
    {
        return symbolTable[address];        //O(1) lookup of symbol; but it takes memory in as ref array...
    }
    public Symbol getSymbol(String name)
    {
        for(int i = 0; i < symbolTable.length; i++)
        {
            if(name.equals(symbolTable[i].getName()))
            {
                return symbolTable[i];
            }
        }
        return null;
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
    
    public void startRunning()
    {
        mcr.write2Bytes((short)(mcr.read2Bytes() | 0x8000));
    }
    public void stopRunning()
    {
        mcr.write2Bytes((short)(mcr.read2Bytes() & 0x7FFF));
    }
    
    public boolean isRunning()
    {
        if( (mcr.read2Bytes() & 0x8000) == 0)
        {
            return false;
        }
        return true;
    }
    public boolean isCoreLoaded()
    {
        return (core != null);
    }
}

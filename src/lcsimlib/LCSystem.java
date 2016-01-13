package lcsimlib;

import java.io.File;
import java.util.Vector;
import java.util.Hashtable;

/*
 * The LCSystem class acts as the entire Little Computer system, with the CPU, memory, bus, debugger, and devices
 */

public class LCSystem extends Component
{
    private Core core = null;
    private Debugger debugger = null;
    private Vector<Device> devices = null;
    private Vector<CodeLoader> loaders = null ;
    private Register mcr = null;    //machine control register
    private Symbol[] symbolTable;
    public LCSystem()
    {
        super();
        devices = new Vector<Device>();
        loaders = new Vector<CodeLoader>();
        symbolTable = new Symbol[0x10000];
        name = "Little Computer";
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
    
    public void cycleInternal()
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
    
    public void cycleCheck()
    {
        if(!isRunning()){return;}
        cycle();
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
    
    public void setDebugger(Debugger debugger)
    {
        this.debugger = debugger;
        System.out.println("Initializing debugger");
        this.debugger.init(this);
    }
    public void unsetDebugger()
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
        File file = new File(pathName);
        return load(file);
    }
    public boolean load(File file)
    {
        CodeLoader loader;
        if(!file.exists())
        {
            return false;
        }
        for(int i = 0; i < loaders.size(); i++)
        {
            loader = loaders.get(i);
            if(loader.checkExtension(file.getAbsolutePath()))
            {
                //Load a symbol table
                String symPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('.'))+".sym";
                loadSymbolTable(symPath);
                return loader.load(file);
            }
        }
        return false;
    }
    public boolean loadSymbolTable(String pathName)
    {
        return loadSymbolTable(new File(pathName));
    }
    public boolean loadSymbolTable(File file)
    {
        if(file.exists())
        {
            Symbol[] arr = Symbol.symbolsFromFile(file);
            if(arr == null){return false;}
            for(int i = 0; i < arr.length; i++)
            {
                addSymbol(arr[i]);
            }
            return true;
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
    
    public String getProfile()
    {
        int overheadP = 100;
        long overheadT = getAverageTime();
        String output = getName() + ": " + getAverageTime() + " nsec";
        output += "\n" +core.getName() + ": " + core.getAverageTime() + " nsec (" + core.getAverageTime() * 100 / getAverageTime()+ "%)";
        overheadT -= core.getAverageTime();
        overheadP -= core.getAverageTime() * 100 / getAverageTime();
        for(int i = 0; i < devices.size(); ++i)
        {
            Device dev = devices.get(i);
            output += "\n" + dev.getName() + ": " + dev.getAverageTime() + " nsec (" + dev.getAverageTime() * 100 / getAverageTime()+ "%)";
            overheadT -= dev.getAverageTime();
            overheadP -= dev.getAverageTime() * 100 / getAverageTime();
        }
        output += "\n" + getName() + " overhead: " + overheadT + " nsec (" + overheadP + "%)";
        return output;
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
    
    public boolean hasCore()
    {
        return (core != null);
    }
    public boolean hasDebugger()
    {
        return (debugger != null);
    }
    
    public long getRealFrequency()
    {
        long output;
        long avgTime = this.getAverageTime();
        output = avgTime!=0?1000000000 / avgTime:0;
        return output;
    }
    
    public Core getCore()
    {
        return core;
    }
    
    public Debugger getDebugger()
    {
        return debugger;
    }
}

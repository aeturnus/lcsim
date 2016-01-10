package lcsimlib;

public abstract class Core extends Component 
{
    //abstract public void init(LCSystem system);
    //abstract public void cycle();   //Up to programmer for how to implement: instruction or cycle level sim
    
    
    
    abstract public Register getIORegister(int address);    //Get a reference to an IO register for use
    abstract public Register getIORegister(int address, int size);    //Get a reference to an IO register for use; with size!
    abstract public InterruptLine getIntLine(int vector);   //Get a reference to a particular interrupt line

    //////////////////////////////
    //Ways to peer into the Core//
    //////////////////////////////
    
    //Getters
    abstract public int numGPR();   //How many general purpose registers
    abstract public int numSPR();   //How many special purpose registers
    abstract public int numMem();   //How many special purpose registers
    
    
    //Direct memory reading and writing: perhaps DMA devices?
    abstract public byte readMem1Bytes(int address);        
    abstract public short readMem2Bytes(int address);       
    abstract public int readMem4Bytes(int address);         
    abstract public long readMem8Bytes(int address);        

    abstract public void writeMem1Bytes(int address,byte data);
    abstract public void writeMem2Bytes(int address,short data);
    abstract public void writeMem4Bytes(int address,int data);
    abstract public void writeMem8Bytes(int address,long data);


    //Direct general purpose register reading and writing
    abstract public byte readGPR1Bytes(int regnum);
    abstract public short readGPR2Bytes(int regnum);
    abstract public int readGPR4Bytes(int regnum);
    abstract public long readGPR8Bytes(int regnum);
    
    abstract public void writeGPR1Bytes(int regnum, byte data);
    abstract public void writeGPR2Bytes(int regnum, short data);
    abstract public void writeGPR4Bytes(int regnum, int data);
    abstract public void writeGPR8Bytes(int regnum, long data);
    
    //Direct special purpose register reading and writing
    //Constants for 
    public static final int SPR_PC = 0;  //program counter or instruction pointer
    public static final int SPR_IR = 1;
    public static final int SPR_PSR = 2;
    
    abstract public byte readSPR1Bytes(int regnum);
    abstract public short readSPR2Bytes(int regnum);
    abstract public int readSPR4Bytes(int regnum);
    abstract public long readSPR8Bytes(int regnum);
    
    abstract public void writeSPR1Bytes(int regnum, byte data);
    abstract public void writeSPR2Bytes(int regnum, short data);
    abstract public void writeSPR4Bytes(int regnum, int data);
    abstract public void writeSPR8Bytes(int regnum, long data);
    
    abstract public void printRegisters();
    abstract public void printMemory(int low,int high);
}

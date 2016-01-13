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
    abstract public Register getRegister(RegEnum reg);
    abstract public Register[] getRegisters();
    
    //Direct special purpose register reading and writing
    //Constants for 
    
    
    
    abstract public void printRegisters();
    abstract public void printMemory(int low,int high);
    
}

package lc3standard;

import java.util.Vector;

import lcsimlib.InterruptLine;
import lcsimlib.LCSystem;
import lcsimlib.Register;
import lcsimlib.Symbol;

import lcsimlib.Bits;

public class Core extends lcsimlib.Core 
{
    //Data structures for stuff
    private Register[] gprs;            //gen purpose regs
    private Register[] sprs;            //spec purpose regs
    private Vector<Register> iors;      //io regs
    private Vector<Integer> iorAddrs;   //io regs addresses
    private Vector<InterruptLine> intLines;  //io regs
    private Vector<Integer> intVector;  //io regs
    private short[] memory;
    
    private Symbol[] symbolTable;
    
    private int state;
    private final int FETCH = 0;
    private final int DEXECUTE = 1;
    
    
    private final int PC = 8;
    private final int IR = 9;
    private final int PSR = 10;
    private final int MAR = 11;
    private final int MDR = 12;
    private final int USP = 13;
    private final int SSP = 14;
    
    private final int ADD   = 0b0001;
    private final int AND   = 0b0101;
    private final int BR    = 0b0000;
    private final int JMP   = 0b1100;
    private final int JSR   = 0b0100;
    private final int LD    = 0b0010;
    private final int LDI   = 0b1010;
    private final int LDR   = 0b0110;
    private final int LEA   = 0b1110;
    private final int NOT   = 0b1001;
    private final int RTI   = 0b1000;
    private final int ST    = 0b0011;
    private final int STI   = 0b1011;
    private final int STR   = 0b0111;
    private final int TRAP  = 0b1111;
    private final int RESV  = 0b1101;
    
    private final int PRIV_E = 0;
    private final int ILLOP_E= 1;
 
    public Core()
    {
        super();
        name = "LC3 Standard Core";
    }
    
    @Override
    public void init(LCSystem system) 
    {
        state = 0;
        
        gprs = new Register[8];
        for(int i = 0; i < 8; i++)
        {
            gprs[i] = new Register(2);
        }
        
        sprs = new Register[7];
        sprs[SPR_PC] = new Register(2,"PC ");
        sprs[SPR_PC].write2Bytes((short)0x3000);
        sprs[SPR_IR] = new Register(2,"IR ");
        sprs[SPR_PSR] = new Register(2,"PSR");
        sprs[SPR_PSR].write2Bytes((short)0x0002);          //init it so that the Z bit is set
        sprs[SPR_PSR+1]= new Register(2,"MAR");
        sprs[SPR_PSR+2] = new Register(2,"MDR");
        sprs[SPR_PSR+3]= new Register(2,"USP");
        sprs[SPR_PSR+4] = new Register(2,"SSP");
        sprs[SPR_PSR+4].write2Bytes((short)0x3000);        //init the SSP to x3000
        
        iors = new Vector<Register>();
        iorAddrs = new Vector<Integer>();
        intLines = new Vector<InterruptLine>();
        intVector = new Vector<Integer>();
        memory = new short[0xFE00]; //No point allocating additional space for memory we can't even reach;
        for(int i = 0; i < 0xFE00; i++)
        {
            memory[i] = 0;
        }
        InterruptLine privException = this.getIntLine(0x00);
        privException.setPL(Integer.MAX_VALUE); //These are supreme level interrupts
        InterruptLine illopException = this.getIntLine(0x01);
        illopException.setPL(Integer.MAX_VALUE);    //Supreme level interrupts
        
    }

    @Override
    public void cycle() 
    {
        switch(state)
        {
        case FETCH:
            //System.out.println("FETCH: "+readReg(PC));
            writeReg(MAR,readReg(PC));  //Write to the MAR
            writeReg(PC,(short)(readReg(PC)+1)); //Increment PC
            checkInterrupts();
            writeReg(MDR,this.readMem2Bytes(readReg(MAR)));  //Read from memory
            writeReg(IR,readReg(MDR));
            state = DEXECUTE;
            //break;
        case DEXECUTE:
            //System.out.println("DEXECUTE: "+readReg(PC));
            int opcode = Bits.isoz(readReg(IR), 12, 15)&0xFFFF;
            short inst = readReg(IR);
            switch(opcode)
            {
            case ADD:
                ALUinst(inst,opcode);
                break;
            case AND:
                ALUinst(inst,opcode);
                break;
            case BR:
                short psr = readReg(PSR);
                boolean ben = 
                        ((Bits.get(inst, 11) & Bits.get(psr,2)))==1 ||
                        ((Bits.get(inst, 10) & Bits.get(psr,1)))==1 ||
                        ((Bits.get(inst,  9) & Bits.get(psr,0)))==1 ;
                        //Branch enable
                if(ben)
                {
                    writeReg(PC,(short)(readReg(PC)+Bits.isos(inst, 0, 8)));
                }
                break;
            case JMP:
                writeReg(PC,readReg(Bits.isoz(inst, 6, 8)));
                break;
            case JSR:
                writeReg(7,readReg(PC));
                if(Bits.get(inst, 11) == 0)
                {
                    //JSRR
                    writeReg(PC,readReg(Bits.isoz(inst, 6, 8)));
                }
                else
                {
                    //JSR
                    writeReg(PC,(short) (readReg(PC) + Bits.isos(inst, 0, 10)));
                }
                break;
            case LD:
                LDinst(inst, opcode);
                break;
            case LDI:
                LDinst(inst, opcode);
                break;
            case LDR:
                LDinst(inst, opcode);
                break;
            case LEA:
                short lea = (short) (readReg(PC) + Bits.isos(inst, 0, 8));
                updateCC(lea);
                writeReg(Bits.isoz(inst,9,11),lea);
                break;
            case NOT:
                int dr = Bits.isoz(inst, 9, 11);
                short num = readReg(Bits.isoz(inst, 6, 8));
                num = (short)~num;
                updateCC(num);
                writeReg(dr,num);
                break;
            case RTI:
                returnInterrupt();
                break;
            case ST:
                STinst(inst, opcode);
                break;
            case STI:
                STinst(inst, opcode);
                break;
            case STR:
                STinst(inst, opcode);
                break;
            case TRAP:
                short trap = Bits.isoz(inst, 0, 7);
                writeReg(7,readReg(PC));    //Save link
                writeReg(PC,readMem(trap)); //PC < mem[trap vector]
                break;
            case RESV:
                //throw exception
                this.fireInterrupt(intLines.get(ILLOP_E),intVector.get(ILLOP_E));
                break;
            }
            state = FETCH;
            break;
        }
    }
    private void ALUinst(short inst, int opcode)
    {
        int dr = Bits.isoz(inst, 9, 11);
        short sr1 = readReg(Bits.isoz(inst, 6, 8));
        short sr2 = 0;
        if(Bits.get(inst, 5)==0)
        {
            //Reg addressing
            sr2 = readReg(inst&0b111);
        }
        else
        {
            //Imm addressing
            sr2 = Bits.isos(inst, 0, 4);
        }
        short res;
        if(opcode == ADD)
        {
            res = (short) (sr1 + sr2);
        }
        else //if (opcode == AND)
        {
            res = (short) (sr1 & sr2);
        }
        writeReg(dr,res);
        updateCC(res);
        
    }
    private void LDinst(short inst, int opcode)
    {
        int dr = Bits.isoz(inst, 9, 11);
        short addr = 0;
        if(opcode == LD)
        {
            //LD
            addr = (short) (readReg(PC) + Bits.isos(inst, 0, 8));
        }
        else if (opcode == LDR)
        {
            //LDR
            addr = (short) (readReg(Bits.isos(inst, 6, 8)) + Bits.isos(inst, 0, 5));
        }
        else if (opcode == LDI)
        {
            addr = readMem((short) (readReg(PC) + Bits.isos(inst, 0, 8)));
        }
        short data= readMem(addr);
        updateCC(data);
        writeReg(dr,data);
    }
    private void STinst(short inst, int opcode)
    {
        int sr = Bits.isoz(inst, 9, 11);
        short addr = 0;
        if(opcode == ST)
        {
            //ST
            addr = (short) (readReg(PC) + Bits.isos(inst, 0, 8));
        }
        else if (opcode == STR)
        {
            //STR
            addr = (short) (readReg(Bits.isos(inst, 6, 8)) + Bits.isos(inst, 0, 5));
        }
        else if (opcode == STI)
        {
            addr = readMem((short) (readReg(PC) + Bits.isos(inst, 0, 8)));
        }
        writeMem(addr, readReg(sr));
    }
    
    private void updateCC(short num)
    {
        short psr = readReg(PSR);
        psr &= ~0b0111;
        if(num == 0)
        {
            psr = Bits.set(psr, 1);
        }
        else if ((num & 0x8000) == 0x8000)
        {
            psr = Bits.set(psr, 2);     //Negative
        }
        else
        {
            psr = Bits.set(psr, 0);
        }
        writeReg(PSR,psr);
    }
    
    private void checkInterrupts()
    {
        for(int i = 0; i < intLines.size(); ++i)
        {
            //If line is triggered, do something
            if(intLines.get(i).check())
            {
                //Initiate interrupt
            }
        }
    }
    
    private void fireInterrupt(InterruptLine intLine, int vector)
    {
        int pl = Bits.isoz(readReg(PSR), 8, 10);
        if(!(pl < intLine.getPL())) {return;};    //PL too low
        if((readReg(PSR) & 0x8000) != 0)    //If if b15 is 1, user mode: get SSP
        {
            writeReg(USP,readReg(6));
            writeReg(6,readReg(SSP));
        }
        
        //Push PSR
        writeReg(6,(short)(readReg(6)-1));  //dec SP
        writeMem(readReg(6),readReg(PSR));
        //Push PC
        writeReg(6,(short)(readReg(6)-1));  //dec SP
        writeMem(readReg(6),readReg(PC));

        writeReg(PC,readMem((short)vector));    //get the exception address
    }
    private void returnInterrupt()
    {
        //Check priority level exceptions
        if((readReg(PSR) & 0x8000) != 0)
        {
            //If 1, we're in user mode: EXCEPTION!
            fireInterrupt(intLines.get(PRIV_E),intVector.get(PRIV_E));
            return;
        }
        //Pop PC
        writeReg(PC,readMem(readReg(6)));
        writeReg(6,(short)(readReg(6)+1));
        //Pop PSR
        writeReg(PSR,readMem(readReg(6)));
        writeReg(6,(short)(readReg(6)+1));
        
        //Get correct SP back
        if((readReg(PSR) & 0x8000) != 0)
        {
            //If it's 1, then we need the USP back
            writeReg(SSP,readReg(6));
            writeReg(6,readReg(USP));
        }
        //else, we're still in supervisor mode
    }
    
    
    private short readReg(int regnum)
    {
        short data = 0;
        if(regnum < 8)
        {
            data = gprs[regnum].read2Bytes();
        }
        else
        {
            data = sprs[regnum-8].read2Bytes();
        }
        return data;
    }
    private void writeReg(int regnum, short data)
    {
        if(regnum < 8)
        {
            gprs[regnum].write2Bytes(data);
        }
        else
        {
            sprs[regnum-8].write2Bytes(data);
        }
    }
    //This will update the mdr
    private short readMem(short addr)
    {
        writeReg(MAR,addr);
        writeReg(MDR,this.readMem2Bytes((int)readReg(MAR)));
        return readReg(MDR);
    }
    //This will write to mem
    private void writeMem(short addr, short data)
    {
        writeReg(MAR,addr);
        writeReg(MDR,data);
        this.writeMem2Bytes((int)readReg(MAR), readReg(MDR));
    }
    

    
    //Required stuff
    @Override
    public Register getIORegister(int address) {
        // TODO Auto-generated method stub
        Register newIO = new Register(2);
        Integer newAddr = new Integer(address);
        
        iors.add(newIO);
        iorAddrs.add(newAddr);
        
        return newIO;
    }

    @Override
    public Register getIORegister(int address, int size) {
        // TODO Auto-generated method stub
        return getIORegister(address); //LC3 says fuck you; you're only getting a 16 bit register
        
    }

    @Override
    public InterruptLine getIntLine(int vector) {
        // TODO Auto-generated method stub
        InterruptLine intLine = new InterruptLine();
        Integer intVec = new Integer(vector);
        
        intLines.add(intLine);
        intVector.add(intVec);
        
        return intLine;
    }

    @Override
    public int numGPR() {
        // TODO Auto-generated method stub
        return 8;
    }

    @Override
    public int numSPR() {
        // TODO Auto-generated method stub
        return 3;
    }

    @Override
    public int numMem() {
        // TODO Auto-generated method stub
        return 0xFE00;
    }

    @Override
    public byte readMem1Bytes(int address) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public short readMem2Bytes(int address) {
        // TODO Auto-generated method stub
        if((address&0xFFFF) < 0xFE00)
        {
            return memory[address&0xFFFF];
        }
        //If it's not lower, check memory mapped IO
        for(int i = 0; i < iors.size(); ++i)
        {
            if( (address&0xFFFF) == iorAddrs.get(i))
            {
                return iors.get(i).read2Bytes();
            }
        }
        //No match? Return 0
        return 0;
    }

    @Override
    public int readMem4Bytes(int address) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long readMem8Bytes(int address) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeMem1Bytes(int address, byte data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeMem2Bytes(int address, short data) {
        // TODO Auto-generated method stub
        if((address&0xFFFF) < 0x0000FE00)
        {
            memory[address] = data;
        }
        //If it's not lower, check memory mapped IO
        for(int i = 0; i < iors.size(); ++i)
        {
            if((address&0xFFFF) == iorAddrs.get(i))
            {
                iors.get(i).write2Bytes(data);
            }
        }
        //No match? Do nothing
    }

    @Override
    public void writeMem4Bytes(int address, int data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeMem8Bytes(int address, long data) {
        // TODO Auto-generated method stub

    }

    @Override
    public byte readGPR1Bytes(int regnum) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public short readGPR2Bytes(int regnum) {
        // TODO Auto-generated method stub
        return gprs[regnum].read2Bytes();
    }

    @Override
    public int readGPR4Bytes(int regnum) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long readGPR8Bytes(int regnum) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeGPR1Bytes(int regnum, byte data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeGPR2Bytes(int regnum, short data) {
        // TODO Auto-generated method stub
        gprs[regnum].write2Bytes(data);

    }

    @Override
    public void writeGPR4Bytes(int regnum, int data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeGPR8Bytes(int regnum, long data) {
        // TODO Auto-generated method stub

    }

    @Override
    public byte readSPR1Bytes(int regnum) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public short readSPR2Bytes(int regnum) {
        // TODO Auto-generated method stub
        return sprs[regnum].read2Bytes();
    }

    @Override
    public int readSPR4Bytes(int regnum) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long readSPR8Bytes(int regnum) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeSPR1Bytes(int regnum, byte data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeSPR2Bytes(int regnum, short data) {
        // TODO Auto-generated method stub
        sprs[regnum].write2Bytes(data);
    }

    @Override
    public void writeSPR4Bytes(int regnum, int data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeSPR8Bytes(int regnum, long data) {
        // TODO Auto-generated method stub

    }
 
    public void show()
    {
    }
    
    public void openConfig()
    {
    }
    
    public void printRegisters()
    {
        int i;
        for(i = 0; i < 8; i++)
        {
            System.out.println("R"+i+": "+String.format("x%04X",readReg(i)&0xFFFF) + " | " + readReg(i));
        }
        for(; i <= SSP; i++)
        {
            System.out.println(sprs[i-8].getName()+": "+String.format("x%04X",readReg(i)&0xFFFF) + " | " + readReg(i));
        }
        System.out.println();
    }
    public void printMemory(int low, int high)
    {
        for(int i = low; i <= high; i++)
        {
            System.out.println(String.format("x%04X", i)+": "+String.format("x%04X", this.readMem2Bytes(i)&0xFFFF) + " | " + this.readMem2Bytes(i));
        }
    }
}

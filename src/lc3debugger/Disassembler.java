package lc3debugger;

import lcsimlib.*;

public class Disassembler
{
    public static String disassemble(int inst, LCSystem sys, int address)
    {
        int opcode = Bits.isoz(inst, 12, 15);
        String out;
        switch(opcode)
        {
        //ADD
        case 0b0001:
            return addand(inst, opcode);
        //AND
        case 0b0101:
            return addand(inst, opcode);
        //BR
        case 0b0000:
            return branch(inst, sys, address);
        //JMP
        case 0b1100:
            return jump(inst);
        //JSR
        case 0b0100:
            return jsr(inst,sys,address);
        //LD
        case 0b0010:
            return ldldiststi(inst, "LD    ", sys, address);
        //LDI
        case 0b1010:
            return ldldiststi(inst, "LDI   ", sys, address);   
        //LDR
        case 0b0110:
            return ldrstr(inst, "LDR   ");
        //NOT
        case 0b1001:
            return not(inst);
        //RTI
        case 0b1000:
            return "RTI";
        //ST
        case 0b0011:
            return ldldiststi(inst, "ST    ", sys, address);
        //STI
        case 0b1011:
            return ldldiststi(inst, "STI   ", sys, address);
        //LDR
        case 0b0111:
            return ldrstr(inst, "STI   ");
        //TRAP
        case 0b1111:
            return trap(inst);
        }
        return null;
    }
    private static String addand(int inst, int opcode)
    {
        String out;
        if(opcode == 0b0001)
        {
            out = "ADD   R";
        }
        else
        {
            out = "AND   R";
        }
        out += Bits.isoz(inst, 9, 11) + ", R";
        out += Bits.isoz(inst, 6, 8) + ", ";
        if((inst & 0x20) == 0)
        {
            //Register
            out += "R"+Bits.isoz(inst, 0, 2);
        }
        else
        {
            //Immediate
            out += "#"+Bits.isos(inst, 0, 4);
        }
        return out;
    }
    private static String branch(int inst, LCSystem sys, int pc)
    {
        int offset =Bits.isos(inst, 0, 8); 
        int dest = offset +pc+1;
        if(offset == 0){return "NOP";}
        String out = "BR";
        if(Bits.get(inst, 11) != 0){out += "n";}
        if(Bits.get(inst, 10) != 0){out += "z";}
        if(Bits.get(inst, 9) != 0){out += "p";}
        for(int i = out.length(); i < 6; i++)
        {
            out += " ";
        }
        Symbol sym = sys.getSymbol(dest);
        if(sym == null)
        {
            out += String.format("x%04X", dest);
        }
        else
        {
            out += sym.getName();
        }
        return out;
    }
    private static String jump(int inst)
    {
        int br = Bits.isoz(inst, 6, 8);
        if(br == 7)
        {
            return "RET   (JMP R7)";
        }
        return ("JMP   R"+br);
    }
    private static String jsr(int inst, LCSystem sys, int pc)
    {
        if(Bits.get(inst, 11) != 0)
        {
            int dest = pc +1 + Bits.isos(inst, 0, 10);
            Symbol sym = sys.getSymbol(dest);
            if(sym == null)
            {
                return String.format("JSR   x%04X", dest);
            }
            return ("JSR   "+sym.getName());
        }
        return "JSRR  "+Bits.isoz(inst, 6, 8);
    }
    private static String ldldiststi(int inst, String opcode, LCSystem sys, int pc)
    {
        int dest = pc + 1 + Bits.isos(inst, 0, 8);
        int reg = Bits.isoz(inst, 9, 11);
        Symbol sym = sys.getSymbol(dest);
        if(sym == null)
        {
            return opcode+"R"+reg+", "+String.format("x%04X", dest);
        }
        return opcode+"R"+reg+", "+sym.getName();
    }
    private static String ldrstr(int inst, String opcode)
    {
        int reg = Bits.isoz(inst, 9, 11);
        int base = Bits.isoz(inst, 6, 8);
        int offset = Bits.isos(inst, 0, 5);
        return opcode+"R"+reg+", R"+base+", #"+offset;
    }
    private static String not(int inst)
    {
        int dst = Bits.isoz(inst, 9, 11);
        int src = Bits.isoz(inst, 6, 8);
        return "NOT   R" +dst+", R"+src;
    }
    private static String trap(int inst)
    {
        int tv = Bits.isoz(inst, 0, 7);
        String operand = String.format("x%02X", tv);
        switch(tv)
        {
        case 0x20:
            operand += " (GETC)";
            break;
        case 0x21:
            operand += " (OUT)";
            break;
        case 0x22:
            operand += " (PUTS)";
            break;
        case 0x23:
            operand += " (IN)";
            break;
        case 0x24:
            operand += " (PUTSP)";
            break;
        case 0x25:
            operand += " (HALT)";
            break;
        }
        
        return "TRAP  "+operand;
    }
}

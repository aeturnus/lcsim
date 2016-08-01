package lc3debugger;

import java.awt.event.MouseEvent;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import lcsimlib.Core;
import lcsimlib.LCSystem;
import lcsimlib.RegEnum;
import lcsimlib.Register;

public class RegisterTable extends JTable
{
    LCSystem sys;
    Debugger debug;
    public RegisterTable(LCSystem system, Debugger debugger)
    {
        // TODO Auto-generated constructor stub
        sys = system;
        debug = debugger;
        
        this.setModel(new RegisterModel());
        for(int i = 0; i < 3; i++)
        {
            this.getColumnModel().getColumn(i).setCellRenderer(new StringRenderer());
        }
        this.setTableHeader(null);
        TableColumnModel cols = this.getColumnModel();
        for(int i = 0; i < 3; i++)
        {
            cols.getColumn(i).setResizable(false);
        }
        this.setShowGrid(false);
        this.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
    }
    public String getRegisterId(int row, int col)
    {
        Core core = sys.getCore();
        switch(row)
        {
        case 0:
            switch(col)
            {
            case 0:
                return "r0";
            case 1:
                return "r4";
            case 2:
                return "pc";
            }
        case 1:
            switch(col)
            {
            case 0:
                return "r1";
            case 1:
                return "r5";
            case 2:
                return "ir";
            }
        case 2:
            switch(col)
            {
            case 0:
                return "r2";
            case 1:
                return "r6";
            case 2:
                return "psr";
            }
        case 3:
            switch(col)
            {
            case 0:
                return "r3";
            case 1:
                return "r7";
            case 2:
                return "cc";
            }
        }
        return null;
    }
    
    private class RegisterModel extends AbstractTableModel
    {
        public void RegisterModel()
        {
        }
        public int getRowCount() {return 4;}
        public int getColumnCount() {return 3;}
        
        
        
        public Object getValueAt(int row, int col)
        {
            Core core = sys.getCore();
            switch(row)
            {
            case 0:
                switch(col)
                {
                case 0:
                    return regToString(core.getRegister(RegEnum.R0));
                case 1:
                    return regToString(core.getRegister(RegEnum.R4));
                case 2:
                    return regToString(core.getRegister(RegEnum.PC));
                }
            case 1:
                switch(col)
                {
                case 0:
                    return regToString(core.getRegister(RegEnum.R1));
                case 1:
                    return regToString(core.getRegister(RegEnum.R5));
                case 2:
                    Register ir = core.getRegister(RegEnum.IR);
                    int address = (core.getRegister(RegEnum.PC).peek2Bytes()-1) & 0xFFFF;
                    int inst = ir.peek2Bytes() & 0xFFFF;
                    String dis = Disassembler.disassemble(inst, sys, address);
                    return regToString(ir) + "  " + dis;
                }
            case 2:
                switch(col)
                {
                case 0:
                    return regToString(core.getRegister(RegEnum.R2));
                case 1:
                    return regToString(core.getRegister(RegEnum.R6));
                case 2:
                    return regToString(core.getRegister(RegEnum.PSR));
                }
            case 3:
                switch(col)
                {
                case 0:
                    return regToString(core.getRegister(RegEnum.R3));
                case 1:
                    return regToString(core.getRegister(RegEnum.R7));
                case 2:
                    return psrToCC(core.getRegister(RegEnum.PSR));
                }
            }
            return null;
        }
    }
    
    private String regToString(Register r)
    {
        int value = r.peek2Bytes()&0xFFFF;
        return String.format("%-4s  x%04X  %d", r.getName(), value, value);
    }
    private String psrToCC(Register psr)
    {
        int val = psr.peek2Bytes();
        if((val & 0x01) != 0)
        {
            return "CC    P";
        }
        else if((val & 0x02) != 0)
        {
            return "CC    Z";
        }
        return "CC    N";
    }
}

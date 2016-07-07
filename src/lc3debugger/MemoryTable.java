package lc3debugger;

import java.util.Vector;

import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JCheckBox;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.DefaultListSelectionModel;

import lcsimlib.*;
import lcsimlib.gui.AppFont;

public class MemoryTable extends JTable
{
    private LCSystem sys;
    private Debugger debug;
    private String[] columns = {"Breakpoint", "Address", "Binary", "Hex", "Symbol", "Disassembly"};
    private TableModel model;
    public MemoryTable(LCSystem system, Debugger debugger)
    {
        sys = system;
        debug = debugger;
        this.setModel(new MemoryModel());
        this.getColumnModel().getColumn(0).setCellRenderer(new AddressStatusRenderer());
        for(int i = 1; i < 6; i++)
        {
            this.getColumnModel().getColumn(i).setCellRenderer(new StringRenderer());
        }
        //this.getTableHeader().setVisible(false);
        this.setTableHeader(null);
        TableColumnModel cols = this.getColumnModel();
        cols.getColumn(0).setPreferredWidth(5);
        cols.getColumn(0).setMaxWidth(5);
        cols.getColumn(1).setPreferredWidth(60);
        cols.getColumn(1).setMaxWidth(60);
        cols.getColumn(2).setPreferredWidth(160);
        cols.getColumn(2).setMaxWidth(160);
        cols.getColumn(3).setPreferredWidth(60);
        cols.getColumn(3).setMaxWidth(60);
        cols.getColumn(4).setPreferredWidth(30);
        cols.getColumn(5).setPreferredWidth(40);
        for(int i = 0; i < 6; i++)
        {
            cols.getColumn(i).setResizable(false);
        }
        this.setShowGrid(false);
        this.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
    }
    public int getSelectedAddress()
    {
        return this.getSelectedRow();
    }
    
    private class MemoryModel extends AbstractTableModel
    {
        public void MemoryModel()
        {
        }
        public Object getValueAt(int row, int col)
        {
            switch(col)
            {
            case 0:
                int pc = sys.getCore().getRegister(RegEnum.PC).read2Bytes() & 0xFFFF;
                boolean bp = debug.getBP(row).isSet();
                if(row == pc)
                {
                    if(bp)
                        return AddressStatus.BPPC;
                    return AddressStatus.PC;
                }
                return bp?AddressStatus.BP:AddressStatus.CLR;
            case 1:
                return String.format("x%04X", row);
            //Binary
            case 2:
                return String.format("%16s", Integer.toBinaryString(sys.getCore().readMem2Bytes(row)&0xFFFF)).replace(' ', '0');
            //Hex
            case 3:
                return String.format("x%04X", sys.getCore().readMem2Bytes(row));
            //Symbol
            case 4:
                Symbol sym = sys.getSymbol(row);
                return sym==null?null:sym.getName();
            //Disassembly
            case 5:
                return Disassembler.disassemble(sys.getCore().readMem2Bytes(row) & 0xFFFF,sys,row);
            }
            return null;
        }
        public int getRowCount(){return 0x10000;}
        public int getColumnCount(){return columns.length;}
        public String getColumnName(int col){return columns[col];}
        public boolean isCellEditable(int row, int col){return false;}
    }
    
    private class AddressStatusRenderer extends DefaultTableCellRenderer 
    //private class BreakpointRenderer extends JCheckBox implements TableCellRenderer
    {
        public AddressStatusRenderer() 
        {
            super();
        }
        public void setValue(Object value)
        {
            this.setFont(AppFont.getMonoFont());
            AddressStatus val = (AddressStatus)value;
            switch(val)
            {
            case CLR:
                setForeground(Color.GRAY);
                setBackground(Color.WHITE);
                //setText("-");
                setText(" ");
                break;
            case BP:
                setForeground(Color.RED);
                setBackground(Color.RED);
                //setText("+");
                setText(" ");
                break;
            case PC:
                setForeground(Color.BLUE);
                setBackground(Color.BLUE);
                //setText(">");
                setText(" ");
                break;
            case BPPC:
                setForeground(Color.BLUE);
                //setBackground(new Color(255,0,255));
                setBackground(new Color(Color.RED.getRGB() + Color.BLUE.getRGB()));
                //setText(">");
                setText(" ");
                break;
            }
        }
    }
    
}

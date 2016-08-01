package lc3debugger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lcsimlib.Core;
import lcsimlib.RegEnum;
import lcsimlib.Register;

public class ChangeFrame
{
    
    private static final String[] startingLocs = {"","x","R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "PC", "PSR", "IR", "CC"};
    
    private Vector<String> locs;
    private Core core;
    private JDialog frame;
    private JComboBox locBox;
    private JTextField valueField;
    
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    private JButton applyButton;
    
    public ChangeFrame(Core core)
    {
        frame = new JDialog();
        frame.setTitle("Set value...");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        
        this.core = core;
        locs = new Vector<String>(startingLocs.length);
        for( int i = 0; i < startingLocs.length; i++ )
        {
            locs.add(startingLocs[i]);
        }
        locBox = new JComboBox(locs);
        locBox.setVisible(true);
        locBox.setEditable(true);
        frame.add(locBox);
        
        valueField = new JTextField();
        valueField.setVisible(true);
        frame.add(valueField);
        
        buttonBar = new JPanel();
        buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.X_AXIS));
        frame.add(buttonBar);
        
        okButton = new JButton("OK");
        okButton.setVisible(true);
        okButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(applyValue())
                {
                    frame.setVisible(false);
                }
                else
                {
                }
            }
        });
        buttonBar.add(okButton);
        
        
        cancelButton = new JButton("Cancel");
        cancelButton.setVisible(true);
        cancelButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.setVisible(false);
            }
        });
        buttonBar.add(cancelButton);
        
        
        applyButton = new JButton("Apply");
        applyButton.setVisible(true);
        applyButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                applyValue();
            }
        });
        buttonBar.add(applyButton);
        
        frame.pack();
        
    }
    
    public void open(String identifier)
    {
        frame.setVisible(true);
        locBox.setSelectedItem(identifier);
        valueField.setText(getValue(identifier));
    }
    
    private boolean applyValue()
    {
        Boolean[] valStatus = {false};
        Boolean[] idStatus = {false};
        setValue( (String) locBox.getSelectedItem(), valueField.getText(), idStatus, valStatus);
        if( !valStatus[0] || !idStatus[0] )
        {
            return false;
        }
        return true;
    }
    
    String getValue(String identifier)
    {
        String id = identifier.toLowerCase();
        switch(id)
        {
        case "r0":
            return String.format("x%04x", core.getRegister(RegEnum.R0).read2Bytes());
        case "r1":
            return String.format("x%04x", core.getRegister(RegEnum.R1).read2Bytes());
        case "r2":
            return String.format("x%04X", core.getRegister(RegEnum.R2).read2Bytes());
        case "r3":
            return String.format("x%04X", core.getRegister(RegEnum.R3).read2Bytes());
        case "r4":
            return String.format("x%04X", core.getRegister(RegEnum.R4).read2Bytes());
        case "r5":
            return String.format("x%04X", core.getRegister(RegEnum.R5).read2Bytes());
        case "r6":
            return String.format("x%04X", core.getRegister(RegEnum.R6).read2Bytes());
        case "r7":
            return String.format("x%04X", core.getRegister(RegEnum.R7).read2Bytes());
        case "pc":
            return String.format("x%04X", core.getRegister(RegEnum.PC).read2Bytes());
        case "ir":
            return String.format("x%04X", core.getRegister(RegEnum.IR).read2Bytes());
        case "psr":
            return String.format("x%04X", core.getRegister(RegEnum.PSR).read2Bytes());
        case "cc":
            return psrToCC(core.getRegister(RegEnum.PSR));   
        }
        
        int addr = 0;
        int xPos = id.indexOf('x');
        try
        {
            if(xPos != -1)  // if we have no 'x', then it's likely not a hex address
            {
                addr = Integer.parseInt(id.substring(xPos+1), 16);
                return String.format("x%04X",core.readMem2Bytes(addr));
            }
            else
            {
                addr = Integer.parseInt(id);
                return String.format("%d", core.readMem2Bytes(addr));
            }
        }
        catch (NumberFormatException nfe)
        {
            return "???";
        }
    }
    
    // pass by reference
    void setValue(String identifier, String value, Boolean[] idStatus, Boolean[] valStatus)
    {
        int val = 0;
        String id = identifier.toLowerCase();
        idStatus[0] = true;
        valStatus[0] = true;
        
        if(id.equals("cc"))
        {
            value = value.toLowerCase();
            Register psr = core.getRegister(RegEnum.PSR);
            int psrVal = psr.peek2Bytes() & 0xFFFF;
            switch(value)
            {
            case "n":
                psr.set2Bytes( (short)((psrVal | 0x4) & 0xFFFF));
                return;
            case "z":
                psr.set2Bytes( (short)((psrVal | 0x2) & 0xFFFF));
                return;
            case "p":
                psr.set2Bytes( (short)((psrVal | 0x1) & 0xFFFF));
                return;
            default:
                valStatus[0] = false;
                return;
            }
        }
        Boolean status = true;
        val = stringToInt(value, status);
        if(!status)
        {
            valStatus[0] = false;
            return;
        }
        // else, other registers
        switch(id)
        {
        case "r0":
            core.getRegister(RegEnum.R0).set2Bytes( (short) (val & 0xFFFF) );
            break;
        case "r1":
            core.getRegister(RegEnum.R1).set2Bytes( (short) (val & 0xFFFF) );
            break;
        case "r2":
            core.getRegister(RegEnum.R2).set2Bytes( (short) (val & 0xFFFF) );
            break;
        case "r3":
            core.getRegister(RegEnum.R3).set2Bytes( (short) (val & 0xFFFF) );
            break;
        case "r4":
            core.getRegister(RegEnum.R4).set2Bytes( (short) (val & 0xFFFF) );
            break;
        case "r5":
            core.getRegister(RegEnum.R5).set2Bytes( (short) (val & 0xFFFF) );
            break;
        case "r6":
            core.getRegister(RegEnum.R6).set2Bytes( (short) (val & 0xFFFF) );
            break;
        case "r7":
            core.getRegister(RegEnum.R7).set2Bytes( (short) (val & 0xFFFF) );
            break;
        case "pc":
            core.getRegister(RegEnum.PC).set2Bytes( (short) (val & 0xFFFF) );
            break;
        case "ir":
            core.getRegister(RegEnum.IR).set2Bytes( (short) (val & 0xFFFF) );
            break;
        case "psr":
            core.getRegister(RegEnum.PSR).set2Bytes( (short) (val & 0xFFFF) );
            break;
        }
        int addr = stringToInt(id,status);
        if(!status)
        {
            idStatus[0] = false;
            return;
        }
        core.writeMem2Bytes(addr, (short) (val & 0xFFFF));
        

    }
    
    int stringToInt(String value, Boolean status)
    {
        value = value.toLowerCase();
        int xPos = value.indexOf('x');
        status = true;
        try
        {
            if(xPos != -1)  // if we have no 'x', then it's likely not a hex address
            {
                return Integer.parseInt(value.substring(xPos+1), 16);
            }
            else
            {
                return Integer.parseInt(value);
            }
        }
        catch (NumberFormatException nfe)
        {
            status = false;
            return 0;
        }
    }
    
    
    private String psrToCC(Register psr)
    {
        int val = psr.peek2Bytes();
        if((val & 0x01) != 0)
        {
            return "P";
        }
        else if((val & 0x02) != 0)
        {
            return "Z";
        }
        return "N";
    }
}

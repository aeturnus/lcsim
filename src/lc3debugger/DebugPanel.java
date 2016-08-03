package lc3debugger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import java.text.NumberFormat;

import lcsimlib.*;
import lcsimlib.gui.RegisterLabel;

public class DebugPanel extends JPanel
{
    LCSystem sys;
    Debugger debug;
    
    DebuggerToolbar toolBar;
    RegisterTable regTable;
    JScrollPane memScroll;
    MemoryTable memTable;
    ChangeFrame changeFrame;
    
    Thread periodicThread;
    Thread runningThread;
    Thread updateThread;
    Thread toolbarThread;
    boolean runUpdate;
    
    public DebugPanel(LCSystem system, Debugger debugger)
    {
        sys = system;
        debug = debugger;
        sys.stopRunning();
        
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        
        toolBar = new DebuggerToolbar();
        
        changeFrame = new ChangeFrame(this.sys.getCore());
        
        this.add(toolBar);
        
        regTable = new RegisterTable(system, debug);
        this.add(regTable);
        regTable.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                handleRegSelect(e);
            }
        });
        
        memScroll = new JScrollPane();
        memTable = new MemoryTable(system, debug);
        memScroll.setViewportView(memTable);
        this.add(memScroll);
        memTable.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                handleMemSelect(e);
            }
        });
        
        
        
        int pc = sys.getCore().getRegister(RegEnum.PC).read2Bytes() & 0xFFFF;
        gotoAddr(pc);
        toolBar.getGotoField().initLocation( String.format("x%04X", pc) );
        
        toolBar.getButtonRun().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                sys.startRunning();
                resumeExecution();
            }
        });
        
        toolBar.getButtonStop().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                sys.stopRunning();
                pauseExecution();
            }
        });
        
        toolBar.getButtonStepIn().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                sys.cycle();
                pauseExecution();
                updateElements();
            }
        });
        
        toolBar.getGotoField().getButton().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                GotoField gotoField = toolBar.getGotoField();
                String loc = gotoField.getFieldText();
                Boolean[] status = {false};
                int addr = LC3Info.stringToInt(loc, status);
                if(status[0])
                {
                    if(LC3Info.validateAddress(addr))
                    {
                        gotoAddr(addr);
                        gotoField.addLocation(loc);
                    }
                    else
                    {
                        gotoField.setFieldText(loc + "is not in the addresss space!");
                    }
                }
                else
                {
                    gotoField.setFieldText(loc + " is an invalid location!");
                }
            }
        });
        
        //Create thread to handle running
        runningThread = new Thread(new Runnable(){
            public void run(){
                try
                {
                    while(true)
                    {
                        if(debug.memBP[sys.getCore().getRegister(RegEnum.PC).read2Bytes() & 0xFFFF].isSet())
                        {
                            pauseExecution();
                        }
                        sys.cycleCheck();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    return;
                }
            }
        });
        
        periodicThread = new Thread(new Runnable(){
            public void run(){
                try
                {
                    while(runUpdate)
                    {
                        updateElements();
                        Thread.sleep(1000);
                    }
                    return;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    return;
                }
            }
        });
        
        //Create thread to handle updating elements
        updateThread = new Thread(new Runnable(){
            public void run(){
                try
                {
                    while(runUpdate)
                    {
                        if(sys.isRunning()){
                            updateElements();
                            Thread.sleep(100);
                        } else {
                            Thread.sleep(500);
                        }
                    }
                    return;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    return;
                }
            }
        });
        
        //Create thread to handle updating toolbar 
        toolbarThread = new Thread(new Runnable(){
            public void run(){
                try
                {
                    while(runUpdate)
                    {
                        updateToolBar();
                        Thread.sleep(250);
                    }
                    return;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    return;
                }
            }
        });
        
        runUpdate = true;
        
        updateThread.setPriority(Thread.MIN_PRIORITY);
        updateThread.start();
        
        toolbarThread.setPriority(Thread.NORM_PRIORITY);
        toolbarThread.start();
        
        runningThread.setPriority(Thread.MAX_PRIORITY);
        runningThread.start();
        
        periodicThread.start();
        pauseExecution();
        updateElements();
    }
    
    
    private void pauseExecution()
    {
        try
        {
            runningThread.suspend();
            updateThread.suspend();
            update();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private void resumeExecution()
    {
        try
        {
            runningThread.resume();
            updateThread.resume();
            update();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void update()
    {
        updateElements();
        updateToolBar();
    }
    
    private void updateElements()
    {
        /*
        String htmlOpen = "<html>";
        String body = NumberFormat.getIntegerInstance().format(sys.getCycles()) + " cycles executed";
        body += "<br>" + NumberFormat.getIntegerInstance().format(sys.getAverageTime()) + " nsec per cycle";
        body += "<br>" + NumberFormat.getIntegerInstance().format(sys.getRealFrequency()) + " Hz real-time";
        String htmlClose = "</html>";
        */
        regTable.repaint();
        memScroll.repaint();
    }
    
    private void updateToolBar()
    {
        if(sys.isRunning())
        {
            toolBar.getButtonRun().setEnabled(false);
            toolBar.getButtonStop().setEnabled(true);
            toolBar.getButtonStepOver().setEnabled(false);
            toolBar.getButtonStepIn().setEnabled(false);
            toolBar.getButtonStepOut().setEnabled(false);
        }
        else
        {
            toolBar.getButtonRun().setEnabled(true);
            toolBar.getButtonStop().setEnabled(false);
            toolBar.getButtonStepOver().setEnabled(true);
            toolBar.getButtonStepIn().setEnabled(true);
            toolBar.getButtonStepOut().setEnabled(true);
        }
    }
    
    private void gotoAddr(int addr)
    {
        memTable.setRowSelectionInterval(addr, addr);
        memTable.scrollRectToVisible(memTable.getCellRect(addr, 0, true));
    }
    
    private void handleMemSelect(MouseEvent e)
    {
        if(e.getClickCount() == 2)
        {
            int row = memTable.getSelectedRow();
            int col = memTable.getSelectedColumn();
            if(col == 0)
            {
                debug.memBP[row].toggle();
            }
            else
            {
                changeFrame.open(String.format("x%04X", row));
            }
            updateElements();
        }
    }
    
    private void handleRegSelect(MouseEvent e)
    {
        if(e.getClickCount() == 2)
        {
            int row = regTable.getSelectedRow();
            int col = regTable.getSelectedColumn();
            changeFrame.open(regTable.getRegisterId(row, col).toUpperCase());
            
            updateElements();
        }
    }
}

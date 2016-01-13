package lcsimlib.gui;

import javax.swing.JToolBar;
import javax.swing.JButton;

public class DebuggerToolbar extends JToolBar
{
    private JButton run;
    private JButton stop;
    private JButton stepOver;
    private JButton stepIn;
    private JButton stepOut;
    
    public DebuggerToolbar()
    {
        // TODO Auto-generated constructor stub
        run = new JButton("Run");
        stop = new JButton("Stop");
        stepOver = new JButton("Step Over");
        stepIn = new JButton("Step In");
        stepOut = new JButton("Step Out");
        this.add(run);
        this.add(stop);
        this.add(stepOver);
        this.add(stepIn);
        this.add(stepOut);
        this.setName("Debugger toolbar");
    }
    
    public JButton getButtonRun()
    {
        return run;
    }
    
    public JButton getButtonStop()
    {
        return stop;
    }
    public JButton getButtonStepOver()
    {
        return stepOver;
    }
    public JButton getButtonStepIn()
    {
        return stepIn;
    }
    public JButton getButtonStepOut()
    {
        return stepOut;
    }
    

}

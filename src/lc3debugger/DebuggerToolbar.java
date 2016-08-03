package lc3debugger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class DebuggerToolbar extends JToolBar
{
    private JButton run;
    private JButton stop;
    private JButton stepOver;
    private JButton stepIn;
    private JButton stepOut;
    
    private GotoField gotoField;
    
    public DebuggerToolbar()
    {
        // TODO Auto-generated constructor stub
        this.setAlignmentX(LEFT_ALIGNMENT);
        run = new JButton("Run");
        stop = new JButton("Stop");
        stepOver = new JButton("Step Over");
        stepIn = new JButton("Step In");
        stepOut = new JButton("Step Out");
        gotoField = new GotoField();
        this.add(run);
        this.add(stop);
        this.add(stepOver);
        this.add(stepIn);
        this.add(stepOut);
        this.add(gotoField);
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
    
    public GotoField getGotoField()
    {
        return gotoField;
    }
    

}
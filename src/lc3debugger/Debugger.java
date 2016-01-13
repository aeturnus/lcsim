package lc3debugger;

import javax.swing.JPanel;

import lcsimlib.Bits;
import lcsimlib.Breakpoint;
import lcsimlib.LCSystem;

public class Debugger extends lcsimlib.Debugger
{
    LCSystem sys;
    DebugPanel panel;
    Breakpoint[] memBP;
    
    public Debugger()
    {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init(LCSystem system)
    {
        System.out.println("INITIALIZING TOOT TOOT!");
        // TODO Auto-generated method stub
        sys = system;
        
        Breakpoint.init(sys);
        memBP = new Breakpoint[0xFFFF+1];
        for(int i = 0; i <= 0xFFFF; i++)
        {
            memBP[i] = new Breakpoint();
        }
        
        
        panel = new DebugPanel(sys,this);
        System.out.println("Initiated lc3 debugger");
    }

    @Override
    public JPanel getGUI()
    {
        // TODO Auto-generated method stub
        return panel;
    }
    
    public Breakpoint getBP(int address){return memBP[address];}
    

}

package lcsimlib;

import javax.swing.JPanel;

public abstract class Debugger
{

    public Debugger()
    {
        // TODO Auto-generated constructor stub
    }
    
    //Initialize. 
    public abstract void init(LCSystem system);
    
    //Retrieve a GUI element from the debugger
    public abstract JPanel getGUI();
}

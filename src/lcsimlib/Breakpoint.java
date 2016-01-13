package lcsimlib;

import lcsimlib.*;

public class Breakpoint
{
    private static LCSystem sys;
    
    private boolean enabled;
    public static void init(LCSystem system)
    {
        sys = system;
    }
    public Breakpoint()
    {
        enabled = false;
    }
    
    public void set() {enabled = true;}
    public boolean isSet() {return enabled;}
    public void toggle(){enabled = !enabled;}
}

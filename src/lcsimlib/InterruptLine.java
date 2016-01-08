package lcsimlib;

public class InterruptLine 
{
    private boolean isSet;
    private int PL;
    
    public InterruptLine()
    {
        setPL(0);
        clr();
    }
    InterruptLine(int PLset)
    {
        setPL(PLset);
        clr();
    }
    
    //What we're interested in: sets the interrupt line
    public void set()
    {
        isSet = true;
    }
    public void clr()
    {
        isSet = false;
    }
    public boolean check()
    {
        return isSet;
    }
    public void setPL(int PLset)
    {
        PL = PLset;
    }
    public int getPL()
    {
        return PL;
    }
}

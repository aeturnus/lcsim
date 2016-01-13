package lcsimlib;

/*
 *  The device abstract class serves as the template for all devices
 */

public abstract class Device extends Component
{
    private boolean ownClock;   //Does this device have its own clock? If it does, it'll need to handle its own threads
    
    abstract public void init(LCSystem system);
    abstract protected void cycleInternal();
    
    public boolean hasOwnClock(){return ownClock;};
}

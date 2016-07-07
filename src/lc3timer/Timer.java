package lc3timer;

import lcsimlib.Device;
import lcsimlib.InterruptLine;
import lcsimlib.LCSystem;
import lcsimlib.Register;

/**
 * This class provides a timer device for the LC3
 * This is a "fancy" timer where you provide how
 * many 10usec you want between interrupts.
 * This timer will not pre-empt itself: you must 
 * 
 * Timer Status reg (TSR): Enable[15], InterruptFlag[14]
 * Timer Delay reg (TDR): Delay[14:0] => 15 bits of delay => 32,767 * 10usec = 327.67 msec maximum delay
 * 
 * @author brandon
 *
 */
public class Timer extends Device
{
    private static final int TSR_ADDR = 0xFE08;
    private static final int TDR_ADDR = 0xFE0A;
    private static final int INT_VECTOR = 0x00;
    private static final int PRIORITY = 0b111;
    private static final int MULTIPLE_USEC = 10;     // multiple of usec
    
    private Register tsr;
    private Register tdr;
    private InterruptLine intLine;
    
    private long lastTime;
    
    public Timer()
    {
    }
    
    public void init(LCSystem system)
    {
        // setup
        tsr = system.getCore().getIORegister(TSR_ADDR);
        tdr = system.getCore().getIORegister(TDR_ADDR);
        intLine = system.getCore().getIntLine(INT_VECTOR);
        intLine.setPL(PRIORITY);
        
        // get our initial t
        lastTime = System.nanoTime();
    }
    
    private long toNano(short tdrValue)
    {
        long value = tdrValue & 0x7FFF;
        return value * 1000 * MULTIPLE_USEC;
    }
    
    public void cycleInternal()
    {
        short tsrValue = tsr.peek2Bytes();
        // Do stuff if enabled and interrupt flag is cleared
        if( ((tsrValue & 0x8000) != 0)  && ((tsrValue & 0x4000) == 0))
        {
            long currentTime = System.nanoTime();
            long delta = currentTime - lastTime;
            
            // check if enough time has elapsed
            if( delta >= toNano(tdr.read2Bytes()) )
            {
                // if it has enough, mark this time as new goalpost
                lastTime = currentTime;
                // set the interrupt flag
                tsr.write2Bytes( (short) ((tsrValue | 0x4000) & 0xFFFF));
                // set the int line
                intLine.set();
            }
        }
    }
    
    public void openConfig()
    {
    }
    
    public void show()
    {
    }
}

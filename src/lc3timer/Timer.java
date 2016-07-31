package lc3timer;

import lcsimlib.Device;
import lcsimlib.InterruptLine;
import lcsimlib.LCSystem;
import lcsimlib.Register;

/**
 * This class provides a timer device for the LC3
 * This is a "dumb" countdown timer where you provide how
 * many cycles you want
 * This timer will not pre-empt itself: you must acknowledge it manually
 *
 * The multiplier allows you to have even larger delays: it serves as how many cycles to run before counting down.
 * Whenever the current register is written to, it is reloaded
 * 
 * Timer Status reg (TSR): Enable[15], InterruptFlag[14], Repeat [13], Multiplier [11:0]
 * Timer Current reg (TCR): Delay[15:0]
 * Timer Delay reg (TDR): Delay[15:0] => 16 bits of delay => 65,356 delay * 4096 multiplier
 * 
 * @author brandon
 *
 */
public class Timer extends Device
{
    private static final int TSR_ADDR = 0xFE08;
    private static final int TDR_ADDR = 0xFE0A;
    private static final int TCR_ADDR = 0xFE0C;
    private static final int INT_VECTOR = 0x00;
    private static final int PRIORITY = 0b111;
    private static final int MUL_MASK = 0x1FFF;
    private static final int ENB_MASK = 0x8000;
    private static final int INT_MASK = 0x4000;
    private static final int REP_MASK = 0x2000;
    
    private Register tsr = null;
    private Register tdr = null;
    private Register tcr = null;
    private InterruptLine intLine = null;
    
    private int multiple = 0;
    
    public Timer()
    {
    }
    
    public void init(LCSystem system)
    {
        // setup
        tsr = system.getCore().getIORegister(TSR_ADDR);
        tdr = system.getCore().getIORegister(TDR_ADDR);
        tcr = system.getCore().getIORegister(TCR_ADDR);
        intLine = system.getCore().getIntLine(INT_VECTOR);
        intLine.setPL(PRIORITY);
    }
    
    public void cycleInternal()
    {
        short tsrValue = tsr.peek2Bytes();
        // check if tsr has been written to: update multiple
        if(tsr.writ())
        {
            multiple = tsrValue & MUL_MASK;
            tsr.writAck();
        }
        // check if the tcr has been written to: if so, set to delay
        if(tcr.writ())
        {
            tcr.set2Bytes(tdr.peek2Bytes());
            tcr.writAck();
        }
        // check if the tdr has been written to: if so, set to delay
        if(tdr.writ())
        {
            tcr.set2Bytes(tdr.peek2Bytes());
            tdr.writAck();
        }
        // Do stuff if enabled and interrupt flag is cleared and the multiplier is > 0
        if( ((tsrValue & ENB_MASK) != 0)  && ((tsrValue & INT_MASK) == 0) && (tsrValue & MUL_MASK) != 0)
        {
            short tcrValue = tcr.read2Bytes();
            multiple--; // decrement the multiple
            // multiple done
            if(multiple <= 0)
            {
                multiple = tsrValue & MUL_MASK;
                tcrValue--;
            }
            // if we hit 0
            if(tcrValue <= 0)
            {
                // set the interrupt flag
                tsrValue = (short)((tsrValue | INT_MASK) & 0xFFFF);
                // set the interrupt line
                if((tsrValue & REP_MASK) == 0)
                {
                    // if not enabled, disable the timer
                    tsrValue = (short)((tsrValue & ~ENB_MASK) & 0xFFFF);
                }
                else
                {
                    // reload our current
                    tcrValue = tdr.peek2Bytes();
                }
                intLine.set();
                tsr.set2Bytes(tsrValue);
            }
            tcr.set2Bytes(tcrValue);
        }
    }
    
    public void openConfig()
    {
    }
    
    public void show()
    {
    }
}

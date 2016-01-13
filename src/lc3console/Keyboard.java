package lc3console;

import lcsimlib.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Keyboard extends Device implements KeyListener
{
    private Register kbsr;
    private Register kbdr;
    private InterruptLine intLine;
    
    private char currentChar;    //The current char to be put into the KBDR
    private boolean critical;
    private boolean readyForInput;
    private boolean inputEntered;
    
    private int DELAY = 10;    //Number of cycles to wait between keyboard processing
    private int delay = 0;
    
    public Keyboard()
    {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init(LCSystem system)
    {
        // TODO Auto-generated method stub
        kbsr = system.getCore().getIORegister(0xFE00);       //Get keyboard io regs
        kbdr = system.getCore().getIORegister(0xFE02);
        //kbdr.read2Bytes();                              //Force it to be read for first keystroke
        intLine = system.getCore().getIntLine(0x80);         //Set up keyboard interrupt line
        intLine.setPL(4);                               //Set priority of 4
        
        critical = false;
        readyForInput = true;
        inputEntered = false;
        currentChar = 0x00;
    }

    @Override
    public void cycleInternal()
    {
        // TODO Auto-generated method stub
        //TODO: Get the logic right
        //Bit 15 of the kbsr tells the lc3 that a character has been receive and is ready for reading
        //Reading from the register clears bit 15
        
        //If it's been read, put in the latest typed char
        
        //Delay is here because we want to spend more time not in a critical section
        if(kbdr.read())
        {
            delay = 0;
        }
        if(delay>0)
        {
            delay--;
        }
        else
        {
            startCritical();
            
            //If data has been read, show that the character isn't fresh anymore
            if(kbdr.read())
            {
                clrFresh();
                readyForInput = true;   //We are ready for character input now
                kbdr.readAck();
            }
            if(inputEntered)
            {
                setFresh();
                writeData(currentChar);
                fireInterrupt();
                inputEntered = false;
            }
            
            delay = DELAY;
            
            endCritical();
        }
    }
    

    @Override
    public void openConfig()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void show()
    {
        // TODO Auto-generated method stub

    }
    
    //Wrap it method calls
    private void clrFresh()
    {
        kbsr.write2Bytes((short)(kbsr.read2Bytes()&(short)0x7FFF));     //Clear the ready bit
    }
    private void setFresh()
    {
        kbsr.write2Bytes((short)(kbsr.read2Bytes() | (short)0x8000));    //Set the ready bit
    }
    private void writeData(char data)
    {
        kbdr.write1Bytes((byte)data);    //Write to data
    }
    private void fireInterrupt()
    {
        if((kbsr.read2Bytes() & 0x4000)!=0) //If the interrupt enable bit is set, fire an interrupt
        {
            intLine.set();
        }
    }
    private void startCritical()
    {
        critical = true;
    }
    private void endCritical()
    {
        critical = false;
    }
    /////Other methods/////
    public void keyPressed(KeyEvent e)
    {
        
    }
    
    public void keyReleased(KeyEvent e)
    {
        
    }
    
    public void keyTyped(KeyEvent e)
    {
        //Only set the char when there's no new character
        if(!critical)
        {
            if(readyForInput)
            {
                currentChar = e.getKeyChar();
                inputEntered = true;
            }
            //System.out.println(e);
        }
    }
}
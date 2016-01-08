package lc3console;

import java.awt.Color;
import javax.swing.JFrame;


//import lcsimlib.Bits;
import lcsimlib.Device;
import lcsimlib.LCSystem;
import lcsimlib.Register;

public class Console extends Device 
{
    //Configurable constants
    private int COLS = 80;
    private int ROWS = 24;
    private int DDELAY = 100;   //100 cycle display delay
   
    private Color bg = Color.WHITE;
    private Color fg = Color.BLACK;
    
    private Register dsr;
    private Register ddr;

    private JFrame frame;
    private Display display;
    
    private Keyboard keyboard;
    
    private int ddelay = 0; //Simulated display delay
    public Console()
    {
        super();
        name = "LC3 Console";
    }
    
    
    
    @Override
    public void init(LCSystem system) 
    {
        name = "LC3 Console";
        ddelay = DDELAY;  //Have a sort of boot delay :D :D :D
        
        dsr = system.core.getIORegister(0xFE04);        //Get display io regs
        ddr = system.core.getIORegister(0xFE06);
        
        keyboard = new Keyboard();  //Keyboard acts as a sub-device under the console
        keyboard.init(system);
        
        createGUI();    //Create gui element
    }

    @Override
    public void cycle() 
    {
        keyboard.cycle();
        
        if(ddelay>0)
        {
           ddelay--;
        }
        else
        {
            dsr.write2Bytes((short)(dsr.read2Bytes() | (short)0x8000));   //Ready it up
        }
        //If the DDR has been written to and display is ready, putchar
        if(ddr.writ() && (dsr.read2Bytes() & (short)0x8000)!=0)
        {
            ddr.writAck();
            putchar((char)(ddr.read1Bytes()));
        }
    }
    
    @Override
    public void show()
    {
        frame.setVisible(true);
    }

    @Override
    public void openConfig()
    {
        
    }
    
    
    ////////////////////////
    //Other methods ////////
    ////////////////////////
    private void createGUI()
    {
        frame = new JFrame("LC3 Console");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        display = new Display(ROWS,COLS);
        display.setVisible(true);
        frame.add(display);
        frame.pack();
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.addKeyListener(keyboard);
        frame.setFocusTraversalKeysEnabled(false);
    }
    
    //Print a character
    public void putchar(char c)
    {
        //When putchar'd, set delay timer
        ddelay = DDELAY;
        dsr.write2Bytes((short)(dsr.read2Bytes() & (short)~0x8000));   //Unready 
        display.putChar(c);
    }
    
    public void setFG(Color color)
    {
        fg = color;
        updateColors();
    }
    public void setBG(Color color)
    {
        bg = color;
        updateColors();
    }
    public void setColors(Color bg, Color fg)
    {
        this.bg = bg;
        this.fg = fg;
        updateColors();
    }
    public Color getFG(){return fg;}
    public Color getBG(){return bg;}
    
    private void updateColors()
    {
        for(int r = 0; r < ROWS; r++)
        {
            for(int c = 0; c < COLS; c++)
            {
                display.setCellColors(bg, fg, r, c);
            }
        }
    }
    
}



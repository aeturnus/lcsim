package lc3console;

import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.GridLayout;

import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;

//Container for cells;
public class Display extends JPanel
{
    private int COLS;
    private int ROWS;
    private int TABSIZE = 4;
    private Cell[][] cells;
    private int curR = 0;
    private int curC = 0;
    
    private int cursorDelay = 500; //Delay for cursor
    private Timer cursorTimer;
    
    private Toolkit beepKit = java.awt.Toolkit.getDefaultToolkit();
    
    private Font cellFont;
    private int cellWidth;
    private int cellHeight;
    private int cellAscent;
    private void construct()
    {
        cells = null;                   //Remove all references
        this.removeAll();
        
        cellFont = new Font("Courier New",Font.PLAIN,12);
        FontMetrics metrics = this.getFontMetrics(cellFont);
        cellWidth = metrics.charWidth(' ');
        cellHeight = metrics.getMaxAscent()+metrics.getMaxDescent();
        cellAscent = metrics.getMaxAscent();
        
        cells = new Cell[ROWS][COLS];   //allocate space for cell container
        Cell newCell;
        for(int r = 0; r < ROWS; r++)   //allocate the actual cells
        {
            for(int c = 0; c < COLS; c++)
            {
                newCell = new Cell(cellFont,cellWidth,cellHeight,cellAscent);
                cells[r][c] = newCell; 
                this.add(newCell);
                newCell.setBounds(r*cellWidth, c*cellHeight, newCell.getWidth(), newCell.getHeight());
                newCell.setVisible(true);
                newCell.repaint();
            }
        }
        curR = 0;
        curC = 0;
        
        
        ActionListener taskPerformer = new ActionListener(){
            public void actionPerformed(ActionEvent evt) {
                cells[curR][curC].toggleCursor();
            }
        };
        cursorTimer = new Timer(cursorDelay,taskPerformer);
        cursorTimer.setRepeats(true);
        cursorTimer.start();
        
    }
    
    public Display(int rows, int cols)
    {
        // TODO Auto-generated constructor stub
        //this.setSize(COLS * Cell.CELLWIDTH, ROWS * Cell.CELLHEIGHT);
        //this.setPreferredSize(new Dimension(COLS * Cell.CELLWIDTH, ROWS * Cell.CELLHEIGHT) );
        ROWS = rows;
        COLS = cols;
        GridLayout layout = new GridLayout(ROWS,COLS);
        layout.setHgap(0);
        layout.setVgap(0);
        this.setLayout(layout);
        construct();
    }

    public Display(LayoutManager arg0)
    {
        super(arg0);
        // TODO Auto-generated constructor stub
        construct();
    }

    public Display(boolean arg0)
    {
        super(arg0);
        // TODO Auto-generated constructor stub
        construct();
    }

    public Display(LayoutManager arg0, boolean arg1)
    {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }
    
    public void setCell(char c, int row, int col)
    {
        Cell cell = cells[row][col];
        cell.setChar(c);
        cell.repaint();
    }
    
    //Don't repaint
    public void setCellQuiet(char c, int row, int col)
    {
        Cell cell = cells[row][col];
        cell.setChar(c);
    }
    
    public void setCellFG(Color color, int row, int col)
    {
        cells[row][col].setFG(color);
        cells[row][col].repaint();
    }
    public void setCellBG(Color color, int row, int col)
    {
        cells[row][col].setBG(color);
        cells[row][col].repaint();
    }
    public void setCellColors(Color bg, Color fg, int row, int col)
    {
        cells[row][col].setBG(bg);
        cells[row][col].setFG(fg);
        cells[row][col].repaint();
    }
    
    public char[] getCellContentsLinear()
    {
        char[] output = new char[ROWS*COLS];
        for(int r = 0; r < ROWS; r++)
        {
            for(int c = 0; c < COLS; c++)
            {
                output[c + r*COLS] = cells[ROWS][COLS].getChar();
            }
        }
        return output;
    }
    
    public void setCursorRow(int row)
    {
        curR = row;
    }
    public void setCursorCol(int col)
    {
        curC = col;
    }
    public void cursorLeft()
    {
        cursorTimer.stop();
        //No wrapping
        if(--curC<0)
        {
            curC = 0;
        }
        cursorTimer.restart();
    }
    public void cursorRight()
    {
        cursorTimer.stop();
        if(++curC >= COLS)
        {
            curC = COLS-1;
        }
        cursorTimer.restart();
    }
    public void cursorUp()
    {
        cursorTimer.stop();
        if(--curR < 0)
        {
            curR = 0;
        }
        cursorTimer.restart();
    }
    public void cursorDown()
    {
        cursorTimer.stop();
        if(++curR < 0)
        {
            curR = ROWS-1;
        }
        cursorTimer.restart();
    }
    
    public int getCursorRow(){return curR;}
    public int getCursorCol(){return curC;}
    
    public void putChar(char c)
    {
        cursorTimer.stop();
        cells[curR][curC].disableCursor();
        //Check if it's a line feed
        if(c == 0x0A)
        {
            if(++curR >= ROWS)
            {
                shiftUp();
                curR = ROWS-1;
            }
            curC = 0;      //Carriage return: makes it easy for us
            cursorTimer.restart();
            cells[curR][curC].enableCursor();
            return;
        }
        
        //Backspace
        if(c == 0x08)
        {
            if(curC > 0)
            {
                curC--;
            }
            else
            {
                //Can backspace at col 0 only if we're not at row 0
                if(curR > 0)
                {
                    curC = COLS-1;
                    curR--;
                }
            }
            cells[curR][curC].setChar(' ');   //Clear it
            cursorTimer.restart();
            cells[curR][curC].enableCursor();
            return;
        }
        
        //Bell
        if(c == 0x07)
        {
            beepKit.beep();    //All for a beep!
            cursorTimer.restart();
            return;
        }
        
        //Tab
        if(c == 0x09)
        {
            if(curC%TABSIZE == 0)
            {
                curC += TABSIZE;
            }
            else
            {
                curC += curC%TABSIZE;
            }
            if(curC >= COLS)
            {
                if(++curR >= ROWS)
                {
                    shiftUp();
                    curR = ROWS-1;
                }
                curC = 0;      //Carriage return: makes it easy for us
            }
            
            cursorTimer.restart();
            cells[curR][curC].enableCursor();
            return;
        }
        
        /*
        //Delete
        if(c == 0x7F)
        {
        }
        */
        
        
        //If not a printable character, replace with a space
        if(!(0x20 <= c && c <= 0x7E) && !(0xA1 <= c))
        {
            c = ' ';
        }
        
        cells[curR][curC].setChar(c);
        if( (++curC) >= COLS)
        {
            curR++;
            curC = 0;
        }
        if(curR >= ROWS)
        {
            shiftUp();
            curR = ROWS-1;
        }
        cells[curR][curC].enableCursor();
        cursorTimer.restart();
    }
    private void shiftUp()
    {
        for(int r = 0; r < ROWS-1; r++)
        {
            for(int c = 0; c < COLS; c++)
            {
                this.setCellQuiet(cells[r+1][c].getChar(), r, c);
            }
        }
        for(int c = 0; c < COLS; c++)
        {
            this.setCellQuiet(' ',ROWS-1,c);
        }
        repaint();  //Roll up all the repaints in one go
    }
}

package lc3console;

import java.awt.LayoutManager;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Dimension;

public class Cell extends JPanel
{
    private int cellWidth;
    private int cellHeight;
    private int cellAscent; //For baseline placement
    
    private Color bg = Color.WHITE;
    private Color fg = Color.BLACK;
    private char character = ' ';
    private Font cellfont;
    private boolean cursor = false;
    private void construct()
    {
        //this.setSize(CELLWIDTH,CELLHEIGHT);
        this.setPreferredSize(new Dimension(cellWidth,cellHeight));
    }
    public Cell()
    {
        // TODO Auto-generated constructor stub
        construct();
        cellWidth = 5;
        cellHeight = 8;
        cellAscent = 8;
        cellfont = new Font("Courier New",Font.PLAIN,12);
    }
    public Cell(Font setFont, int w, int h, int a)
    {
        // TODO Auto-generated constructor stub
        cellfont = setFont;
        cellWidth = w;
        cellHeight = h;
        cellAscent = a;
        construct();
    }

    public Cell(LayoutManager arg0)
    {
        super(arg0);
        // TODO Auto-generated constructor stub
        construct();
    }

    public Cell(boolean arg0)
    {
        //super(arg0);
        // TODO Auto-generated constructor stub
        construct();
    }

    public Cell(LayoutManager arg0, boolean arg1)
    {
        //super(arg0, arg1);
        // TODO Auto-generated constructor stub
        construct();
    }
    
    protected void paintComponent(Graphics g)
    {
        //I could've set locals to define the colors in this particular call, but I'm a performance whore
        if(cursor)
        {
            g.setColor(fg);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(bg);
            // g.drawString("Hello world", 0, 0);
            g.setFont(cellfont);
            //g.drawString(String.valueOf(character), 0, 0);
            char[] temp = {character};
            g.drawChars(temp, 0, 1, 0, cellAscent);

        }
        else
        {
            g.setColor(bg);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(fg);
            // g.drawString("Hello world", 0, 0);
            g.setFont(cellfont);
            //g.drawString(String.valueOf(character), 0, 0);
            char[] temp = {character};
            g.drawChars(temp, 0, 1, 0, cellAscent);
        }
        
    }
    
    
    
    //Need to call repaint outside
    public void setChar(char c)
    {
        character = c;
    }
    public void setFG(Color color)
    {
        fg = color;
    }
    public void setBG(Color color)
    {
        bg = color;
    }
    public char getChar(){return character;}
    public Color getFG(){return fg;}
    public Color getBG(){return bg;}

    public void enableCursor()
    {
        cursor = true;
        repaint();
    }
    public void disableCursor()
    {
        cursor = false;
        repaint();
    }
    public void toggleCursor()
    {
        cursor = !cursor;
        repaint();
    }
    public boolean cursorStatus(){return cursor;}
    
    public void setCellFont(Font font)
    {
        this.cellfont = font;
    }
    public Font getCellFont()
    {
        return cellfont;
    }
    

}

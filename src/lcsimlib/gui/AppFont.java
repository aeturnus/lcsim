package lcsimlib.gui;

import java.awt.Font;

public class AppFont
{
    private static Font monoFont = new Font("Monospaced", Font.PLAIN, 12);
    private static Font monoFontBold = new Font("Monospaced", Font.BOLD, 12);
    private static Font monoFontItalic = new Font("Monospaced", Font.ITALIC, 12);
    
    private static Font sansFont = new Font("SansSerif", Font.PLAIN, 12);
    private static Font sansFontBold = new Font("SansSerif", Font.BOLD, 12);
    private static Font sansFontItalic = new Font("SansSerif", Font.ITALIC, 12);
    
    private static Font serifFont = new Font("Serif", Font.PLAIN, 12);
    private static Font serifFontBold = new Font("Serif", Font.BOLD, 12);
    private static Font serifFontItalic = new Font("Serif", Font.ITALIC, 12);
    
    public static Font getMonoFont()
    {
        return monoFont;
    }
    public static Font getMonoFontBold()
    {
        return monoFontBold;
    }
    public static Font getMonoFontItalic()
    {
        return monoFontItalic;
    }
    
    public static Font getSansFont()
    {
        return sansFont;
    }
    public static Font getSansFontBold()
    {
        return sansFontBold;
    }
    public static Font getSansFontItalic()
    {
        return sansFontItalic;
    }
    
    public static Font getSerifFont()
    {
        return serifFont;
    }
    public static Font getSerifFontBold()
    {
        return serifFontBold;
    }
    public static Font getSerifFontItalic()
    {
        return serifFontItalic;
    }
    
    
}

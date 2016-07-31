package lc3debugger;

import javax.swing.table.DefaultTableCellRenderer;
import lcsimlib.*;
import lcsimlib.gui.AppFont;

public class StringRenderer extends DefaultTableCellRenderer
{

    public StringRenderer()
    {
        // TODO Auto-generated constructor stub
    }
    public void setValue(Object value)
    {
        this.setFont(AppFont.getMonoFont());
        String val = (String)value;
        if(val == null)
        {
            setText("");
        }
        else
        {
            setText(val);
        }
    }

}

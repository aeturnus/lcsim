package lc3debugger;

import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class GotoField extends JPanel
{
    private JButton gotoButton;
    private JComboBox gotoBox;
    
    private Vector<String> locVector;
    
    public GotoField()
    {
        locVector = new Vector<String>();
        
        gotoButton = new JButton( "Go to location: " );
        gotoButton.setVisible(true);
        gotoBox = new JComboBox( locVector );
        gotoBox.setEditable(true);
        gotoBox.setVisible(true);
        
        this.setLayout( new BoxLayout(this, BoxLayout.X_AXIS) );
        this.add( gotoButton );
        this.add( gotoBox );
        this.setVisible(true);
    }
    
    public JButton getButton()
    {
        return gotoButton;
    }
    
    public void setFieldText(String text)
    {
        //gotoBox.setSelectedItem(text);
        //gotoBox.setSelectedItem(text);
        gotoBox.getEditor().setItem(text);
    }
    
    public String getFieldText()
    {
        return (String) gotoBox.getSelectedItem();
    }
    
    public void initLocation( String loc )
    {
        locVector.add(loc);
        gotoBox.setSelectedIndex(0);
    }
    
    public void addLocation( String loc )
    {
        locVector.add(loc);
    }
}

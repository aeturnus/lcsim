package lcsim.pkg.gui;

import javax.swing.ListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import java.awt.Component;

import lcsim.pkg.Package;

public class PackageListCellRenderer extends JCheckBox implements ListCellRenderer<Package>
{
    public PackageListCellRenderer()
    {
        // TODO Auto-generated constructor stub
        this.setOpaque(true);
        this.setHorizontalAlignment(LEFT);
        this.setVerticalAlignment(CENTER);
        
        this.setSelected(false);
        this.setHorizontalTextPosition(RIGHT);
    }
    
    public Component getListCellRendererComponent(JList<? extends Package> list, Package pkg, int index, boolean isSelected, boolean cellHasFocus)
    {
        if(isSelected)
        {
            this.setBackground(list.getSelectionBackground());
            this.setForeground(list.getSelectionForeground());
        }
        else
        {
            this.setBackground(list.getBackground());
            this.setForeground(list.getForeground());
        }
        this.setText(pkg.getName());
        this.setSelected(pkg.isLoaded());
        return this;
    }
    
}

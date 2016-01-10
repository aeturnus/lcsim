package lcsim.pkg.gui;

import lcsim.pkg.*;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Dimension;

import lcsim.pkg.Package;

public class PackageManagerWindow extends JFrame
{
    PackageManager pacman;
    JScrollPane selectorScroll;
    PackageSelector selector;
    PackageDisplay display;
    ListSelectionListener selectorListener;
    
    JSplitPane splitPane;
    
    public PackageManagerWindow(PackageManager packageManager) throws HeadlessException
    {
        pacman = packageManager;
        this.setTitle("Package Manager");
        
        
        //selectorListener = new ListSelectionListener(){public void valueChanged(ListSelectionEvent e){handleSelectorSelection(e);}};
        selector = new PackageSelector(this);
        selector.setVisible(true);
        selector.setPreferredSize(new Dimension(200,400));
        display = new PackageDisplay(this);
        display.setPreferredSize(new Dimension(400,400));
        display.setVisible(true);
        
        selectorScroll = new JScrollPane();
        selectorScroll.setViewportView(selector);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,selectorScroll,display);
        
        
        this.add(splitPane);
        
        this.pack();
        this.setResizable(false);
    }
    
    public void handleSelectorSelection()
    {
        display.displayPackage(selector.getSelectedPackage());
    }
    
    public void update()
    {
        selector.update();
    }
    
    public PackageManager getPackageManager()
    {
        return pacman;
    }
    
    

}

package lcsim.pkg.gui;

import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import lcsim.pkg.Package;
import lcsim.pkg.*;

public class PackageDisplay extends JPanel implements ActionListener
{
    private PackageManagerWindow parentWindow;
    
    private Package pkg;
    
    JLabel infoLabel;
    JButton loadButton;
    
    
    public PackageDisplay(PackageManagerWindow parentWindow)
    {
        this.parentWindow = parentWindow;
        
        //this.setLayout(new GridLayout(2,0));
        //this.setLayout(new FlowLayout());
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        
        infoLabel = new JLabel();
        infoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        infoLabel.setVerticalAlignment(SwingConstants.TOP);
        infoLabel.setVisible(true);
        
        loadButton = new JButton("Load package");
        loadButton.addActionListener(this);
        loadButton.setVisible(false);
        
        this.add(infoLabel);
        this.add(loadButton);
        
        displayPackage(null);
    }
    
    public void displayPackage(Package pack)
    {
        if(pack == null)
        {
            infoLabel.setVisible(false);
            loadButton.setVisible(false);
        }
        else
        {
            if(pkg != pack)
            {
                pkg = pack;
                String htmlOpen = "<html><body style='width: 300 px'>";
                String htmlClose = "</html>";
                String text = "Name: " + pkg.getName()
                            + "<br>Author: " + pkg.getAuthor()
                            + "<br>Version: " + pkg.getVersion()
                            + "<br>Classname: " + pkg.getClassName()
                            + "<br><br>" + pkg.getDescription();
                infoLabel.setText(htmlOpen + text + htmlClose);
            }
            infoLabel.setVisible(true);
            if(pkg.isLoaded())
            {
                loadButton.setVisible(false);
            }
            else
            {
                loadButton.setVisible(true);
            }
        }
    }
    
    public void actionPerformed(ActionEvent e)
    {
        parentWindow.getPackageManager().loadPackage(pkg);
        parentWindow.update();
    }
    
    
}

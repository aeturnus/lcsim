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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

import lcsim.pkg.Package;
import lcsim.pkg.*;

public class PackageDisplay extends JPanel implements ActionListener
{
    private PackageManagerWindow parentWindow;
    
    private Package pkg;
    
    JLabel infoLabel;
    JButton loadButton;
    JLabel extraLabel;
    
    
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
        
        extraLabel = new JLabel();
        infoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        infoLabel.setVerticalAlignment(SwingConstants.TOP);
        infoLabel.setVisible(true);
        
        this.add(infoLabel);
        this.add(extraLabel);
        this.add(loadButton);
        
        this.addComponentListener( new ComponentAdapter() {
            public void componentResized(ComponentEvent e){
                //updateText();
            }
        });
        
        displayPackage(null);
    }
    
    public void displayPackage(Package pack)
    {
        if(pack == null)
        {
            infoLabel.setVisible(false);
            loadButton.setVisible(false);
            extraLabel.setVisible(false);
        }
        else
        {
            if(pkg != pack)
            {
                pkg = pack;
                updateText();
            }
            infoLabel.setVisible(true);
            extraLabel.setVisible(true);
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
    
    private void updateText()
    {
        if(pkg == null)
            return;
        String htmlOpen = "<html><body style='width: " + this.getWidth() + " px'>";
        String htmlClose = "</html>";
        String text = "Name: " + pkg.getName()
                    + "<br>Author: " + pkg.getAuthor()
                    + "<br>Version: " + pkg.getVersion()
                    + "<br>Classname: " + pkg.getClassName()
                    + "<br><hr>" + pkg.getDescription()
                    + "<hr>";
        infoLabel.setText(htmlOpen + text + htmlClose);
        
        switch(pkg.getType())
        {
        case CORE:
            displayCore();
            break;
        case DEVICE:
            displayDevice();
            break;
        case CODELOADER:
            displayLoader();
            break;
        case DEBUGGER:
            displayDebugger();
            break;
        }
    }
    
    private void displayCore()
    {
        CorePackage pack = (CorePackage) pkg;
        String htmlOpen = "<html><body style='width: " + this.getWidth() + " px'>";
        String htmlClose = "<hr></html>";
        String text = "Address Low :" + String.format("0x%08X",pack.getAddressLow())
                + "<br>Address High:" + String.format("0x%08X",pack.getAddressHigh())
                + "<br>Addressabilities: [ ";
        for(int i = 0; i < pack.getAddressabilities().length-1; i++)
        {
            text += pack.getAddressabilities()[i] + ", ";
        }
        if(pack.getAddressabilities().length != 0)
            text += pack.getAddressabilities()[pack.getAddressabilities().length-1];
        text += " ]";
        text += "<br>Word Size: " + pack.getWordSize();
        extraLabel.setText(htmlOpen + text + htmlClose);
    }
    private void displayDevice()
    {
        DevicePackage pack = (DevicePackage) pkg;
        String htmlOpen = "<html><body style='width: " + this.getWidth() + " px'>";
        String htmlClose = "<hr></html>";
        String text = "IO Registers: [ ";
        for(int i = 0; i < pack.getIORegisters().length-1; i++)
        {
            text += String.format("0x%08X, ",pack.getIORegisters()[i]);
        }
        if(pack.getIORegisters().length != 0)
            text += String.format("0x%08X",pack.getIORegisters()[pack.getIORegisters().length-1]);
        text += " ]";
        text += "<br>Interrupt Vectors: [ ";
        for(int i = 0; i < pack.getInterruptVectors().length-1; i++)
        {
            text += String.format("0x%08X, ",pack.getInterruptVectors()[i]);
        }
        if(pack.getInterruptVectors().length != 0)
            text += String.format("0x%08X",pack.getInterruptVectors()[pack.getInterruptVectors().length-1]);
        text += " ]";
        
        extraLabel.setText(htmlOpen + text + htmlClose);
    }
    private void displayLoader()
    {
        CodeLoaderPackage pack = (CodeLoaderPackage) pkg;
        String htmlOpen = "<html><body style='width: " + this.getWidth() + " px'>";
        String htmlClose = "<hr></html>";
        String text = "File Extensions: [ ";
        for(int i = 0; i < pack.getExtensions().length-1; i++)
        {
            text += pack.getExtensions()[i] + ", ";
        }
        if(pack.getExtensions().length != 0)
            text += pack.getExtensions()[pack.getExtensions().length-1];
        text += " ]";
        
        extraLabel.setText(htmlOpen + text + htmlClose);
    }
    private void displayDebugger()
    {
    }
    
    
    public void actionPerformed(ActionEvent e)
    {
        parentWindow.getPackageManager().loadPackage(pkg);
        parentWindow.update();
    }
    
    
}

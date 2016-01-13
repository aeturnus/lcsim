package lcsim.gui;

import java.awt.HeadlessException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.SwingConstants;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JDialog;
import javax.swing.JLabel;

import java.io.File;

import lcsimlib.*;
import lcsim.pkg.*;
import lcsim.pkg.Package;
import lcsim.pkg.gui.PackageManagerWindow;
import lcsim.LCSim;

//It's like a main, with a frame! :D
public class MainFrame extends JFrame
{
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenu simMenu;
    JMenu packageMenu;
    
    LCSystem sys;
    PackageManager pacman;
    
    JFileChooser fileChooser;
    
    public MainFrame(LCSystem system, PackageManager packageManager) throws HeadlessException
    {
        super();
        System.out.println("MainFrame: super()");
        sys = system;
        pacman = packageManager;
        fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        System.out.println("MainFrame: Made a JFileChooser");
        // TODO Auto-generated constructor stub
        this.setTitle("Little Computer Simulator");
        
        JMenuItem item;
        menuBar = new JMenuBar();
        
        //
        fileMenu = new JMenu("File");
        item = new JMenuItem("Load code");
        item.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                handleCodeLoad();
            }
        });
        fileMenu.add(item);
        menuBar.add(fileMenu);
        
        //
        simMenu = new JMenu("Simulator");
        item = new JMenuItem("Print Diagnostics");
        item.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String rawProfile = sys.getProfile();
                String profile = rawProfile.replaceAll("\n", "<br>");
                System.out.println(rawProfile);
                sys.printProfile();
                DialogText profileWindow = new DialogText("System Profile", profile);
                profileWindow.setVisible(true);
            }
        });
        simMenu.add(item);
        menuBar.add(simMenu);
        
        //
        packageMenu = new JMenu("Package");
        item = new JMenuItem("Open Package Manager");
        item.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                PackageManagerWindow pacmanWindow = new PackageManagerWindow(pacman);
                pacmanWindow.setVisible(true);
            }
        });
        packageMenu.add(item);
        
        menuBar.add(packageMenu);
        
        this.setJMenuBar(menuBar);
        System.out.println("MainFrame: Added menu bar and items");
        
        this.pack();
        this.setSize(400,400);
        
        System.out.println("MainFrame: Main window constructed!");
    }
    
    public void handleCodeLoad()
    {
        if(!sys.hasCore())
        {
            DialogText dialog = new DialogText("Error","No core loaded!");
            dialog.setVisible(true);
            return;
        }
        String[] extensions = sys.getExtensions();
        if(extensions.length == 0)
        {
            DialogText dialog = new DialogText("Error","No code loaders loaded!");
            dialog.setVisible(true);
            return;
        }
        FileNameExtensionFilter filter;
        fileChooser.resetChoosableFileFilters();
        for(int i = 0; i < extensions.length; i++)
        {
            filter = new FileNameExtensionFilter("*"+extensions[i],extensions[i].substring(extensions[i].lastIndexOf('.')+1)); 
            fileChooser.addChoosableFileFilter(filter);
        }
        fileChooser.setAcceptAllFileFilterUsed(false);
        int ret = fileChooser.showOpenDialog(null);
        if(ret == JFileChooser.APPROVE_OPTION)
        {
            sys.load(fileChooser.getSelectedFile());
        }
    }
}

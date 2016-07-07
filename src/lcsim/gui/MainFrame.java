package lcsim.gui;

import java.awt.HeadlessException;
import java.awt.Toolkit;
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
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.event.KeyEvent;

import java.io.File;

import java.awt.Image;
import javax.imageio.ImageIO;

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
    JMenu sysMenu;
    JMenu packageMenu;
    
    LCSystem sys;
    PackageManager pacman;
    
    JFileChooser fileChooser;
    
    JPanel debuggerPanel;
    
    public MainFrame(LCSystem system, PackageManager packageManager) throws HeadlessException
    {
        super();
        System.out.println("MainFrame: super()");
        try
        {
            Image icon = ImageIO.read(MainFrame.class.getResource("/lcsim/gui/LCSimIcon256.png"));
            this.setIconImage(icon);
        }
        catch (Exception e){e.printStackTrace();}
        //this.setIconImage(Toolkit.getDefaultToolkit().getImage("LCSimIcon256.png"));
        sys = system;
        pacman = packageManager;
        debuggerPanel = null;
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
        fileMenu.setMnemonic(KeyEvent.VK_P);
        fileMenu.add(item);
        menuBar.add(fileMenu);
        
        //
        sysMenu = new JMenu("System");
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
        sysMenu.add(item);
        sysMenu.add(new JSeparator());
        
        menuBar.add(sysMenu);
        
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
    
    public void setDebuggerPanel(JPanel panel)
    {
        if(debuggerPanel != null)
        {
            this.remove(debuggerPanel);
            //debuggerPanel = null;   //Doesn't actually do anything
        }
        debuggerPanel = panel;
        this.add(debuggerPanel);
        this.pack();
        this.getContentPane().validate();
        this.getContentPane().repaint();
        System.out.println("Debugger panel set");
    }
}

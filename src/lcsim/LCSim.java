package lcsim;

import javax.swing.UIManager;
import lcsim.pkg.*;
import lcsim.gui.*;
import lcsimlib.*;
import lcsimlib.gui.*;
import org.w3c.dom.Document;
//This serves as the main program

public class LCSim 
{
    static String VERSION = "0.1a";
    public static void main(String[] args) 
    {
        //NOTE: .pkg's need to maintain the package directory structure
        try
        {
            //FileOutputStream fout = new FileOutputStream("log.txt");
            //System.setOut(new PrintStream(fout));
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        System.out.printf("Little Computer Simulator v%s\n",VERSION);
        //We need to lookup classes that implement our interfaces
        try
        {
            System.out.println("Working dir = " + System.getProperty("user.dir"));
            LCSystem sys = new LCSystem();
            PackageManager pacman = new PackageManager(sys);
            
            //Create a MainFrame to show that stuff is working
            MainFrame window = new MainFrame(sys,pacman);
            window.setVisible(true);
            pacman.setMainFrame(window);
            
            Document initXML = DOM.newDocument("init.xml");
            System.out.println("Loaded init.xml");
            //Scan package directories
            String[] packageDirs = DOM.getElements(initXML, "package-directory");
            for(int i = 0; i < packageDirs.length; i++)
            {
                pacman.scanDirectory(packageDirs[i]);
                System.out.println("Scanned "+packageDirs[i]+" for packages");
            }
            System.out.println("Scanned package directories");
            //Run package load files
            String[] loadFiles = DOM.getElements(initXML, "package-load");
            for(int i = 0; i < loadFiles.length; i++)
            {
                pacman.loadXML(loadFiles[i]);
                System.out.println("Ran load configuration "+loadFiles[i]);
            }
            System.out.println("Ran all load configurations");
            
            //Wait for a core to be loaded
            while(!sys.hasCore())
            {
                Thread.sleep(100);
            }
            //Wait for a debugger to be loaded
            while(!sys.hasDebugger())
            {
                Thread.sleep(100);
            }
            //Once a debugger has been loaded
            System.out.println("******Main has ended. Child threads are doing work now");
        }
        catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
            System.out.println("Terminating...\n");
            System.exit(0);
            System.out.println("THIS SHOULD NOT PRINT\n");
        }
    }

}
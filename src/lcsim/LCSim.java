package lcsim;

import javax.swing.*;
import java.awt.*;

import java.net.URLClassLoader;
import java.net.URL;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import lcsimlib.*;

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
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        try
        {
            //URLClassLoader URLloader = new URLClassLoader(urls);
            
            LCSystem sys = new LCSystem();
            System.out.println("Working dir = " + System.getProperty("user.dir"));
            
            Test.testMain(sys);
            //Test.testGUI(sys);
            //Test.testLoadScript(sys);
            //Test.testPackageManagerGui(sys);
            //Test.testPackageManagerTerm(sys);
            //Test.testPackageManager(sys);
            //Test.testPackage(sys);
            
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
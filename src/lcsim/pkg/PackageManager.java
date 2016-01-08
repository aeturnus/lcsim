package lcsim.pkg;

import java.util.Vector;

import java.net.URLClassLoader;
import java.net.URL;
import java.io.File;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FilenameFilter;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

public class PackageManager
{
    private Vector<CorePackage> corePackages;
    private Vector<DevicePackage> devicePackages;
    private Vector<CodeLoaderPackage> codeloaderPackages;
    public PackageManager(String directoryPath)
    {
        corePackages = new Vector<CorePackage>();
        devicePackages = new Vector<DevicePackage>();
        codeloaderPackages = new Vector<CodeLoaderPackage>();
        
        //Scans the package directory for packages
        File pkgDir = new File(directoryPath);
        File[] pkgFiles = pkgDir.listFiles(new FilenameFilter(){
            public boolean accept(File dir, String filename) { return filename.endsWith(".pkg");}
        });
        
        for(int i = 0; i < pkgFiles.length; i++)
        {
            System.out.println(pkgFiles[i]);
            switch(Package.idPackage(pkgFiles[i]))
            {
            case CORE:
                corePackages.add(new CorePackage(pkgFiles[i]));
                System.out.println(corePackages.lastElement().toString()+"\n");
                break;
            case DEVICE:
                devicePackages.add(new DevicePackage(pkgFiles[i]));
                System.out.println(devicePackages.lastElement().toString()+"\n");
                break;
            case CODELOADER:
                codeloaderPackages.add(new CodeLoaderPackage(pkgFiles[i]));
                System.out.println(codeloaderPackages.lastElement().toString()+"\n");
                break;
            }
            
        }
    }

}

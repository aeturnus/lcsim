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

import lcsimlib.*;

public class PackageManager
{
    private Vector<CorePackage> corePackages;
    private Vector<DevicePackage> devicePackages;
    private Vector<CodeLoaderPackage> codeloaderPackages;
    private Vector<DebuggerPackage> debuggerPackages;
    
    private LCSystem sys;
    
    public PackageManager(LCSystem system)
    {
        construct(system);
    }
    
    public PackageManager(LCSystem system, String directoryPath)
    {
        construct(system);
        scanDirectory(directoryPath);
    }
    
    public PackageManager(LCSystem system, File directoryFile)
    {
        construct(system);
        scanDirectory(directoryFile);
    }
    
    private void construct(LCSystem system)
    {
        sys = system;
        corePackages = new Vector<CorePackage>();
        devicePackages = new Vector<DevicePackage>();
        codeloaderPackages = new Vector<CodeLoaderPackage>();
        debuggerPackages = new Vector<DebuggerPackage>();
    }
    
    public void scanDirectory(String directoryPath)
    {
        File dirFile = new File(directoryPath);
        scanDirectory(dirFile);
    }
    
    public void scanDirectory(File dirFile)
    {
        File[] pkgFiles = dirFile.listFiles(new FilenameFilter(){
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
            case DEBUGGER:
                debuggerPackages.add(new DebuggerPackage(pkgFiles[i]));
                System.out.println(debuggerPackages.lastElement().toString()+"\n");
                break;
            }
        }
    }
    
    public boolean loadXML(String filePath)
    {
        return loadXML(new File(filePath));
    }
    public boolean loadXML(File file)
    {
        Document doc = DOM.newDocument(file);
        String coreFile = DOM.getElement(doc, "core");
        //Find the core package
        if(coreFile != null)
        {
            for(int i = 0; i < corePackages.size(); i++)
            {
                if(coreFile.equals(corePackages.get(i).getFileName()))
                {
                    loadCore(corePackages.get(i));
                    break;
                }
            }
        }
        
        String[] deviceFiles = DOM.getElements(doc, "device");
        if(deviceFiles != null)
        {
            for(int i = 0; i < devicePackages.size(); i++)
            {
                for(int j = 0; j < deviceFiles.length; j++)
                {
                    if(deviceFiles[j].equals(devicePackages.get(i).getFileName()))
                    {
                        loadDevice(devicePackages.get(i));
                    }
                }
            }
        }
        
        String[] codeloaderFiles = DOM.getElements(doc, "codeloader");
        if(codeloaderFiles != null)
        {
            for(int i = 0; i < codeloaderPackages.size(); i++)
            {
                for(int j = 0; j < codeloaderFiles.length; j++)
                {
                    if(codeloaderFiles[j].equals(codeloaderPackages.get(i).getFileName()))
                    {
                        loadCodeLoader(codeloaderPackages.get(i));
                    }
                }
            }
        }
        
        String[] debuggerFiles = DOM.getElements(doc, "debugger");
        if(debuggerFiles != null)
        {
            for(int i = 0; i < debuggerPackages.size(); i++)
            {
                for(int j = 0; j < debuggerFiles.length; j++)
                {
                    if(debuggerFiles[j].equals(debuggerPackages.get(i).getFileName()))
                    {
                        loadDebugger(debuggerPackages.get(i));
                    }
                }
            }
        }
        
        return true;
    }
    
    public boolean loadPackage(Package pkg)
    {
        if(pkg.isLoaded())
        {
            return false;
        }
        switch(pkg.getType())
        {
        case CORE:
            return loadCore((CorePackage)pkg);
        case DEVICE:
            return loadDevice((DevicePackage)pkg);
        case CODELOADER:
            return loadCodeLoader((CodeLoaderPackage)pkg);
        case DEBUGGER:
            return loadDebugger((DebuggerPackage)pkg);
        }
        return true;
    }
    private boolean loadCore(CorePackage pkg)
    {
        pkg.load();
        sys.setCore(pkg.createObject());
        return true;
    }
    private boolean loadDevice(DevicePackage pkg)
    {
        pkg.load();
        sys.addDevice(pkg.createObject());
        return true;
    }
    private boolean loadCodeLoader(CodeLoaderPackage pkg)
    {
        pkg.load();
        sys.addLoader(pkg.createObject());
        return true;
    }
    private boolean loadDebugger(DebuggerPackage pkg)
    {
        pkg.load();
        System.out.println("Setting debugger...");
        sys.setDebugger(pkg.createObject());
        return true;
    }
    
    
    public CorePackage[] getCores()
    {
        CorePackage[] output = new CorePackage[corePackages.size()];
        for(int i= 0; i < output.length; i++)
        {
            output[i] = corePackages.get(i);
        }
        return output;
    }
    
    public DevicePackage[] getDevices()
    {
        DevicePackage[] output = new DevicePackage[devicePackages.size()];
        for(int i= 0; i < output.length; i++)
        {
            output[i] = devicePackages.get(i);
        }
        return output;
    }
    
    public CodeLoaderPackage[] getCodeLoaders()
    {
        CodeLoaderPackage[] output = new CodeLoaderPackage[codeloaderPackages.size()];
        for(int i= 0; i < output.length; i++)
        {
            output[i] = codeloaderPackages.get(i);
        }
        return output;
    }
    
    public DebuggerPackage[] getDebuggers()
    {
        DebuggerPackage[] output = new DebuggerPackage[debuggerPackages.size()];
        for(int i= 0; i < output.length; i++)
        {
            output[i] = debuggerPackages.get(i);
        }
        return output;
    }
    
    public Package[] getPackages()
    {
        int size;
        size = corePackages.size() + devicePackages.size() + codeloaderPackages.size()
             + debuggerPackages.size();
        Package[] output = new Package[size];
        int index = 0;
        for(int i = 0; i < corePackages.size(); i++,index++)
        {
            output[index] = corePackages.get(i);
        }
        for(int i = 0; i < devicePackages.size(); i++,index++)
        {
            output[index] = devicePackages.get(i);
        }
        for(int i = 0; i < codeloaderPackages.size(); i++,index++)
        {
            output[index] = codeloaderPackages.get(i);
        }
        for(int i = 0; i < debuggerPackages.size(); i++,index++)
        {
            output[index] = debuggerPackages.get(i);
        }
        
        return output;
    }
    public Package[] getLoadedPackages()
    {
        Vector<Package> vector = new Vector<Package>();
        Package[] list = getPackages();
        for(int i = 0; i < list.length; i++)
        {
            if(list[i].isLoaded())
            {
                vector.add(list[i]);
            }
        }
        Package[] output = new Package[vector.size()];
        vector.toArray(output);
        return output;
    }
    public Package[] getUnloadedPackages()
    {
        Vector<Package> vector = new Vector<Package>();
        Package[] list = getPackages();
        for(int i = 0; i < list.length; i++)
        {
            if(!list[i].isLoaded())
            {
                vector.add(list[i]);
            }
        }
        Package[] output = new Package[vector.size()];
        vector.toArray(output);
        return output;
    }
    
    public LCSystem getSystem()
    {
        return sys;
    }

}

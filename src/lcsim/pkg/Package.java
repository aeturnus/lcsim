package lcsim.pkg;

import java.net.URLClassLoader;
import java.net.URL;
import java.io.File;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.Enumeration;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import lcsimlib.DOM;

import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class Package
{
    private PackageType type = null;
    private Class<?> rawClass = null;
    private File file = null;
    private ZipFile pkg = null;
    
    private String name;
    private String author;
    private String version;
    private String className;
    private String description;
    
    private boolean loaded = false;
    
    //protected Document doc;
    //Parent constructor 
    public Package(File pkgFile)
    {
        try
        {
            file = pkgFile;
            pkg = new ZipFile(pkgFile);
            //Need to identify package type
            Document doc = DOM.newDocument(pkg, "meta.xml");
            loadMetaData(doc);
        }
        catch(Exception e)
        {
            handleException(e);
        }
    }
    
    public boolean load()
    {
        try
        {
            URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()});
            loadAllClasses(loader,pkg);
            rawClass = loader.loadClass(className); //get my main class
            System.out.println("Loaded raw class: " + rawClass.getCanonicalName());
            loader.close();
            System.out.println("Closed loader");
            loaded = true;
            System.out.println("Load of " + pkg.getName() + " is complete");
            return true;
        }
        catch(Exception e)
        {
            handleException(e);
        }
        return false;
    }
    
    protected void loadMetaData(Document doc)
    {
        //Get type
        type = PackageType.fromString(doc.getDocumentElement().getAttribute("type"));
        //get metadata strings
        name = doc.getElementsByTagName("name").item(0).getTextContent();
        author = doc.getElementsByTagName("author").item(0).getTextContent();
        version = doc.getElementsByTagName("version").item(0).getTextContent();
        description = doc.getElementsByTagName("description").item(0).getTextContent();
        className = doc.getElementsByTagName("class").item(0).getTextContent();
    }
    
    public static PackageType idPackage (File pkgFile)
    {
        try
        {
            ZipFile pkg = new ZipFile(pkgFile);
            Document doc = DOM.newDocument(pkg,"meta.xml");
            return PackageType.fromString(doc.getDocumentElement().getAttribute("type"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Error id'ing package");
        }
        return PackageType.INVALID;
    }
    
    
    
    public Object createObject()
    {
        if(loaded)
        {
            try
            {
                Object out = rawClass.newInstance();
                System.out.println("Instantiated instance of "+rawClass.getCanonicalName());
                return out;
            }
            catch(Exception e)
            {
                System.out.println("createInstance() exception");
                System.out.println(e);
            }
        }
        return null;
    }
    
    private void handleException(Exception e)
    {
        //System.out.println(e);
        System.out.println(className);
        e.printStackTrace();
    }
    
    private static void loadAllClasses(URLClassLoader loader, ZipFile zip)
    {
        try
        {
            //Loop, looking for class files
            Enumeration<? extends ZipEntry> entries = zip.entries();
            String className;
            while(entries.hasMoreElements())
            {
                ZipEntry nextElement = entries.nextElement();
                //System.out.println("Next Entry in "+zip.getName() +": " + nextElement.getName());
                System.out.println("Next Element: "+nextElement.getName());
                className = getClassName(nextElement);
                if(className != null)
                {
                    loader.loadClass(className);
                    System.out.println("Loaded class: " + className+"\n");
                }
            }
            System.out.println("Classes loaded from "+zip.getName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private static String getClassName(ZipEntry entry)
    {
        String name = entry.getName();
        if(name.endsWith(".class"))
        {
            String noClass = name.substring(0, name.lastIndexOf(".class")); //Remove the class extension
            String repSlash = noClass.replace('/','.'); //Replace directory divisions with . for packages
            return repSlash;
        }
        return null;
    }
    
    public String toString()
    {
        //String output = "<" + doc.getDocumentElement().getAttribute("type") + "> " + file.getAbsolutePath() + ": " + rawClass.getName();
        String output = "====" + "    " + type  +"    " + "====";
        output += "\nName: " + name ;
        output += "\nAuthor: " + author;
        output += "\nVersion: " + version;
        output += "\nClassname: " + className;
        output += "\nDescription: " + description;
        return output;
    }
    
    public PackageType getType()
    {
        return type;
    }
    public String getName()
    {
        return name;
    }
    public String getAuthor()
    {
        return author;
    }
    public String getVersion()
    {
        return version;
    }
    public String getClassName()
    {
        return className;
    }
    public String getDescription()
    {
        return description;
    }
    public String getFileName()
    {
        return file.getName();
    }
    
    public boolean isLoaded()
    {
        return loaded;
    }
}

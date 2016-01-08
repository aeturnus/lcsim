package lcsim.pkg;

import lcsim.DOM;

import java.net.URLClassLoader;
import java.net.URL;
import java.io.File;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
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
    private String classpath;
    private String description;
    
    protected Document doc;
    //Parent constructor 
    public Package(File pkgFile)
    {
        try
        {
            file = pkgFile;
            pkg = new ZipFile(pkgFile);
            
            //Need to identify package type
            doc = DOM.newDocument(pkg, "meta.xml");
            type = findType(doc);
            
            //Extract the classpath
            classpath = doc.getElementsByTagName("classpath").item(0).getTextContent();
            URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()});
            rawClass = loader.loadClass(classpath);
            loader.close();
            
            //get metadata strings
            name = doc.getElementsByTagName("name").item(0).getTextContent();
            author = doc.getElementsByTagName("author").item(0).getTextContent();
            version = doc.getElementsByTagName("version").item(0).getTextContent();
            description = doc.getElementsByTagName("description").item(0).getTextContent();
            
        }
        catch(Exception e)
        {
            handleException(e);
        }
    }
    
    public static PackageType idPackage (File pkgFile)
    {
        try
        {
            ZipFile pkg = new ZipFile(pkgFile);
            Document doc = DOM.newDocument(pkg,"meta.xml");
            return findType(doc);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Error id'ing package");
        }
        return PackageType.INVALID;
    }
    
    private static PackageType findType(Document doc)
    {
        PackageType type = PackageType.INVALID;
        String typestring = doc.getDocumentElement().getAttribute("type");
        switch(typestring)
        {
        case "core":
            type = PackageType.CORE;
            break;
        case "device":
            type = PackageType.DEVICE;
            break;
        case "codeloader":
            type = PackageType.CODELOADER;
            break;
        case "plugin":
            type = PackageType.PLUGIN;
            break;
        }
        return type;
    }
    
    public PackageType getType()
    {
        return type;
    }
    
    public Object createObject()
    {
        try
        {
            return rawClass.newInstance();
        }
        catch(Exception e)
        {
            System.out.println("createInstance() exception");
            System.out.println(e);
        }
        return null;
    }
    
    private void handleException(Exception e)
    {
        System.out.println(e);
    }
    
    public String toString()
    {
        //String output = "<" + doc.getDocumentElement().getAttribute("type") + "> " + file.getAbsolutePath() + ": " + rawClass.getName();
        String output = "====" + "    " + type.name()  +"    " + "====";
        output += "\nName: " + name ;
        output += "\nAuthor: " + author;
        output += "\nVersion: " + version;
        output += "\nClasspath: " + classpath;
        output += "\nDescription: " + description;
        return output;
    }
}

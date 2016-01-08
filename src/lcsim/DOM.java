package lcsim;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipFile;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;


public class DOM
{
    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private static DocumentBuilder builder;
    static{
        try
        {
            builder = factory.newDocumentBuilder();
        }
        catch(Exception e)
        {
            System.out.println(e);
            System.out.println("DOM initialization failed");
            System.exit(0);
        }
    }

    public static Document newDocument(File file)
    {
        try
        {
            Document doc = builder.parse(file);
            return doc;
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }
    
    public static Document newDocument(String filePath)
    {
        try
        {
            File file = new File(filePath);
            Document doc = builder.parse(file);
            return doc;
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }
    
    public static Document newDocument(InputStream stream)
    {
        try
        {
            Document doc = builder.parse(stream);
            return doc;
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }
    
    public static Document newDocument(ZipFile zip, String entry)
    {
        try
        {
            InputStream stream = zip.getInputStream(zip.getEntry(entry));
            return newDocument(stream);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }
    
    private static void handleException(Exception e)
    {
    }

}

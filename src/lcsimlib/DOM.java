package lcsimlib;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.Enumeration;

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
            ZipEntry zipEntry = zip.getEntry(entry);
            if(zipEntry == null)    //If it's null, try looking for it
            {
                Enumeration<? extends ZipEntry> entries = zip.entries();
                String entryName;
                while(entries.hasMoreElements())
                {
                    zipEntry = entries.nextElement();
                    entryName = zipEntry.getName();
                    if(entryName.endsWith(entry))   //Check if it ends with the file we're looking for
                    {
                        break;
                    }
                }
                if(zipEntry == null)
                {
                    return null;
                }
            }
            InputStream stream = zip.getInputStream(zipEntry);
            return newDocument(stream);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }
    
    public static String getElement(Document doc, String name)
    {
        String output;
        NodeList list = doc.getElementsByTagName(name);
        if(list == null)
            return null;
        Node node = list.item(0);
        if(node == null)
            return null;
        output = node.getTextContent();
        return output;
    }
    public static String[] getElements(Document doc, String name)
    {
        String[] output;
        NodeList list = doc.getElementsByTagName(name);
        output = new String[list.getLength()];
        for(int i= 0; i < output.length; i++)
        {
            output[i] = list.item(i).getTextContent();
        }
        return output;
    }
    
    private static void handleException(Exception e)
    {
        e.printStackTrace();
    }

}

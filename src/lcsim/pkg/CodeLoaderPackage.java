package lcsim.pkg;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import lcsimlib.CodeLoader;

public class CodeLoaderPackage extends Package
{
    private String[] extensions;
    public CodeLoaderPackage(File pkgFile)
    {
        super(pkgFile);
        
    }
    
    protected void loadMetaData(Document doc)
    {
        super.loadMetaData(doc);
        Element codeloaderNode = (Element) doc.getElementsByTagName("codeloader").item(0);
        NodeList extList = codeloaderNode.getElementsByTagName("extension");
        extensions = new String[extList.getLength()];
        for(int i = 0; i < extensions.length; i++)
        {
            extensions[i] = extList.item(i).getTextContent();
        }
    }
    
    public CodeLoader createObject()
    {
        return (CodeLoader) super.createObject();
    }
    
    public String toString()
    {
        String output = super.toString();
        output += "\nExtensions: [ ";
        for(int i = 0; i < extensions.length - 1; i++)
        {
            output += extensions[i] + ", ";
        }
        output += extensions[extensions.length - 1] + " ]";
        
        return output;
    }
    
    public String[] getExtensions()
    {
        return extensions.clone();
    }
    
    

}

package lcsim.pkg;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

//import java.util.Vector;

public class CorePackage extends Package
{

    private String instructionSet;
    private int addressLow;
    private int addressHigh;
    private int[] addressabilities;
    private int wordSize;
    
    public CorePackage(File pkgFile)
    {
        // TODO Auto-generated constructor stub
        super(pkgFile);
        Element coreNode = (Element) doc.getElementsByTagName("core").item(0);
        instructionSet = coreNode.getElementsByTagName("instruction_set").item(0).getTextContent();
        addressLow = Integer.decode(coreNode.getElementsByTagName("address_low").item(0).getTextContent());
        addressHigh = Integer.decode(coreNode.getElementsByTagName("address_high").item(0).getTextContent());
        Element ads = (Element) coreNode.getElementsByTagName("addressabilities").item(0);
        NodeList adsList = ads.getElementsByTagName("addressability");
        addressabilities = new int[adsList.getLength()];
        for(int i = 0; i < adsList.getLength(); i++)
        {
            addressabilities[i] = Integer.parseUnsignedInt(adsList.item(i).getTextContent());
        }
        wordSize = Integer.parseUnsignedInt(coreNode.getElementsByTagName("word_size").item(0).getTextContent());
        
    }
    
    public String getString()
    {
        return instructionSet;
    }
    public int getAddressLow()
    {
        return addressLow;
    }
    public int getAddressHigh()
    {
        return addressHigh;
    }
    public int[] getAddressabilities()
    {
        return addressabilities.clone();
    }
    public int getWordSize()
    {
        return wordSize;
    }
    
    public String toString()
    {
        String output = super.toString() ;
        output += "\nInstruction Set: " + instructionSet;
        output += "\nAddress Low : " + String.format("0x%08X",addressLow) ;
        output += "\nAddress High: " + String.format("0x%08X",addressHigh);
        output += "\nAddressabilities: [ ";
        for(int i = 0; i < addressabilities.length-1; i++)
        {
            output += addressabilities[i] + ", ";
        }
        output += addressabilities[addressabilities.length-1] + " ]";
        output += "\nWord Size: " + wordSize;
        return output;
    }
    
}

package lcsim.pkg;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import lcsimlib.Device;

public class DevicePackage extends Package
{
    private int[] ioRegisters;
    private int[] interruptVectors;
    
    public DevicePackage(File pkgFile)
    {
        super(pkgFile);
        
    }
    
    protected void loadMetaData(Document doc)
    {
        super.loadMetaData(doc);
        
        Element deviceNode = (Element) doc.getElementsByTagName("device").item(0);
        NodeList ioNodes = deviceNode.getElementsByTagName("io_register");
        NodeList ivNodes = deviceNode.getElementsByTagName("interrupt_vector");
        
        //read io_registers
        ioRegisters = new int[ioNodes.getLength()];
        for(int i = 0; i < ioRegisters.length; i++)
        {
            ioRegisters[i] = Integer.decode(ioNodes.item(i).getTextContent());
        }
        
        //read interrupt_vectors
        interruptVectors = new int[ivNodes.getLength()];
        for(int i = 0; i < interruptVectors.length; i++)
        {
            interruptVectors[i] = Integer.decode(ivNodes.item(i).getTextContent());
        }
    }
    
    public Device createObject()
    {
        return (Device) super.createObject();
    }
    
    public String toString()
    {
        String output = super.toString();
        
        //Print out io regs used
        output += "\nIO Registers: [ ";
        for(int i = 0; i < ioRegisters.length-1; i++)
        {
            output += String.format("0x%08X, ", ioRegisters[i]);
        }
        output += String.format("0x%08X ]", ioRegisters[ioRegisters.length-1]);
        
        //Print out iv's used
        output += "\nInterrupt Vectors: [ ";
        for(int i = 0; i < interruptVectors.length-1; i++)
        {
            output += String.format("0x%08X, ", interruptVectors[i]);
        }
        output += String.format("0x%08X ]", interruptVectors[interruptVectors.length-1]);

        return output;
    }
    
    public int[] getIORegisters()
    {
        return ioRegisters.clone();
    }
    
    public int[] getInterruptVectors()
    {
        return interruptVectors.clone();
    }

}

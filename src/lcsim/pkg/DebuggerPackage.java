package lcsim.pkg;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import lcsimlib.Debugger;

public class DebuggerPackage extends Package
{

    private String instruction_set;
    
    public DebuggerPackage(File pkgFile)
    {
        super(pkgFile);
        // TODO Auto-generated constructor stub
    }
    
    protected void loadMetaData(Document doc)
    {
        super.loadMetaData(doc);
    }
    
    public Debugger createObject()
    {
        return (Debugger) super.createObject();
    }
    
    public String toString()
    {
        String output = super.toString();
        return output;
    }

}

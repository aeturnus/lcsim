package lcsimlib;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Vector;

public class Symbol
{
    private String name;
    private int address;
    public Symbol(String name, int address)
    {
        this.name = name;
        this.address = address;
    }
    public int getAddress()
    {
        return address;
    }
    public String getName()
    {
        return name;
    }
    
    public static Symbol[] symbolsFromFile(String filePath)
    {
        return symbolsFromFile(new File(filePath));
    }
    public static Symbol[] symbolsFromFile(File file)
    {
        try
        {
            Vector<Symbol> vector = new Vector<Symbol>();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null)
            {
                if(line.charAt(2) == '\t')
                {
                    int nameStart = 3;
                    int nameEnd = line.indexOf(' ', nameStart);
                    int addrStart = line.lastIndexOf(' ')+1;
                    String name = line.substring(nameStart, nameEnd);
                    int address = Integer.parseInt(line.substring(addrStart), 16) & 0xFFFF;
                    vector.add(new Symbol(name, address));
                }
            }
            Symbol[] symbols = new Symbol[vector.size()];
            vector.copyInto(symbols);
            return symbols;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

}

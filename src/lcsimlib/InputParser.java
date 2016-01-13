package lcsimlib;

import java.lang.StringBuilder;
import java.util.Vector;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedInputStream;

public class InputParser
{
    private File file;
    private BufferedReader reader;
    private String data;
    public InputParser(String filePath)
    {
        // TODO Auto-generated constructor stub
        file = new File(filePath);
        init();
    }
    public InputParser(File f)
    {
        file = f;
        init();
    }
    private void init()
    {
        try
        {
            //reader = new FileReader(file);
            reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            char c;
            while( (c = (char)reader.read()) != -1)
            {
                builder.append(c);
            }
            data = builder.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public String[] getWords()
    {
        Vector<String> vector = new Vector<String>();
        int end;
        for(int i = 0; i < data.length(); i++)
        {
            if(! (data.charAt(i) == ' ' || data.charAt(i) == '\t'))
            {
            }
        }
        String[] output = new String[vector.size()];
        vector.copyInto(output);
        return output;
    }

}

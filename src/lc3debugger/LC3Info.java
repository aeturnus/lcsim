package lc3debugger;

/**
 * String-based sanity checking for LC3
 * @author brandon
 */
public class LC3Info
{
    
    public static boolean validateAddress( String addrString )
    {
        Boolean[] status = {false};
        int addr = stringToInt(addrString, status);
        if(!status[0])
        {
            return false;
        }
        addr = addr & 0xFFFF;
        if( addr >= 0 && addr <= 0xFFFF )
        {
            return true;
        }
        return false;
    }
    
    public static boolean validateAddress( int addr )
    {
        addr = addr & 0xFFFF;
        if( addr >= 0 && addr <= 0xFFFF )
        {
            return true;
        }
        return false;
    }
    
    public static int stringToInt(String value, Boolean[] status)
    {
        value = value.toLowerCase();
        int xPos = value.indexOf('x');
        status[0] = true;
        try
        {
            if(xPos != -1)  // if we have no 'x', then it's likely not a hex address
            {
                return Integer.parseInt(value.substring(xPos+1), 16);
            }
            else
            {
                return Integer.parseInt(value);
            }
        }
        catch (NumberFormatException nfe)
        {
            status[0] = false;
            return 0;
        }
    }
    
}
package lcsim.pkg;

public enum PackageType
{
    CORE,
    DEVICE,
    CODELOADER,
    DEBUGGER,
    PLUGIN,
    INVALID;
    
    public static PackageType fromString(String type)
    {
        PackageType output = INVALID;
        type = type.toLowerCase();
        switch(type)
        {
        case "core":
            output = CORE;
            break;
        case "device":
            output = DEVICE;
            break;
        case "codeloader":
            output = CODELOADER;
            break;
        case "debugger":
            output = DEBUGGER;
            break;
        case "plugin":
            output = PLUGIN;
            break;
        }
        return output;
    }
    
    public String toString()
    {
        String output = "ERROR: Invalid";
        switch(this)
        {
        case CORE:
            output = "Core";
            break;
        case DEVICE:
            output = "Device";
            break;
        case CODELOADER:
            output = "Code Loader";
            break;
        case DEBUGGER:
            output = "Debugger";
            break;
        case PLUGIN:
            output = "Plugin";
            break;
        case INVALID:
            //It already is invalid
            break;
        }
        return output;
    }
}

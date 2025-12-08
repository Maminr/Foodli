package Helper;

public class Logger
{
    private final boolean isDebugMode;
    private static Logger instance = null;

    private Logger(boolean isDebugMode)
    {
        this.isDebugMode = isDebugMode;
    }

    public static Logger getInstance()
    {
        if (instance == null)
        {
            instance = new Logger(false);
        }
        return instance;
    }

    public static void init(boolean isDebugMode)
    {
        if (instance != null)
        {
            throw new RuntimeException("Kos kesh nabayad init koni donbare singletonee");
        }
        if(isDebugMode){
            System.out.println("Debug mode is ON");
        }
        instance = new Logger(isDebugMode);
    }


    public void log(String message)
    {
        System.out.println(message);
    }

    public void error(String message)
    {
        if (isDebugMode)
            System.err.println("ERROR: " + message);
    }
}

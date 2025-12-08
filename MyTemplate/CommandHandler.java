import Errors.InvalidInputError;
import Errors.NotFoundError;
import Handlers.Handler;
import Models.CommandAfterFunction;

import java.util.HashMap;

public class CommandHandler
{
    private HashMap<String, Handler> handlers;

    public CommandHandler()
    {
        handlers = new HashMap<>();
        handlers.put("exit", new Handlers.ExitHandler());
        handlers.put("restaurant", new Handlers.RestaurantHandler());
    }

    public CommandAfterFunction handleCommand(String[] input)
    {
        if (!handlers.containsKey(input[0]))
        {
            System.out.println("Unknown command: " + input[0]);
            return CommandAfterFunction.CONTINUE;
        }
        Handler handler = handlers.get(input[0]);
        CommandAfterFunction function;

        try
        {
            function = handler.handle(input);
        }
        catch (InvalidInputError e)
        {
            System.out.println("Invalid input: " + e.getMessage());
            function = CommandAfterFunction.CONTINUE;
        }

        return function;
    }
}

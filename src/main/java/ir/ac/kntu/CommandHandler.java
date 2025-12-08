package ir.ac.kntu;

import ir.ac.kntu.handlers.IHandler;
import ir.ac.kntu.handlers.SigninHandler;
import ir.ac.kntu.handlers.SignupHandler;
import ir.ac.kntu.helper.InvalidInputError;
import ir.ac.kntu.models.enums.CommandAfterFunction;

import java.util.HashMap;

public class CommandHandler {
    HashMap<String, IHandler> handlers;

    public CommandHandler(){
        handlers = new HashMap<>();
        handlers.put("signup", new SignupHandler());
    }

    public CommandAfterFunction handleCommand(String[] input)
    {
        if (!handlers.containsKey(input[0]))
        {
            System.out.println("Unknown command: " + input[0]);
            return CommandAfterFunction.CONTINUE;
        }
        IHandler handler = handlers.get(input[0]);
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

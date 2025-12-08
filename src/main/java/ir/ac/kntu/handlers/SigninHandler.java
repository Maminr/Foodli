package ir.ac.kntu.handlers;

import ir.ac.kntu.helper.InvalidInputError;
import ir.ac.kntu.models.enums.CommandAfterFunction;

import java.util.HashMap;

public class SigninHandler implements IHandler {


    HashMap<String, Runnable> commands;

    public SigninHandler() {
        commands = new HashMap<>();
        commands.put("hi", this::printHi);
    }

    @Override
    public CommandAfterFunction handle(String[] input)
    {
        if (input.length < 2 || !commands.containsKey(input[1]))
        {
            //return  CommandAfterFunction.EXIT;
            throw new InvalidInputError("Can not process this command! arguments error.");
        }
        Runnable command = commands.get(input[1]);
        command.run();
        return CommandAfterFunction.CONTINUE;
    }

    private void printHi(){
        System.out.println("Hi!");
    }
}

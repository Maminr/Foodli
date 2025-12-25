package Handlers;

import Errors.NotFoundError;
import Models.CommandAfterFunction;

public class ExitHandler implements Handler
{
    @Override
    public CommandAfterFunction handle(String[] input)
    {
        return CommandAfterFunction.EXIT;
    }
}

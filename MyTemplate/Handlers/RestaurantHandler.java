package Handlers;

import Errors.InvalidInputError;
import Errors.NotFoundError;
import Managers.RestaurantManager;
import Models.CommandAfterFunction;
import Models.Restaurant;

import java.util.HashMap;

public class RestaurantHandler implements Handler
{
    RestaurantManager rm = RestaurantManager.getInstance();
    private HashMap<String, Runnable> commands;

    public RestaurantHandler()
    {
        commands = new HashMap<>();
        commands.put("list", this::listRestaurants);
    }

    @Override
    public CommandAfterFunction handle(String[] input)
    {
        if (input.length < 2 || !commands.containsKey(input[1]))
        {
            throw new InvalidInputError("Resturan injoori nemishe dadash");
        }
        Runnable command = commands.get(input[1]);
        command.run();
        return CommandAfterFunction.CONTINUE;
    }

    private void listRestaurants()
    {
        for (Restaurant r : rm.getRestaurantList())
        {
            System.out.println(r);
        }
    }
}

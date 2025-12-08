import Helper.Logger;
import Models.CommandAfterFunction;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Logger.init(args.length >= 1 && args[0].equals("--dev"));

        Scanner sc = new Scanner(System.in);

        CommandHandler commandHandler = new CommandHandler();
        CommandAfterFunction function;
        do {

            String[] input = sc.nextLine().split("\\s");
            function = commandHandler.handleCommand(input);

        } while (function != CommandAfterFunction.EXIT);
    }
}

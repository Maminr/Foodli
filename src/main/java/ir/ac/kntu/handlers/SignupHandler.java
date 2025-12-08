package ir.ac.kntu.handlers;

import ir.ac.kntu.helper.InvalidInputError;
import ir.ac.kntu.managers.UserManager;
import ir.ac.kntu.models.User;
import ir.ac.kntu.models.enums.CommandAfterFunction;

import java.util.HashMap;
import java.util.Scanner;

public class SignupHandler implements IHandler {


    HashMap<String, Runnable> commands;

    public SignupHandler() {
        commands = new HashMap<>();
        commands.put("customer", this::createCustomerCommand);
        commands.put("manager", this::printHi);
    }

    @Override
    public CommandAfterFunction handle(String[] input) {
        if (input.length < 2 || !commands.containsKey(input[1])) {
            //return  CommandAfterFunction.EXIT;
            throw new InvalidInputError("Can not process this command! arguments error.");
        }

        Runnable command = commands.get(input[1]);
        command.run();
        return CommandAfterFunction.CONTINUE;
    }

    private void printHi() {
        System.out.println("Hi!");
    }

    private void createCustomerCommand() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- NEW CUSTOMER REGISTRATION ---");

        System.out.print("Enter First Name: ");
        String name = scanner.next();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.next();

        System.out.print("Enter Phone Number: ");
        String phone = scanner.next();

        System.out.print("Enter Password: ");
        String password = scanner.next();

        User newUser = UserManager.getInstance().signUpCustomer(name, lastName, phone, password);

        if (newUser != null) {
            System.out.println("✅ Customer registered successfully: " + newUser.getFullName());
        } else {
            System.out.println("❌ Registration Failed.");
        }
    }
    private void createManagerCommand() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- NEW Manager REGISTRATION ---");

        System.out.print("Enter First Name: ");
        String name = scanner.next();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.next();

        System.out.print("Enter Phone Number: ");
        String phone = scanner.next();

        System.out.print("Enter Password: ");
        String password = scanner.next();

        User newUser = UserManager.getInstance().signUpManager(name, lastName, phone, password);

        if (newUser != null) {
            System.out.println("✅ Customer registered successfully: " + newUser.getFullName());
        } else {
            System.out.println("❌ Registration Failed.");
        }
    }
}

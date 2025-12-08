package ir.ac.kntu.handlers;

import ir.ac.kntu.models.enums.CommandAfterFunction;

public interface IHandler {
    CommandAfterFunction handle(String[] input);
}

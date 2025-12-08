package Handlers;

import Errors.NotFoundError;
import Models.CommandAfterFunction;

public interface Handler
{
    CommandAfterFunction handle(String[] input);
}

package Errors;

public class InvalidInputError extends RuntimeException
{
    public InvalidInputError(String message)
    {
        super(message);
    }
}

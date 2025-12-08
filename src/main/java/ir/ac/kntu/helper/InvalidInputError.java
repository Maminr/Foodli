package ir.ac.kntu.helper;

public class InvalidInputError extends RuntimeException {
    public InvalidInputError(String message)
    {
        super(message);
    }
}

package ir.ac.kntu.models.errors;

public class InvalidInputError extends RuntimeException {
    public InvalidInputError(String message)
    {
        super(message);
    }
}

package ir.ac.kntu.models.errors;

public class MenuNotFoundError extends RuntimeException {
    public MenuNotFoundError(String message)
    {
        super(message);
    }
}

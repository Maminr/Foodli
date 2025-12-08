package ir.ac.kntu.utilities;

public class StringUtils {
    public static boolean hasCorrectLength(String text, int min, int max) {
        if (text == null) {
            return false;
        }
        int length = text.trim().length(); 
        return length >= min && length <= max;
    }
}
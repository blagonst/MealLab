package UniPi.MealLab.API;

public class MealApiException extends Exception {
    public MealApiException(String message) { super(message); }
    public MealApiException(String message, Throwable cause) { super(message, cause); }
}

package online_shopping_system.Exceptions;

public class UserNotifyException extends Exception {
    
    public UserNotifyException() {
        super();
    }
    
    public UserNotifyException(String message) {
        super(message);
    }
}
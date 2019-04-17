package repository;

/**
 * signals the exceptions occurred due to data insignificance
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String msg) {
        super(msg);
    }
}

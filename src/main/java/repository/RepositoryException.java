package repository;

/**
 * signals the exceptions occurred when trying to read from file
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException(String msg) {
        super(msg);
    }
}

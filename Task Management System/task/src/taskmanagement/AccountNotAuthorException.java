package taskmanagement;

public class AccountNotAuthorException extends RuntimeException {

    public AccountNotAuthorException(String message) {
        super(message);
    }
}
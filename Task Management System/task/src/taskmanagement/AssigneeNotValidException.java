package taskmanagement;

public class AssigneeNotValidException extends RuntimeException {

    public AssigneeNotValidException(String message) {
        super(message);
    }
}
package expression;

public class IllegalOperationException extends ParsingExceptions {
    public IllegalOperationException(String message, int position) {
        super(message,  position);
    }
}

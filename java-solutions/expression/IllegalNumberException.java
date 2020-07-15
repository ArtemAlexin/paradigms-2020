package expression;

public class IllegalNumberException extends ParsingExceptions {
    public IllegalNumberException(String message, int position) {
        super(message,  position);
    }
}

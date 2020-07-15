package expression;

public class IllegalArgumentException extends ParsingExceptions {
    public IllegalArgumentException(String message, int position) {
        super(message, position);
    }
}

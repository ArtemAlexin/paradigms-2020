package expression;

public class UnclosedBracketException extends ParsingExceptions {
    public UnclosedBracketException(String message, int position) {
        super(message,  position);
    }
}

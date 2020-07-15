package expression;

public class UnopenedBracketException extends ParsingExceptions {
    public UnopenedBracketException(String message, int position) {
        super(message, position);
    }
}

package expression;

public class ParsingExceptions extends Exception {
    public ParsingExceptions(String message, int position) {
        super(message + ", position :" + position);
    }
}

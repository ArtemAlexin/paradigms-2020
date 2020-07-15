package expression;

public class OverflowException extends ExpressionExeption {
    public OverflowException(String type, String arguments) {
        super("overflow in " + type, arguments);
    }
}

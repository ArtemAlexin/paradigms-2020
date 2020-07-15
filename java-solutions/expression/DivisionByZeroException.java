package expression;

public class DivisionByZeroException extends ExpressionExeption {
    public DivisionByZeroException(String type, String arguments) {
        super(type, arguments);
    }
}

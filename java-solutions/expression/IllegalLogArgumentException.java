package expression;

public class IllegalLogArgumentException extends ExpressionExeption {
    public IllegalLogArgumentException(String type, String arguments) {
        super(type, arguments);
    }
}

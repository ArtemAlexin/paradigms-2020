package expression;

public class IllegalPowerException extends ExpressionExeption {
    public IllegalPowerException(String type, String arguments) {
        super(type, arguments);
    }
}

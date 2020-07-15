package expression;

public class ExpressionExeption extends RuntimeException {
    public ExpressionExeption(String type, String arguments) {
        super(type + "; arguments : " + arguments);
    }
}

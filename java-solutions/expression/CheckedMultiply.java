package expression;

public class CheckedMultiply<T> extends BinaryOperations<T> {
    public CheckedMultiply(TripleExpression<T> left, TripleExpression<T> right, Operator<T> op) {
        super(left, right, op);
    }

    public CheckedMultiply() {
        super();
    }

    @Override
    public T calc(T left, T right) {
        return op.multiply(left, right);
    }

    @Override
    public void checkForException(int l, int r) {
       Calculations.checkMulForException(l, r, "mul");
    }

    @Override
    protected String sign() {
        return "*";
    }

    @Override
    public int priority() {
        return 1;
    }
}

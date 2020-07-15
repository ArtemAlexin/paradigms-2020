package expression;

public class CheckedSubtract<T> extends BinaryOperations<T> {
    public CheckedSubtract(TripleExpression<T> left, TripleExpression<T> right, Operator<T> op) {
        super(left, right, op);
    }

    @Override
    public T calc(T left, T right) {
        return op.subtract(left, right);
    }

    @Override
    public void checkForException(int l, int r) {
        Calculations.checkSubtractForException(l, r, "Subtract");
    }

    @Override
    protected String sign() {
        return "-";
    }

    @Override
    public int priority() {
        return 2;
    }
}

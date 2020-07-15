package expression;

public class Min<T> extends BinaryOperations<T> {
    public Min(TripleExpression<T> left, TripleExpression<T> right, Operator<T> op) {
        super(left, right, op);
    }

    @Override
    public T calc(T left, T right) {
        return op.min(left, right);
    }

    @Override
    public void checkForException(int l, int r) {
        return;
    }

    @Override
    protected String sign() {
        return "min";
    }

    @Override
    public int priority() {
        return 3;
    }
}

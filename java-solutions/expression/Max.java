package expression;

public class Max<T> extends BinaryOperations<T> {
    public Max(TripleExpression<T> left, TripleExpression<T> right, Operator<T> mod) {
        super(left, right, mod);
    }

    @Override
    public T calc(T left, T right) {
        return op.max(left, right);
    }

    @Override
    public void checkForException(int l, int r) {
        return;
    }

    @Override
    protected String sign() {
        return "max";
    }

    @Override
    public int priority() {
        return 3;
    }
}

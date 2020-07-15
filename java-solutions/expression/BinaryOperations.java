package expression;


public abstract class BinaryOperations<T> extends MainAbstract implements Operations, TripleExpression<T> {
    private final TripleExpression<T> left, right;
    protected final Operator<T> op;
    public BinaryOperations() {
        left = null;
        right = null;
        op = null;
    }

    protected BinaryOperations(TripleExpression<T> left, TripleExpression<T> right, Operator<T> op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public abstract T calc(T left, T right);

    public T evaluate(T x, T y, T z) {
        return makeCalculations(x, y, z);
    }

    public abstract void checkForException(int l, int r);

    private T makeCalculations(T x1, T x2, T x3) {
        T l = left.evaluate(x1, x2, x3);
        T r = right.evaluate(x1, x2, x3);
        return calc(l, r);
    }

    protected abstract String sign();

}

package expression;

public abstract class Unary<T> extends MainAbstract implements Operations, TripleExpression<T> {
    protected final TripleExpression<T> cur;
    protected final Operator<T> op;
    public Unary(TripleExpression<T> x, Operator<T> op) {
        this.op = op;
        cur = x;
    }

    public abstract String sign();

    public Unary() {
        op = null;
        cur = null;
    }

    public abstract void checkForExceptions(int cur);
    protected abstract T calc(T x1);
    @Override
    public T evaluate(T x, T y, T z) {
        return calc(cur.evaluate(x, y, z));
    }

    @Override
    public int priority() {
        return 0;
    }

}

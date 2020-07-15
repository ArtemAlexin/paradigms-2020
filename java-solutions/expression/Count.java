package expression;

public class Count<T> extends Unary<T> {
    public Count(TripleExpression<T> x, Operator<T> op) {
        super(x, op);
    }

    @Override
    public String sign() {
        return "count";
    }

    @Override
    public void checkForExceptions(int cur) {
        return;
    }

    @Override
    protected T calc(T x1) {
        return op.count(x1);
    }
}

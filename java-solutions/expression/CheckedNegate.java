package expression;

public class CheckedNegate<T> extends Unary<T> {

    public CheckedNegate(TripleExpression<T> x, Operator<T> op) {
        super(x, op);
    }

    public CheckedNegate() {
        super();
    }

    @Override
    public String sign() {
        return "-";
    }

    @Override
    public void checkForExceptions(int cur) {
       Calculations.checkNegateForException(cur, "Negate");
    }

    @Override
    protected T calc(T x1) {
        return op.negate(x1);
    }

    @Override
    public int priority() {
        return 0;
    }

}

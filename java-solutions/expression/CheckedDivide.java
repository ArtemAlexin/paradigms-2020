package expression;


public class CheckedDivide<T> extends BinaryOperations<T> {
    public CheckedDivide(TripleExpression<T> left, TripleExpression<T> right, Operator<T> op) {
        super(left, right, op);
    }

    public CheckedDivide() {
        super();
    }

    @Override
    public T calc(T left, T right) {
        return op.divide(left, right);
    }


    @Override
    public void checkForException(int l, int r) {
        Calculations.checkDivideForException(l, r, "Divide");
    }

    @Override
    protected String sign() {
        return "/";
    }

    @Override
    public int priority() {
        return 1;
    }
}

package expression;

public class CheckedAdd<T> extends BinaryOperations<T> {
    public CheckedAdd(TripleExpression<T> left, TripleExpression<T> right, Operator<T> op) {
        super(left, right, op);
    }

    public CheckedAdd() {
    }

    @Override
    public T calc(T left, T right) {
        return op.add(left, right);
    }


    @Override
    public void checkForException(int l, int r) {
        Calculations.checkAddForException(l, r, "Add");
    }

    @Override
    protected String sign() {
        return "+";
    }

    @Override
    public int priority() {
        return 2;
    }

}

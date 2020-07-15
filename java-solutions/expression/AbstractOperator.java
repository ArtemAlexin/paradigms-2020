package expression;

import java.util.function.Function;

@FunctionalInterface
interface CheckedBinary<T> {
    void check(T x1, T x2, String op);
}

@FunctionalInterface
interface CheckedUnary<T> {
    void check(T x1, String op);
}

@FunctionalInterface
interface CalculateBinary<T> {
     T calc(T x1, T x2);
}

class Pair<U, V> {
    final U first;
    final V second;
    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }
}

// :NOTE: bounds on type T required b/c now I can divide String by String
public abstract class AbstractOperator<T> implements Operator<T> {
    protected boolean checkForOverflow = false;

    protected AbstractOperator(boolean checkForOverflow) {
        this.checkForOverflow = checkForOverflow;
    }
    protected AbstractOperator() {}
    protected T calcBinaryOperation(T x1, T x2, CheckedBinary<T> f, CalculateBinary<T> g, String op) {
        return calcOperations(x1, x2, (x,s)->{}, f, x->null, g, op).second;
    }
    protected T calcUnaryOperation(T x1, CheckedUnary<T> f, Function<T, T> g, String op) {
        return calcOperations(x1, x1, f, (x,y,s)->{}, g, (x,y)->null, op).first;
    }
    private Pair<T, T> calcOperations(T x1, T x2, CheckedUnary<T> f1, CheckedBinary<T> f2,
                                      Function<T, T> g1, CalculateBinary<T> g2, String op) {
        try {
            f1.check(x1, op);
            f2.check(x1, x2, op);
            return new Pair<>(g1.apply(x1), g2.calc(x1, x2));
        } catch (OverflowException e) {
            if (checkForOverflow) {
                return new Pair<>(null, null);
            }
            return new Pair<>(g1.apply(x1), g2.calc(x1, x2));
        } catch (ExpressionExeption e) {
            return new Pair<>(null, null);
        }
    }
}

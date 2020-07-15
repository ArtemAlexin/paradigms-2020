package expression;

public abstract class Single<T> extends MainAbstract implements TripleExpression<T> {
    public int priority() {
        return 0;
    }
}

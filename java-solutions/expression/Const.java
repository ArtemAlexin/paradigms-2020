package expression;

public class Const<T> extends Single<T> {
    private final T value;

    public Const(T value) {
        this.value = value;
    }


    @Override
    public T evaluate(T x, T y, T z) {
        return value;
    }
}

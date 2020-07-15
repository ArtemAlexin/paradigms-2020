package expression;

public interface Operator<T> {
    T add(T x1, T x2);
    T multiply(T x1, T x2);
    T divide(T x1, T x2);
    T subtract(T x1, T x2);
    T negate(T x1);
    T parse(String x);
    T count(T x);
    T min(T x1, T x2);
    T max(T x1, T x2);
}

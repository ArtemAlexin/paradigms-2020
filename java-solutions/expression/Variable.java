package expression;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Variable<T> extends Single<T> {
    public static final Set<String> VARIABLES = new HashSet<>(Set.of(
            "x",
            "y",
            "z"
    ));
    private String var;

    public Variable(String var) {
        this.var = var;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        int i = 0;
        for (String o : VARIABLES) {
            if (var.equals(o)) {
                return Arrays.asList(x, y, z).get(i);
            }
            i++;
        }
        return null;
    }
}

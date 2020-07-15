package expression;

public class Calculations {

    public static void checkMulForException(int l, int r, String type) {
        if(l == 0 || r == 0) return;
        if (l == Integer.MIN_VALUE || r == Integer.MIN_VALUE) {
            throw new OverflowException(type, l + "*" + r);
        }
        int sign = Integer.signum(l) * Integer.signum(r);
        if (sign > 0) {
            if (Math.abs(l) > Integer.MAX_VALUE / Math.abs(r)) {
                throw new OverflowException(type, l + "*" + r);
            }
        } else {
            if (sign * Math.abs(l) < Integer.MIN_VALUE / Math.abs(r)) {
                throw new OverflowException(type, l + "*" + r);
            }
        }
    }
    public static void checkAddForException(int l, int r, String type) {
        if(l > 0 && r > 0 && l > Integer.MAX_VALUE - r || l < 0 && r < 0 && l < Integer.MIN_VALUE - r) {
            throw new OverflowException(type, l + "+" + r);
        }
    }
    public static void checkDivideForException(int l, int r, String type) {
        if (r == -1 && l == Integer.MIN_VALUE) {
            throw new OverflowException(type, l + "/" + r);
        }
        if (r == 0) {
            throw new DivisionByZeroException("Division by zero", l + "/" + r);
        }
    }

    public static void checkNegateForException(int cur, String type) {
        if(cur == Integer.MIN_VALUE)
            throw new OverflowException(type, Integer.toString(cur));
    }

    public static void checkSubtractForException(int l, int r, String type) {
        if(r > 0 && l < Integer.MIN_VALUE + r || r < 0 && l > Integer.MAX_VALUE + r)
            throw new OverflowException(type, l + "-" + r);
    }
}

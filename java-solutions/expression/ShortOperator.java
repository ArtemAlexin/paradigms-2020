package expression;

public class ShortOperator extends AbstractOperator<Short> {
    @Override
    public Short add(Short x1, Short x2) {
        return calcBinaryOperation(x1, x2, (x, y, s) -> {
        }, (x, y) -> (short) (x + y), "Add");
    }

    @Override
    public Short multiply(Short x1, Short x2) {
        return calcBinaryOperation(x1, x2, (x, y, s) -> {
        }, (x, y) -> (short) (x * y), "Multiply");
    }

    @Override
    public Short divide(Short x1, Short x2) {
        return calcBinaryOperation(x1, x2, (x, y, s) -> {
            if (x2 == 0) {
                throw new DivisionByZeroException(s, x + "/" + y);
            }
        }, (x, y) -> (short) (x / y), "Division");
    }

    @Override
    public Short subtract(Short x1, Short x2) {
        return calcBinaryOperation(x1, x2, (x, y, s) -> {
        }, (x, y) -> (short) (x - y), "Subtract");
    }

    @Override
    public Short negate(Short x1) {
        return calcUnaryOperation(x1, (x, s) -> {
        }, x -> (short) -x, "Negate");
    }

    @Override
    public Short parse(String x) {
        return (short) Integer.parseInt(x);
    }

    @Override
    public Short count(Short x1) {
        return calcUnaryOperation(x1, (x, s) -> {
        }, x -> (short) Integer.bitCount(Short.toUnsignedInt(x)), "Count");
    }

    @Override
    public Short min(Short x1, Short x2) {
        return calcBinaryOperation(x1, x2, (x, y, s) -> {
        }, (x, y) -> (short) Math.min(x, y), "Min");
    }

    @Override
    public Short max(Short x1, Short x2) {
        return calcBinaryOperation(x1, x2, (x, y, s) -> {
        }, (x, y) -> (short) Math.max(x, y), "Max");
    }
}

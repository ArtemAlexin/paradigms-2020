package expression;

public class DoubleOperator extends AbstractOperator<Double> {
    @Override
    public Double add(Double x1, Double x2) {
        return calcBinaryOperation(x1, x2, (x, y, z) -> {
        }, Double::sum, "Add");
    }

    @Override
    public Double multiply(Double x1, Double x2) {
        return calcBinaryOperation(x1, x2, (x, y, z) -> {
        }, (x, y) -> x * y, "Multiply");
    }

    @Override
    public Double divide(Double x1, Double x2) {
        return calcBinaryOperation(x1, x2, (x, y, z) -> {
        }, (x, y) -> x / y, "Divide");
    }

    @Override
    public Double subtract(Double x1, Double x2) {
        return calcBinaryOperation(x1, x2, (x, y, z) -> {
        }, (x, y) -> x - y, "Multiply");
    }

    @Override
    public Double negate(Double x1) {
        return calcUnaryOperation(x1, (x,y)->{}, x->-x, "Negate");
    }

    @Override
    public Double parse(String x) {
        return Double.parseDouble(x);
    }

    @Override
    public Double count(Double x1) {
        return calcUnaryOperation(x1, (x,y)->{}, (x)->(double)(Long.bitCount(Double.doubleToLongBits(x))), "count");
    }

    @Override
    public Double min(Double x1, Double x2) {
        return calcBinaryOperation(x1, x2, (x, y, s)->{}, Math::min, "min");
    }

    @Override
    public Double max(Double x1, Double x2) {
        return calcBinaryOperation(x1, x2, (x, y, s)->{}, Math::max, "max");
    }
}

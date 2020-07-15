package expression;

public class IntegerOperator extends AbstractOperator<Integer> {

    public IntegerOperator(boolean settings) {
        super(settings);
    }

    @Override
    public Integer add(Integer x1, Integer x2) {
        return calcBinaryOperation(x1, x2, Calculations::checkAddForException, Integer::sum, "Add");
    }

    @Override
    public Integer multiply(Integer x1, Integer x2) {
        return calcBinaryOperation(x1, x2, Calculations::checkMulForException, (x, y) -> x * y, "Multiply");
    }

    @Override
    public Integer divide(Integer x1, Integer x2) {
        return calcBinaryOperation(x1, x2, Calculations::checkDivideForException, (x, y) -> x / y, "Divide");
    }

    @Override
    public Integer subtract(Integer x1, Integer x2) {
        return calcBinaryOperation(x1, x2, Calculations::checkSubtractForException, (x, y) -> x - y, "Subtract");
    }

    @Override
    public Integer negate(Integer x1) {
        return calcUnaryOperation(x1, Calculations::checkNegateForException, x -> -x, "Negate");
    }

    @Override
    public Integer parse(String x) {
        return Integer.parseInt(x);
    }

    @Override
    public Integer count(Integer x) {
        return calcUnaryOperation(x, (a, b)->{}, a->Integer.bitCount(x), "Count");
    }

    @Override
    public Integer min(Integer x1, Integer x2) {
        return calcBinaryOperation(x1, x2, (a,b,c)->{}, Math::min, "Min");
    }

    @Override
    public Integer max(Integer x1, Integer x2) {
        return calcBinaryOperation(x1, x2, (a, b, c)->{}, Math::max, "Max");
    }
}

package expression;

public class LongOperator extends AbstractOperator<Long> {
    @Override
    public Long add(Long x1, Long x2) {
        return calcBinaryOperation(x1, x2, (x, y, s) -> {
        }, Long::sum, "Add");
    }

    @Override
    public Long multiply(Long x1, Long x2) {
        return calcBinaryOperation(x1, x2, (x, y, s) -> {
        }, (x, y) -> x * y, "Multiply");
    }

    @Override
    public Long divide(Long x1, Long x2) {
        return calcBinaryOperation(x1, x2, (x, y, s)->{
            if(y == 0) {
                throw new DivisionByZeroException(s, x + "/" + y);
            }
        }, (x, y)->(x/y), "Division");
    }

    @Override
    public Long subtract(Long x1, Long x2) {
        return calcBinaryOperation(x1, x2, (x, y, s)->{}, (x,y)->x-y, "Subtract");
    }

    @Override
    public Long negate(Long x1) {
        return calcUnaryOperation(x1, (x,s)->{}, x->-x, "Negate");
    }

    @Override
    public Long parse(String x) {
        return Long.parseLong(x);
    }

    @Override
    public Long count(Long x1) {
        return calcUnaryOperation(x1, (x, s)->{}, x->(long)Long.bitCount(x), "Count");
    }

    @Override
    public Long min(Long x1, Long x2) {
        return calcBinaryOperation(x1, x2, (x,y,s)->{}, Math::min, "Min");
    }

    @Override
    public Long max(Long x1, Long x2) {
        return calcBinaryOperation(x1, x2, (x, y, s)->{}, Math::max, "Max");
    }
}

package expression;

import java.math.BigInteger;

public class BigIntegerOperator extends AbstractOperator<BigInteger> {
    @Override
    public BigInteger add(BigInteger x1, BigInteger x2) {
        return calcBinaryOperation(x1, x2, (x,y,s)->{}, BigInteger::add, "Add");
    }

    @Override
    public BigInteger multiply(BigInteger x1, BigInteger x2) {
        return calcBinaryOperation(x1, x2, (x,y,s)->{}, BigInteger::multiply, "Multiply");
    }

    @Override
    public BigInteger divide(BigInteger x1, BigInteger x2) {
        return calcBinaryOperation(x1, x2, (x,y,s)->{
            if(y.equals(BigInteger.ZERO)) throw new DivisionByZeroException("Multiply", x1 + "*" + x2);
        }, BigInteger::divide, "Divide");
    }

    @Override
    public BigInteger subtract(BigInteger x1, BigInteger x2) {
        return calcBinaryOperation(x1, x2, (x,y,s)->{}, BigInteger::subtract, "Subtract");
    }

    @Override
    public BigInteger negate(BigInteger x1) {
        return calcUnaryOperation(x1, (x,s)->{}, BigInteger::negate, "Negate");
    }

    @Override
    public BigInteger parse(String x) {
        return new BigInteger(x);
    }

    @Override
    public BigInteger count(BigInteger x) {
        return calcUnaryOperation(x, (a, b)->{}, a-> BigInteger.valueOf(a.bitCount()), "Count");
    }

    @Override
    public BigInteger min(BigInteger x1, BigInteger x2) {
        return calcBinaryOperation(x1, x2, (a,b,c)->{}, BigInteger::min, "Min");
    }

    @Override
    public BigInteger max(BigInteger x1, BigInteger x2) {
        return calcBinaryOperation(x1, x2, (a, b, c)->{}, BigInteger::max, "Max");
    }
}

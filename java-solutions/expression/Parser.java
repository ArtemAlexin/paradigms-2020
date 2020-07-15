package expression;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static expression.Variable.VARIABLES;

@FunctionalInterface
interface Op {
    <T> TripleExpression<T> get(TripleExpression<T> f, TripleExpression<T> s, Operator<T> op);
}

@FunctionalInterface
interface Op2 {
   <T> TripleExpression<T> apply(TripleExpression<T> f, Operator<T> op);
}

public abstract class Parser extends BaseParser {

    protected Parser(EquationSource source) {
        super(source);
    }

    public Parser() {
    }

    protected static final int MAXPRIORITY = 100;

    protected static final Map<String, Op2>UNARY = Map.of(
            "-", CheckedNegate::new,
            "count", Count::new
    );

    protected static final Map<String, Op> O = Map.of(
            "+", CheckedAdd::new,
            "-", CheckedSubtract::new,
            "/", CheckedDivide::new,
            "*", CheckedMultiply::new,
            "max", Max::new,
            "min", Min::new
    );

    protected static final Map<String, Integer> PRIORITIES = new HashMap<>();

    static {
        for (Map.Entry<String, Op> entry : O.entrySet()) {
            // :NOTE: where is parameterization of BinaryOperations?
			// :it happened by chance, fixed
            PRIORITIES.put(entry.getKey(), ((BinaryOperations<?>)entry.getValue().get(null, null, null)).priority());
        }
        PRIORITIES.put(")", MAXPRIORITY + 1);
    }

    protected static final Set<String> OPERATIONS = new HashSet<>();

    static {
        OPERATIONS.addAll(UNARY.keySet());
        OPERATIONS.addAll(PRIORITIES.keySet());
    }

    protected static final Trie trie;

    static {
        trie = new Trie();
        for (String op : OPERATIONS) {
            trie.insert(op);
        }
    }

    protected boolean beginsWithBracket() {
        return test('(');
    }

    protected int getPriority(String t) {
        return PRIORITIES.getOrDefault(t, MAXPRIORITY + 2);
    }

    protected boolean isUnaryOperation(String operation) {
        return UNARY.containsKey(operation);
    }

    protected boolean isBeginningOfNumber(char c) {
        return Character.isDigit(c) || c == '-';
    }

    protected boolean isBinaryOperation(String o) {
        return PRIORITIES.containsKey(o);
    }

    protected boolean isAllowedVariable(String o) {
        return VARIABLES.contains(o);
    }

    protected String checkForOperation(String operation) throws IllegalOperationException {
        if (!OPERATIONS.contains(operation)) {
            throw new IllegalOperationException("illegal argument", getPos());
        }
        return operation;
    }

    protected void checkForNumber(boolean hasWhitespace) throws IllegalNumberException {
        if (between('0', '9') && hasWhitespace) {
            throw new IllegalNumberException("Spaces in numbers", getPos());
        }
    }

    protected void checkForCloseBracket(boolean b) throws UnclosedBracketException {
        if (b) {
            throw new UnclosedBracketException("extra close bracket", getPos());
        }
    }

    protected void checkForOperation(StringBuilder cur) throws IllegalArgumentException {
        if (cur.length() == 0) {
            throw new IllegalArgumentException(isEnd() ? "operand is missed" : "Illegal argument", isEnd() ? getPos() : getPos() + 1);
        }
    }

    protected void checkForClosedBracketOrBinaryOperation(String o) throws IllegalOperationException {
        if (!isBinaryOperation(o)) {
            return;
        }
        if (o.equals(")")) {
            throw new IllegalOperationException("close bracket instead operand", getPos());
        }
        throw new IllegalOperationException("binary operation has no two arguments, probably on illegal position", getPos());
    }
}

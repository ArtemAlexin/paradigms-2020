package expression;


public class ExpressionParser<T> extends Parser {
    protected Operator<T> mod;
    public ExpressionParser(EquationSource source, Operator<T> mod) {
        super(source);
        this.mod = mod;
    }

    public ExpressionParser(Operator<T> mod) {
        this.mod = mod;
    }

    public TripleExpression<T> parse(String source) throws ParsingExceptions {
        return new EquationParser(new StringSource(source)).parse();
    }

    private class EquationParser extends Parser {
        protected EquationParser(EquationSource source) {
            super(source);
            nextChar();
        }

        private TripleExpression<T> parse() throws ParsingExceptions {
            TripleExpression<T> cur = parseBinaryExpression(MAXPRIORITY);
            checkForCloseBracket(!isEnd());
            return cur;
        }

        private TripleExpression<T> parseEquation() throws ParsingExceptions {
            return parseBinaryExpression(MAXPRIORITY);
        }

        private TripleExpression<T> parseBinaryExpression(int priority) throws ParsingExceptions {
            TripleExpression<T> left = parseSimpleExpression();
            while (true) {
                if (isEnd()) {
                    return left;
                }
                String operation = checkForOperation(parseToken());
                int curPriority = getPriority(operation);
                if (curPriority >= priority) {
                    prev(operation.length());
                    return left;
                }
                TripleExpression<T> right = parseBinaryExpression(curPriority);
                left = getBinary(left, operation, right);
            }
        }

        private String parseNumber() throws IllegalNumberException {
            StringBuilder sb = new StringBuilder();
            boolean hasWhitespace = false;
            while (between('0', '9') || isWhiteSpace()) {
                checkForNumber(hasWhitespace);
                if (isWhiteSpace()) {
                    hasWhitespace = true;
                } else {
                    sb.append(ch);
                }
                nextChar();
            }
            return sb.toString();
        }

        private String parseToken() throws ParsingExceptions {
            skipWhitespace();
            if (between('0', '9')) {
                return parseNumber();
            } else {
                return parseOperationOrVariable();
            }
        }

        private String parseOperationOrVariable() throws IllegalArgumentException {
            StringBuilder cur = new StringBuilder();
            while (cur.length() == 0 && Character.isJavaIdentifierStart(ch) || Character.isJavaIdentifierPart(ch) && !isEnd()) {
                cur.append(ch);
                nextChar();
            }
            if (cur.length() != 0) {
                return cur.toString();
            }
            int id = 0;
            while ((id = trie.find(ch, id)) != -1) {
                cur.append(ch);
                nextChar();
            }
            checkForOperation(cur);
            return cur.toString();
        }

        private TripleExpression<T> getUnaryOperation(String op, TripleExpression<T> a) throws IllegalArgumentException {
            if (!UNARY.containsKey(op)) {
                throw new IllegalArgumentException("illegal argument", getPos());
            }
            return UNARY.get(op).apply(a, mod);
        }

        protected TripleExpression<T> tryToGetNumber(String o) throws IllegalNumberException {
            try {
                return new Const<>(mod.parse(o));
            } catch (NumberFormatException e) {
                throw new IllegalNumberException("Value is too long", getPos());
            }
        }

        private TripleExpression<T> getSingle(String o) throws ParsingExceptions {
            if (isBeginningOfNumber(o.charAt(0))) {
                return tryToGetNumber(o);
            } else if (isAllowedVariable(o)) {
                return new Variable<>(o);
            } else {
                throw new IllegalArgumentException("illegal argument", getPos());
            }
        }

        private TripleExpression<T> getBinary(TripleExpression<T> left, String op, TripleExpression<T> right) throws IllegalArgumentException {
            if (!O.containsKey(op)) {
                throw new IllegalArgumentException("illegal argument", getPos());
            }
            return O.get(op).get(left, right, mod);
        }

        private TripleExpression<T> parseSimpleExpression() throws ParsingExceptions {
            skipWhitespace();
            if (beginsWithBracket()) {
                TripleExpression<T> cur = parseEquation();
                checkForCloseBracket(ch != ')');
                nextChar();
                return cur;
            } else {
                String o = parseToken();
                if (isUnaryOperation(o)) {
                    skipWhitespace();
                    if (o.equals("-") && Character.isDigit(ch)) {
                        return getSingle("-" + parseNumber());
                    }
                    return getUnaryOperation(o, parseSimpleExpression());
                }
                checkForClosedBracketOrBinaryOperation(o);
                return getSingle(o);
            }
        }
    }
}

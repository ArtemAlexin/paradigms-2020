"use strict";
const createSuccessor = function (suc, parent) {
    suc.prototype = Object.create(parent);
    suc.prototype.constructor = suc;
};

const variablesArray = ["x",
    "y",
    "z"
];
const consts = new Set();
const operations = new Map();
const variables = new Map(variablesArray.map((s, id, variablesArray) => [s, id]));
const makeString = (pre, post, f, args) => pre + args.map(x => x[f.name]()).join(' ') + post;
const AbstractOperation = {
    evaluate: function (...other) {
        return this.getCalc().apply(this, this.getArgs().map(x => x.evaluate(...other)));
    },
    toString: function () {
        return makeString('', ' ' + this.getSign(), this.toString, this.getArgs());
    },
    postfix: function () {
        return makeString('(', ' ' + this.getSign() + ')', this.postfix, this.getArgs());
    },
    diff: function (variable) {
        return this.getCalcDiff().apply(this, Array.from(variable).concat(this.getArgs()));
    },
    prefix: function () {
        return makeString('(' + this.getSign() + ' ', ')', this.prefix, this.getArgs());
    },
};

const createOperation = function (name, calc, calcDiff) {
    function inner(...args) {
        this.getArgs = () => args;
        this.getCalc = () => calc;
        this.getSign = () => name;
        this.getCalcDiff = () => calcDiff;
    }

    createSuccessor(inner, AbstractOperation);
    inner.countOfArguments = () => calc.length;
    operations.set(name, inner);
    return inner;
};

const complexFunction = function (arg, calcDiff, v) {
    return new Multiply(arg.diff(v), calcDiff(arg))
};

const linearFunction = function (v, f, ...args) {
    return new f(...args.map(x => x.diff(v)));
};

const sum = function (...args) {
    return args.reduce((pr, cur) => pr + cur);
}

const sumExp = function (...args) {
    return args.reduce((pr, cur) => pr + Math.exp(cur), 0);
}

const Sumexp = createOperation('sumexp', sumExp, (v, ...args) => args.length === 0 ? CONSTS.ZERO() :
    new Sum(...args.map(x => new Exp(x).diff(v))));

const Softmax = createOperation('softmax', (...args) => args.length === 0 ? 0 : Math.exp(args[0]) / sumExp(...args),
    (v, ...args) => args.length === 0 ? CONSTS.ZERO() : new Divide(new Exp(args[0]), new Sumexp(...args)).diff(v));

const Sum = createOperation("sum", sum,
    (v, ...args) => linearFunction(v, Sum, ...args));

const Add = createOperation("+", (x, y) => x + y,
    (v, x, y) => linearFunction(v, Add, x, y));

const Subtract = createOperation("-", (x, y) => x - y,
    (v, x, y) => linearFunction(v, Subtract, x, y));

const Multiply = createOperation("*", (x, y) => x * y,
    (v, x, y) => new Add(new Multiply(x.diff(v), y), new Multiply(y.diff(v), x)));

const Divide = createOperation("/", (x, y) => x / y, (v, x, y) =>
    new Divide(new Subtract(new Multiply(x.diff(v), y), new Multiply(y.diff(v), x)), new Multiply(y, y)));

const Cosh = createOperation("cosh", x => Math.cosh(x),
    (v, x) => complexFunction(x, arg => new Sinh(arg), v));
const Sinh = createOperation("sinh", x => Math.sinh(x),
    (v, x) => complexFunction(x, arg => new Cosh(arg), v));

const Negate = createOperation("negate", x => -x,
    (v, x) => linearFunction(v, Negate, x));

const Power = createOperation("pow", (x, y) => Math.pow(x, y),
    (v, x, y) => new Exp(new Multiply(new Ln(x), y)).diff(v)
);
const Log = createOperation("log", (x, y) => Math.log(Math.abs(y)) / Math.log(Math.abs(x)),
    (v, x, y) => new Divide(new Ln(y), new Ln(x)).diff(v)
);
const Exp = createOperation("exp", x => Math.exp(x), (v, x) => complexFunction(x, x => new Exp(x), v));
const Ln = createOperation("ln", x => Math.log(Math.abs(x)), (v, x) =>
    complexFunction(x, x => new Divide(CONSTS.ONE(), x), v));


const createConstOrVariable = function (evaluate, diff) {
    function F(value) {
        this.getValue = () => value;
    }

    createSuccessor(F, AbstractOperation);
    F.prototype.prefix = F.prototype.toString = F.prototype.postfix = function () {
        return this.getValue().toString();
    };
    F.prototype.diff = diff;
    F.prototype.evaluate = evaluate;
    return F;
};

const Const = createConstOrVariable(function (...args) {
    return this.getValue();
}, x => CONSTS.ZERO());

const CONSTS = function () {
    let ZERO = new Const(0);
    let ONE = new Const(1);
    return {
        ZERO: () => ZERO,
        ONE: () => ONE,
    }
}();

const Variable = createConstOrVariable(function (...args) {
    return args[variables.get(this.getValue())];
}, function (variable) {
    return variable === this.getValue() ? CONSTS.ONE() : CONSTS.ZERO();
});

//trie is used to provide higher productivity. It's encapsulated and only one instance of trie exists. P.S :
// method add is optional and is used only in method fillTrie
const trie = function () {
    function Node(term = false) {
        let next = new Array(alphabet.size).fill(beginningOfTrie);
        this.getNext = (i) => next[i];
        this.setNext = (i, v) => next[i] = v;
        this.getTerminal = () => term;
        this.setTerminal = v => term = v;
    }

    function fillAlph() {
        let alph = new Map();
        operations.forEach((value, key) => Array.from(key).forEach(x => {
            if (!alph.has(x)) {
                alph.set(x, alph.size);
            }
        }));
        return alph;
    }

    function fillTrie(trieInst) {
        operations.forEach((value, key) => trieInst.add(key));
    }

    const beginningOfTrie = 0;
    const alphabet = fillAlph();
    let nodes = [new Node()];
    let res = {
        isTerminal: id => nodes[id].isTerminal,
        add: function (str) {
            let id = beginningOfTrie;
            Array.from(str).forEach((x, i) => {
                let c = alphabet.get(x);
                if (nodes[id].getNext(c) === beginningOfTrie) {
                    nodes.push(new Node((i === str.length - 1)));
                    nodes[id].setNext(c, nodes.length - 1);
                }
                id = nodes[id].getNext(c);
            });
            nodes[id].setTerminal(true);
        },
        contains: function (sign, id) {
            if (id === -1) {
                return id;
            }
            if (alphabet.has(sign)) {
                let d = alphabet.get(sign);
                if (nodes[id].getNext(d) !== 0) {
                    return nodes[id].getNext(d);
                }
            }
            return -1;
        },
        containsString: function (str) {
            let id = beginningOfTrie;
            Array.from(str).forEach(x => {
                if (id === -1) {
                    return id;
                }
                id = this.contains(x, id);
            });
            return id;
        }
    };
    fillTrie(res);
    return res;
}();
const StringSource = function () {
    let ch, str;
    this.setDefault = s => {
        ch = 0;
        str = s
    };
    this.getSource = () => str;
    this.getCh = () => ch;
    this.setSource = s => str = s;
    this.setCh = id => ch = id;
    this.nextChar = () => str.charAt(ch++);
    this.curChar = () => str.charAt(ch);
    this.hasNextChar = () => ch < str.length;
    this.showNextChar = () => str.charAt(ch + 1);
    this.test = c => {
        this.skipWhitespaces();
        if (this.curChar() === c) {
            this.nextChar();
            return true;
        }
        return false;
    };
    this.skipWhitespaces = () => {
        for (; this.curChar() === " "; this.nextChar());
    };
    this.isEndOfExpression = () => {
        this.skipWhitespaces();
        return !this.hasNextChar();
    }
};

const isDigit = x => x >= '0' && x <= '9';
const isVariable = s => variables.has(s);
const isBeginningOfIdentifier = x => x >= 'a' && x <= 'z' || x >= 'A' && x <= 'Z' || x === '_';
const isPartOfIdentifier = x => isBeginningOfIdentifier(x) || isDigit(x);
const isOperation = s => operations.has(s);

const identity = x => x;
const makeCommonActions = (action, f) => {
    return function (...args) {
        action(...args);
        return f();
    }
};
const checkAndAction = function (action, check, reducer) {
    let res = action();
    check(res);
    return reducer(res);
};
const getNumber = function (s) {
    return consts.has(s) ? consts.get(s) : new Const(Number.parseFloat(s));
};
const getPrimitiveOrFunction = function (token) {
    return isOperation(token) ? operations.get(token) :
        isVariable(token) ? new Variable(token) : getNumber(token);
};
const getFilter = (endFilter, argsFilter) => (token, args, isEnd) => isOperation(token) === (endFilter(isEnd) && argsFilter(args));
const unnecessaryFilter = (...args) => true;


const ParsingError = function (message, position) {
    this.message = "Error: " + message + ", position: " + position;
};
createSuccessor(ParsingError, Error.prototype);

const createError = function (message, name) {
    let ParseError = function (position, token = "") {
        ParsingError.call(this, message, position - token.length);
        this.name = name;
    };
    createSuccessor(ParseError, ParsingError.prototype);
    return ParseError;
};
const UnclosedBracketError = createError("unclosed bracket", "UnclosedBracketError");
const InvalidArgumentError = createError("invalid argument", "InvalidArgumentError");
const ExtraArgumentError = createError("extra argument found", "ExtraArgumentError");
const MissingArgumentError = createError("expected argument, but end of expression found", "MissingArgumentError");
const IllegalNumberOfArgument = createError("invalid number of arguments", "IllegalNumberOfArgument");

//:NOTE: too many code for parser. Limit is 100 lines (without blank lines)
//:ANS: fixed
const Parser = new function () {
    const stringSource = new StringSource();
    const getParser = (getOperation, filter) => getParsingFunc(str => parseExpression(getOperation, filter));
    const checkForError = (condition, er, arg) => {
        if (condition()) throw new er(stringSource.getCh() + 1, arg);
    };
    const curChar = stringSource.curChar;
    const nextChar = stringSource.nextChar;
    const getParsingFunc = f => makeCommonActions(source => stringSource.setDefault(source), f);
    const isEndOfExpression = stringSource.isEndOfExpression;
    const isEndOfInnerExpression = () => isEndOfExpression() || curChar() === ')';
    const test = stringSource.test;
    const isBeginningOfNumber = () => isDigit(curChar()) || (curChar() === '-') && isDigit(stringSource.showNextChar());
    function parseAccordingToFunction(f, prefix = "") {
        let pos = stringSource.getCh();
        for (; f(curChar()); nextChar());
        return prefix + stringSource.getSource().substring(pos, stringSource.getCh());
    }
    const parseVariableOrOperation = () => checkAndAction(() => {
        let res = parseAccordingToFunction(isPartOfIdentifier), id = trie.containsString(res);
        if (stringSource.hasNextChar() && id >= 0) {
            res = parseAccordingToFunction(x => (id = trie.contains(x, id)) !== -1, res);
        }
        return res;
    },res => checkForError(() => !(isVariable(res) || isOperation(res)), InvalidArgumentError, res), identity);
    function parseToken() {
        checkForError(isEndOfExpression, MissingArgumentError);
        return isBeginningOfNumber() ? checkAndAction(() => parseAccordingToFunction(isDigit, nextChar()),
            res => checkForError(() =>
                !isFinite(res) || res.length === 0, InvalidArgumentError, res), identity) : parseVariableOrOperation();
    }
    function parseExpression(getOperation, filter) {
        function parseExpressionInBracketsOrPrimitive(f, array) {
            if (test('(')) {
                return checkAndAction(parseSimpleExpression,
                        res => checkForError(() => !test(')'), UnclosedBracketError), identity);
            }
            return checkAndAction(parseToken, token => checkForError(
                () => !f(token, array, isEndOfInnerExpression), InvalidArgumentError, token), getPrimitiveOrFunction);
        }
        function parseSimpleExpression() {
            let array = [];
            for (; !isEndOfInnerExpression(); array.push(parseExpressionInBracketsOrPrimitive(filter, array))) ;
            return getOperation(...array);
        }
        return checkAndAction(() => parseExpressionInBracketsOrPrimitive(token =>
            !isOperation(token)), () => checkForError(() => !isEndOfExpression(), ExtraArgumentError), identity)
    }
    const checkElementForOperation = (getId) => (...args) => checkOperation(
        () => args.filter((x, i) => getId(args) !== i), getId(args), ...args);
    function checkOperation(getOperation, id, ...args) {
        checkForError(() => args.length === 0 || args[id].countOfArguments() !== 0
            && args[id].countOfArguments() !== args.length - 1, IllegalNumberOfArgument);
        return new args[id](...getOperation());
    }
    this.parsePrefixExpr = getParser(checkElementForOperation(arg => 0)
        , getFilter(unnecessaryFilter, args => args.length === 0));
    this.parsePostfixExpr = getParser(checkElementForOperation(args => args.length - 1),
        getFilter(f => f(), unnecessaryFilter));
}();

const parsePrefix = function (str) {
    return Parser.parsePrefixExpr(str);
};
const parsePostfix = function (str) {
    return Parser.parsePostfixExpr(str);
};

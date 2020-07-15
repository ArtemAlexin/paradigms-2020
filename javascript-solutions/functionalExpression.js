"use strict";
const variablesArray = ["x",
    "y",
    "z"
];
const variables = new Map(variablesArray.map((s, id, variablesArray) => [s, id]));
const consts = new Map();
const operations = new Map();

function operation(calc, name) {
    let f = (...args) => (...vals) => {
        let res = args.slice();
        res.forEach((val, i, res) => res[i] = res[i](...vals));
        return calc(...res);
    };
    operations.set(name, f);
    f.countOfArguments = calc.length;
    return f;
}

const add = operation((a, b) => a + b, "+");
const sin = operation(x => Math.sin(x), "sin");
const cos = operation(x => Math.cos(x), "cos");
const subtract = operation((a, b) => a - b, "-");
const divide = operation((a, b) => a / b, "/");
const multiply = operation((a, b) => a * b, "*");
const negate = operation(a => -a, "negate");
const med3 = operation( (a, b, c) =>  med(a, b, c), "med3");
function med(...args) {
    let ans = args.slice();
    ans.sort((x, y) => x - y);
    return ans[Math.floor(ans.length / 2)];
}
function sum(...args) {
    let res = 0;
    args.forEach(x => res+=x);
    return res;
}
const avg5 = operation((x1, x2, x3, x4, x5) => sum(x1, x2, x3, x4, x5) / 5, "avg5");
const cnst = left => (...args) => left;
const variable = left => function (...args) {
    return args[variables.get(left)];
};

function makeConst(first, second) {
    let res = cnst(second);
    consts.set(first, res);
    return res;
}

const pi = makeConst("pi", Math.PI);
const e = makeConst("e", Math.E);

function parse(str) {
    const nextChar = () => str.charAt(ch++);
    const isOperation = s => operations.has(s);
    const curChar = () => str.charAt(ch);
    const hasNextChar = () => ch < str.length;
    const isVariable = s => variables.has(s);

    function skipWhitespaces() {
        for (; curChar() === " "; nextChar()) ;
    }

    function parseToken() {
        let res = "";
        for (; curChar() !== " " && hasNextChar(); res = res.concat(nextChar()));
        return res;
    }

    function getOperation(s) {
        let g = operations.get(s), res = [];
        for (let i = 0; i < g.countOfArguments; i++) {
            res.push(stack.pop());
        }
        res.reverse();
        return g(...res);
    }

    function getNumber(s) {
        return consts.has(s) ? consts.get(s) : cnst(Number.parseFloat(s));
    }

    function parseTokenAndPush() {
        skipWhitespaces();
        let s = parseToken();
        stack.push(isOperation(s) ? getOperation(s) : isVariable(s) ? variable(s) : getNumber(s));
    }

    let stack = [];
    let ch = 0;
    while (hasNextChar()) {
        parseTokenAndPush();
    }
    return stack[0];
}

function test() {
    let expression = parse('x x * 2 x * - 1 +');
    for (let x = 0; x <= 10; x++) {
        console.log(expression(x, 2, 3));
    }
}
test();

package expression;

public abstract class BaseParser {
    private final EquationSource source;
    protected char ch;

    protected BaseParser(final EquationSource source) {
        this.source = source;
    }

    public BaseParser() {
        source = null;
    }

    protected void skipWhitespace() {
        while (isWhiteSpace()) nextChar();
    }

    protected boolean isEnd() {
        skipWhitespace();
        return ch == '\0';
    }
    protected boolean isWhiteSpace() {
        return ch == ' ' || ch == '\r' || ch == '\t';
    }
    protected void nextChar() {
        ch = '\0';
        if(source.hasNext()) {
            ch = source.next();
            return;
        }
        source.skip(1);
    }
    public int getPos() {
        return source.getPos() - 1;
    }

    protected boolean test(char c) {
        if(ch == c) {
            nextChar();
            return true;
        }
        return false;
    }
    protected boolean test(String expected) {
        for(int i = 0; i < expected.length(); i++) {
            if(!test(expected.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    protected void prev(int delta) {
        source.back(delta);
        ch = source.charAt(source.getPos() - 1);
    }


    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
}

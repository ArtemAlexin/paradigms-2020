package expression;


public class StringSource implements EquationSource {
    private final String data;
    private int pos;

    public StringSource(final String data) {
        this.data = data;
    }
    @Override
    public boolean hasNext() {
        return pos < data.length();
    }
    public void skip(int cnt) {
        pos+=cnt;
    }
    @Override
    public char next() {
        return data.charAt(pos++);
    }
    public int getPos() {
        return pos;
    }
    @Override
    public void back(int delta) {
        pos-=delta;
    }
    public boolean hasChar(int id) {
        return id < data.length();
    }
    public char charAt(int pos) {
        return data.charAt(pos);
    }
}

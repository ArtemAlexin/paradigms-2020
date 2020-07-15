package expression;


public interface EquationSource {
    boolean hasNext();
    boolean hasChar(int delta);
    char next();
    char charAt(int pos);
    void back(int delta);
    int getPos();
    void skip(int cnt);
}

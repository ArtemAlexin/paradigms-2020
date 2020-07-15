package expression;

public abstract class MainAbstract implements Operations {
    public boolean equals(Object o) {
        if(o!= null && o.getClass() == this.getClass()) {
            return this.toString().equals(o.toString());
        } else {
            return false;
        }
    }
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}

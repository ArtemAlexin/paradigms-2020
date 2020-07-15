package queue;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue, Iterable<Object> {
    protected int size;

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    public Object element() {
        if (size > 0) {
            return getElement();
        }
        throw new NoSuchElementException("queue is empty");
    }

    @Override
    public void enqueue(Object a) {
        checkForNull(a);
        doEnqueue(a);
        size++;
    }

    private void checkForNull(Object a) {
        if(a == null) {
            throw new IllegalArgumentException("null can not be added in queue");
        }
    }

    @Override
    public Object dequeue() {
        Object t = element();
        doDequeue();
        size--;
        return t;
    }

    @Override
    public void clear() {
        size = 0;
        doClear();
    }

    public Queue map(Function<Object, Object> f) {
        return actOnFunction(f, x->true);
    }
    public Queue filter(Predicate<Object> f) {
        return actOnFunction(x->x, f);
    }

    private Queue actOnFunction(Function<Object, Object> g, Predicate<Object> f) {
        Queue ans = this.getQueue();
        for (Object o : this) {
            if (f.test(o)) {
                Object t = g.apply(o);
                checkForNull(t);
                ans.enqueue(t);
            }
        }
        return ans;
    }
    protected abstract void doClear();

    protected abstract void doDequeue();

    protected abstract void doEnqueue(Object a);

    protected abstract Queue getQueue();

    protected abstract Object getElement();

}

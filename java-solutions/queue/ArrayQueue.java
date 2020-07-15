package queue;

import java.util.Iterator;

public class ArrayQueue extends AbstractQueue{
    private int front;
    private final int defaultSize = 2;
    private Object[] arr;
    private int back() {
        return (front + size) % arr.length;
    }
    public ArrayQueue() {
        this.clear();
    }

    private int add(int id) {
        return (++id) % arr.length;
    }

    private void extend() {
        Object[] arr2 = new Object[arr.length * 2];
        System.arraycopy(arr, front, arr2, 0, arr.length - front);
        System.arraycopy(arr, 0, arr2, arr.length - front, front);
        front = 0;
        arr = arr2;
    }

    public void doEnqueue(Object t) {
        if (size == arr.length) {
            extend();
        }
        assign(back(), t);
    }

    @Override
    protected Queue getQueue() {
        return new ArrayQueue();
    }

    @Override
    protected Object getElement() {
        return arr[front];
    }

    public void doDequeue() {
        front = assign(front, null);
    }


    private int assign(int push, Object value) {
        arr[push] = value;
        push = add(push);
        return push;
    }

    public void doClear() {
        arr = new Object[defaultSize];
        front = 0;
    }

    @Override
    public Iterator<Object> iterator() {
        return new Iterator<>() {
            int id = front;
            int passed = 0;

            @Override
            public boolean hasNext() {
                return (passed < size);
            }

            @Override
            public Object next() {
                Object ans = arr[id];
                id = add(id);
                passed++;
                return ans;
            }
        };
    }
}

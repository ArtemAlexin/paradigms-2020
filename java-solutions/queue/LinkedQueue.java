package queue;


import java.util.Iterator;
import java.util.Optional;

public class LinkedQueue extends AbstractQueue {

    // :NOTE: to hard and redundant logic
    // ANS: fixed
    private Node front;
    private Node back;

    public LinkedQueue() {
        clear();
    }

    @Override
    protected void doDequeue() {
        // :NOTE: direct access and modification of `next`
        // ANS : fixed
        front = front.getNext();
        if(front == null) {
            back = null;
        }
    }

    @Override
    protected void doEnqueue(Object a) {
        if(back != null) {
            back = back.next = new Node(a);
        } else {
            back = front = new Node(a);
        }
    }

    @Override
    protected Queue getQueue() {
        return new LinkedQueue();
    }

    @Override
    protected Object getElement() {
        return front.getValue();
    }

    @Override
    public void doClear() {
        front = back = null;
    }

    @Override
    public Iterator<Object> iterator() {
        return new Iterator<>() {
            Node cur = front;
            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public Object next() {
                Object ans = cur.getValue();
                cur = cur.getNext();
                return ans;
            }
        };
    }

    private static class Node {
        // :NOTE: why it's not private and final?
        // ANS :private is fixed. It is not final, because in this case working with final field becomes inconvenient,
        // moreover without final program does not become less  productive and protected
        private Node next;
        private final Object value;

        public Node getNext() {
            return next;
        }

        public Object getValue() {
            return value;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node(Node next, Object value) {
            this.next = next;
            this.value = value;
        }

        Node(Object value) { ;
            this.value = value;
        }
    }
}

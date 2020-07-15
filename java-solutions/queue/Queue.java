package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Queue {
    // :NOTE: not all constraints mentioned (2 missed)
    // ANS: fixed dequeue and map
    //Pre : f != null, ADDED: f(ai) != null for all i < n
    //Post : R = a1', a2' ... an' : ai' = f(ai)
    Queue map(Function<Object, Object> f);
    //Pre : f != null
    //Post : R = aj1, aj2, ... ajk : ajk = at(для некоторого t) && jk < jp -> t(k) < t(p) && f(ajk) = true, forAll r : f(ar) = true ar in R
    Queue filter(Predicate<Object> f);

    //Pre : a != null
    //Post : a'[n] = t, a'[i] = a[i] for all i < n
    void enqueue(Object a);
    //Pre : нет
    //Post: R = size
    int size();

    //Pre : size > 0
    //Post : R = a0, ADDED: a'[i] = a[i] for all i > 0
    Object dequeue();
    //Pre : нет
    //Post : R = true && size == 0 || R = false , size != 0
    boolean isEmpty();

    //Pre: size > 0
    //Post: R = arr0
    Object element();
    //Pre : нет
    //Post : size = 0, queue is empty
    void clear();
}

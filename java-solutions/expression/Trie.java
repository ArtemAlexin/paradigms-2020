package expression;

import java.util.HashMap;
import java.util.Map;

public class Trie {
    private static final Map<Character, Integer> M = new HashMap<>(Map.of(
            ')', 26,
            '*', 27,
            '/', 28,
            '+', 29,
            '-', 30,
            '2', 31,
            '>', 32,
            '<', 33
    ));
    private Node[] trie;
    private int size;
    public Trie() {
        trie = new Node[100];
        for(int i = 0; i < 100; i++) {
            trie[i] = new Node();
        }
        size = 0;
    }
    public void insert(String s) {
        int id = 0;
        for(int i = 0; i < s.length(); i++) {
            int c = M.getOrDefault(s.charAt(i), s.charAt(i) - 'a');
            if(trie[id].next[c] == 0) {
                trie[id].next[c] = ++size;
            }
            id = trie[id].next[c];
        }
        trie[id].term = true;
    }
    public int find(char c, int id) {
        if(M.containsKey(c) || Character.isLetter(c)) {
            int d = M.getOrDefault(c, c - 'a');
            if (trie[id].next[d] != 0) {
                return trie[id].next[d] ;
            }
        }
        return -1;
    }
}

class Node {
    int[] next;
    boolean term;
    Node() {
        next = new int[34];
        term = false;
    }
}
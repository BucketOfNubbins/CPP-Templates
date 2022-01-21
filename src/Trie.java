public class Trie<E> {
    static class Node<E> {
        Node<E>[] children;
        E data;

        @SuppressWarnings("unchecked")
        public Node(int size) {
            children = new Node[size];
            data = null;
        }
    }

    Node<E> root;
    int size;

    public Trie(int size) {
        this.size = size;
        this.root = new Node<>(size);
    }

    // string should only have values between [0, size)
    public void add(int[] string, E data) {
        Node<E> cur = root;
        for (int c : string) {
            if (cur.children[c] == null) {
                cur.children[c] = new Node<>(size);
            }
            cur = cur.children[c];
        }
        cur.data = data;
    }

    public void add(char[] string, E data) {
        Node<E> cur = root;
        for (char c : string) {
            if (cur.children[c] == null) {
                cur.children[c] = new Node<>(size);
            }
            cur = cur.children[c];
        }
        cur.data = data;
    }

    public E get(int[] string) {
        Node<E> cur = root;
        for (int i = 0; i < string.length && cur != null; i++) {
            cur = cur.children[string[i]];
        }
        return cur == null ? null : cur.data;
    }

    public E get(char[] string) {
        Node<E> cur = root;
        for (int i = 0; i < string.length && cur != null; i++) {
            cur = cur.children[string[i]];
        }
        return cur == null ? null : cur.data;
    }

}

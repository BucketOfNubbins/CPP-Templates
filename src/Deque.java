public class Deque {
    int[] array;
    int left;
    int right;
    int size;

    public Deque() {
        array = new int[4];
        left = 0;
        right = 0;
        size = 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(int x) {
        if (this.size() == array.length) {
            this.resize(array.length * 2);
        }
        left = getPreviousIndex(left);
        array[left] = x;
        size++;
    }

    public void addLast(int x) {
        if (this.size() == array.length) {
            this.resize(array.length * 2);
        }
        array[right] = x;
        right = getNextIndex(right);
        size++;
    }

    public int popFirst() {
        if (size == 0) {
            throw new IllegalStateException("Cannot pop from empty deque.");
        }
        int x = array[left];
        array[left] = 0;
        left = getNextIndex(left);
        if (this.size() < array.length / 4) {
            this.resize(array.length / 2);
        }
        size--;
        return x;
    }

    public int popLast() {
        if (size == 0) {
            throw new IllegalStateException("Cannot pop from empty deque.");
        }
        right = getPreviousIndex(right);
        int x = array[right];
        array[right] = 0;
        if (this.size() < array.length / 4) {
            this.resize(array.length / 2);
        }
        size--;
        return x;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(String.format("Cannot get index %d for deque of size %d.", index, size));
        }
        int i = left + index;
        if (i >= array.length) {
            i = index - (array.length - left);
        }
        return array[i];
    }

    private int getPreviousIndex(int i) {
        i--;
        if (i < 0) {
            i = array.length - 1;
        }
        return i;
    }

    private int getNextIndex(int i) {
        i++;
        if (i >= array.length) {
            i = 0;
        }
        return i;
    }

    private void resize(int newLength) {
        int[] next = new int[newLength];
        int nextLeft = 0;
        int nextRight = 0;
        int i = left;
        do {
            next[nextRight++] = array[i];
            i = getNextIndex(i);
        } while (i != right);
        array = next;
        left = nextLeft;
        right = nextRight;
    }
}
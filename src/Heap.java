import java.util.*;

@SuppressWarnings("unchecked")
class Heap<E extends Comparable<E>> {
    private Object[] data;
    private int size;

    public Heap() {
        data = new Object[16];
        size = 1;
    }

    public Heap(E[] arr) {
        data = arr;
        size = arr.length;
        for (int i = size >> 1; i > 0; i--) {
            heapify(i);
        }
    }

    public int size() {
        return size - 1;
    }

    private int left(int i) {
        long a = ((long) i) << 1;
        return a + 1 > size ? -1 : (int) a;
    }

    private int right(int i) {
        long a = ((long) i) << 1;
        return a + 1 > size ? -1 : (int) a + 1;
    }

    private int getParent(int i) {
        return i >> 1;
    }

    private void swap(int a, int b) {
        Object temp = data[a];
        data[a] = data[b];
        data[b] = temp;
    }

    public void add(E element) {
        if (size == data.length) {
            if (size >= Integer.MAX_VALUE >> 1) {
                data = Arrays.copyOf(data, Integer.MAX_VALUE);
            } else {
                data = Arrays.copyOf(data, data.length << 1);
            }
        }
        data[size] = element;
        bUp(size);
        size++;
    }

    private void bUp(int index) {
        int pIndex = getParent(index);
        while (pIndex != 0 && ((E) data[index]).compareTo((E) data[pIndex]) < 0) {
            swap(index, pIndex);
            index = pIndex;
            pIndex = getParent(index);
        }
    }

    private void heapify() {
        data[1] = data[size - 1];
        size--;
        heapify(1);
    }

    private void heapify(int index) {
        int left = left(index);
        int right = right(index);
        while ((left > 0 && ((E) data[left]).compareTo((E) data[index]) < 0) || (right > 0 && ((E) data[right]).compareTo((E) data[index]) < 0)) {
            if (right <= 0 || ((E) data[left]).compareTo((E) data[right]) < 0) {
                swap(index, left);
                index = left;
            } else {
                swap(index, right);
                index = right;
            }
            left = left(index);
            right = right(index);
        }
    }

    public E remove() {
        E ans = (E) data[1];
        heapify();
        return ans;
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOfRange(data, 1, size));
    }
}

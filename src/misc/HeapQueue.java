package misc;
import java.util.ArrayList;


class HeapItem<T extends Comparable<? super T>> {
    public T value;
    public int priority;
    public int pos;

    public HeapItem(T value, int priority, int pos) {
        this.value = value;
        this.priority = priority;
        this.pos = pos;
    }

	@Override
	public String toString() {
		return "HeapItem [value=" + value + ", priority=" + priority + ", pos=" + pos + "]";
	}
    
}


public class HeapQueue<T extends Comparable<? super T>> {
    private int used_slots;
    private int total;
    private ArrayList<HeapItem<T>> array;

    public HeapQueue(int n) {
        total = n;
        used_slots = 0;
        array = new ArrayList<HeapItem<T>>();
    }

    public int size() { return used_slots; };

    public HeapItem<T> insert(T value, int priority) {
        if (used_slots >= total) {
            return null;
        }

        HeapItem<T> new_item = new HeapItem<T>(value, priority, used_slots);
        array.add(new_item);

        int i = used_slots;
        while (i > 0 && array.get(i).priority > array.get(parent(i)).priority) {
            swap(i, parent(i));
            i = parent(i);
        }

        used_slots++;

        return new_item;
    }

    public void maxHeapRestore(int i) {
        int max = i;
        if (left(i) < used_slots && array.get(left(i)).priority > array.get(max).priority) {
        	max = left(i);
        }

        if (right(i) < used_slots && array.get(right(i)).priority > array.get(max).priority) {
        	max = right(i);
        }

        if (i != max) {
            swap(i, max);
            maxHeapRestore(max);
        }
    };

    public T deleteMax() {
        if (used_slots == 0) {
            return null;
        }

        used_slots--;
        swap(0, used_slots);

        maxHeapRestore(0);
        
        T valore = array.get(used_slots).value;
        array.remove(used_slots);
        return valore;
    }

    void increase(HeapItem<T> x, int p) {
        if (p > x.priority) {
            return;
        }
        x.priority = p;
        int i = x.pos;
        while (i>0 && array.get(i).priority > array.get(parent(i)).priority) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    private void swap(int i, int j) {
        HeapItem<T> first = array.get(i);
        HeapItem<T> second = array.get(j);
        array.remove(i);
        array.add(i, second);
        array.remove(j);
        array.add(j, first);
        array.get(i).pos = i;
        array.get(j).pos = j;
    }

    private int parent(int i) {
        return i / 2;  // integer division
    }

    private int left(int i) {
        return 2 * i;
    }

    private int right(int i) {
        return (2 * i) + 1;
    }

	@Override
	public String toString() {
		return "HeapQueue [array=" + array + "]";
	}
    
    
}
package misc;

public class HeapItem<T extends Comparable<? super T>> {
    public T value;
    public int priority;
    public int pos;
    
    public HeapItem(T value, int priority, int pos) {
        this.value = value;
        this.priority = priority;
        this.pos = pos;
    }
    
}
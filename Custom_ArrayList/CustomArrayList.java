import java.util.Collection;

public class CustomArrayList<G>{
    private static final int DEFAULT_SIZE=10;
    private int size;
    private Object[] data;

    public CustomArrayList(){
        data=new Object[DEFAULT_SIZE];
        size=0;
    }

    public CustomArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        this.data = new Object[initialCapacity];
        this.size = 0;
    }

    public int size(){
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (data[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(data[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int lastIndexOf(Object o){
        if (o == null) {
            for (int i = size-1; i >=0; i--) {
                if (data[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size-1; i >=0; i--) {
                if (o.equals(data[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    private void ensureCapacity(int mCapacity) {
        int oCapacity = data.length;
        if (mCapacity > oCapacity) {
            int newCapacity = (oCapacity * 2)/3+1;
            if (newCapacity < mCapacity)
                newCapacity = mCapacity;
            Object[] temp=new Object[newCapacity];
            for(int i=0; i<size;i++){
                temp[i]=data[i];
            }
            data=temp;
            // data = Arrays.copyOf(data, newCapacity);
        }
    }

    public void add(G e) {
        ensureCapacity(size + 1);
        data[size++]=e;
    }

    public boolean isValidIndex(int index){
        if(index<0 || index>=size){
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return true;
    }

    public void add(int index, G e){
        
        if(isValidIndex(index)){
            ensureCapacity(size + 1);
            arrayShift(index, index+1);
            data[index]=e;
            size++;
        }
    }
    private void arrayShift(int fromIndex, int ToIndex){
        int numMoved = ToIndex-fromIndex;
        
        if (numMoved != 0 && fromIndex+numMoved>=0) {
            for (int i = fromIndex; i < size; i++) {
            data[i + numMoved] = data[i];
            }
        }
    }

    public void addAll(int index, Collection<? extends G> c ){
        if(isValidIndex(index)){
            int newSize=c.size();
            ensureCapacity(size+newSize);

            arrayShift(index, index+newSize-1);

            int i=index;
            for (G element : c) {
                data[i++]=element;
            }
            size+=newSize;
        }
    }
    

    public G get(int index) {
        if(isValidIndex(index)){
            return (G) data[index];
        }
        return null;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    public G remove(int index){
        if(isValidIndex(index)){
            G oelement=(G)data[index];
            arrayShift(index, index-1);
            
            data[--size] = null;
            return oelement;
        }
        return null;
    }

    public boolean removeAll(Collection<? extends G> c) {
        boolean modified = false;
        for (int i = 0; i < size; ) {
            if (c.contains(data[i])) {
                remove(i);
                modified = true;
            } else {
                i++;
            }
        }
        return modified;
    }
}

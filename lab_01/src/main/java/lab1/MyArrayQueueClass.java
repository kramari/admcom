package lab1;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayQueueClass<T> implements MyArrayQueue<T>, Iterable<T> {
	
	private int front;
    private int rear;
    private final int size;
    private final T[] queue;
	
    public MyArrayQueueClass(int inSize) {
        size = inSize;
        queue = (T[]) new Object[size];
        front = -1;
        rear = -1;
        return;
    }

    public boolean isEmpty() {
        return (front == -1 && rear == -1);
    }
		
	/**
     * Добавляет объект в очередь
     * @param o
     */
	
	public void add(T o){
		if ((rear + 1) % size == front) {
            throw new IllegalStateException("Queue is full");

        } else if (isEmpty()) {
            front ++;
            rear ++;
            queue[rear] = o;

        } else {
            rear = (rear + 1) % size;
            queue[rear] = o;

        }
	}
	
	/**
     * Забирает объект из очереди. Возвращаеммый объект удаляется.
     * @return
     */
    public Object get(){ 
    	T o = null;
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty, cant dequeue");
        } else if (front == rear) {
            o = queue[front];
            front = -1;
            rear = -1;

        } else {
            o = queue[front];
            front = (front + 1) % size;

        }
        return o;
    	
    }

    /**
     * Возвращает размер очереди
     * @return
     */
    public int size() {
		return 0;
	}
 

    public void rotate(int posNum){
    	for(int i = 0; i != posNum; i++) {
    		T temp = queue[i];
    		for(int j = i; j !=size-1; j++){
    			queue[j] = queue[j+1];
    		}
    		queue[size - 1] = temp;
    	}
    }
    

	public Iterator<T> iterator() {
		return new Iterator<T>() {
				public boolean hasNext(){
					if (queue != null){
						return true;
					}
					return false;
				}
				
				public T next() {
					if (! hasNext())
						throw new UnsupportedOperationException("Not supported yet.");

				      front ++;

				      return queue[front - 1];
				}
				
				public void remove() {
			        throw new UnsupportedOperationException();
			    }
		
	};
	}

    public String toString() {
        return "Queue [front=" + front + ", rear=" + rear + ", size=" + size
                + ", queue=" + Arrays.toString(queue) + "]";
    }
    
    public static<T> void main(String[] args) {
		// TODO Auto-generated method stub
    	MyArrayQueueClass newQueue = new MyArrayQueueClass(6);
        newQueue.add(1);
        newQueue.add(2);
        newQueue.add(3);
        newQueue.add(4);
        newQueue.add(5);
        newQueue.add(8);
        System.out.println((T) newQueue.toString());
        System.out.println((T) newQueue.get().toString());
        System.out.println((T) newQueue.get().toString());
        newQueue.add(10);
        newQueue.add(12);
        System.out.println((T) newQueue.toString());
        newQueue.rotate(2);
        System.out.println((T) newQueue.toString());
        System.out.println((T) newQueue.get().toString());
        System.out.println((T) newQueue.get().toString());
        System.out.println((T) newQueue.get().toString());
        System.out.println((T) newQueue.get().toString());
        System.out.println((T) newQueue.get().toString());
        
        //Iterator<String> it = newQueue.iterator();
		//while(it.hasNext() == true) {
		//	System.out.print(it.next() + "\t");
        //}
	}
}

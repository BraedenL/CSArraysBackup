import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Array-based implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported. 
 * 
 * @author 
 *
 * @param <T> type to store
 */
public class IUArrayList<T> implements IndexedUnsortedList<T> {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int NOT_FOUND = -1;
	
	private T[] array;
	private int rear;
	private int modCount;
	
	/** Creates an empty list with default initial capacity */
	public IUArrayList() {
		this(DEFAULT_CAPACITY);
	}
	
	/** 
	 * Creates an empty list with the given initial capacity
	 * @param initialCapacity
	 */
	@SuppressWarnings("unchecked")
	public IUArrayList(int initialCapacity) 
	{
		array = (T[])(new Object[initialCapacity]);
		rear = 0;
		modCount = 0;
	}
	
	/** Double the capacity of array */
	private void expandCapacityIfNeeded() 
	{
		if(rear > array.length)
		{
			array = Arrays.copyOf(array, array.length*2);
		}
	}

	@Override
	public void addToFront(T element) 
	{	 
		expandCapacityIfNeeded();
		for(int i = rear; i > 0; i--)
		{
			array[i] = array[i-1];
		}
		array[0] = element;
		rear++;
		modCount++;
	}

	@Override
	public void addToRear(T element) 
	{
		expandCapacityIfNeeded();
		array[rear] = element;
		modCount++;
		rear++;
	}

	@Override
	public void add(T element) 
	{	
		addToRear(element);
	}

	@Override
	public void addAfter(T element, T target) 
	{
		 if(!contains(target))
		 {
			throw new NoSuchElementException(); 
		 }
		 expandCapacityIfNeeded();
		 int addAfterTarget = indexOf(target);
		 for(int i = rear; i > addAfterTarget; i--)
		 {
			 array[rear] = array[rear-1];
		 }
		 array[indexOf(target)] = element;
		 rear++;
		 modCount++;
	}

	@Override
	public void add(int index, T element) 
	{
		if(index < 0 || index > rear)
		{
			throw new IndexOutOfBoundsException();
		}
		else
		{
			expandCapacityIfNeeded();
			for(int i = rear; i > index; i--)
			{
				array[i] = array[i-1];
			}
			array[index] = element;
			rear++;
			modCount++;
		}
	}

	@Override
	public T removeFirst() 
	{
		if(isEmpty())
		{
			throw new NoSuchElementException();
		}
		T retVal = array[0];

		for(int i = 0; i < rear-1; i++)
		{
			array[i] = array[i+1];
		}
		array[rear-1] = null;
		rear--;
		modCount++;
		return retVal;
	}

	@Override
	public T removeLast() 
	{
		if(isEmpty())
		{
			throw new NoSuchElementException();
		}
		
		T retVal = array[rear-1];
		array[rear] = null;
		rear--;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(T element) 
	{
		int index = indexOf(element);
		if (index == NOT_FOUND) {
			throw new NoSuchElementException();
		}
		
		T retVal = array[index];
		
		rear--;
		//shift elements
		for (int i = index; i < rear; i++) {
			array[i] = array[i+1];
		}
		array[rear] = null;
		modCount++;
		
		return retVal;
	}

	@Override
	public T remove(int index) 
	{
		if(index < 0 || index >= rear)
		{
			throw new IndexOutOfBoundsException();
		}
		expandCapacityIfNeeded();
		T retVal = array[index];
		
		for(int i = index; i < rear-1; i++)
		{
			array[i] = array[i+1];
		}
		array[rear] = null;
		rear--;
		modCount++;
		return retVal;
	}

	@Override
	public void set(int index, T element) 
	{
		if(index < 0 || index >= rear)
		{
			throw new IndexOutOfBoundsException();
		}
		array[index] = element;
	}

	@Override
	public T get(int index) 
	{
		if(index < 0 || index >= rear)
		{
			throw new IndexOutOfBoundsException();
		}
		return array[index];
	}

	@Override
	public int indexOf(T element) 
	{
		int index = NOT_FOUND;
		
		if (!isEmpty()) {
			int i = 0;
			while (index == NOT_FOUND && i < rear) {
				if (element.equals(array[i])) {
					index = i;
				} else {
					i++;
				}
			}
		}
		
		return index;
	}

	@Override
	public T first() 
	{
		if(isEmpty())
		{
			throw new NoSuchElementException();
		}
		return array[0];
	}

	@Override
	public T last() 
	{
		if(isEmpty())
		{
			throw new NoSuchElementException();
		}
		return array[rear-1];
	}

	@Override
	public boolean contains(T target) 
	{
		return (indexOf(target) != NOT_FOUND);
	}

	@Override
	public boolean isEmpty() 
	{
		return (rear == 0);
	}

	@Override
	public int size() 
	{ 
		return rear;
	}

	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		String output;
		
		str.append("[");
		for(int i = 0; i < rear-1; i++)
		{
			str.append(array[i]);
			str.append(", ");
		}
		str.append(array[rear]);
		str.append("]");
		
		output = str.toString();
		return output;
	}
	@Override
	public Iterator<T> iterator() 
	{
		return new ALIterator();
	}

	@Override
	public ListIterator<T> listIterator() 
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) 
	{
		throw new UnsupportedOperationException();
	}

	/** Iterator for IUArrayList */
	private class ALIterator implements Iterator<T> 
	{
		private int nextIndex;
		private int iterModCount;
		private boolean isRemoveable;
		
		public ALIterator() 
		{
			nextIndex = 0;
			iterModCount = modCount;
			isRemoveable = false;
		}

		@Override
		public boolean hasNext() 
		{ 
			return (nextIndex < rear);
		}

		@Override
		public T next() 
		{
			if(!hasNext())
			{
				throw new NoSuchElementException();
			}
			if(modCount != iterModCount)
			{
				throw new ConcurrentModificationException();
			}
			nextIndex++;
			isRemoveable = true;
			return array[nextIndex-1];
		}
		
		@Override
		public void remove() 
		{
			if(!isRemoveable)
			{
				throw new IllegalStateException();
			}
			isRemoveable = false;
			
			for(int i = nextIndex-1; i < rear-1; i++)
			{
				array[i] = array[i+1];
			}
			array[rear-1] = null;
			rear--;
			nextIndex--;
		}
	}
}

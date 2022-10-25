import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/*
 * Double Linked list with node implementation of IndexUnsortedList
 * @author Braeden LaCombe
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T>{
	private DoubleNode<T> head;
	private DoubleNode<T> tail;
	private int size;
	private int modCount;

	/*
	 * Constructor for the Double Linked List
	 */
	
	public void IUDoubleLinkedLIst()
	{
		head = tail = null;
		size = 0;
		modCount = 0;
	}
	
	@Override
	public void addToFront(T element) 
	{
		DoubleNode<T> newNode = new DoubleNode<T>(element);
		newNode.setNextNode(head);
		if(head == null)
		{
			tail = newNode;
		}
		else
		{
			head.setPrevNode(newNode);
		}
		head = newNode;
		size++;
		modCount++;
	}

	@Override
	public void addToRear(T element) 
	{
		DoubleNode<T> newNode = new DoubleNode<T>(element);
		newNode.setPrevNode(tail);
		if(tail == null)
		{
			head = newNode;
		}
		else
		{
			tail.setPrevNode(newNode);
		}
		tail = newNode;
		size++;
		modCount++;
		
	}

	@Override
	public void add(T element) 
	{
		addToRear(element);
	}

	@Override
	public void addAfter(T element, T target) 
	{
		DoubleNode<T> targetNode = head;
		while (targetNode != null && !targetNode.getElement().equals(target))
		{
			targetNode = targetNode.getNextNode();
		}
		if(targetNode == null)
		{
			throw new NoSuchElementException();
		}
		DoubleNode<T> newNode = new DoubleNode<T>(element);
		newNode.setNextNode(targetNode.getNextNode());
		newNode.setPrevNode(targetNode);
		if(targetNode == tail)
		{
			tail = newNode;
		}
		else
		{
			targetNode.getNextNode().setPrevNode(newNode);
		}
		targetNode.setNextNode(newNode);
		size++;
		modCount++;
	}

	@Override
	public void add(int index, T element) 
	{
		if(index < 0 || index > size)
		{
			throw new IndexOutOfBoundsException();
		}
		DoubleNode<T> newNode = new DoubleNode<T>(element);
		DoubleNode<T> targetNode = head;
		if(size > 1)
		{
			for(int i = 0; i < index - 1; i++)
			{
				targetNode = targetNode.getNextNode();
			}
		}
		if(head == null && tail == null)
		{
			head = tail = newNode;
		}
		else if(targetNode == head)
		{
			newNode.setNextNode(targetNode);
			targetNode.setPrevNode(newNode);
			head = newNode;
			//targetNode.getNextNode().setPrevNode(newNode);
		}
		else if(targetNode == tail)
		{
			targetNode.setNextNode(newNode);
			newNode.setPrevNode(targetNode);
			tail = newNode;
			
			//newNode.setPrevNode(targetNode.getPrevNode());
			//targetNode.getPrevNode().setNextNode(newNode);
		}
		else
		{
			newNode.setNextNode(targetNode.getNextNode());
			targetNode.getNextNode().setPrevNode(newNode);
			
			targetNode.setNextNode(newNode);
			newNode.setPrevNode(targetNode);
		}
		size++;
		modCount++;
	}

	@Override
	public T removeFirst() 
	{
		if(isEmpty())
		{
			throw new NoSuchElementException();
		}
		T retVal = head.getElement();
		
		if(head == tail)
		{
			head = tail = null;
		}
		else
		{
			//head.getNextNode().setNextNode(null);
			head = head.getNextNode();
		}
		size--;
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
		T retVal = tail.getElement();
		
		if(head == tail)
		{
			head = tail = null;
		}
		else
		{
			tail.getPrevNode().setNextNode(null);
			tail = tail.getPrevNode();
		}
		
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(T element) {
		if(isEmpty())
		{
			throw new NoSuchElementException();
		}
		
		boolean found = false;
		DoubleNode<T> current = head;
		
		while (current != null && !found)
		{
			if (element.equals(current.getElement()))
			{
				found = true;
			}
			else
			{
				current = current.getNextNode();
			}	
		}
		if (!found)
		{
			throw new NoSuchElementException();
		}
		
		if (size() == 1)
		{
			head = tail = null;
		}
		else if (current == head)
		{
			head = current.getNextNode();
		}
		else if(current == tail)
		{
			tail = current.getPrevNode();
			tail.setNextNode(null);
		}
		else
		{
			current.getPrevNode().setNextNode(current.getNextNode());
			current.getNextNode().setPrevNode(current.getPrevNode());
		}
		
		size--;
		modCount++;
		return current.getElement();
	}

	@Override
	public T remove(int index) {
		if(index < 0 || index >= size)
		{
			throw new IndexOutOfBoundsException();
		}
		DoubleNode<T> targetNode = head;
		for(int i = 0; i < index; i++)
		{
			targetNode = targetNode.getNextNode();
		}
		T retVal = targetNode.getElement();
		if(size == 1)
		{
			head = tail = null;
		}
		else if (targetNode == tail)
		{
			tail = targetNode.getPrevNode();
			tail.setNextNode(null);
		}
		else if (targetNode == head)
		{
			head = targetNode.getNextNode();
		}
		else
		{
			targetNode.getPrevNode().setNextNode(targetNode.getNextNode());
			targetNode.getNextNode().setPrevNode(targetNode.getPrevNode());
		}
	
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public void set(int index, T element) 
	{
		if (index < 0 || index >= size)
		{
			throw new IndexOutOfBoundsException();
		}
		DoubleNode<T> current = head;
		for(int i = 0; i < index; i++)
		{
			current = current.getNextNode();
		}
		current.setElement(element);
	}

	@Override
	public T get(int index) 
	{
		if(index < 0 || index >= size)
		{
			throw new IndexOutOfBoundsException();
		}
		DoubleNode<T> current = head;
		for(int i = 0; i < index; i++)
		{
			current = current.getNextNode();
		}
		return current.getElement();
	}

	@Override
	public int indexOf(T element) 
	{
		DoubleNode<T> current = head;
		int curIndex = 0;
		while(current != null && !element.equals(current.getElement()))
		{
			current = current.getNextNode();
			curIndex++;
		}
		if(current == null)
		{
			curIndex = -1;
		}
		return curIndex;

	}

	@Override
	public T first() 
	{
		if(isEmpty())
		{
			throw new NoSuchElementException();
		}
		return head.getElement();
	}

	@Override
	public T last() 
	{
		if(isEmpty())
		{
			throw new NoSuchElementException();
		}
		return tail.getElement();
	}

	@Override
	public boolean contains(T target) 
	{
		return indexOf(target) > -1;
	}

	@Override
	public boolean isEmpty() 
	{
		return size == 0;
	}

	@Override
	public int size() 
	{
		return size;
	}
	
	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		str.append("[");
		for(T item : this)
		{
		str.append(item.toString());
		str.append(", ");
		}
		if (!isEmpty())
		{
		str.delete(str.length()-2, str.length());
		}
		str.append("]");
		return str.toString();
	}

	@Override
	public Iterator<T> iterator() 
	{
		return new DLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() 
	{
		return new DLLIterator();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) 
	{	
		return new DLLIterator(startingIndex);
	}
	
	/*
	 * ListIterator for IUDoubleLinkedList can also works as iterator 
	 */
	
	private class DLLIterator implements ListIterator<T>
	{
		private DoubleNode<T> nextNode;
		private DoubleNode<T> lastReturned;
		private int nextIndex;
		private int iterModCount;

		/*
		 * Constructor and sets default values for the iterator
		 */
		private DLLIterator()
		{
			//this(0);
			nextNode = head;
			lastReturned = null;
			nextIndex = 0;
			iterModCount = modCount;
		}

		/*
		 * Constructor when starting somewhere from given index 	
		 * @param startingIndex of element that would be next
		 */
		public DLLIterator(int startingIndex)
		{
			if(startingIndex < 0 || startingIndex > size)
			{
				throw new IndexOutOfBoundsException();
			}
			nextNode = head;
			lastReturned = null;
			for(int i = 0; i < startingIndex; i++)
			{
				nextNode = nextNode.getNextNode();
			}
			nextIndex = startingIndex;
			iterModCount = modCount;
		}
		
		@Override
		public boolean hasNext() 
		{
			if(iterModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}
			return nextNode != null;
		}

		@Override
		public T next() 
		{
			if(!hasNext())
			{
				throw new NoSuchElementException();
			}
			T retVal = nextNode.getElement();
			lastReturned = nextNode;
			nextNode = nextNode.getNextNode();
			nextIndex++;
			return retVal;
		}

		@Override
		public boolean hasPrevious() 
		{
			if(iterModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}
			return nextNode != head;
		}

		@Override
		public T previous() {
			if(!hasPrevious())
			{
				throw new NoSuchElementException();
			}
			if(nextNode == null)
			{
				nextNode = tail;
			}
			else
			{
				nextNode = nextNode.getPrevNode();
			}
			lastReturned = nextNode;
			nextIndex--;
			return nextNode.getElement();
		}

		@Override
		public int nextIndex() 
		{
			return nextIndex;
		}

		@Override
		public int previousIndex() 
		{	
			return nextIndex - 1;
		}

		@Override
		public void remove() 
		{
			if(iterModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}
			if (lastReturned == null)
			{
				throw new IllegalStateException();
			}
			
			if(lastReturned == head)
			{
				head = head.getNextNode();
			}
			else
			{
				lastReturned.getPrevNode().setNextNode(lastReturned.getNextNode());
			}
			
			if(lastReturned == tail)
			{
				tail = tail.getPrevNode();
			}
			else
			{
				lastReturned.getNextNode().setPrevNode(lastReturned.getPrevNode());
			}
			
			if(nextNode == lastReturned)
			{
				nextNode = nextNode.getNextNode();
			}
			else
			{
				nextIndex--;
			}
			size--;
			modCount++;
			iterModCount++;
			lastReturned = null;
		}

		@Override
		public void set(T e) 
		{
			if(lastReturned == null)
			{
				throw new IllegalStateException();
			}
			else
			{
				lastReturned.setElement(e);
			}
			
			iterModCount++;
			modCount++;
		}

		@Override
		public void add(T e) 
		{
			if(iterModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}
			
			DoubleNode<T> newNode = new DoubleNode<T>(e);
			if(nextNode != null)
			{
				newNode.setNextNode(nextNode);
				newNode.setPrevNode(nextNode.getPrevNode());
				nextNode.setPrevNode(newNode);
			}
			else
			{
				newNode.setPrevNode(tail);
				tail = newNode;
			}
			if (nextNode != head)
			{
				newNode.getPrevNode().setNextNode(newNode);
			}
			else
			{
				head = newNode;
			}
			
			size++;
			modCount++;
			iterModCount++;
			nextIndex++;
		}		
	}

}

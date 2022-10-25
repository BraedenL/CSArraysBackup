import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author 
 * 
 * @param <T> type to store
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
	private Node<T> head, tail;
	private int size;
	private int modCount;
	
	/** Creates an empty list */
	public IUSingleLinkedList() 
	{
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) 
	{
		Node<T> newNode = new Node<T>(element);
		newNode.setNextNode(head);
		head = newNode;
		if(tail == null)
		{
			tail = newNode;
		}
		size++;
		modCount++;
	}

	@Override
	public void addToRear(T element) 
	{
		Node<T> newNode = new Node<T>(element);
		if(isEmpty())
		{
			head = newNode;
		}
		else
		{
			tail.setNextNode(newNode);
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
		//find target Node
		Node<T> targetNode = head;
		while (targetNode != null && !targetNode.getElement().equals(target))
		{
			targetNode = targetNode.getNextNode();
		}
		//Found?
		if (targetNode == null)
		{
			throw new NoSuchElementException();
		}
		//New Node
		Node<T> newNode = new Node<T>(element);
		//Update Nodes
		newNode.setNextNode(targetNode.getNextNode());
		targetNode.setNextNode(newNode);
		//Special Cases
		if(tail == targetNode) //or newNode.getNextNode() = null
		{
			tail = newNode;
		}
		//Generic (every function)
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
		Node<T> newNode = new Node<T>(element);
		Node<T> targetNode = head;
		Node<T> previousNode = null;
		for(int i = 0; i < index; i++)
		{
			previousNode = targetNode;
			targetNode = targetNode.getNextNode();
		}
		if(head == null && tail == null)
		{
			head = tail = newNode;
		}
		else if(targetNode == head)
		{
			newNode.setNextNode(head);
			head = newNode;
		}
		else if(targetNode == tail)
		{
			previousNode.setNextNode(newNode);
			tail = newNode;	
		}
		else
		{
			previousNode.setNextNode(newNode);
			newNode.setNextNode(targetNode);
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
		head = head.getNextNode();
		if(head == null)
		{
			tail = null;
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
			Node<T> newTail = head;
			while(newTail.getNextNode() != tail)
			{
				newTail = newTail.getNextNode();
			}
			//newTail.setNextNode(newTail);
			tail = newTail;
		}
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(T element) 
	{
		if (isEmpty()) 
		{
			throw new NoSuchElementException();
		}
		
		boolean found = false;
		Node<T> previous = null;
		Node<T> current = head;
		
		while (current != null && !found) 
		{
			if (element.equals(current.getElement())) 
			{
				found = true;
			} 
			else {
				previous = current;
				current = current.getNextNode();
			}
		}
		
		if (!found) {
			throw new NoSuchElementException();
		}
		
		if (size() == 1) { //only node
			head = tail = null;
		} else if (current == head) { //first node
			head = current.getNextNode();
		} else if (current == tail) { //last node
			tail = previous;
			tail.setNextNode(null);
		} else { //somewhere in the middle
			previous.setNextNode(current.getNextNode());
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
		Node<T> targetNode = head;
		Node<T> beforeTargetNode = null;
		for(int i = 0; i < index; i++)
		{
			beforeTargetNode = targetNode;
			targetNode = targetNode.getNextNode();
		}
		T retVal = targetNode.getElement();
		if(size == 1)
		{
			head = tail = null;
		}
		else if(targetNode == tail)
		{
			beforeTargetNode.setNextNode(tail);
		}
		else if(targetNode == head)
		{
			targetNode.setNextNode(head);
		}
		else
		{
		beforeTargetNode.setNextNode(targetNode.getNextNode());
		}
		
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public void set(int index, T element) {
		if (index < 0 || index >= size)
		{
			throw new IndexOutOfBoundsException();
		}
		Node<T> current = head;
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
		Node<T> current = head;
		for(int i = 0; i < index; i++)
		{
			current = current.getNextNode();
		}
		return current.getElement();
	}

	@Override
	public int indexOf(T element) 
	{
		Node<T> current = head;
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
	public Iterator<T> iterator() {
		return new SLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}

	/** Iterator for IUSingleLinkedList */
	private class SLLIterator implements Iterator<T> {
		private Node<T> nextNode;
		private int iterModCount;
		private boolean canRemove = false;
		
		/** Creates a new iterator for the list */
		public SLLIterator()
		{
			nextNode = head;
			iterModCount = modCount;
		}

		public boolean hasNext()
		{
			if(iterModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}
			return (nextNode != null);
		}

		public T next()
		{
			if (!hasNext())
			{
				throw new NoSuchElementException();
			}
			T retVal = nextNode.getElement();
			nextNode = nextNode.getNextNode();
			canRemove = true;
			return retVal;
		}

		public void remove()
		{
			if (iterModCount != modCount)
			{
				throw new ConcurrentModificationException();
			} 
			if(!canRemove)
			{
				throw new IllegalStateException();
			}
			canRemove = false;
			if(head == tail) //or size ==1 - situation for only being one node in list
			{
				head = tail = null;
			}
			else if(head.getNextNode() == nextNode) //Removing head node
			{
				head = nextNode;
			}
			else
			{
				//General case - somewhere inside a long list
				Node<T> targetNode = head;
				while (targetNode.getNextNode().getNextNode() != nextNode)
				{
					targetNode = targetNode.getNextNode();
				}
				targetNode.setNextNode(nextNode);
				if(nextNode == null) //Removed the old tail
				{
					tail = targetNode;
				}
				modCount++;
				iterModCount++;
				size--;
			}
		}
	}
}

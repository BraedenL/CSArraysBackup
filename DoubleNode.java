/**
 * Node represents a node in a linked list.
 *
 * @author Java Foundations, mvail
 * @version 4.0
 */
public class DoubleNode<E> 
{
	private DoubleNode<E> next;
	private DoubleNode<E> prevNode;
	private E element;

	/**
  	 * Creates an empty node.
  	 */
	public DoubleNode() 
	{
		next = null;
		prevNode = null;
		element = null;
	}

	/**
  	 * Creates a node storing the specified element.
 	 *
  	 * @param elem
  	 *            the element to be stored within the new node
  	 */
	public DoubleNode(E elem) 
	{
		next = null;
		prevNode = null;
		element = elem;
	}

	/**
 	 * Returns the node that follows this one.
  	 *
  	 * @return the node that follows the current one
  	 */
	public DoubleNode<E> getNextNode() 
	{
		return next;
	}
	public DoubleNode<E> getPrevNode()
	{
		return prevNode;
	}

	/**
 	 * Sets the node that follows this one.
 	 *
 	 * @param node
 	 *            the node to be set to follow the current one
 	 */
	public void setNextNode(DoubleNode<E> node) 
	{
		next = node;
	}
	public void setPrevNode(DoubleNode<E> node)
	{
		prevNode = node;
	}

	/**
 	 * Returns the element stored in this node.
 	 *
 	 * @return the element stored in this node
 	 */
	
	public E getElement() 
	{
		return element;
	}

	/**
 	 * Sets the element stored in this node.
  	 *
  	 * @param elem
  	 *            the element to be stored in this node
  	 */
	public void setElement(E elem) 
	{
		element = elem;
	}

	@Override
	public String toString() 
	{
		return "Element: " + element.toString() + " Has next: " + (next != null);
	}
}


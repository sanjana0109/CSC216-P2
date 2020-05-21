/**
 * 
 */
package edu.ncsu.csc216.business.list_utils;

import java.util.NoSuchElementException;

/**
 * Sorted Linked List with an Iterator to store elements
 * 
 * @author Kaitlyn Gosline
 * @author Sanjana Cheerla
 * @param <E> the type of element being stored
 *
 */
public class SortedLinkedListWithIterator<E extends Comparable<E>> implements SortedList<E> {

	/** First node in LinkedList */
	private Node<E> head;

	/** Size of the list */
	private int size;

	/**
	 * Creates a Sorted LinkedList
	 */
	public SortedLinkedListWithIterator() {
		head = null;

	}

	/**
	 * Returns size of Sorted LinkedList
	 * 
	 * @return size of Sorted LinkedList
	 */
	@Override
	public int size() {
		return this.size;
	}

	/**
	 * Returns whether or not the Sorted LinkedList is empty
	 * 
	 * @return true if the Sorted LinkedList is empty and false if it is not
	 */
	@Override
	public boolean isEmpty() {
		if (size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Returns whether or not the Sorted LinkedList contains a given element
	 * 
	 * @param e the element being checked to see if it is in the Sorted LinkedList
	 * @return true if the element is in the Sorted LinkedList and false if it is
	 *         not
	 */
	@Override
	public boolean contains(E e) {
		SimpleListIterator<E> iterator = iterator();
		while (iterator.hasNext()) {
			if (iterator.next().equals(e)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds element to the SortedLinkedList in sorted order
	 * 
	 * @param e the element to be added
	 * @return true if the element is added and false if it is not
	 * @throws NullPointerException     if e is null
	 * @throws IllegalArgumentException if list already contains e
	 */
	@Override
	public boolean add(E e) {
		if (e == null) {
			throw new NullPointerException();
		}
		if (contains(e)) {
			throw new IllegalArgumentException();
		}
		if (isEmpty() || head.data.compareTo(e) > 0) {
			head = new Node<E>(e, head);
			size++;
			return true;
		} else {
			Node<E> curr = head;

			while (curr.next != null && curr.next.data.compareTo(e) <= 0) {
				curr = curr.next;
			}
			curr.next = new Node<E>(e, curr.next);
			size++;
			return true;
		}
	}

	/**
	 * Clears the Sorted LinkedList, making it empty
	 */
	@Override
	public void clear() {
		head = null;
		this.size = 0;
	}

	/**
	 * Returns the element at the given index
	 * 
	 * @param index the index of the element to be returned
	 * @return element at the given index
	 * @throws IndexOutOfBoundsException if the index is less than 0 or greater than the size of the list
	 */
	@Override
	public E get(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		SimpleListIterator<E> iterator = iterator();
		int currIdx = 0;
		while (iterator.hasNext()) {
			if (currIdx == index) {
				return iterator.next();
			} else {
				iterator.next();
				currIdx++;
			}
		}
		return null;
	}

	/**
	 * Removes the element at the specified position in this list. Shifts any
	 * subsequent elements to the left (subtracts one from their indices). Returns
	 * the element that was removed from the list. *
	 * 
	 * @param index the index of the element to be removed
	 * @return element which was removed
	 * @throws IndexOutOfBoundsException if the index is less than 0 or greater than the size of the list
	 */
	@Override
	public E remove(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		Node<E> curr = head;
		E returnValue = null;

		if (index == 0) {
			returnValue = curr.data;
			if (size() == 1) {
				size--;
				head = null;
			} else {
				size--;
				curr = curr.next;
				head = curr;
			}
		} else {
			for (int i = 0; i < index - 1; i++) {
				curr = curr.next;
			}
			returnValue = curr.next.data;
			curr.next = curr.next.next;
			size--;

		}
		return returnValue;
	}

	/**
	 * Truncates this list starting at the given index and returns the truncated
	 * tail. (Example, if the list contains 6 elements, then list.truncate(2) would
	 * contain the first two elements of the list. The truncated tail would contain
	 * the remaining 4 elements.) The ordering of elements in the original list does
	 * not change nor does the ordering of elements in the truncated tail.
	 * 
	 * @param start truncate the list starting at this index
	 * @return the tail of this list that was truncated.
	 * @throws IllegalArgumentException if start is less than 0 or greater than the size of the list
	 */
	@Override
	public SortedList<E> truncate(int start) {
		SortedList<E> tail = new SortedLinkedListWithIterator<E>();
		if (isEmpty() || start == size()) {
			return tail;
		}
		if (start < 0 || start > size()) {
			throw new IllegalArgumentException();
		}
		SimpleListIterator<E> iterator = iterator();
		int currIdx = 0;
		while (iterator.hasNext()) {
			if (currIdx >= start) {
				tail.add(iterator.next());
				this.remove(currIdx);
			} else {
				iterator.next();
				currIdx++;
			}
		}
		return tail;

	}

	/**
	 * Returns the index of the first occurrence of the specified element in this
	 * list, or -1 if this list does not contain the element.
	 *
	 * @param e the element to search for
	 * @return the index of the first occurrence of the specified element in this
	 *         list, or -1 if this list does not contain the element
	 */
	@Override
	public int indexOf(E e) {
		SimpleListIterator<E> iterator = iterator();
		int currIdx = 0;
		while (iterator.hasNext()) {
			if (iterator.next().equals(e)) {
				return currIdx;
			} else {
				currIdx++;
			}
		}
		return -1;
	}

	/**
	 * Iterator for a simple list
	 * 
	 * @return the SimpleListIterator
	 */
	public SimpleListIterator<E> iterator() {
		return new Cursor();
	}

	/**
	 * Returns Sorted LinkedList as a string
	 * 
	 * @return a string of the Sorted LinkedList data
	 */
	@Override
	public String toString() {
		SimpleListIterator<E> iterator = iterator();
		String list = "[";
		if (iterator.hasNext()) {
			list += iterator.next().toString();
			while (iterator.hasNext()) {
				list += ", " + iterator.next().toString();
			}
		}
		list += "]";
		return list;
	}

	/**
	 * Cursor used to iterate through the SortedLinkedList
	 * 
	 * @author Kaitlyn Gosline
	 *
	 */
	public class Cursor implements SimpleListIterator<E> {

		/** Node cursor is on */
		private Node<E> traveler;

		/**
		 * Creates a cursor
		 */
		public Cursor() {
			traveler = head;
		}

		/**
		 * Checks to see if there is another node
		 * 
		 * @return true if there is another element and false if there is not
		 */
		@Override
		public boolean hasNext() {
			if (traveler == null) {
				return false;
			}
			return true;
		}

		/**
		 * Returns the next element and advances iterator to that element
		 * 
		 * @return element that is next
		 * @throws NoSuchElementException if the cursor is at the end of the list
		 */
		@Override
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			E nextVal = traveler.data;
			traveler = traveler.next;
			return nextVal;
		}

	}

	/**
	 * Node to be used in SortedLinkedList
	 * 
	 * @param <E> type of element being stored
	 * 
	 * @author Kaitlyn Gosline
	 *
	 */
	@SuppressWarnings("hiding")
	public class Node<E> {

		/** Data in node */
		E data;
		/** Node to follow the current node */
		private Node<E> next;

		/**
		 * Creates a node given the data and the next node
		 * 
		 * @param data for the node
		 * @param next node
		 */
		public Node(E data, Node<E> next) {
			this.data = data;
			this.next = next;
		}

	}

}

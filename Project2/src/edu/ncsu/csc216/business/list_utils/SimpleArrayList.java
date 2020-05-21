/**
 * 
 */
package edu.ncsu.csc216.business.list_utils;

import java.util.Arrays;

/**
 * Array List for storing objects
 * 
 * @author Kaitlyn Gosline
 * @author Sanjana Cheerla
 * @param <E> the type of object being stored
 *
 */
public class SimpleArrayList<E> implements SimpleList<E> {

	/**
	 * Constant used to resize the underlying array when more elements need to be
	 * added
	 */
	private static final int RESIZE = 10;

	/** Underlying array */
	private Object[] list;
	/** size of ArrayList */
	private int size;

	/**
	 * Creates an array list
	 */
	@SuppressWarnings("unchecked")
	public SimpleArrayList() {
		list = (E[]) new Object[RESIZE];
		this.size = 0;
	}

	/**
	 * Creates an array list with a given size
	 * 
	 * @param size size to initialize ArrayList
	 * @throws IllegalArgumentException if size is less than or equal to 0
	 */
	@SuppressWarnings("unchecked")
	public SimpleArrayList(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException();
		}
		list = (E[]) new Object[size];
		this.size = 0;
	}

	/**
	 * Returns the size of the ArrayList
	 * 
	 * @return size of the ArrayList
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Returns whether or not the ArrayList is empty
	 * 
	 * @return true if ArrayList is empty, false if it is not
	 */
	@Override
	public boolean isEmpty() {
		if (size == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Returns whether or not the ArrayList contains a given element
	 * 
	 * @param e the element being checked to see if it is in the ArrayList
	 * @return true if the ArrayList contains the element and false if it doesn't
	 */
	@Override
	public boolean contains(E e) {
		for (int i = 0; i < size(); i++) {
			if (get(i).equals(e)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds the specified element to the end of the ArrayList.
	 *
	 * @param e element to be appended to this list
	 * @return true if the element can be added and false if it cannot be
	 * @throws NullPointerException     if the specified element is null
	 * @throws IllegalArgumentException if the list already contains e
	 */
	@Override
	public boolean add(E e) {
		if (e == null) {
			throw new NullPointerException();
		}
		for (int i = 0; i < this.size; i++) {
			if (e.equals(this.get(i))) {
				throw new IllegalArgumentException();
			}
		}
		try {
			add(size(), e);
			return true;
		} catch (IndexOutOfBoundsException exp) {
			return false;
		}
	}

	/**
	 * Inserts the specified element at the specified position in this list. Shifts
	 * any element currently at that position plus all subsequent elements to the
	 * right (adds one to their indexes).
	 *
	 * @param idx index at which the specified element is to be inserted
	 * @param e   element to be inserted
	 * @throws NullPointerException      if the specified element is null
	 * @throws IllegalArgumentException  if the list already contains e
	 * @throws IndexOutOfBoundsException if the index is less than 0 or greater than the size of the list
	 */
	@Override
	public void add(int idx, E e) {
		if (e == null) {
			throw new NullPointerException();
		}
		if (contains(e)) {
			throw new IllegalArgumentException();
		}
		if (idx < 0 || idx > size()) {
			throw new IndexOutOfBoundsException();
		}
		if (idx == size()) {
			list[size()] = e;
			size++;
		} else {
			for (int i = size(); i > idx; i--) {
				list[i] = list[i - 1];
			}
			list[idx] = e;
			size++;
		}

		if (size() >= list.length) {
			Object[] temp = Arrays.copyOf(list, size() * RESIZE);
			list = temp;
		}
	}

	/**
	 * Removes the element at the specified position in this list, shifting any
	 * subsequent elements to the left (subtracts one from their indexes). Returns
	 * the element that was removed from the list.
	 *
	 * @param idx the index of the element to be removed
	 * @return the element previously at the specified position
	 * @throws IndexOutOfBoundsException if the index is less than 0 or greater than the size of the list
	 */
	@Override
	public E remove(int idx) {
		if (idx < 0 || idx > size()) {
			throw new IndexOutOfBoundsException();
		}
		E temp = get(idx);
		for (int i = idx; i < size() - 1; i++) {
			list[i] = list[i + 1];
		}
		list[size - 1] = null;
		size--;
		return temp;
	}

	/**
	 * Returns the element at the specified position in this list.
	 *
	 * @param idx index of the element to return
	 * @return the element at the specified position in this list
	 * @throws IndexOutOfBoundsException if the index is less than 0 or greater than the size of the list
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E get(int idx) {
		if (idx < 0 || idx >= size()) {
			throw new IndexOutOfBoundsException();
		}
		return (E) list[idx];
	}

	/**
	 * Returns the index of the first occurrence of the specified element in this
	 * list, or -1 if this list does not contain the element.
	 *
	 * @param e element to search for
	 * @return the index of the first occurrence of the specified element in this
	 *         list, or -1 if this list does not contain the element
	 */
	@Override
	public int indexOf(E e) {
		for (int i = 0; i < size(); i++) {
			if (get(i).equals(e)) {
				return i;
			}
		}
		return -1;
	}

}

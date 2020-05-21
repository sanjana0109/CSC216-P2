/**
 * 
 */
package edu.ncsu.csc216.business.list_utils;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the SortedLinkedListWithIterator class.
 * 
 * Note that the test method for get has been omitted. It will be tested through
 * other methods.
 * 
 * @author Kaitlyn Gosline
 * @author Sanjana Cheerla
 *
 */
public class SortedLinkedListWithIteratorTest {

	/**
	 * Tests constructing SortedLinkedListWithIterator. Test method for
	 * {@link edu.ncsu.csc216.business.list_utils.SortedLinkedListWithIterator#SortedLinkedListWithIterator()}.
	 */
	@Test
	public void testSortedLinkedListWithIterator() {
		SortedLinkedListWithIterator<String> l = new SortedLinkedListWithIterator<String>();
		assertEquals(0, l.size());
	}

	/**
	 * Tests checking to see if SortedLinkedListWithIterator contains an element.
	 * Test method for
	 * {@link edu.ncsu.csc216.business.list_utils.SortedLinkedListWithIterator#contains(java.lang.Comparable)}.
	 */
	@Test
	public void testContains() {
		SortedLinkedListWithIterator<String> l = new SortedLinkedListWithIterator<String>();
		l.add("a");
		l.add("b");
		l.add("c");

		assertTrue(l.contains("a"));
		assertFalse(l.contains("d"));
	}

	/**
	 * Tests adding an element to SortedLinkedListWithIterator. Test method for
	 * {@link edu.ncsu.csc216.business.list_utils.SortedLinkedListWithIterator#add(java.lang.Comparable)}.
	 */
	@Test
	public void testAdd() {
		SortedLinkedListWithIterator<String> l = new SortedLinkedListWithIterator<String>();
		assertTrue(l.add("a"));
		assertTrue(l.add("c"));
		assertTrue(l.add("b"));

		assertEquals("a", l.get(0));
		assertEquals("c", l.get(2));
		assertEquals("b", l.get(1));
		assertEquals(3, l.size());

	}

	/**
	 * Tests clearing SortedLinkedListWithIterator. Test method for
	 * {@link edu.ncsu.csc216.business.list_utils.SortedLinkedListWithIterator#clear()}.
	 */
	@Test
	public void testClear() {
		SortedLinkedListWithIterator<String> l = new SortedLinkedListWithIterator<String>();
		l.add("a");
		l.add("b");
		l.add("c");

		l.clear();
		assertTrue(l.isEmpty());
	}

	/**
	 * Tests removing an element from SortedLinkedListWithIterator. Test method for
	 * {@link edu.ncsu.csc216.business.list_utils.SortedLinkedListWithIterator#remove(int)}.
	 */
	@Test
	public void testRemove() {
		SortedLinkedListWithIterator<String> l = new SortedLinkedListWithIterator<String>();
		l.add("a");
		l.add("c");
		l.add("b");

		assertEquals("b", l.remove(1));
		assertEquals(2, l.size());
		assertEquals("a", l.get(0));
		assertEquals("c", l.get(1));

		assertEquals("a", l.remove(0));
		assertEquals(1, l.size());
		assertEquals("c", l.get(0));

		assertEquals("c", l.remove(0));
		assertTrue(l.isEmpty());

	}

	/**
	 * Tests truncating the SortedLinkedListWithIterator. Test method for
	 * {@link edu.ncsu.csc216.business.list_utils.SortedLinkedListWithIterator#truncate(int)}.
	 */
	@Test
	public void testTruncate() {
		SortedLinkedListWithIterator<String> l = new SortedLinkedListWithIterator<String>();
		l.add("a");
		l.add("c");
		l.add("b");

		assertEquals("[c]", l.truncate(2).toString());
		assertEquals(2, l.size());
		assertEquals("a", l.get(0));
		assertEquals("b", l.get(1));

	}

	/**
	 * Tests returning the index of a given element in SortedLinkedListWithIterator.
	 * Test method for
	 * {@link edu.ncsu.csc216.business.list_utils.SortedLinkedListWithIterator#indexOf(java.lang.Comparable)}.
	 */
	@Test
	public void testIndexOf() {
		SortedLinkedListWithIterator<String> l = new SortedLinkedListWithIterator<String>();
		l.add("a");
		l.add("c");
		l.add("b");

		assertEquals(0, l.indexOf("a"));
		assertEquals(1, l.indexOf("b"));
		assertEquals(2, l.indexOf("c"));
	}

	/**
	 * Tests displaying SortedLinkedListWithIterator data as as String. Test method
	 * for
	 * {@link edu.ncsu.csc216.business.list_utils.SortedLinkedListWithIterator#toString()}.
	 */
	@Test
	public void testToString() {
		SortedLinkedListWithIterator<String> l = new SortedLinkedListWithIterator<String>();
		l.add("a");
		l.add("c");
		l.add("b");

		assertEquals("[a, b, c]", l.toString());

	}

}

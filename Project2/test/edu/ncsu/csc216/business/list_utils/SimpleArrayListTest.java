/**
 * 
 */
package edu.ncsu.csc216.business.list_utils;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the SimpleArrayList class.
 * 
 * Note that the test method for get has been omitted. It will be tested through
 * other methods.
 * 
 * @author Kaitlyn Gosline
 * @author Sanjana Cheerla
 *
 */
public class SimpleArrayListTest {

	/**
	 * Tests consturction of SimpleArrayList. Test method for
	 * {@link edu.ncsu.csc216.business.list_utils.SimpleArrayList#SimpleArrayList()}.
	 */
	@Test
	public void testSimpleArrayList() {
		SimpleArrayList<String> l = new SimpleArrayList<String>();
		assertEquals(0, l.size());

	}

	/**
	 * Tests construction of SimpleArrayList with a interger parameter. Test method
	 * for
	 * {@link edu.ncsu.csc216.business.list_utils.SimpleArrayList#SimpleArrayList(int)}.
	 */
	@Test
	public void testSimpleArrayListInt() {
		SimpleArrayList<String> l = new SimpleArrayList<String>(20);
		assertEquals(0, l.size());
	}

	/**
	 * Tests adding an element to end of the SimpleArrayList. Test method for
	 * {@link edu.ncsu.csc216.business.list_utils.SimpleArrayList#add(java.lang.Object)}.
	 */
	@Test
	public void testAddE() {
		SimpleArrayList<String> l = new SimpleArrayList<String>();
		assertTrue(l.add("a"));
		assertEquals("a", l.get(0));
		assertTrue(l.add("b"));
		assertEquals("b", l.get(1));
		assertTrue(l.add("c"));
		assertEquals("c", l.get(2));
	}

	/**
	 * Tests adding an element to the SimpleArrayList at a given index. Test method
	 * for
	 * {@link edu.ncsu.csc216.business.list_utils.SimpleArrayList#add(int, java.lang.Object)}.
	 */
	@Test
	public void testAddIntE() {
		SimpleArrayList<String> l = new SimpleArrayList<String>();

		// adding an index < 0
		try {
			l.add(-1, "a");
			fail();
		} catch (IndexOutOfBoundsException e) {
			assertTrue(l.isEmpty());
		}

		// adding an index > size
		try {
			l.add(1, "a");
			fail();
		} catch (IndexOutOfBoundsException e) {
			assertTrue(l.isEmpty());
		}

		// adding a null element
		try {
			l.add(0, null);
			fail();
		} catch (NullPointerException e) {
			assertTrue(l.isEmpty());
		}

		// add to empty ArrayList
		l.add(0, "a");
		assertEquals(1, l.size());
		assertEquals("a", l.get(0));

		// adding the same element
		try {
			l.add(1, "a");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(1, l.size());
			assertEquals("a", l.get(0));
		}

		// add to front of ArrayList
		l.add(0, "b");
		assertEquals(2, l.size());
		assertEquals("b", l.get(0));
		assertEquals("a", l.get(1));

		// add to back of ArrayList
		l.add(2, "c");
		assertEquals(3, l.size());
		assertEquals("b", l.get(0));
		assertEquals("a", l.get(1));
		assertEquals("c", l.get(2));

		// add to middle of ArrayList
		l.add(2, "d");
		assertEquals(4, l.size());
		assertEquals("b", l.get(0));
		assertEquals("a", l.get(1));
		assertEquals("d", l.get(2));
		assertEquals("c", l.get(3));
	}

	/**
	 * Tests contains method to see if SimpleArrayList contains a element. Test
	 * method for
	 * {@link edu.ncsu.csc216.business.list_utils.SimpleArrayList#contains(java.lang.Object)}.
	 */
	@Test
	public void testContains() {
		SimpleArrayList<String> l = new SimpleArrayList<String>();
		l.add("a");
		l.add("b");
		l.add("c");

		assertTrue(l.contains("a"));
		assertFalse(l.contains("d"));
	}

	/**
	 * Tests removing an element from the SimpleArrayList. Test method for
	 * {@link edu.ncsu.csc216.business.list_utils.SimpleArrayList#remove(int)}.
	 */
	@Test
	public void testRemove() {
		SimpleArrayList<String> l = new SimpleArrayList<String>();

		// add to empty ArrayList
		l.add(0, "a");
		assertEquals(1, l.size());
		assertEquals("a", l.get(0));

		// add to the front of ArrayList
		l.add(0, "b");
		assertEquals(2, l.size());
		assertEquals("b", l.get(0));
		assertEquals("a", l.get(1));

		// add to back of ArrayList
		l.add(2, "c");
		assertEquals(3, l.size());
		assertEquals("b", l.get(0));
		assertEquals("a", l.get(1));
		assertEquals("c", l.get(2));

		// add to middle of ArrayList
		l.add(2, "d");
		assertEquals(4, l.size());
		assertEquals("b", l.get(0));
		assertEquals("a", l.get(1));
		assertEquals("d", l.get(2));
		assertEquals("c", l.get(3));

		// remove element from back
		assertEquals("c", l.remove(3));
		assertEquals(3, l.size());
		assertEquals("b", l.get(0));
		assertEquals("a", l.get(1));
		assertEquals("d", l.get(2));

		// remove element from middle
		assertEquals("a", l.remove(1));
		assertEquals(2, l.size());
		assertEquals("b", l.get(0));
		assertEquals("d", l.get(1));

		// remove element from front
		assertEquals("b", l.remove(0));
		assertEquals(1, l.size());
		assertEquals("d", l.get(0));

	}

	/**
	 * Tests returning of the index of a given element in the SimpleArrayList. Test
	 * method for
	 * {@link edu.ncsu.csc216.business.list_utils.SimpleArrayList#indexOf(java.lang.Object)}.
	 */
	@Test
	public void testIndexOf() {
		SimpleArrayList<String> l = new SimpleArrayList<String>();
		l.add("a");
		l.add("b");
		l.add("c");

		assertEquals(0, l.indexOf("a"));
		assertEquals(1, l.indexOf("b"));
		assertEquals(2, l.indexOf("c"));

	}

}
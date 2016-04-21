package com.team3925.robot2016.util;

import java.util.LinkedList;

/**
 * A modification to the standard LinkedList that implements a max size parameter.
 * <p>
 * <i>Currently, the only way to maintain size while adding values is through use of the {@link #addFirst(Object)}</i>
 *
 * @see LinkedList
 * @param <E> the type of elements held in this collection
 */
public class FixedSizeLinkedList<E> extends LinkedList<E> {
		private static final long serialVersionUID = 2021650288089001522L;
		private final int MAX_ENTRIES;

		public FixedSizeLinkedList(int maxEntries) {
			MAX_ENTRIES = maxEntries;
		}
		
		@Override
		public void addFirst(E e) {
			super.addFirst(e);
			if (size() > MAX_ENTRIES) {
				removeLast();
			}
		}

		public int getMaxEntries() {
			return MAX_ENTRIES;
		}
		
	}
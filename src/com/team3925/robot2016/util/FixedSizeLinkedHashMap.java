package com.team3925.robot2016.util;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * <code>FixedSizeLinkedHashMap</code> is an override of {@link LinkedHashMap} to implement a 
 * fixed size capacity. The number of entries in this array will never exceed maxEntries.
 * <p>
 * This was functionality was created as demonstrated on the java tutorial website.
 * See <a href="https://docs.oracle.com/javase/tutorial/collections/implementations/map.html">The Java Tutorial</a>
 *
 * @see LinkedHashMap
 */
public class FixedSizeLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = -1505281431045446875L;
	private final int MAX_ENTRIES;

	/**
	 * Makes a {@link FixedSizeLinkedHashMap} with a default initial capacity of 16
	 * @param maxEntries int of maximum number of entries allowed in map
	 */
	public FixedSizeLinkedHashMap(int maxEntries) {
		this(maxEntries, 16);
	}

	/**
	 *
	 * @param maxEntries int of maximum number of entries allowed in map
	 * @param initialCapacity int of initial capcaity of the map
	 */
	public FixedSizeLinkedHashMap(int maxEntries, int initialCapacity) {
		super(initialCapacity);
		MAX_ENTRIES = maxEntries;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() > MAX_ENTRIES;
	}

	public int getMaxEntries() {
		return MAX_ENTRIES;
	}
}
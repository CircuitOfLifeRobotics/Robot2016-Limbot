package com.team3925.robot2016.util;

import java.util.TimerTask;

/**
 * This class watches values returned by a {@link InputWatcherInput} 
 * (an interface inside {@link InputWatcher})
 * and checks if they are moving – defined as having two values father
 * apart than tolerance.
 * <p>
 * To use, schedule a Timer to run an instance of this class at
 * a fixed period. Then, call isChanging() to get information.
 * 
 * @author Bryan
 */
public class InputWatcher extends TimerTask {
	
	/**
	 * Interface for input to use in {@link InputWatcher}
	 */
	public static interface InputWatcherInput {
		/**
		 * @return double of data to monitor
		 */
		public double getInputWatcherInput();
	}
	
	// CONSTANTS
	private final double TOLERANCE;
	private final int STASH_SIZE;

	private FixedSizeLinkedList<Double> list;
	private boolean isChanging;
	private InputWatcherInput input;


	/**
	 * Construct a new {@link InputWatcher} with the given arguments.
	 * 
	 * @param stashSize int of number of samples to compare
	 * @param tolerance double in degrees of deadzone for not moving
	 * @param input a implementation of the {@link InputWatcherInput}
	 * 
	 * @throws IllegalArgumentException if <code>stashSize</code> < 1
	 * @throws IllegalArgumentException if <code>tolerance</code> is not a finite number
	 */
	public InputWatcher(int stashSize, double tolerance, InputWatcherInput input) {
		if (!Double.isFinite(tolerance)) {
			throw new IllegalArgumentException("Cannot have a non-finite argument for tolerance!");
		}
		TOLERANCE = Math.abs(tolerance);
		if (stashSize < 1) {
			throw new IllegalArgumentException("Cannot have a stash size of less than 1!");
		}
		// NOTE: The latest value is also placed in the array before
		// running logic. That is why it is stashsize + 1
		STASH_SIZE = stashSize+1;
		this.input = input;
		list = new FixedSizeLinkedList<>(STASH_SIZE);
		isChanging = false;
	}

	@Override
	public void run() {
		list.addFirst(input.getInputWatcherInput());
		
		// Do not run if there are less than the required number of entries
		if (list.size() >= STASH_SIZE) {
			// slightly modified logic from N-size AND gate
			boolean tmp_notChanging = insideTolerance(list.get(0), list.get(1));
			for (int i = 1; i < STASH_SIZE-1; i++) {
				tmp_notChanging = tmp_notChanging && insideTolerance(list.get(i), list.get(i+1));
			}
			isChanging = !tmp_notChanging;
		}
	}
	
	/**
	 * Warning: {@link InputWatcher} does not run calculations until the stash is
	 * filled with data. <i>Until then, the condition will return false, regardless
	 * as to what the actual values may be.</i>
	 * 
	 * @return if the value is considered changing
	 */
	public boolean isChanging() {
		return isChanging;
	}
	
	/**
	 * @param val1 first value
	 * @param val2 second value
	 * @return false if within tolerance, true if not
	 */
	private boolean insideTolerance(double val1, double val2) {
		if (Math.abs(val1 - val2) < TOLERANCE) {
			return true;
		} else {
			return false;
		}
	}
	
}

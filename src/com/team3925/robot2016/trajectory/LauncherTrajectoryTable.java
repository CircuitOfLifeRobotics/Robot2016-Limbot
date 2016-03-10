package com.team3925.robot2016.trajectory;

import java.util.HashMap;

/**
 * Holds a set of intake speed and distance values gathered from testing.
 */
public class LauncherTrajectoryTable {
	
	private final HashMap<Double, Double> table;
	private final double ANGLE;
	
	public LauncherTrajectoryTable(int size, double angle) {
		table = new HashMap<>(size);
		ANGLE = angle;
	}
	
	public void addValue(double speed, double distance) {
		table.put(speed, distance);
	}
	
	public double getValue(int speed) {
		return table.get(speed);
	}
	
	public double getAngle() {
		return ANGLE;
	}
	
	public double getCoefficient() {
		return 0; // TODO Add implementation
	}

}

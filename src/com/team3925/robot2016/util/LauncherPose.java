package com.team3925.robot2016.util;

public final class LauncherPose {
	public LauncherPose(double leftEncPos, double rightEncPos, double leftEncVel, double rightEncVel,
			double aimEncPos, double aimEncVel) {
		this.intakeLeftEncPos = leftEncPos;
		this.intakeRightEncPos = rightEncPos;
		this.intakeLeftEncVel = leftEncVel;
		this.intakeRightEncVel = rightEncVel;
		this.aimEncPos = aimEncPos;
		this.aimEncVel = aimEncVel;
	}
	
	private double intakeLeftEncPos;
	private double intakeRightEncPos;
	private double intakeLeftEncVel;
	private double intakeRightEncVel;
	private double aimEncPos;
	private double aimEncVel;
	
	public void reset(double leftEncPos, double rightEncPos, double leftEncVel, double rightEncVel,
			double aimEncPos, double aimEncVel) {
		this.intakeLeftEncPos = leftEncPos;
		this.intakeRightEncPos = rightEncPos;
		this.intakeLeftEncVel = leftEncVel;
		this.intakeRightEncVel = rightEncVel;
		this.aimEncPos = aimEncPos;
		this.aimEncVel = aimEncVel;
	}
	
	public double getIntakeLeftEncPos() {
		return intakeLeftEncPos;
	}
	
	public double getIntakeRightEncPos() {
		return intakeRightEncPos;
	}
	
	public double getIntakeLeftEncVel() {
		return intakeLeftEncVel;
	}

	public double getIntakeRightEncVel() {
		return intakeRightEncVel;
	}

	public double getAimEncPos() {
		return aimEncPos;
	}

	public double getAimEncVel() {
		return aimEncVel;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LauncherPose))
			return false;
		if (obj == this)
			return true;
		LauncherPose otherPose = (LauncherPose) obj;
		return otherPose.getIntakeLeftEncPos() == getIntakeLeftEncPos() && otherPose.getIntakeRightEncPos() == getIntakeRightEncPos() &&
				otherPose.getIntakeLeftEncVel() == getIntakeLeftEncVel() && otherPose.getIntakeRightEncVel() == getIntakeRightEncVel() &&
				otherPose.getAimEncPos() == getAimEncPos() && otherPose.getAimEncVel() == getAimEncVel();
	}
}

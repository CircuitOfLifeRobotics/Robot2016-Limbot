package com.team3925.robot2016.util;

public class LauncherPose {
	public LauncherPose(double leftEncPos, double rightEncPos, double leftEncVel, double rightEncVel,
			double aimEncPos, double aimEncVel) {
		this.leftEncPos = leftEncPos;
		this.rightEncPos = rightEncPos;
		this.leftEncVel = leftEncVel;
		this.rightEncVel = rightEncVel;
		this.aimEncPos = aimEncPos;
		this.aimEncVel = aimEncVel;
	}
	
	public double leftEncPos;
	public double rightEncPos;
	public double leftEncVel;
	public double rightEncVel;
	public double aimEncPos;
	public double aimEncVel;
	
	public void reset(double leftEncPos, double rightEncPos, double leftEncVel, double rightEncVel,
			double aimEncPos, double aimEncVel) {
		this.leftEncPos = leftEncPos;
		this.rightEncPos = rightEncPos;
		this.leftEncVel = leftEncVel;
		this.rightEncVel = rightEncVel;
		this.aimEncPos = aimEncPos;
		this.aimEncVel = aimEncVel;
	}
	
	public double getLeftEncPos() {
		return leftEncPos;
	}
	
	public double getRightEncPos() {
		return rightEncPos;
	}
	
	public double getLeftEncVel() {
		return leftEncVel;
	}
	
	public double getRightEncVel() {
		return rightEncVel;
	}
	
	public double getAimEncPos() {
		return aimEncPos;
	}
	
	public double getAimEncVel() {
		return aimEncVel;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LauncherPose))
			return false;
		if (obj == this)
			return true;
		LauncherPose otherPose = (LauncherPose) obj;
		return otherPose.getLeftEncPos() == getLeftEncPos() && otherPose.getRightEncPos() == getRightEncPos();
//				&& otherPose.getLeftEncVel() == getLeftEncVel() && otherPose.getRightVelocity() == getRightVelocity() &&
//				otherPose.getHeading() == getHeading() && otherPose.getHeadingVelocity() == getHeadingVelocity();
	}
}

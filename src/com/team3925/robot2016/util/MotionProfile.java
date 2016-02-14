package com.team3925.robot2016.util;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class MotionProfile {
	
	private static final int minPointsInTalon = 5;
	private static final int numLoopsTimeout = 10;
	
	private CANTalon.MotionProfileStatus status = new CANTalon.MotionProfileStatus();
	private CANTalon talon;
	private int state = 0;
	private int loopTimeout = -1; // -1 will never timeout
	private boolean bStart = false;
	private CANTalon.SetValueMotionProfile setValue = CANTalon.SetValueMotionProfile.Disable;
	
	private double[][] profile;
	
	class PeriodicRunnable implements java.lang.Runnable {
		public void run() {talon.processMotionProfileBuffer();}
	}
	Notifier notifier = new Notifier(new PeriodicRunnable());
	
	public MotionProfile(CANTalon talon, double[][] motionProfile) {
		this.talon = talon;
		this.talon.changeMotionControlFramePeriod(5);
		this.profile = motionProfile;
		notifier.startPeriodic(0.005);
	}
	
	/**
	 * Called to clear the motion profile buffer and reset state info
	 */
	public void reset() {
		talon.clearMotionProfileTrajectories();
		setValue = CANTalon.SetValueMotionProfile.Disable;
		state = 0;
		loopTimeout = -1;
		bStart = false;
	}
	
	/**
	 * Called every loop
	 */
	public void control() {
		talon.getMotionProfileStatus(status);
		
		/*
		 * Rudimentary time tracking, make sure things don't get stuck
		 */
		if (loopTimeout < 0) {
			//timeout is disabled
		}else {
			if (loopTimeout == 0) {
				//something is wrong
			}else {
				--loopTimeout;
			}
		}
		
		if (talon.getControlMode() != TalonControlMode.MotionProfile) {
			state = 0;
			loopTimeout = -1;
		}else {
			switch (state) {
			//wait for application to start the motion profile
			case 0:
				if (bStart) {
					bStart = false;
					setValue = CANTalon.SetValueMotionProfile.Disable;
					startFilling();
					//wait some time while data is sent to CAN bus
					state = 1;
					loopTimeout = numLoopsTimeout;
				}
				break;
			//wait for first few points to stream to talon
			case 1:
				//if there is a minimum amount of points on the talon
				if (status.btmBufferCnt > minPointsInTalon) {
					//start
					setValue = CANTalon.SetValueMotionProfile.Enable;
					//motion profile will start
					state = 2;
					loopTimeout = numLoopsTimeout;
				}
				break;
			//check status of motion profile
			case 2:
				//if talon is good, add to timeout
				if (status.isUnderrun == false) {
					loopTimeout = numLoopsTimeout;
				}
				
				//if the motion profile is finished, start loading another
				if (status.activePointValid && status.activePoint.isLastPoint) {
					setValue = CANTalon.SetValueMotionProfile.Hold;
					state = 0;
					loopTimeout = -1;
				}
				break;
			}
		}
	}
	
	/**
	 * Start filling the motion profiles to all of the talons
	 */
	private void startFilling() {
		startFilling(profile, profile.length);
	}
	
	private void startFilling(double[][] profile, int totalCount) {
		CANTalon.TrajectoryPoint point = new CANTalon.TrajectoryPoint();
		
		if (status.hasUnderrun) {
			talon.clearMotionProfileHasUnderrun();
		}
		talon.clearMotionProfileTrajectories();
		
		for (int i=0; i<totalCount; ++i) {
			point.position = profile[i][0];
			point.velocity = profile[i][1];
			point.timeDurMs = (int) profile[i][2];
			point.profileSlotSelect = 0;
			point.velocityOnly = false;
			point.zeroPos = (i==0);
			point.isLastPoint = ((i+1) == totalCount);
			talon.pushMotionProfileTrajectory(point);
		}
	}
	
	/**
	 * Called by application to start the buffered motion profile
	 */
	public void startMotionProfile() {
		bStart = true;
	}
	
	public CANTalon.SetValueMotionProfile getSetValue() {
		return setValue;
	}
}

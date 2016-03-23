package com.team3925.robot2016.commands.defensecommands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

/**
 * Does default crossing routine except with arms up
 */
public class CrossDefault extends DefenseCrossBase implements SmartdashBoardLoggable {
	
	private boolean armsUp = true;
//	private SynchronousPID pidLoop;
//	private double startAngle, currentAngle, lastAngle, rotations, moveVal = -1;
	
	
	/**
	 * Defaults to putting arms up
	 */
	public CrossDefault() {
		this(true);
//		pidLoop = new SynchronousPID(Constants.GYRO_DRIVE_KP, Constants.GYRO_DRIVE_KI, Constants.GYRO_DRIVE_KD);
//		
//		startAngle = currentAngle = lastAngle = navx.getFusedHeading();
//		rotations = 0;
//		
//		pidLoop.setSetpoint(startAngle);
	}
	
	public CrossDefault(boolean putArmsUp) {
		super(true);
		armsUp = putArmsUp;
	}
	
	@Override
	protected void routine() {
		Robot.plexiArms.setArmUp(armsUp);
//		currentAngle = navx.getFusedHeading();
//		if (Math.abs(currentAngle - lastAngle) > 180) {
//			rotations += (currentAngle-lastAngle)>0 ? -1:1;
//		}
//		pidLoop.calculate(currentAngle + rotations*360);
//		
//		driveTrain.arcadeDrive(moveVal, -pidLoop.get(), false);
//		
//		lastAngle = currentAngle;
	}
	
	@Override
	public String getFormattedName() {
		return "CrossDefault_";
	}
	
	public void logData() {
//		putNumberSD("StartAngle", startAngle);
//		putNumberSD("CurrentAngle", currentAngle);
	}
}

package com.team3925.robot2016.commands.defensecommands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.SynchronousPID;

public class CrossLowBar extends DefenseCrossBase implements SmartdashBoardLoggable {
	
	private SynchronousPID pidLoop;
	
	private double startAngle, currentAngle, lastAngle, rotations, moveVal = -1;
	
	public CrossLowBar() {
		pidLoop = new SynchronousPID(Constants.GYRO_DRIVE_KP, Constants.GYRO_DRIVE_KI, Constants.GYRO_DRIVE_KD);
		
		startAngle = currentAngle = lastAngle = navx.getFusedHeading();
		rotations = 0;
		
		pidLoop.setSetpoint(startAngle);
	}
	
	@Override
	public String getFormattedName() {
		return "LowBarAuto_";
	}
	
	@Override
	protected void routine() {
		currentAngle = navx.getFusedHeading();
		if (Math.abs(currentAngle - lastAngle) > 180) {
			rotations += (currentAngle-lastAngle)>0 ? -1:1;
		}
		pidLoop.calculate(currentAngle + rotations*360);
		
		driveTrain.arcadeDrive(moveVal, pidLoop.get(), false);
		
		lastAngle = currentAngle;
	}
	
	public void logData() {
		putNumberSD("StartAngle", startAngle);
		putNumberSD("CurrentAngle", currentAngle);
	}
}

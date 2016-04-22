package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.commands.auto.GyroTurn;
import com.team3925.robot2016.util.CameraHelper;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class AimHoriz extends Command implements SmartdashBoardLoggable {
	private GyroTurn gyroTurn;
	private CameraHelper camHelper = CameraHelper.getInstance();
	private boolean doLoop;
	private boolean hasStarted;
	private int iteration;
	
	public void AimHoriz() {
		doLoop = true;
	}
	
	public void AimHoriz(boolean doLoop) {
		this.doLoop = doLoop;
	}
	
	@Override
	protected void initialize() {
		iteration = 0;
		iteration++;
		System.out.println("Iteration "+iteration);
		hasStarted = false;
		if (camHelper.dataAge<=Constants.CAMERA_MAX_DATA_AGE) {
			gyroTurn = new GyroTurn(camHelper.xDegOffset);
			gyroTurn.start();
			hasStarted = true;
		}else {
			DriverStation.reportError("Cannot begin gyro turn, camera data is too old!", false);
		}
	}

	@Override
	protected void execute() {
		if (!hasStarted) {
			if (camHelper.dataAge<=Constants.CAMERA_MAX_DATA_AGE) {
				gyroTurn = new GyroTurn(camHelper.xDegOffset);
				putNumberSD("XdegOffset", camHelper.xDegOffset);
				gyroTurn.start();
				hasStarted = true;
			}else {
				DriverStation.reportError("Cannot begin gyro turn, camera data is too old!", false);
			}
		}
		
		logData();
	}

	@Override
	protected boolean isFinished() {
		if (!gyroTurn.isRunning()) {
			if (camHelper.xDegOffset>Constants.CAMERA_X_DEG_OFFSET_TOL && doLoop)
				initialize();
			else
				return true;
		}
		return false;
	}

	@Override
	protected void end() {
		gyroTurn.cancel();
	}

	@Override
	protected void interrupted() {
		gyroTurn.cancel();
	}

	@Override
	public void logData() {
		putBooleanSD("HasStarted", hasStarted);
		putNumberSD("Iteration", iteration);
	}

	@Override
	public String getFormattedName() {
		return "AimHorizontal_";
	}
	
	
	
}

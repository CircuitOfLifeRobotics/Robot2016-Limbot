package com.team3925.robot2016.commands;

import com.kauailabs.navx.frc.AHRS;
import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.DriveTrain;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.SynchronousPID;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.command.Command;

public class GyroDrive extends Command implements SmartdashBoardLoggable {
	
	private SynchronousPID pidLoop;
	private double startAngle, currentAngle, lastAngle, rotations, moveVal = -1, timeout;
	private final AHRS navx = Robot.navx;
	private final DriveTrain driveTrain = Robot.driveTrain;
	private final TimeoutAction timeoutAction = new TimeoutAction();
	
	/**
	 *  Creates a gyrodrive with the default timeout of 3 seconds
	 */
	public GyroDrive() {
		this(3);
	}

	/**
	 * @param timeout time to drive in seconds
	 */
	public GyroDrive(double timeout) {
		requires(driveTrain);
		this.timeout = timeout;
	}
	
	@Override
	protected void initialize() {
		pidLoop = new SynchronousPID(Constants.GYRO_DRIVE_KP, Constants.GYRO_DRIVE_KI, Constants.GYRO_DRIVE_KD);
		
		startAngle = currentAngle = lastAngle = navx.getFusedHeading();
		rotations = 0;
		
		pidLoop.setSetpoint(startAngle);
		timeoutAction.config(timeout);
	}

	@Override
	protected void execute() {
		currentAngle = navx.getFusedHeading();
		if (Math.abs(currentAngle - lastAngle) > 180) {
			rotations += (currentAngle-lastAngle)>0 ? -1:1;
		}
		pidLoop.calculate(currentAngle + rotations*360);
		
		driveTrain.arcadeDrive(moveVal, -pidLoop.get(), false);
		
		lastAngle = currentAngle;
	}

	@Override
	protected boolean isFinished() {
		return timeoutAction.isFinished();
	}

	@Override
	protected void end() {
		driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
	}

	@Override
	protected void interrupted() {
		driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
	}

	@Override
	public void logData() {
		putNumberSD("StartAngle", startAngle);
		putNumberSD("CurrentAngle", currentAngle);
	}

	@Override
	public String getFormattedName() {
		return "GyroDrive_";
	}

}

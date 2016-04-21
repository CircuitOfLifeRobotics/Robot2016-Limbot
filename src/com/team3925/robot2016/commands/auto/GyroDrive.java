package com.team3925.robot2016.commands.auto;

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
	private double startAngle, currentAngle, lastAngle, rotations, distance, timeout, forwardPower;
	private final AHRS navx = Robot.navx;
	private final DriveTrain driveTrain = Robot.driveTrain;
	private final TimeoutAction timeoutAction = new TimeoutAction();
	
	/**
	 * Creates a gyro drive with default parameters of disabled timeout and no timeout
	 * @param distance distance to travel in inches
	 */
	public GyroDrive(double distance) {
		this(distance, false, -1, 1);
	}
	
	/**
	 * Creates a default gyrodrive that drives until stopped
	 */
	public GyroDrive() {
		this(Double.NaN, true, -1, 1);
	}
	
	/**
	 * @param distance distance to travel in inches
	 * @param disableEncPos override distance and travel until stopped
	 * @param timeout if encPos disabled, time to drive until command stops.
	 * 	Only works when <code>disableEncPos</code> is true
	 * @param forwardPower power from -1 to 1 to pass to arcade drive
	 */
	public GyroDrive(double distance, boolean disableEncPos, double timeout, double forwardPower) {
		requires(driveTrain);
		this.distance = disableEncPos ? Double.NaN : distance;
		this.timeout = disableEncPos ? timeout : -1;
		this.forwardPower = forwardPower;
	}
	
	
	@Override
	protected void initialize() {
		pidLoop = new SynchronousPID(Constants.GYRO_DRIVE_KP, Constants.GYRO_DRIVE_KI, Constants.GYRO_DRIVE_KD);
		
		startAngle = currentAngle = lastAngle = navx.getFusedHeading();
		rotations = 0;
		
		pidLoop.setSetpoint(startAngle);
		timeoutAction.config(timeout);
		driveTrain.resetEncoders();
	}

	@Override
	protected void execute() {
		currentAngle = navx.getFusedHeading();
		if (Math.abs(currentAngle - lastAngle) > 180) {
			rotations += (currentAngle-lastAngle)>0 ? -1:1;
		}
		pidLoop.calculate(currentAngle + rotations*360);
		
		// normally negative for drive power | pid loop input was positive on comp robot
		driveTrain.arcadeDrive(-forwardPower, -pidLoop.get(), false);
		
		// logic for slowing down or stopping here
		
		lastAngle = currentAngle;
	}

	@Override
	protected boolean isFinished() {
		if (Double.isNaN(distance)) {
			return timeoutAction.isFinished();
		} else {
			return Math.abs(getAverageDriveTrainEncoders() - distance) < Constants.GYRO_DRIVE_ON_TARGET_ERROR_INCHES;
		}
	}
	
	private double getAverageDriveTrainEncoders() {
		return (driveTrain.getPhysicalPose().m_left_distance + driveTrain.getPhysicalPose().m_right_distance) / 2;
	}
	
	@Override
	protected void end() {
		driveTrain.setMotorSpeeds(0,0);
	}

	@Override
	protected void interrupted() {
		driveTrain.setMotorSpeeds(0,0);
	}

	@Override
	public void logData() {
		putNumberSD("StartAngle", startAngle);
		putNumberSD("CurrentAngle", currentAngle);
		putNumberSD("AverageEncDistance", getAverageDriveTrainEncoders());
		putNumberSD("Timeout", timeout);
	}

	@Override
	public String getFormattedName() {
		return "GyroDrive_";
	}

}

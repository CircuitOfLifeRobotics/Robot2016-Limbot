package com.team3925.robot2016.commands;

import static com.team3925.robot2016.util.XboxHelper.AXIS_LEFT_Y;
import static com.team3925.robot2016.util.XboxHelper.AXIS_RIGHT_X;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.DriveTrain;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ManualDrive extends Command implements SmartdashBoardLoggable {
	private DriveTrain driveTrain = Robot.driveTrain;
	
	private double fwdSet, turnSet, lastFwdSet, lastTurnSet, deltaFwd, deltaTurn;
	
	public ManualDrive() {
		requires(Robot.driveTrain);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		fwdSet = XboxHelper.getDriverAxis(AXIS_LEFT_Y);
		turnSet = XboxHelper.getDriverAxis(AXIS_RIGHT_X);
		deltaFwd = fwdSet - lastFwdSet;
		deltaTurn = turnSet - lastTurnSet;
		
		if (Math.abs(fwdSet) > Math.abs(lastFwdSet)) {
			if (Math.abs(deltaFwd) > Constants.MAX_DRIVETRAIN_ACCEL_PWR_PER_TICK) {
				fwdSet = lastFwdSet + Constants.MAX_DRIVETRAIN_ACCEL_PWR_PER_TICK * (deltaFwd>0 ? 1:-1);
			}
		}
		
		driveTrain.arcadeDrive(fwdSet, turnSet, true);
		boolean left = XboxHelper.getDriverButton(XboxHelper.TRIGGER_LT);
		boolean right = XboxHelper.getDriverButton(XboxHelper.TRIGGER_RT);
		if (right || left) {
			driveTrain.setHighGear(true);
		} else {
			driveTrain.setHighGear(false);
		}
		
		lastFwdSet = fwdSet;
		lastTurnSet = turnSet;
		
		logData();
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
	}
	
	@Override
	public void logData() {
		SmartDashboard.putNumber("Fwd_MotorPwr", fwdSet);
	}
	
	@Override
	public String getFormattedName() {
		return "ManualDrive_";
	}
}

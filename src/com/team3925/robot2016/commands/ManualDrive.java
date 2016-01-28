package com.team3925.robot2016.commands;

import static com.team3925.robot2016.util.ControllersHelper.AXIS_LEFT_Y;
import static com.team3925.robot2016.util.ControllersHelper.AXIS_RIGHT_X;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.DriveTrain;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.ControllersHelper;
import com.team3925.robot2016.util.ControllersHelper.CurrentController;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ManualDrive extends Command {
	private DriveTrain driveTrain = Robot.driveTrain;

	public ManualDrive() {
		requires(Robot.driveTrain);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		driveTrain.setMotorSpeeds(
				arcadeDrive(-ControllersHelper.getAxis(CurrentController.DRIVER, AXIS_LEFT_Y), 
						ControllersHelper.getAxis(CurrentController.DRIVER, AXIS_RIGHT_X), true));
	}

	private DriveTrainSignal arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs) {
		//	Copied from RobotDrive and modified to output a DriveTrainSignal instead
		double leftMotorSpeed;
		double rightMotorSpeed;

		moveValue = MiscUtil.limit(moveValue);
		rotateValue = MiscUtil.limit(rotateValue);

		if (squaredInputs) {
			// square the inputs (while preserving the sign) to increase fine control
			// while permitting full power
			if (moveValue >= 0.0) {
				moveValue = (moveValue * moveValue);
			} else {
				moveValue = -(moveValue * moveValue);
			}
			if (rotateValue >= 0.0) {
				rotateValue = (rotateValue * rotateValue);
			} else {
				rotateValue = -(rotateValue * rotateValue);
			}
		}

		if (moveValue > 0.0) {
			if (rotateValue > 0.0) {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = Math.max(moveValue, rotateValue);
			} else {
				leftMotorSpeed = Math.max(moveValue, -rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			}
		} else {
			if (rotateValue > 0.0) {
				leftMotorSpeed = -Math.max(-moveValue, rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			} else {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
			}
		}

		return new DriveTrainSignal(leftMotorSpeed, rightMotorSpeed);

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
}

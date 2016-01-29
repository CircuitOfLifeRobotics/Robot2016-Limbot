package com.team3925.robot2016.commands;

import static com.team3925.robot2016.util.ControllersHelper.AXIS_LEFT_Y;
import static com.team3925.robot2016.util.ControllersHelper.AXIS_RIGHT_X;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.DriveTrain;
import com.team3925.robot2016.util.ControllersHelper;
import com.team3925.robot2016.util.ControllersHelper.CurrentController;
import com.team3925.robot2016.util.DriveTrainSignal;

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
		driveTrain. arcadeDrive(-ControllersHelper.getAxis(CurrentController.DRIVER, AXIS_LEFT_Y), 
						ControllersHelper.getAxis(CurrentController.DRIVER, AXIS_RIGHT_X), true);
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

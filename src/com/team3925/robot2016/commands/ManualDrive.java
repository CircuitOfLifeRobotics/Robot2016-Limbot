package com.team3925.robot2016.commands;

import com.team3925.robot2016.OI;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.DriveTrain;
import com.team3925.robot2016.util.DriveTrainSignal;

import edu.wpi.first.wpilibj.command.Command;

public class ManualDrive extends Command {
	private final DriveTrain driveTrain = Robot.driveTrain;
	private final OI oi = Robot.oi;
	
	public ManualDrive() {
		requires(Robot.driveTrain);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.cdh.cheesyDrive(
				oi.getManualDrive_ForwardValue(),
				oi.getManualDrive_RotateValue(),
				oi.getManualDrive_QuickTurn(),
				driveTrain.isHighGear());
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		driveTrain.setMotorSpeeds(0,0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		driveTrain.setMotorSpeeds(0,0);
	}
	
}

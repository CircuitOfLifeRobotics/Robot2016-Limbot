package com.team3925.robot2016.commands;

import edu.wpi.first.wpilibj.command.Command;
import com.team3925.robot2016.Robot;

/**
 *
 */
public class CollectBall extends Command {
	private boolean hasBall = false;

	public CollectBall() {

		requires(Robot.launcher);
		requires(Robot.driveTrain);
		//	Can you even do this?
		//	It is stored in a Set so...

	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return hasBall;
	}

	// Called once after isFinished returns true
	protected void end() {

	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}

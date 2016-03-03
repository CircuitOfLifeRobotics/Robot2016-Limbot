package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.command.Command;

enum CollectMode {
	WAIT_FOR_DOWN, INTAKE;
}

/**
 * Turns on intake until launcher has a ball
 */
public class CollectBall extends Command {
	
	private final Launcher launcher = Robot.launcher;
	TimeoutAction timeout = new TimeoutAction();
	CollectMode mode;
	
	public CollectBall() {
		requires(launcher);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		mode = CollectMode.WAIT_FOR_DOWN;
		
		launcher.setAimSetpoint(0);
		timeout.config(7);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		switch (mode) {
		case WAIT_FOR_DOWN:
			launcher.setIntakeSetpoint(-Constants.LAUNCHER_MAX_INTAKE_SPEED);
			if (launcher.isAimOnSetpoint() || timeout.isFinished()) {
				mode = CollectMode.INTAKE;
				timeout.config(4);
			}
			break;
		case INTAKE:
			break;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return launcher.hasBall() || (timeout.isFinished() && mode==CollectMode.INTAKE) || Robot.oi.getCommandCancel();
	}

	// Called once after isFinished returns true
	protected void end() {
		launcher.setIntakeSetpoint(0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		launcher.setIntakeSetpoint(0);
	}
}

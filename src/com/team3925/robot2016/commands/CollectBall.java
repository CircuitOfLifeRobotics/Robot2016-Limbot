package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.IntakeAssist;
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
	private final IntakeAssist intakeAssist = Robot.intakeAssist;
	TimeoutAction timeout = new TimeoutAction();
	private CollectMode mode;
	
	public CollectBall() {
		requires(launcher);
		requires(intakeAssist);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		mode = CollectMode.WAIT_FOR_DOWN;
		
		launcher.setAimSetpoint(0);
		timeout.config(30);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		switch (mode) {
		case WAIT_FOR_DOWN:
			launcher.setIntakeSetpoint(-24000);
			intakeAssist.setWheelSpeeds(1d);
//			if (launcher.isAimOnSetpoint() || timeout.isFinished()) {
//				mode = CollectMode.INTAKE;
//				timeout.config(4);
//				end();
//			}
//			break;
//		case INTAKE:
//			break;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return /*launcher.hasBall() ||*/ (timeout.isFinished() && mode==CollectMode.INTAKE) || Robot.oi.getCommandCancel();
	}

	// Called once after isFinished returns true
	protected void end() {
		launcher.setIntakeSetpoint(0);
		launcher.setAimSetpoint(0);
		intakeAssist.setWheelSpeeds(0d);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		launcher.setAimSetpoint(0);
		launcher.setIntakeSetpoint(0);
		intakeAssist.setWheelSpeeds(0d);
	}
}

package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.IntakeAssist;
import com.team3925.robot2016.subsystems.Launcher;

import edu.wpi.first.wpilibj.command.Command;

public class CollectBall extends Command {
	
	private final Launcher launcher = Robot.launcher;
	private final IntakeAssist intakeAssist = Robot.intakeAssist;
	
	public CollectBall() {
		super("CollectBall");
		
		requires(launcher);
		requires(intakeAssist);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		launcher.setAimSetpoint(0);
		launcher.setIntakeSpeed(-1);
		intakeAssist.setWheelSpeeds(1);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Robot.oi.getCommandCancel();
	}

	// Called once after isFinished returns true
	protected void end() {
		launcher.setIntakeSpeed(0);
		launcher.setAimSetpoint(0);
		intakeAssist.setWheelSpeeds(0d);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}

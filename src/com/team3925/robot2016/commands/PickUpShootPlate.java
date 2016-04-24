package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class PickUpShootPlate extends Command {
	
	public PickUpShootPlate() {
		super("CollectBall");
		
		requires(Robot.launcher);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		System.out.println("[" + Timer.getFPGATimestamp() + "] PickupPlate started");
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.launcher.setPuncherSolenoid(true);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		System.out.println("[" + Timer.getFPGATimestamp() + "] PickupPlate ended");
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
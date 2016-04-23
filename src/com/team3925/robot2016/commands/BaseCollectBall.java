package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class BaseCollectBall extends Command {
	
	private final Launcher launcher = Robot.launcher;
	
	public BaseCollectBall() {
		super("CollectBall");
		
		requires(launcher);
		
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		launcher.setPuncherSolenoid(false);
		launcher.setArmSetpoint(0);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		launcher.setFlywheelFarSetpoint(Constants.LAUNCHER_COLLECT_BALL_FAR);
		launcher.setFlywheelNearSetpoint(Constants.LAUNCHER_COLLECT_BALL_NEAR);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Robot.oi.getCollectBall_Continue();
	}

	// Called once after isFinished returns true
	protected void end() {
		System.out.println("[" + Timer.getFPGATimestamp() + "] BaseCollectPlate Finished");
		launcher.setFlywheelFarSetpoint(0);
		launcher.setFlywheelNearSetpoint(0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}

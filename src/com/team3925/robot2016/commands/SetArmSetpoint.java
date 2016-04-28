package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class SetArmSetpoint extends Command {
	private double angle;
	
	public SetArmSetpoint(double angle) {
		super("SetArmSetpoint-" + angle, 0.01);
		this.angle = angle;
	}
	
	@Override
	protected void initialize() {
		System.out.println("[" + Timer.getFPGATimestamp() + "] Setting Arm Setpoint To " + angle);
		Robot.launcher.setArmSetpoint(angle);
	}

	@Override
	protected void execute() {
		System.out.println("Is Timed Out: " + isTimedOut());
	}

	@Override
	protected boolean isFinished() {
		return isTimedOut();
	}

	@Override
	protected void end() {
		System.out.println("[" + Timer.getFPGATimestamp() + "] FINISHED Setting Arm Setpoint to " + angle);
	}

	@Override
	protected void interrupted() {
		end();
	}

}

package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class SetArmSetpoint extends Command {
	private double angle;
	
	public SetArmSetpoint(double angle) {
		super("SetArmSetpoint-" + angle, 0.1);
		this.angle = angle;
	}
	
	@Override
	protected void initialize() {
		System.out.println("[" + Timer.getFPGATimestamp() + "] Setting Arm Setpoint To " + angle);
	}

	@Override
	protected void execute() {
		Robot.launcher.setArmSetpoint(angle);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}

}

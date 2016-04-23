package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class SetFlywheelSpingSpeeds extends Command {
	private double far, near;
	
	public SetFlywheelSpingSpeeds(double far, double near) {
		super("SetFlywheelSpeeds [near = " + near + " far = " + far + "]", 0.1);
		this.far = far;
		this.near = near;
	}
	
	@Override
	protected void initialize() {
		System.out.println("[" + Timer.getFPGATimestamp() + "] Setting Wheel Spin Speeds: Far = " + far + " Near = " + near);
	}

	@Override
	protected void execute() {
		Robot.launcher.setFlywheelFarSetpoint(far);
		Robot.launcher.setFlywheelNearSetpoint(near);
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

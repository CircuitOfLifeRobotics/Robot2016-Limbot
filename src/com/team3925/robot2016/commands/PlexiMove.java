package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class PlexiMove extends Command {
	private boolean up;
	
	public PlexiMove(boolean up) {
		super("PlexiMove");
		requires(Robot.plexiArms);
		this.up = up;
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Robot.plexiArms.setSolenoids(up);
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

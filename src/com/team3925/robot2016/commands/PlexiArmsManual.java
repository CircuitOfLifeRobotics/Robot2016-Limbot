package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class PlexiArmsManual extends Command {
	
	public PlexiArmsManual() {
		requires(Robot.plexiArms);
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Robot.plexiArms.setSolenoids(Robot.oi.getPlexiArms_Control());
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

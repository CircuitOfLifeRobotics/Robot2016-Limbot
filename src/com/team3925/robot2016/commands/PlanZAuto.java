package com.team3925.robot2016.commands;

import com.team3925.robot2016.subsystems.DriveTrain;
import com.team3925.robot2016.subsystems.PlexiArms;

import edu.wpi.first.wpilibj.command.Command;

public class PlanZAuto extends Command {

	PlexiArms  plexi = new PlexiArms();
	DriveTrain drive = new DriveTrain();

	protected void initialize() {
		plexi.setArmUp(false);
	}


	protected void execute() {

	}


	protected boolean isFinished() {
		return false;
	}


	protected void end() {
		
	}


	protected void interrupted() {
		
	}

}

package com.team3925.robot2016.commands;

import com.team3925.robot2016.OI;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.PlexiArms;

import edu.wpi.first.wpilibj.command.Command;

public class ManualPlexiArms extends Command {
	private final OI oi = Robot.oi;
	private final PlexiArms plexiArms = Robot.plexiArms;

	private boolean armVal, lastArmVal, armsEngaged = false;
	
	public ManualPlexiArms() {
		requires(Robot.plexiArms);
	}
	
	@Override
	protected void initialize() {
		plexiArms.setArmUp(false);

	}

	@Override
	protected void execute() {
		armVal = oi.getManualArms_GetArmValue();
		if (armVal && !lastArmVal) {
			armsEngaged = !armsEngaged;
		}
		plexiArms.setArmUp(armsEngaged);
//		arms.setClimbMotor(climberVal);
		
		lastArmVal = armVal;
		
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		plexiArms.setArmUp(true);
	}

	@Override
	protected void interrupted() {
		plexiArms.setArmUp(true);
	}

}

package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitUntilCommand;

public class ResetArms extends CommandGroup {
	public ResetArms() {
		addParallel(new PlexiMove(true));
		addParallel(new SetArmSetpoint(Constants.COMMAND_RESET_ANGLE));
		// Run forever
		addParallel(new WaitUntilCommand(-1));
	}

}

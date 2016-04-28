package com.team3925.robot2016.commands;

import com.team3925.robot2016.subsystems.ZeroLauncher;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class LowGoal extends CommandGroup {
	
	public LowGoal() {
		addSequential(new ZeroLauncher());
		addSequential(new SetArmSetpoint(0));
		addSequential(new PickUpShootPlate(false));
		addSequential(new SetFlywheelSpeeds(1, -1)); //TODO move constants
		addSequential(new WaitCommand(2)); //TODO Constant
		addSequential(new SetFlywheelSpeeds(0, 0));
	}
}

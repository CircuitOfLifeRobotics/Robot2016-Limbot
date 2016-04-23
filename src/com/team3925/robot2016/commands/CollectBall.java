package com.team3925.robot2016.commands;

import com.team3925.robot2016.subsystems.ZeroLauncher;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class CollectBall extends CommandGroup {
	public CollectBall() {
		addSequential(new ZeroLauncher());
		addSequential(new BaseCollectBall());
		addSequential(new WaitCommand(1));
		addSequential(new PickUpShootPlate(), .1);
	}

}

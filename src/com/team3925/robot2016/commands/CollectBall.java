package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.subsystems.ZeroLauncher;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class CollectBall extends CommandGroup {
	public CollectBall() {
		addSequential(new ZeroLauncher());
		addSequential(new BaseCollectBall());
		addSequential(new WaitCommand(Constants.LAUNCHER_COLLECT_BALL_WAIT));
		addSequential(new PickUpShootPlate(true), .1);
		addSequential(new SetArmSetpoint(Constants.LAUNCHER_LAUNCH_BALL_MIDZONE_ANGLE));
	}

}

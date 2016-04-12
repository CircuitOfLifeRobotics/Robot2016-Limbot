package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.LauncherNew;

import edu.wpi.first.wpilibj.command.Command;

public class ZeroLauncher extends Command {
	private final LauncherNew launcher = Robot.launcherNew;
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		launcher.setMotorArmSpeedRaw(-.25);
	}

	@Override
	protected boolean isFinished() {
		return launcher.getLimitSwitch();
	}

	@Override
	protected void end() {
		launcher.setMotorArmSpeedRaw(0);
		launcher.setLauncherZeroed();
	}

	@Override
	protected void interrupted() {
		launcher.setMotorArmSpeedRaw(0);
	}

}

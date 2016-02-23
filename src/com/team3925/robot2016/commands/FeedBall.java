package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.TimeoutAction;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.command.Command;

public class FeedBall extends Command {
	
//	LauncherPID launcherPID = Robot.launcherPID;
	Launcher launcher = Robot.launcher;
	TimeoutAction timer = new TimeoutAction();
	
	@Override
	protected void initialize() {
		launcher.setPuncher(false);
		launcher.enableAim(true);
		launcher.enableIntake(true);
		launcher.setAimSetpoint(0);
		launcher.setIntakeSetpoint(-1);
		timer.config(2);
	}

	@Override
	protected void execute() {
		if (launcher.isAimOnSetpoint() && launcher.isIntakeOnSetpoint()) {
			
		}
		if (timer.isFinished()) {
			end();
		}
	}

	@Override
	protected boolean isFinished() {
		return XboxHelper.getShooterButton(XboxHelper.START) || timer.isFinished();
	}

	@Override
	protected void end() {
		launcher.setIntakeSetpoint(0);
		launcher.setAimSetpoint(0);
	}

	@Override
	protected void interrupted() {
		launcher.setIntakeSetpoint(0);
		launcher.setAimSetpoint(0);
	}
	
}

package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.hidhelpers.FlightStickHelper;

import edu.wpi.first.wpilibj.command.Command;

public class DropCode extends Command{
	Launcher launcher = new Launcher(RobotMap.launcherMotorArm,RobotMap.launcherMotorFar, RobotMap.launcherMotorNear, RobotMap.launcherPuncherSolenoid, RobotMap.launcherFwdLimitSwitch, RobotMap.launcherRevLimitSwitch);
	boolean JustPressed;
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		launcher.setArmSetpoint(0);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		launcher.setArmSetpoint(Constants.LAUNCHER_RESTING_ANGLE);
	}

	@Override
	protected void interrupted() {
		end();
	}

}

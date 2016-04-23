package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.subsystems.Launcher;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LaunchUltrasonic extends Command {

	Launcher launcher = new Launcher(RobotMap.launcherMotorArm, RobotMap.launcherMotorFar, RobotMap.launcherMotorNear, RobotMap.launcherPuncherSolenoid, RobotMap.launcherFwdLimitSwitch, RobotMap.launcherRevLimitSwitch);
	@Override
	protected void initialize() {
		
	}

	@Override
	protected void execute() {
		int distance = (int) launcher.getUltraSonic();
		SmartDashboard.putNumber("Final Setpoint", Constants.LAUNCHER_SETPOINTS[distance]);
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

package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.command.PIDCommand;

public class LauncherPID extends PIDCommand implements SmartdashBoardLoggable {
	
	private final Launcher launcher = Robot.launcher;
	
	private double setPoint;
	
	
	public LauncherPID() {
		super(Constants.LAUNCHER_AIM_KP, Constants.LAUNCHER_AIM_KI, Constants.LAUNCHER_AIM_KD);
		this.setPoint = 0;
	}
	
	public LauncherPID(double p, double i, double d, double setPoint) {
		super(p, i, d);
		this.setPoint = setPoint;
	}

	@Override
	protected double returnPIDInput() {
		return launcher.getAimMotorPosition();
	}

	@Override
	protected void usePIDOutput(double output) {
		launcher.setAimMotorSpeed(output);
	}

	@Override
	protected void initialize() {}

	@Override
	protected void execute() {
		// TODO get rid of user control for testing purposes
		if (XboxHelper.getShooterButton(XboxHelper.STICK_LEFT)) {
			launcher.setAimMotorSpeed(0);
		}
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		launcher.setAimMotorSpeed(0);
	}

	@Override
	protected void interrupted() {
		launcher.setAimMotorSpeed(0);
	}

	@Override
	public void logData() {
		putNumberSD("Setpoint", setPoint);
		putDataSD("PIDController", getPIDController());
		putNumberSD("Setpoint2", getSetpoint());
		putNumberSD("Error", getPIDController().getError());
		putBooleanSD("Enabled", getPIDController().isEnabled());
	}

	@Override
	public String getFormattedName() {
		return "Launcher_PID_";
	}
	
}

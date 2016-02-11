package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LauncherPID extends PIDCommand implements SmartdashBoardLoggable {
	
	private final Launcher launcher = Robot.launcher;
	
	private double setpoint, pidSetpoint, difference;
	
	public LauncherPID() {
		super(Constants.LAUNCHER_AIM_KP, Constants.LAUNCHER_AIM_KI, Constants.LAUNCHER_AIM_KD);
		this.setpoint = 0;
	}
	
	public LauncherPID(double setPoint) {
		super(Constants.LAUNCHER_AIM_KP, Constants.LAUNCHER_AIM_KI, Constants.LAUNCHER_AIM_KD);
		this.setpoint = setPoint;
	}
	
	public LauncherPID(double p, double i, double d, double setPoint) {
		super(p, i, d);
		this.setpoint = setPoint;
	}

	@Override
	protected double returnPIDInput() {
		SmartDashboard.putNumber("test_pid_input", launcher.getAimMotorPosition());
		return launcher.getAimMotorPosition();
	}

	@Override
	protected void usePIDOutput(double output) {
		SmartDashboard.putNumber("test_pid_output", output);
		launcher.setAimMotorSpeed(output);
	}

	@Override
	protected void initialize() {
		setInputRange(0, 700);
	}

	@Override
	protected void execute() {
//		setpoint = XboxHelper.getShooterAxis(XboxHelper.AXIS_RIGHT_Y) * Constants.MAX_LAUNCHER_HEIGHT;
		
//		difference = setpoint - launcher.getAimMotorPosition();
//		
//		if (difference < Constants.LAUNCHER_INCREMENT) {
//			pidSetpoint = setpoint;
//		} else {
//			pidSetpoint = launcher.getAimMotorPosition();
//			if (difference > 0) {
//				pidSetpoint += Constants.LAUNCHER_INCREMENT;
//			}else {
//				pidSetpoint -= Constants.LAUNCHER_INCREMENT;
//			}
//		}
		
		setSetpoint(pidSetpoint);
		
		logData();
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
		putNumberSD("Setpoint", setpoint);
		putNumberSD("PIDSetpoint", pidSetpoint);
		putNumberSD("GetSetpoint", getSetpoint());
		putNumberSD("difference", difference);
		putDataSD("PIDController", getPIDController());
		putNumberSD("Error", getPIDController().getError());
		putBooleanSD("Enabled", getPIDController().isEnabled());
	}

	@Override
	public String getFormattedName() {
		return "Launcher_PID_";
	}
	
}

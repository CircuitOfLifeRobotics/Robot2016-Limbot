package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.command.Command;

public class LauncherPID extends Command implements SmartdashBoardLoggable {
	
	private final Launcher launcher = Robot.launcher;
	
	private double setpoint, position, difference;
	
	public LauncherPID(double setPoint) {
		this.setpoint = setPoint;
	}
	
	public LauncherPID(double pUp, double iUp, double dUp, double fUp, int izoneUp, double closeLoopRampRateUp,
			double pDown, double iDown, double dDown, double fDown, int izoneDown, double closeLoopRampRateDown, 
			double setPoint) {
		this.setpoint = setPoint;
		launcher.setAimPID(pUp, iUp, dUp, fUp, izoneUp, closeLoopRampRateUp, 0);
		launcher.setAimPID(pDown, iDown, dDown, fDown, izoneDown, closeLoopRampRateDown, 1);
	}
	
	protected void initialize() {
		launcher.changeAimControlMode(TalonControlMode.Position);
	}

	protected void execute() {
		setpoint = -XboxHelper.getShooterAxis(XboxHelper.AXIS_LEFT_Y) * Constants.LAUNCHER_MAX_HEIGHT;
		position = launcher.getAimMotorPosition();
		difference = setpoint - position;
		
		if (difference > 0)
			launcher.setAimProfile(0);
		else
			launcher.setAimProfile(1);
		
		if (Math.abs(difference) > Constants.LAUNCHER_AIM_TOLERANCE) {
			setpoint = position + Constants.LAUNCHER_AIM_INCREMENT * (difference>0 ? 1:-1);
		}
		
//		launcher.setAim(setpoint);
		
		logData();
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		launcher.setAimMotorSpeed(0, false);
	}

	@Override
	protected void interrupted() {
		launcher.setAimMotorSpeed(0);
	}

	@Override
	public void logData() {
		putNumberSD("Difference", difference);
		putNumberSD("Setpoint", setpoint);
		putNumberSD("Position", position);
		putBooleanSD("UporDown", difference>0);
	}

	@Override
	public String getFormattedName() {
		return "Launcher_PID_";
	}
	
}

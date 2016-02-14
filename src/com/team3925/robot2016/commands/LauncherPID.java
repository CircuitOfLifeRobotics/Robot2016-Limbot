package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.ChezyMath;
import com.team3925.robot2016.util.LimitPID;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class LauncherPID extends Command implements SmartdashBoardLoggable {
	
	private final Launcher launcher = Robot.launcher;
	
	private LimitPID pidLoop = new LimitPID();
	
	private double aimJoystickSetpoint, aimSetpointDiff, aimLastSetpoint, aimSetpoint, aimPosition, aimDifference, aimOutput, aimAngleMultiplier;
	private double intakeOutput;
	private double lastTime, thisTime, deltaTime;
	
	public LauncherPID(double setPoint) {
		this.aimSetpoint = setPoint;
	}
	
	public LauncherPID(double p, double i, double d, double setPoint) {
		this.aimSetpoint = setPoint;
		pidLoop.setPID(p, i, d);
	}
	
	protected void initialize() {
		launcher.changeAimControlMode(TalonControlMode.PercentVbus);
		
//		pidLoop.setInputRange(-10, Constants.LAUNCHER_MAX_HEIGHT+10);
//		pidLoop.setOutputRange(-0.1, 0.6);
		pidLoop.setPIDLimits(10000, 10000, 10000, 10000, -10000, -10000, -10000, -10000);
		
		intakeOutput = 0;
		aimJoystickSetpoint = 0;
		
		aimLastSetpoint = 0;
		lastTime = Timer.getFPGATimestamp();
	}

	protected void execute() {
		thisTime = Timer.getFPGATimestamp();
		deltaTime = thisTime-lastTime;
		aimJoystickSetpoint = XboxHelper.getShooterButton(XboxHelper.TRIGGER_LT) ? ChezyMath.joystickToDegrees(XboxHelper.getShooterAxis(XboxHelper.AXIS_LEFT_Y)):aimJoystickSetpoint;
		aimSetpoint = aimJoystickSetpoint;
		aimPosition = ChezyMath.encoderTicksToDegrees(launcher.getAimMotorPosition());
		aimDifference = aimSetpoint - aimPosition;
		aimSetpointDiff = aimSetpoint - aimLastSetpoint;
		
		if (Math.abs(aimSetpointDiff) > Constants.LAUNCHER_AIM_INCREMENT) {
			aimSetpoint = aimLastSetpoint + Constants.LAUNCHER_AIM_INCREMENT * (aimSetpointDiff>0 ? 1:-1);
		}
		
		pidLoop.setSetpoint(aimSetpoint);
		pidLoop.calculate(aimPosition);
		aimOutput = pidLoop.get();
		aimAngleMultiplier = (2*Math.cos(Math.toRadians(aimPosition))+0.3)/2.5;
		aimOutput = aimOutput * aimAngleMultiplier;
		aimOutput = Math.min(Math.max(aimOutput, -0.2), 0.8);
		
		launcher.setAim(aimOutput);
		
		
		
		if (XboxHelper.getShooterButton(XboxHelper.START)) {intakeOutput = 0;}
		else if (XboxHelper.getShooterButton(XboxHelper.Y)) {intakeOutput = 1;}
		else if (XboxHelper.getShooterButton(XboxHelper.X)) {intakeOutput = 0.5;}
		else if (XboxHelper.getShooterButton(XboxHelper.B)) {intakeOutput = -0.5;}
		else if (XboxHelper.getShooterButton(XboxHelper.A)) {intakeOutput = -1;}
		
		launcher.setIntakeSpeeds(intakeOutput);
		
		launcher.setPuncher(XboxHelper.getShooterButton(XboxHelper.TRIGGER_RT));
		
		logData();
		
		aimLastSetpoint = aimSetpoint;
		lastTime = thisTime;
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
		putNumberSD("Difference", aimDifference);
		putNumberSD("Setpoint", aimSetpoint);
		putNumberSD("Position", aimPosition);
		putNumberSD("Output", aimOutput);
		putNumberSD("AngleMultiplier", aimAngleMultiplier);
		putNumberSD("Differential", pidLoop.getDValue());
		putNumberSD("DeltaTime", deltaTime);
		putNumberSD("Error", pidLoop.getError());
		putNumberSD("PrevError", pidLoop.getPrevError());
		putBooleanSD("UporDown", aimDifference>0);
	}

	@Override
	public String getFormattedName() {
		return "Launcher_PID_";
	}
	
}

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
	
	private double setpointDiff, lastSetpoint, setpoint, position, difference, output, angleMultiplier;
	private double lastTime, thisTime, deltaTime;
	private double intakeSpeed;
	
	public LauncherPID(double setPoint) {
		this.setpoint = setPoint;
	}
	
	public LauncherPID(double p, double i, double d, double setPoint) {
		this.setpoint = setPoint;
		pidLoop.setPID(p, i, d);
	}
	
	protected void initialize() {
		launcher.changeAimControlMode(TalonControlMode.PercentVbus);
		
//		pidLoop.setInputRange(-10, Constants.LAUNCHER_MAX_HEIGHT+10);
//		pidLoop.setOutputRange(-0.1, 0.6);
		pidLoop.setPIDLimits(10000, 10000, 10000, 10000, -10000, -10000, -10000, -10000);
		
		intakeSpeed = 0;
		
		lastSetpoint = 0;
		lastTime = Timer.getFPGATimestamp();
	}

	protected void execute() {
		thisTime = Timer.getFPGATimestamp();
		deltaTime = thisTime-lastTime;
		setpoint = ChezyMath.joystickToDegrees(XboxHelper.getShooterAxis(XboxHelper.AXIS_LEFT_Y));
		position = ChezyMath.encoderTicksToDegrees(launcher.getAimMotorPosition());
		difference = setpoint - position;
		setpointDiff = setpoint - lastSetpoint;
		
		if (Math.abs(setpointDiff) > Constants.LAUNCHER_AIM_INCREMENT) {
			setpoint = lastSetpoint + Constants.LAUNCHER_AIM_INCREMENT * (setpointDiff>0 ? 1:-1);
		}
		
		pidLoop.setSetpoint(setpoint);
		pidLoop.calculate(position);
		output = pidLoop.get();
		angleMultiplier = (2*Math.cos(Math.toRadians(position))+0.3)/2.5;
		output = output * angleMultiplier;
		output = Math.min(Math.max(output, -0.2), 0.8);
		
		launcher.setAim(output);
		
		
		
		if (XboxHelper.getShooterButton(XboxHelper.START)) {intakeSpeed = 0;}
		else if (XboxHelper.getShooterButton(XboxHelper.Y)) {intakeSpeed = 0.8;}
		else if (XboxHelper.getShooterButton(XboxHelper.X)) {intakeSpeed = 0.4;}
		else if (XboxHelper.getShooterButton(XboxHelper.B)) {intakeSpeed = -0.4;}
		else if (XboxHelper.getShooterButton(XboxHelper.A)) {intakeSpeed = -0.8;}
		
//		launcher.setIntakeSpeeds(intakeSpeed);
		
		launcher.setPuncher(XboxHelper.getShooterButton(XboxHelper.TRIGGER_RT));
		
		logData();
		
		lastSetpoint = setpoint;
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
		putNumberSD("Difference", difference);
		putNumberSD("Setpoint", setpoint);
		putNumberSD("Position", position);
		putNumberSD("Output", output);
		putNumberSD("AngleMultiplier", angleMultiplier);
		putNumberSD("Differential", pidLoop.getDValue());
		putNumberSD("DeltaTime", deltaTime);
		putNumberSD("Error", pidLoop.getError());
		putNumberSD("PrevError", pidLoop.getPrevError());
		putBooleanSD("UporDown", difference>0);
	}

	@Override
	public String getFormattedName() {
		return "Launcher_PID_";
	}
	
}

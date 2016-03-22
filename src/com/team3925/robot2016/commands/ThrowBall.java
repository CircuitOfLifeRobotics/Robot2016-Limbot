package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.IntakeAssist;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.command.Command;

enum Mode {
	WAIT_FOR_AIM, SHOOT, DONE;
}

public class ThrowBall extends Command implements SmartdashBoardLoggable {
	
	private double intakeSpeed;
	private double angle;
	private double timeout;
	private double wheelSpeed;
	private final Launcher launcher = Robot.launcher;
	private final IntakeAssist intakeAssist = Robot.intakeAssist;
	
	private final TimeoutAction timer = new TimeoutAction();
	private final TimeoutAction buttonTimer = new TimeoutAction();
	private Mode mode;
	private double lowestValSinceSetpoint = (30000*100/4096) * Constants.LAUNCHER_WHEEL_CIRCUM;
	
	
	public ThrowBall() {
		this(Constants.LAUNCHER_MAX_INTAKE_SPEED);
	}
	
	/**
	 * @param intakeSpeed in native units of encoder ticks/100ms
	 */
	public ThrowBall(double intakeSpeed) {
		this(80d, intakeSpeed);
	}
	
	/**
	 * @param angle in degrees
	 * @param intakeSpeed in native units of encoder ticks/100ms
	 */
	public ThrowBall(double angle, double intakeSpeed) {
		this(angle, intakeSpeed, 5, 0);
	}
	
	/**
	 * @param angle in degrees
	 * @param intakeSpeed in native units of encoder ticks/100ms
	 * @param timeOut timeout
	 */
	public ThrowBall(double angle, double intakeSpeed, double timeOut) {
		this(angle, intakeSpeed, timeOut, 0);
	}
	
	public ThrowBall(double angle, double intakeSpeed, double timeout, double wheelSpeed) {
		this.intakeSpeed = intakeSpeed;
		this.angle = angle;
		this.timeout = timeout;
		this.wheelSpeed = wheelSpeed;
		requires(launcher);
		requires(intakeAssist);
	}
	
//	public void setAngle(double angle) {
//		this.angle = angle;
//	}
//	
//	public void setIntakeSpeed(double speed) {
//		intakeSpeed = speed;
//	}
	
	@Override
	protected void initialize() {
		mode = Mode.WAIT_FOR_AIM;
		
		launcher.setPuncher(false);
		launcher.enableAim(true);
		launcher.enableIntake(true);
		launcher.setAimSetpoint(angle);
		launcher.setIntakeSetpoint(intakeSpeed);
		intakeAssist.setWheelSpeeds(wheelSpeed);
		
		buttonTimer.config(0.5);
		timer.config(timeout);
	}

	@Override
	protected void execute() {
		switch (mode) {
		case WAIT_FOR_AIM:
			if (((launcher.isAimOnSetpoint() && launcher.isIntakeOnSetpoint()) || 
					timer.isFinished() || Robot.oi.getThrowBall_LaunchBallOverride()) && buttonTimer.isFinished()) {
				mode = Mode.SHOOT;
				launcher.setPuncher(true);
				timer.config(0.3);
			}
			break;
		case SHOOT:
			lowestValSinceSetpoint = Math.min(lowestValSinceSetpoint, Math.abs((launcher.getIntakeSpeedLeft()*100/4096) * Constants.LAUNCHER_WHEEL_CIRCUM));
			lowestValSinceSetpoint = Math.min(lowestValSinceSetpoint, Math.abs((launcher.getIntakeSpeedRight()*100/4096) * Constants.LAUNCHER_WHEEL_CIRCUM));
			if (timer.isFinished()) {
				launcher.setPuncher(false);
				mode = Mode.DONE;
			}
			break;
		case DONE:
			break;
		}
		
		logData();
	}
	
	@Override
	protected boolean isFinished() {
		return mode == Mode.DONE || Robot.oi.getCommandCancel();
	}
	
	@Override
	protected void end() {
		launcher.setAimSetpoint(0);
		launcher.setIntakeSetpoint(0);
		intakeAssist.setWheelSpeeds(0d);
	}
	
	@Override
	protected void interrupted() {
		launcher.setAimSetpoint(0);
		launcher.setIntakeSetpoint(0);
		intakeAssist.setWheelSpeeds(0d);
	}

	@Override
	public void logData() {
		putNumberSD("IntakeSpeed", intakeSpeed);
		putNumberSD("Angle", angle);
		putBooleanSD("AimOnSetpoint", launcher.isAimOnSetpoint());
		putBooleanSD("IntakeOnSetpoint", launcher.isIntakeOnSetpoint());
		putNumberSD("AimOnSetpoint", launcher.isAimOnSetpoint() ? 1:0);
		putNumberSD("IntakeOnSetpoint", launcher.isIntakeOnSetpoint() ? 1:0);
		putStringSD("Mode", mode.toString());
		putNumberSD("LowestValSinceMax", lowestValSinceSetpoint);
	}

	@Override
	public String getFormattedName() {
		return "ThrowBall_";
	}

}

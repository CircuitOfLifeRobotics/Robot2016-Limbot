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
	
	@Override
	protected void initialize() {
		mode = Mode.WAIT_FOR_AIM;
		
		buttonTimer.config(0.3);
		timer.config(timeout);
	}

	@Override
	protected void execute() {
		switch (mode) {
		case WAIT_FOR_AIM:
			launcher.setPuncher(false);
			launcher.enableAim(true);
			launcher.enableIntake(true);
			launcher.setAimSetpoint(angle);
			intakeAssist.setWheelSpeeds(wheelSpeed);
		
			if ((launcher.isAimOnSetpoint() || timer.isFinished()) && buttonTimer.isFinished()) {
				mode = Mode.SHOOT;
				launcher.setIntakeSpeed(intakeSpeed);
				timer.config(0.4);
			}
			break;
		case SHOOT:
			if (timer.isFinished()) {
				launcher.setPuncher(true);
				timer.config(0.5);
				mode = Mode.DONE;
			}
			break;
		case DONE:
			end();
			break;
		}
		
		logData();
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.oi.getCommandCancel();
	}
	
	@Override
	protected void end() {
		launcher.setAimSetpoint(0);
		launcher.setIntakeSpeed(0);
		intakeAssist.setWheelSpeeds(0d);
		launcher.setPuncher(false);
	}
	
	@Override
	protected void interrupted() {
		end();
	}

	@Override
	public void logData() {
		putNumberSD("IntakeSpeed", intakeSpeed);
		putNumberSD("Angle", angle);
		putBooleanSD("AimOnSetpoint", launcher.isAimOnSetpoint());
		putNumberSD("AimOnSetpoint", launcher.isAimOnSetpoint() ? 1:0);
		putStringSD("Mode", mode.toString());
	}

	@Override
	public String getFormattedName() {
		return "ThrowBall_";
	}

}

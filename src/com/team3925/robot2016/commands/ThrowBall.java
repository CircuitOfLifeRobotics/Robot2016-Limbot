package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.IntakeAssist;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.command.Command;

enum Mode {
	WAIT_FOR_AIM, SHOOT, HOLD_ANGLE;
}

public class ThrowBall extends Command implements SmartdashBoardLoggable {
	
	private double intakeSpeed;
	private double angle;
	private double timeout;
	private final Launcher launcher = Robot.launcher;
	private final IntakeAssist intakeAssist = Robot.intakeAssist;
	
	private final TimeoutAction timer = new TimeoutAction();
	private final TimeoutAction shootTimer = new TimeoutAction();
	private final TimeoutAction holdAngle = new TimeoutAction();
	
	private Mode mode;
	
	public ThrowBall(double angle) {
		this(angle, 0);
	}
	
	/**
	 * @param angle in degrees
	 * @param intakeSpeed in native units of encoder ticks/100ms
	 */
	public ThrowBall(double angle, double intakeSpeed) {
		this(angle, intakeSpeed, 5);
	}
	
	/**
	 * @param angle in degrees
	 * @param intakeSpeed power to give to intake assist subsystem
	 * @param timeOut timeout to get to launcher angle
	 */
	public ThrowBall(double angle, double intakeSpeed, double timeOut) {
		super("ThrowBall");
		
		this.intakeSpeed = intakeSpeed;
		this.angle = angle;
		this.timeout = timeOut;
		
		requires(launcher);
		requires(intakeAssist);
	}
	
	@Override
	protected void initialize() {
		mode = Mode.WAIT_FOR_AIM;
		
		timer.config(timeout);
	}

	@Override
	protected void execute() {
		intakeAssist.setWheelSpeeds(intakeSpeed);
		
		switch (mode) {
		case WAIT_FOR_AIM:
			launcher.setPuncher(false);
			launcher.enableAim(true);
			launcher.setAimSetpoint(angle);
			
			if (launcher.isAimOnSetpoint() || timer.isFinished()) {
				mode = Mode.SHOOT;
				shootTimer.config(0.4); // wait to spin up fly wheels
			}
			break;
			
		case SHOOT:
			launcher.setIntakeSpeed(1d); // always fire at max flywheel speeds
			
			if (shootTimer.isFinished()) {
				launcher.setPuncher(true);
				holdAngle.config(0.4); // wait a moment after punching solenoid
				mode = Mode.HOLD_ANGLE;
			}
			break;
			
		case HOLD_ANGLE:
			// launcher holds its aim setpoints already
			
			if (holdAngle.isFinished()) {
				end();
			}
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
		putNumberSD("IntakeAssistSpeed", intakeSpeed);
		putNumberSD("Angle", angle);
		putBooleanSD("AimOnSetpoint", launcher.isAimOnSetpoint());
		putNumberSD("AimOnSetpoint", launcher.isAimOnSetpoint() ? 1:0);
		putStringSD("Mode", mode.toString());
	}

	@Override
	public String getFormattedName() {
		return getName() + "_";
	}

}

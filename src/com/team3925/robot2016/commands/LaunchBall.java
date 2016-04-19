package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LaunchBall extends Command implements SmartdashBoardLoggable {
	
	private enum State {
		WAIT_ARM_TO_MID, WAIT_ARM_TO_UP, WAIT_AT_TOP, WAIT_BALL_LEAVE, WAIT_ARM_TO_DOWN;
	}
	
	Launcher launcher;
	TimeoutAction maxTimeout;
	TimeoutAction minTimeout;
	State state;
	
	public LaunchBall() {
		launcher = Robot.launcher;
		state = State.WAIT_ARM_TO_MID;
		maxTimeout = new TimeoutAction();
		minTimeout = new TimeoutAction();
		requires(launcher);
	}

	@Override
	protected void initialize() {
		launcher.setArmSetpoint(60);
		state = State.WAIT_ARM_TO_MID;
		maxTimeout.config(3);
		minTimeout.config(0.1);
		launcher.setPuncherSolenoid(true);
	}

	@Override
	protected void execute() {
		SmartDashboard.putString("ShootState", state.toString());
		switch (state) {
		case WAIT_ARM_TO_MID:
			//TODO: add safety to check if launcher is stuck
			if ((maxTimeout.isFinished() || launcher.getArmPosition()>30)&&minTimeout.isFinished()) {
				launcher.setPuncherSolenoid(false);
				launcher.setFlywheelNearSetpoint(0.25);
				state = State.WAIT_ARM_TO_UP;
				maxTimeout.config(3);
				minTimeout.config(0.5);
			}
			break;
		case WAIT_ARM_TO_UP:
			if ((Math.abs(launcher.getArmPosition()-60)<5 || maxTimeout.isFinished())&&minTimeout.isFinished()) {
				state = State.WAIT_AT_TOP;
				launcher.setFlywheelFarSetpoint(-1);
				launcher.setFlywheelNearSetpoint(-1);
				minTimeout.config(1);
			}
			break;
		case WAIT_AT_TOP:
			if (minTimeout.isFinished()) {
				state = State.WAIT_BALL_LEAVE;
				launcher.setPuncherSolenoid(true);
				minTimeout.config(0.5);
			}
			break;
		case WAIT_BALL_LEAVE:
			if (minTimeout.isFinished()) {
				launcher.setFlywheelFarSetpoint(0);
				launcher.setFlywheelNearSetpoint(0);
				launcher.setArmSetpoint(30);
			}
			break;
		case WAIT_ARM_TO_DOWN:
			if ((maxTimeout.isFinished() || Math.abs(launcher.getArmPosition()-30)<5)&&minTimeout.isFinished()) {
				end();
			}
		default:
			state = State.WAIT_ARM_TO_MID;
			break;
		}
		
		logData();
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {}

	@Override
	protected void interrupted() {
		launcher.setFlywheelFarSetpoint(0);
		launcher.setFlywheelNearSetpoint(0);
		launcher.setArmSetpoint(30);
		launcher.setPuncherSolenoid(false);
	}

	@Override
	public void logData() {
		putStringSD("State", state.toString());
		putNumberSD("TimeoutLeft", maxTimeout.getTimeRemaining());
	}

	@Override
	public String getFormattedName() {
		return "LaunchBallTest_";
	}
	
}

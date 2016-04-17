package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.command.Command;

public class LaunchBall extends Command implements SmartdashBoardLoggable {
	
	private enum State {
		WAIT_LET_GO_BALL, WAIT_AIM_AT_SETPOINT, WAIT_BALL_LEAVE, WAIT_AIM_AT_FINISH;
	}
	
	Launcher launcher;
	TimeoutAction timeout;
	State state;
	
	public LaunchBall() {
		launcher = Robot.launcherNew;
		state = State.WAIT_LET_GO_BALL;
		timeout = new TimeoutAction();
	}

	@Override
	protected void initialize() {
		launcher.setArmSetpoint(60);
		state = State.WAIT_LET_GO_BALL;
		timeout.config(3);
	}

	@Override
	protected void execute() {
		switch (state) {
		case WAIT_LET_GO_BALL:
			//TODO: add safety to check if launcher is stuck
			if (timeout.isFinished()) {
				launcher.setPuncherSolenoid(false);
				launcher.setFlywheelFarSetpoint(-1);
				launcher.setFlywheelNearSetpoint(-1);
				state = State.WAIT_AIM_AT_SETPOINT;
				timeout.config(3);
			}
			else if (launcher.getArmPosition() < 30) launcher.setPuncherSolenoid(true);
			break;
		case WAIT_AIM_AT_SETPOINT:
			if (Math.abs(launcher.getArmPosition()-60)<5 || timeout.isFinished()) {
				launcher.setPuncherSolenoid(true);
				state = State.WAIT_BALL_LEAVE;
				timeout.config(0.3);
			}
			break;
		case WAIT_BALL_LEAVE:
			if (timeout.isFinished()) {
				launcher.setPuncherSolenoid(false);
				launcher.setFlywheelFarSetpoint(0);
				launcher.setFlywheelNearSetpoint(0);
				launcher.setArmSetpoint(30);
				state = State.WAIT_AIM_AT_FINISH;
				timeout.config(3);
			}
			break;
		case WAIT_AIM_AT_FINISH:
			if (Math.abs(launcher.getArmPosition()-30)<5 || timeout.isFinished()) {
				end();
			}
			break;
		default:
			state = State.WAIT_LET_GO_BALL;
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
		putNumberSD("TimeoutLeft", timeout.getTimeRemaining());
	}

	@Override
	public String getFormattedName() {
		return "LaunchBallTest_";
	}
	
}

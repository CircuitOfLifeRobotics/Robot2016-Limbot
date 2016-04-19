package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.subsystems.Launcher.EncoderWatcher;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.command.Command;

public class LaunchBall extends Command implements SmartdashBoardLoggable {
	
	public static int ANGLE_TOLERANCE = 5;
	public static int AIMING_ANGLE;
	public static int RESTING_POSITION = 30;

	private enum State {
		WAIT_LET_GO_BALL, WAIT_AIM_AT_SETPOINT, WAIT_BALL_LEAVE, WAIT_AIM_AT_FINISH;
	}
	
// TODO implement the encoder watcher class, it is not working very well
	Launcher launcher;
	TimeoutAction timeout;
	State state;
	
	
	public LaunchBall(int aim_angle) {
		launcher = Robot.launcher;
		state = State.WAIT_LET_GO_BALL;
		timeout = new TimeoutAction();
		AIMING_ANGLE = aim_angle;
	}

	@Override
	protected void initialize() {
		launcher.setArmSetpoint(AIMING_ANGLE);
		state = State.WAIT_LET_GO_BALL;
		launcher.setPuncherSolenoid(false);
		timeout.config(3);
	}

	@Override
	protected void execute() {
		switch (state) {
		case WAIT_LET_GO_BALL:
			launcher.setPuncherSolenoid(false);	
			//TODO: add safety to check if launcher is stuck
			if (timeout.isFinished()) {
				launcher.setFlywheelFarSetpoint(-1);
				launcher.setFlywheelNearSetpoint(-1);
				state = State.WAIT_AIM_AT_SETPOINT;
				timeout.config(3);
			}else if (launcher.getArmPosition() < RESTING_POSITION){
				launcher.setPuncherSolenoid(true);
			}
			break;
		case WAIT_AIM_AT_SETPOINT:
			if (Math.abs(launcher.getArmPosition()-AIMING_ANGLE)<ANGLE_TOLERANCE || timeout.isFinished()) {
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
				launcher.setArmSetpoint(RESTING_POSITION);
				state = State.WAIT_AIM_AT_FINISH;
				timeout.config(3);
			}
			break;
		case WAIT_AIM_AT_FINISH:
			if (Math.abs(launcher.getArmPosition()-RESTING_POSITION)<ANGLE_TOLERANCE || timeout.isFinished()) {
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
		return Robot.oi.getCommandCancel();
	}

	@Override
	protected void end() {
		launcher.setFlywheelFarSetpoint(0);
		launcher.setFlywheelNearSetpoint(0);
		launcher.setArmSetpoint(RESTING_POSITION);
		launcher.setPuncherSolenoid(false);		
	}

	@Override
	protected void interrupted() {
		end();
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

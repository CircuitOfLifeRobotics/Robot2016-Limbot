package com.team3925.robot2016.commands;

import static com.team3925.robot2016.Constants.LAUNCHER_RESTING_ANGLE;
import static com.team3925.robot2016.Constants.LAUNCHER_SETPOINT_REACH_WAIT;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.command.Command;

public class LaunchBall extends Command implements SmartdashBoardLoggable {


	private final double SHOOTING_ANGLE;

	private enum State {
		WAIT_LET_GO_BALL, WAIT_AIM_AT_SETPOINT, WAIT_BALL_LEAVE, WAIT_AIM_AT_FINISH;
	}

	Launcher launcher;
	TimeoutAction timeout;
	State state;

	public LaunchBall(double kAngle) {
		super("LauncherBall", 15);
		launcher = Robot.launcher;
		state = State.WAIT_LET_GO_BALL;
		timeout = new TimeoutAction();
		SHOOTING_ANGLE = kAngle;
	}

	@Override
	protected void initialize() {
		launcher.setArmSetpoint(SHOOTING_ANGLE);
		state = State.WAIT_LET_GO_BALL;
		timeout.config(LAUNCHER_SETPOINT_REACH_WAIT);
		launcher.setPuncherSolenoidSetpoint(false);
	}

	@Override
	protected void execute() {
		switch (state) {
		case WAIT_LET_GO_BALL:
			launcher.setPuncherSolenoidSetpoint(false);
			//TODO: add safety to check if launcher is stuck
			if (timeout.isFinished()) {
				launcher.setFlywheelFarSetpoint(-1);
				launcher.setFlywheelNearSetpoint(-1);
				state = State.WAIT_AIM_AT_SETPOINT;
				timeout.config(LAUNCHER_SETPOINT_REACH_WAIT);
			}
			else if (launcher.getArmPosition() < LAUNCHER_RESTING_ANGLE) launcher.setPuncherSolenoidSetpoint(true);
			break;
		case WAIT_AIM_AT_SETPOINT:
			if (Math.abs(launcher.getArmPosition()-SHOOTING_ANGLE)<Constants.LAUNCHER_ARM_TOLERANCE || timeout.isFinished()) {
				launcher.setPuncherSolenoidSetpoint(true);
				state = State.WAIT_BALL_LEAVE;
				timeout.config(0.3);
			}
			break;
		case WAIT_BALL_LEAVE:
			if (timeout.isFinished()) {
				launcher.setPuncherSolenoidSetpoint(false);
				launcher.setFlywheelFarSetpoint(0);
				launcher.setFlywheelNearSetpoint(0);
				launcher.setArmSetpoint(LAUNCHER_RESTING_ANGLE);
				state = State.WAIT_AIM_AT_FINISH;
				timeout.config(LAUNCHER_SETPOINT_REACH_WAIT);
			}
			break;
		case WAIT_AIM_AT_FINISH:
			if (Math.abs(launcher.getArmPosition()-LAUNCHER_RESTING_ANGLE)<Constants.LAUNCHER_ARM_TOLERANCE || timeout.isFinished()) {
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
		launcher.setArmSetpoint(LAUNCHER_RESTING_ANGLE);
		launcher.setPuncherSolenoidSetpoint(false);
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

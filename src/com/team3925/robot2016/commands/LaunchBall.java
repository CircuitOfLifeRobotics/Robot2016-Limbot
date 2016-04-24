package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LaunchBall extends Command implements SmartdashBoardLoggable {

	private enum State {
		WAIT_ARM_TO_MID, WAIT_ARM_TO_UP, WAIT_AT_TOP, WAIT_BALL_LEAVE, WAIT_ARM_TO_DOWN;
	}

	private final Launcher launcher;
	private final TimeoutAction maxTimeout;
	private final TimeoutAction minTimeout;
	private State state;
	private double kAngle;


	public LaunchBall(double kAngle) {
		launcher = Robot.launcher;
		state = State.WAIT_ARM_TO_MID;
		maxTimeout = new TimeoutAction();
		minTimeout = new TimeoutAction();
		requires(launcher);
		this.kAngle = kAngle;
		System.out.println("[" + Timer.getFPGATimestamp() + "] LauncherBall Angle Inputted: " + kAngle);
		//		putNumberSD("SETTER", kAngle);
	}

	@Override
	protected void initialize() {
		kAngle = getNumberSD("SETTER", kAngle);
		launcher.setArmSetpoint(kAngle);
		System.out.println("[" + Timer.getFPGATimestamp() + "] LauncherBall Angle Inputted: " + kAngle);

		state = State.WAIT_ARM_TO_MID;
		maxTimeout.config(.7);
		// Move ball into launcher
		launcher.setPuncherSolenoid(false);
		launcher.setFlywheelNearSetpoint(0.5);
		launcher.setFlywheelFarSetpoint(0);
	}

	@Override
	protected void execute() {
		SmartDashboard.putString("ShootState", state.toString());
		switch (state) {
		case WAIT_ARM_TO_MID:
			//TODO: add safety to check if launcher is stuck
			if (maxTimeout.isFinished()) {
				// Ball should be in mechanism now
				launcher.setPuncherSolenoid(false);
				launcher.setFlywheelNearSetpoint(0);
				launcher.setFlywheelFarSetpoint(0);
				state = State.WAIT_ARM_TO_UP;
				System.out.println("[" + Timer.getFPGATimestamp() + "] Moving on to WAIT_ARM_TO_UP");
				maxTimeout.config(3);
				minTimeout.config(0.5);
			}
			break;
		case WAIT_ARM_TO_UP:
			if ((launcher.getAimOnTarget() || maxTimeout.isFinished()) && minTimeout.isFinished()) {
				state = State.WAIT_AT_TOP;
				System.out.println("[" + Timer.getFPGATimestamp() + "] Moving on to WAIT_AT_TOP");
				// Set to spinning direction
				launcher.setFlywheelNearSetpoint(-1);
				launcher.setFlywheelFarSetpoint(-1);
				minTimeout.config(Constants.LAUNCHER_SPIN_UP_TIME);
			}
			break;
		case WAIT_AT_TOP:
			if (minTimeout.isFinished()) {
				state = State.WAIT_BALL_LEAVE;
				System.out.println("[" + Timer.getFPGATimestamp() + "] Moving On To WAIT_BALL_LEAVE");
				// Actual shooting sequence
				launcher.setPuncherSolenoid(true);
				minTimeout.config(1);
			}
			break;
		case WAIT_BALL_LEAVE:
			if (minTimeout.isFinished()) {
				System.out.println("[" + Timer.getFPGATimestamp() + "] Final State");
				cancel();
			}
			break;
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
	protected void end() {
		System.out.println("[" + Timer.getFPGATimestamp() + "] LaunchBall.end()");

		launcher.setFlywheelFarSetpoint(0);
		launcher.setFlywheelNearSetpoint(0);
		launcher.setArmSetpoint(Constants.LAUNCHER_LAUNCH_BALL_MIDZONE_ANGLE);
		launcher.setPuncherSolenoid(false);
	}

	@Override
	protected void interrupted() {
		end();
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
	public void Raise(){
		
	}

}

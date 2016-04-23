package com.team3925.robot2016.commands;

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
	private final double ANGLE;
	
	
	public LaunchBall(double kAngle) {
		launcher = Robot.launcher;
		state = State.WAIT_ARM_TO_MID;
		maxTimeout = new TimeoutAction();
		minTimeout = new TimeoutAction();
		requires(launcher);
		ANGLE = kAngle;
		System.out.println("[" + Timer.getFPGATimestamp() + "] LauncherBall Angle Inputted: " + ANGLE);
	}

	@Override
	protected void initialize() {
		launcher.setArmSetpoint(ANGLE);
		state = State.WAIT_ARM_TO_MID;
		maxTimeout.config(3);
		minTimeout.config(0.1);
		launcher.setPuncherSolenoid(false);
		launcher.setFlywheelNearSetpoint(1);
		launcher.setFlywheelFarSetpoint(0);
	}

	@Override
	protected void execute() {
		SmartDashboard.putString("ShootState", state.toString());
		switch (state) {
		case WAIT_ARM_TO_MID:
			//TODO: add safety to check if launcher is stuck
			if ((maxTimeout.isFinished() || launcher.getArmPosition()>30)&&minTimeout.isFinished()) {
				launcher.setPuncherSolenoid(false);
				launcher.setFlywheelNearSetpoint(1);
				state = State.WAIT_ARM_TO_UP;
				System.out.println("[" + Timer.getFPGATimestamp() + "] Moving on to WAIT_ARM_TO_UP");
				maxTimeout.config(3);
				minTimeout.config(0.5);
			}
			break;
		case WAIT_ARM_TO_UP:
			if ((Math.abs(launcher.getArmPosition()-60)<5 || maxTimeout.isFinished())&&minTimeout.isFinished()) {
				state = State.WAIT_AT_TOP;
				System.out.println("[" + Timer.getFPGATimestamp() + "] Moving on to WAIT_AT_TOP");
				launcher.setFlywheelNearSetpoint(-1);
				launcher.setFlywheelFarSetpoint(-1);
				minTimeout.config(5);
				maxTimeout.config(0);
			}
			break;
		case WAIT_AT_TOP:
//			if (maxTimeout.isFinished()) {
//			System.out.println("[" + Timer.getFPGATimestamp() + "] BaseCollectPlate Finished");
//			}
			if (minTimeout.isFinished()) {
				state = State.WAIT_BALL_LEAVE;
				System.out.println("[" + Timer.getFPGATimestamp() + "] Moving On To WAIT_BALL_LEAVE");
				launcher.setPuncherSolenoid(true);
				minTimeout.config(3);
			}
			break;
		case WAIT_BALL_LEAVE:
			if (minTimeout.isFinished()) {
				System.out.println("[" + Timer.getFPGATimestamp() + "] Final State Condition Satisfied");
				launcher.setFlywheelFarSetpoint(0);
				launcher.setFlywheelNearSetpoint(0);
				launcher.setArmSetpoint(30);
			}
			break;
		case WAIT_ARM_TO_DOWN:
			if ((maxTimeout.isFinished() || Math.abs(launcher.getArmPosition()-30)<5)&&minTimeout.isFinished()) {
				System.out.println("[" + Timer.getFPGATimestamp() + "] Unknown Condition Satisfied");
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

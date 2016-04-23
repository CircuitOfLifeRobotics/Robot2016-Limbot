package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class BaseCollectBall extends Command {
//	enum State {
//		INTAKE, PISTON_UP;
//	}
	
	private final Launcher launcher = Robot.launcher;
//	private State state;
	private TimeoutAction timeout;
	
	public BaseCollectBall() {
		super("CollectBall");
		
		requires(launcher);
		
	}

	// Called just before this Command runs the first time
	protected void initialize() {
//		state = State.INTAKE;
		launcher.setArmSetpoint(0);
		launcher.setPuncherSolenoid(false);
		timeout = new TimeoutAction();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
//		switch (state) {
//		case INTAKE:
//			if (Robot.oi.shooterXbox.getRawButton(XboxHelper.A)) {
//				state = State.PISTON_UP;
//				timeout.config(0.5);
//			}
//			break;
//		case PISTON_UP:
//			if (timeout.isFinished()) {
//				end();
//			}
//		default:
//			state = State.INTAKE;
//			break;
//		}
		launcher.setFlywheelFarSetpoint(-.6);
		launcher.setFlywheelNearSetpoint(1);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Robot.oi.getCollectBall_Continue();
	}

	// Called once after isFinished returns true
	protected void end() {
		System.out.println("[" + Timer.getFPGATimestamp() + "] BaseCollectPlate Finished");
		launcher.setFlywheelFarSetpoint(0);
		launcher.setFlywheelNearSetpoint(0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}

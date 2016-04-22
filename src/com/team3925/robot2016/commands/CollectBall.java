package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.TimeoutAction;
import com.team3925.robot2016.util.hidhelpers.XboxHelper;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CollectBall extends Command {
	enum State {
		INTAKE, PISTON_UP;
	}
	
	private final Launcher launcher = Robot.launcher;
	private State state;
	private TimeoutAction timeout;
	
	public CollectBall() {
		super("CollectBall");
		
		requires(launcher);
		
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		state = State.INTAKE;
		launcher.setArmSetpoint(0);
		launcher.setPuncherSolenoid(false);
		timeout = new TimeoutAction();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		switch (state) {
		case INTAKE:
			if (Robot.oi.shooterXbox.getRawButton(XboxHelper.A)) {
				state = State.PISTON_UP;
				timeout.config(0.5);
			}
			break;
		case PISTON_UP:
			if (timeout.isFinished()) {
				end();
			}
		default:
			state = State.INTAKE;
			break;
		}
		launcher.setFlywheelFarSetpoint(-.3);
		launcher.setFlywheelNearSetpoint(1);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Robot.oi.getCommandCancel();
	}

	// Called once after isFinished returns true
	protected void end() {
		launcher.setFlywheelFarSetpoint(0);
		launcher.setFlywheelNearSetpoint(0);
		launcher.setPuncherSolenoid(true);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}

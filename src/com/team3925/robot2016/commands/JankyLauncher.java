package com.team3925.robot2016.commands;

import com.team3925.robot2016.OI;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.command.Command;

public class JankyLauncher extends Command{

	private final Launcher launcher = Robot.launcher;
	private final OI oi = Robot.oi;
	private State state;
	private final TimeoutAction timeout1;

	private static enum State {
		SPIN_UP_WHEELS, LIFT_PLATE, HOLD, RESET;
	}

	public JankyLauncher() {
		super("JankyLauncher");
		requires(Robot.launcher);

		timeout1 = new TimeoutAction();
	}

	@Override
	protected void initialize() {
		state = State.RESET;
	}

	@Override
	protected void execute() {
		if (oi.getJankyLauncherUp() > 0 && oi.getJankyLauncherDown() > 0) {
			launcher.setMotorArmSpeed(0);
		} else {
			if (oi.getJankyLauncherUp() > 0){
				launcher.setMotorArmSpeed(oi.getJankyLauncherUp() * 0.5);
			} else if (oi.getJankyLauncherDown() > 0){
				launcher.setMotorArmSpeed(oi.getJankyLauncherDown() * -0.5);
			} else {
				launcher.setMotorArmSpeed(0);
			}
		}

		if (oi.getJankyFire()){
			jankyLaunchinStuff();
		} else {
			state = State.SPIN_UP_WHEELS;
			launcher.setFlywheelNearSetpoint(0);
			launcher.setFlywheelFarSetpoint(0);
		}

	}

	private void jankyLaunchinStuff() {
		switch (state) {
		case SPIN_UP_WHEELS:
			launcher.setPuncherSolenoid(false);
			launcher.setFlywheelNearSetpoint(0);
			launcher.setFlywheelFarSetpoint(0);
			state = State.LIFT_PLATE;
			timeout1.config(0.5);
			break;
		case LIFT_PLATE:
			if (timeout1.isFinished()){
				launcher.setFlywheelNearSetpoint(-1);
				launcher.setFlywheelFarSetpoint(-1);
				state = State.HOLD;
				timeout1.config(4);

			}
			break;
		case HOLD:
			if (timeout1.isFinished()){
				launcher.setPuncherSolenoid(true);
				state = State.RESET;
				timeout1.config(1);
			}
			break;
		case RESET:
			if(timeout1.isFinished()){
				cancel();
			}
			break;
		default:
			state = State.RESET;
			break;
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		launcher.setMotorArmSpeed(0);
		launcher.setFlywheelNearSetpoint(0);
		launcher.setFlywheelFarSetpoint(0);
	}

	@Override
	protected void interrupted() {
		end();
	}

}

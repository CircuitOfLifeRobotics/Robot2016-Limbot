package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.command.Command;

enum State {
	HORIZ_AIMING, VERT_AIMING, SHOOTING, DONE;
}

public class LaunchBall extends Command {
	
	GyroTurn turnCommand;
	LauncherPID launcherPID = Robot.launcherPID;
	TimeoutAction timer = new TimeoutAction();
	State state = State.HORIZ_AIMING;
	
	double horizDeltaSetpoint, horizDelta, vertSetpoint;
	
	public LaunchBall(double horizAimSetpoint, double vertAimSetpoint) {
		horizDeltaSetpoint = horizAimSetpoint;
		vertSetpoint = vertAimSetpoint;
	}
	
	public void reset() {
		launcherPID.setAimSetpoint(0);
		launcherPID.setIntakeSetpoint(0);
		timer.config(5);
	}
	
	@Override
	protected void initialize() {
		turnCommand = new GyroTurn(horizDeltaSetpoint);
		reset();
		turnCommand.start();
	}
	
	@Override
	protected void execute() {
		switch (state) {
		case HORIZ_AIMING:
			if ((!turnCommand.isRunning()) || timer.isFinished()) {
				launcherPID.setAimEnabled(true);
				launcherPID.setIntakeEnabled(true);
				launcherPID.setAimSetpoint(vertSetpoint);
				launcherPID.setIntakeSetpoint(20000);
				timer.config(5);
				state = State.VERT_AIMING;
			}
			break;
		case VERT_AIMING:
			if ((launcherPID.isAimOnSetpoint() && launcherPID.isIntakeOnSetpoint()) || timer.isFinished()) {
				launcherPID.setPuncher(true);
				timer.config(0.1);
				state = State.SHOOTING;
			}
			break;
		case SHOOTING:
			if (timer.isFinished()) {
				launcherPID.setPuncher(false);
				state = State.DONE;
			}
			break;
		case DONE:
			end();
			break;
		}
	}
	
	@Override
	protected boolean isFinished() {
		return state == State.DONE;
	}
	
	@Override
	protected void end() {}
	
	@Override
	protected void interrupted() {
		launcherPID.setAimSetpoint(0);
		launcherPID.setIntakeSetpoint(0);
	}
	
}

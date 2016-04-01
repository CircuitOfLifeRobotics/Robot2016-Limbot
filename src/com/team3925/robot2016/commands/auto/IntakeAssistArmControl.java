package com.team3925.robot2016.commands.auto;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.IntakeAssist;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeAssistArmControl extends Command {
	private final IntakeAssist intake = Robot.intakeAssist;
	private boolean putArmsDown;
	
	public IntakeAssistArmControl(boolean putArmsDown) {
		this.putArmsDown = putArmsDown;
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		if (putArmsDown) {
			intake.setArmSpeed(-0.5);
		}else {
			intake.setArmSpeed(0.25);
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		intake.setArmSpeed(0);
	}

	@Override
	protected void interrupted() {
		intake.setArmSpeed(0);
	}

}

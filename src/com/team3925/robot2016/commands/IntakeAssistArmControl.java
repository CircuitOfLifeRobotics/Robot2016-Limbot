package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.IntakeAssist;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeAssistArmControl extends Command {
	private final IntakeAssist intake = Robot.intakeAssist;
	private boolean putArmsDown;
	private final TimeoutAction timeout = new TimeoutAction();
	
	public IntakeAssistArmControl(boolean putArmsDown) {
		this.putArmsDown = putArmsDown;
	}
	
	@Override
	protected void initialize() {
		timeout.config(-1);
	}

	@Override
	protected void execute() {
		if (!timeout.isFinished()) {
			if (putArmsDown) {
				intake.setArmSpeed(-0.5);
			}else {
				intake.setArmSpeed(0.25);
			}
		}else {
			intake.setArmSpeed(0);
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

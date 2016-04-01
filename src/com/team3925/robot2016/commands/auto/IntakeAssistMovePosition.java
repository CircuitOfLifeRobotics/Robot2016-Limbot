package com.team3925.robot2016.commands.auto;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.IntakeAssist;
import com.team3925.robot2016.subsystems.IntakeAssist.IntakeAssistArmMode;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeAssistMovePosition extends Command {
	
	private final IntakeAssist intakeAssist = Robot.intakeAssist;
	
	private boolean putArmsUp;
	
	public IntakeAssistMovePosition(boolean putArmsUp, double timeout) {
		super("IntakeAssistMovePosition", timeout);
		this.putArmsUp = putArmsUp;
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		intakeAssist.setArmMode(putArmsUp ? IntakeAssistArmMode.UP : IntakeAssistArmMode.DOWN);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		intakeAssist.setArmMode(IntakeAssistArmMode.NEUTRAL);
	}

	@Override
	protected void interrupted() {
		end();
	}

}

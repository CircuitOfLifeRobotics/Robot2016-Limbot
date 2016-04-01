package com.team3925.robot2016.commands;

import com.team3925.robot2016.OI;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.IntakeAssist;
import com.team3925.robot2016.subsystems.IntakeAssist.IntakeAssistArmMode;

import edu.wpi.first.wpilibj.command.Command;

public class ManualIntakeAssist extends Command {
	private final IntakeAssist intakeAssist = Robot.intakeAssist;
	private final OI oi = Robot.oi;
	
	public ManualIntakeAssist() {
		super("ManualIntakeAssist");
		requires(intakeAssist);
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		if (oi.getIntakeAssist_ArmValue_Down() == oi.getIntakeAssist_ArmValue_Up() == false) {
			if (oi.getIntakeAssist_ArmValue_Up()) {
				intakeAssist.setArmMode(IntakeAssistArmMode.UP);
			} else if (oi.getIntakeAssist_ArmValue_Down()) {
				intakeAssist.setArmMode(IntakeAssistArmMode.DOWN);
			} else {
				intakeAssist.setArmMode(IntakeAssistArmMode.NEUTRAL);
			}
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		intakeAssist.setArmMode(IntakeAssistArmMode.NEUTRAL);
		intakeAssist.setWheelSpeeds(0d);
	}

	@Override
	protected void interrupted() {
		end();
	}

}

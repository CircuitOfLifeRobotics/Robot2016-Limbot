package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Arms;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.command.Command;

public class ManualArms extends Command implements SmartdashBoardLoggable {
	
	Arms arms = Robot.arms;
	private double climberVal;
	private boolean armVal, lastArmVal, armsEngaged = false;
	
	public ManualArms() {
		requires(arms);
	}
	
	@Override
	protected void initialize() {
		arms.setArm(false);
		arms.setClimbMotor(0);
		climberVal = 0;
	}

	@Override
	protected void execute() {
		climberVal = XboxHelper.getShooterAxis(XboxHelper.AXIS_RIGHT_Y);
		armVal = XboxHelper.getDriverAxis(XboxHelper.AXIS_TRIGGER_LEFT)>0.5 || XboxHelper.getDriverAxis(XboxHelper.AXIS_TRIGGER_RIGHT)>0.5;
		if (armVal && !lastArmVal) {
			armsEngaged = !armsEngaged;
		}
		arms.setArm(armsEngaged);
//		arms.setClimbMotor(climberVal);
		
		lastArmVal = armVal;
		
		logData();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		arms.setArm(false);
		arms.setClimbMotor(0);
	}

	@Override
	protected void interrupted() {
		arms.setArm(false);
		arms.setClimbMotor(0);
	}

	@Override
	public void logData() {
		putNumberSD("JoystickValYOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO", climberVal);
		putNumberSD("ClimbMotorSetpoint", arms.getClimbSetpoint());
	}

	@Override
	public String getFormattedName() {
		return "ManualArms_";
	}
	
}

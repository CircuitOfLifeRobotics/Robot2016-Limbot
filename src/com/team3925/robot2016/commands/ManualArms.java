package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Arms;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.command.Command;

public class ManualArms extends Command implements SmartdashBoardLoggable {
	
	Arms arms = Robot.arms;
	double joystickVal, armVal;
	
	public ManualArms() {
		requires(arms);
	}
	
	@Override
	protected void initialize() {
		arms.setArm(false);
		arms.setClimbMotor(0);
		joystickVal = 0;
	}

	@Override
	protected void execute() {
		joystickVal = XboxHelper.getShooterAxis(XboxHelper.AXIS_TRIGGER_LEFT);
		armVal = XboxHelper.getDriverAxis(2);
		arms.setArm(armVal>0.5);
		arms.setClimbMotor(joystickVal);
		
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
		putNumberSD("ArmValue", armVal);
		putNumberSD("JoystickVal", joystickVal);
		putNumberSD("ClimbMotorSetpoint", arms.getClimbSetpoint());
	}

	@Override
	public String getFormattedName() {
		return "ManualArms_";
	}

}

package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Arms;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Controls the candy cane arms on the robot for
 * lifting up at end of match
 * 
 * @author Adam C
 *
 */
public class CandyCane extends Command {
	
	Arms arms = Robot.arms;
	
	@Override
	protected void initialize() {
		arms.setClimbMotor(0);
	}
	
	@Override
	protected void execute() {
		arms.setCandyCaneSolenoid(true);
		
		if (XboxHelper.getShooterPOV()>270 || XboxHelper.getShooterPOV()<90) {
			arms.setClimbMotor(1);
		}else if (XboxHelper.getShooterPOV()>90 && XboxHelper.getShooterPOV()<270) {
			arms.setClimbMotor(-1);
		}
	}
	
	@Override
	protected void end() {
		arms.setClimbMotor(0);
	}
	
	@Override
	protected void interrupted() {
		arms.setClimbMotor(0);
	}
	
	@Override
	protected boolean isFinished() {return false;}
	
}

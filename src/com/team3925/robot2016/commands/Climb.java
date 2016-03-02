package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.CandyCanes;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Controls the candy cane arms on the robot for
 * lifting up at end of match
 * 
 * @author Adam C
 */
public class Climb extends Command implements SmartdashBoardLoggable {
	
	private final CandyCanes arms = Robot.candyCanes;
	
	@Override
	protected void initialize() {
		arms.setClimbMotor(0);
	}
	
	@Override
	protected void execute() {
//		arms.setCandyCaneSolenoid(true);
		
		if (Robot.oi.getCandyCanes_GoUp()) {
			arms.setClimbMotor(1d);
		} else if (Robot.oi.getCandyCanes_GoDown()) {
			arms.setClimbMotor(-1d);
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
	protected boolean isFinished() { return false; }

	@Override
	public void logData() {
	}

	@Override
	public String getFormattedName() {
		return "Climber_";
	}
	
}

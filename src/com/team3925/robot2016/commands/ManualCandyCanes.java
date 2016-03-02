package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.CandyCanes;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ManualCandyCanes extends Command {
	
	private final CandyCanes arms = Robot.candyCanes;
	private double climberVal;
	
    public ManualCandyCanes() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
		arms.setClimbMotor(0);
		climberVal = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}

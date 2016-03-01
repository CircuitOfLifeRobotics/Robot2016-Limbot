package com.team3925.robot2016.commands.defensecommands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Arms;
import com.team3925.robot2016.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 * 
 */
public abstract class DefenseCrossBase extends Command {
	private final DriveTrain driveTrain = Robot.driveTrain;
	private final Arms arms = Robot.arms;
	private boolean hasCrossed = false;
	
    public DefenseCrossBase() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveTrain);
    	requires(Robot.arms);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return hasCrossed;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}

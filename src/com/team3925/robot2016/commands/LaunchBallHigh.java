package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Shoots ball into high goal
 */
public class LaunchBallHigh extends Command {

    public LaunchBallHigh() {
        requires(Robot.launcher);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
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
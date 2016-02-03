package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Shoots ball into low goal
 */
public class LaunchBallLow extends Command {
	private final Launcher launcher = Robot.launcher;

    public LaunchBallLow() {
        requires(Robot.launcher);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	launcher.setIntakeSpeeds(XboxHelper.getShooterAxis(XboxHelper.AXIS_RIGHT_Y));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return XboxHelper.getShooterButton(XboxHelper.START);
    }

    // Called once after isFinished returns true
    protected void end() {
    	launcher.setIntakeSpeeds(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	launcher.setIntakeSpeeds(0);
    }

}

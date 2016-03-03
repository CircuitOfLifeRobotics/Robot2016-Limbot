package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Shoots ball into low goal
 */
public class LaunchBallLow extends Command {
	private final Launcher launcher = Robot.launcher;
	private TimeoutAction timeout = new TimeoutAction();

    public LaunchBallLow() {
        requires(Robot.launcher);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	launcher.enableIntake(true);
    	timeout.config(4d);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	launcher.setIntakeSetpoint(-Constants.LAUNCHER_MAX_INTAKE_SPEED);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return Robot.oi.getCommandCancel() || timeout.isFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
    	launcher.setIntakeSetpoint(0d);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	launcher.setIntakeSetpoint(0d);
    }

}

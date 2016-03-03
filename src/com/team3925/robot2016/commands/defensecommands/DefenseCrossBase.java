package com.team3925.robot2016.commands.defensecommands;

import com.kauailabs.navx.frc.AHRS;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.DriveTrain;
import com.team3925.robot2016.subsystems.PlexiArms;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 *  The base class for all the individual command that will cross the defenses.
 *  The only method that should need to be made/changed is <code>routine()</code>.
 */
public abstract class DefenseCrossBase extends Command implements SmartdashBoardLoggable {
	
	protected final DriveTrain driveTrain = Robot.driveTrain;
	protected final PlexiArms arms = Robot.plexiArms;
	protected final AHRS navx = Robot.navx;
	private State state = State.START;
	
	private enum State {
		START, CROSSING, CROSSED;
	}
	
    public DefenseCrossBase() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveTrain);
    	requires(Robot.candyCanes);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	state = State.START;
    	driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
    }

    // Called repeatedly when this Command is scheduled to run
    protected final void execute() {
    	routine();
    	
    	switch (state) {
		case START:
			if (Math.abs(navx.getPitch()) > 10) { //TODO fine tune values
				state = State.CROSSING;
			}
			break;
		case CROSSING:
			if (Math.abs(navx.getPitch()) < 0 && Math.abs(navx.getDisplacementY()) > 0.6) { //TODO Check axes and fine tune values
				state = State.CROSSED;
			}
			
			break;
		case CROSSED:
			break;

		default:
			DriverStation.reportError("Defense crossing command defaulted!", true);
			state = State.START;
			break;
		}
    }
    
    protected abstract void routine();
    
    public boolean hasCrossed() {
    	return state == State.CROSSED;
    }
    
    @Override
    public void logData() {
    	putStringSD("State", state.toString());
    }
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return hasCrossed();
    }

    // Called once after isFinished returns true
    protected void end() {
    	driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
    	arms.setArmUp(true);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
    	arms.setArmUp(true);
    }
}

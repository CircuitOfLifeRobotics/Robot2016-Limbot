package com.team3925.robot2016.commands.auto.defensecross;

import com.kauailabs.navx.frc.AHRS;
import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.commands.auto.GyroDrive;
import com.team3925.robot2016.subsystems.DriveTrain;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 *  The base class for all the individual command that will cross the defenses.
 *  The only method that should need to be made/changed is <code>routine()</code>.
 */
public abstract class DefenseCrossBase extends Command implements SmartdashBoardLoggable {
	
	protected final DriveTrain driveTrain = Robot.driveTrain;
	protected final AHRS navx = Robot.navx;
	protected State state = State.START;
	protected double currentRoll, lastRoll, deltaRoll;
	private final TimeoutAction timeout;
	private final GyroDrive gyroDrive;
	private final boolean runGyroDrive;
	
	enum State {
		START, CROSSING, CROSSED;
	}
	
    public DefenseCrossBase() {
    	this(true);
    }
    
    protected DefenseCrossBase(boolean runGyroDrive) {
    	requires(Robot.driveTrain);
		timeout = new TimeoutAction();
		gyroDrive = new GyroDrive(0, true, Constants.AUTONOMOUS_CROSS_DEFENSE_DRIVE_TIME, 1);
		this.runGyroDrive = runGyroDrive;
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	state = State.START;
    	driveTrain.setMotorSpeeds(0,0);
    	navx.resetDisplacement();
    	currentRoll = lastRoll = navx.getRoll();
    	deltaRoll = 0;
    	
    	if (runGyroDrive) {
			gyroDrive.start();
		}
    }

    // Called repeatedly when this Command is scheduled to run
    protected final void execute() {
    	routine();
		
		currentRoll = navx.getRoll();
		deltaRoll = currentRoll - lastRoll;
		
    	switch (state) {
		case START:
			if (Math.abs(currentRoll) > 3) { //TODO fine tune values
				state = State.CROSSING;
				timeout.config(0.2);
			}
			break;
		case CROSSING:
			if (Math.abs(currentRoll) < 2.5 /*&& Math.abs(navx.getDisplacementY()) > 0.06*/ && Math.abs(deltaRoll) < 0.05 && timeout.isFinished()) {
				state = State.CROSSED;
			}
			
			break;
		case CROSSED:
			end();
			break;
		default:
			DriverStation.reportError("Defense crossing command defaulted!", true);
			state = State.START;
			break;
		}
    	
    	lastRoll = navx.getRoll();
    	
    	logData();
    }
    
    protected abstract void routine();
    
    public boolean hasCrossed() {
    	return state == State.CROSSED;
    }
    
    public boolean gyroDriveFinished() {
    	return gyroDrive.isRunning();
    }
    
    @Override
    public void logData() {
    	putStringSD("State", state.toString());
    	putNumberSD("Roll", currentRoll);
    	putNumberSD("DeltaRoll", deltaRoll);
    	putNumberSD("Displacement", navx.getDisplacementY());
    }
    
    @Override
    public String getFormattedName() {
    	return "DefenseCross_";
    }
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return hasCrossed();
    }

    // Called once after isFinished returns true
    protected void end() {
    	driveTrain.setMotorSpeeds(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	driveTrain.setMotorSpeeds(0,0);
    }
}

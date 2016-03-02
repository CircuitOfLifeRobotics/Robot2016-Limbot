package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.OI;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.DriveTrain;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ManualDrive extends Command implements SmartdashBoardLoggable {
	private final DriveTrain driveTrain = Robot.driveTrain;
	private final OI oi = Robot.oi;
	
	private double fwdSet, turnSet, lastFwdSet, lastTurnSet, deltaFwd, deltaTurn;
	private boolean lowGear = true, shiftPressed, shiftWasPressed;
	
	public ManualDrive() {
		requires(Robot.driveTrain);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
//		driveTrain.setPIDEnabled(false);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		// negate fwd on practice robot
		fwdSet = oi.getManualDrive_ForwardValue();
		turnSet = oi.getManualDrive_RotateValue();
		deltaFwd = fwdSet - lastFwdSet;
		deltaTurn = turnSet - lastTurnSet;
		
		if (Math.abs(fwdSet) > Math.abs(lastFwdSet)) {
			if (Math.abs(deltaFwd) > Constants.MAX_DRIVETRAIN_ACCEL_PWR_PER_TICK) {
				fwdSet = lastFwdSet + Constants.MAX_DRIVETRAIN_ACCEL_PWR_PER_TICK * (deltaFwd>0 ? 1:-1);
			}
		}
		
		driveTrain.arcadeDrive(fwdSet, turnSet, true);
		
		shiftPressed = oi.getManualDrive_HighGearToggle();
		if (shiftPressed && !shiftWasPressed) {
			lowGear = !lowGear;
		}
		driveTrain.setHighGear(lowGear);
		
		lastFwdSet = fwdSet;
		lastTurnSet = turnSet;
		shiftWasPressed = shiftPressed;
		
		logData();
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
	}
	
	@Override
	public void logData() {
		putNumberSD("FwdPwr", fwdSet);
		putNumberSD("TurnPwr", turnSet);
	}
	
	@Override
	public String getFormattedName() {
		return "ManualDrive_";
	}
}

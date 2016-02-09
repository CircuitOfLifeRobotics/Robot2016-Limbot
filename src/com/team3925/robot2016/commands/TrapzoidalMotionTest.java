package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.DriveTrain;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.command.Command;

/**
 *	Trapezoidal motion tester
 */
public class TrapzoidalMotionTest extends Command implements SmartdashBoardLoggable {
	private final DriveTrain driveTrain = Robot.driveTrain;
	private double pidVel = 0;
	private double pidPos = 0;
	private int pidCounter = 0;
	private boolean pidGoingUp = true;
	private final int pidCounterMax = 40;
	private final double pidVelIncrement = 0.03;
	DriveTrainSignal input = new DriveTrainSignal(0, 0);

	public TrapzoidalMotionTest() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.driveTrain);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		driveTrain.resetEncoders();
		pidGoingUp = true;
		pidCounter = 0;
		pidVel = 0;
		pidPos = 0;
		input.left = 0;
		input.right = 0;
		driveTrain.setPIDEnabled(true);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		logData();

		if (pidGoingUp && pidCounter < pidCounterMax) {
			pidVel += pidVelIncrement;
			input.left = pidVel + pidPos;
			input.right = pidVel + pidPos;
			pidPos += pidVel;
			driveTrain.setSetpoint(input);
			pidCounter++;
		}

		if (pidGoingUp && pidCounter >= pidCounterMax) {
			pidGoingUp = false;
		}

		if (!pidGoingUp && pidCounter > 0) {
			pidVel -= pidVelIncrement;
			input.left = pidVel + pidPos;
			input.right = pidVel + pidPos;
			pidPos += pidVel;
			driveTrain.setSetpoint(input);
			pidCounter--;
		}

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		driveTrain.setPIDEnabled(false);
		driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		driveTrain.setPIDEnabled(false);
		driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
	}

	@Override
	public void logData() {
		putNumberSD("CurrentVelocitySetpoint", pidVel);
		putNumberSD("CurrentPositionSetpoint", pidPos);
		putNumberSD("CurrentProfileCount", pidCounter);
		putBooleanSD("PIDPathDone", driveTrain.onTarget());
		putBooleanSD("GoingUpProfile", pidGoingUp);
	}

	@Override
	public String getFormattedName() {
		return "TrapMotion_";
	}
}

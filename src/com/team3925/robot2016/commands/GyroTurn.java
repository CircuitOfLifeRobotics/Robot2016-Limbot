package com.team3925.robot2016.commands;

import com.kauailabs.navx.frc.AHRS;
import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.DriveTrain;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.command.PIDCommand;

public class GyroTurn extends PIDCommand implements SmartdashBoardLoggable {
	
	DriveTrain driveTrain = Robot.driveTrain;
	AHRS navx = Robot.navx;
	private double startAngle, targetAngle, currentAngle, lastAngle, errorAngle, rotations, relativeSetpoint;
	private DriveTrainSignal output = DriveTrainSignal.NEUTRAL;
	private boolean isRunning = true;
	
	public GyroTurn() {
		super(Constants.GYROTURN_P, Constants.GYROTURN_I, Constants.GYROTURN_D);
		relativeSetpoint = 0;
	}
	
	public GyroTurn(double relativeTurnAngle) {
		super(Constants.GYROTURN_P, Constants.GYROTURN_I, Constants.GYROTURN_D);
		relativeSetpoint = relativeTurnAngle;
	}
	
	@Override
	protected void initialize() {
		driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
		navx.reset();
		
		startAngle = navx.getFusedHeading();
		targetAngle = startAngle + relativeSetpoint;
		lastAngle = startAngle;
		errorAngle = 0;
		
		setSetpoint(targetAngle);
	}
	
	@Override
	protected double returnPIDInput() {
		return currentAngle + rotations*360;
	}
	
	@Override
	protected void usePIDOutput(double output) {
		//when positive output, turn clockwise, left side fwd
		output += Constants.GYROTURN_F * (output>0 ? 1:-1);
		this.output.left = -output;
		this.output.right = output;
		driveTrain.setMotorSpeeds(this.output);
	}
	
	@Override
	protected void execute() {
		currentAngle = navx.getFusedHeading();
		if (Math.abs(currentAngle-lastAngle) > 180) {
			rotations += currentAngle>lastAngle ? -1:1;
		}
		errorAngle = targetAngle - currentAngle - rotations*360;
		
		lastAngle = currentAngle;
		logData();
	}
	
	@Override
	protected boolean isFinished() {
		return Math.abs(currentAngle-targetAngle) < Constants.GYROTURN_POS_TOLERANCE;
	}
	
	@Override
	protected void end() {
		driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
		isRunning = false;
	}
	
	@Override
	protected void interrupted() {
		driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
		isRunning = false;
	}
	
	@Override
	public void logData() {
		putNumberSD("CurrentAngle", currentAngle);
		putNumberSD("TargetAngle", targetAngle);
		putNumberSD("ErrorAngle", errorAngle);
		putNumberSD("OutputLeft", output.left);
		putNumberSD("OutputRight", output.right);
		putNumberSD("Rotations", rotations);
		putBooleanSD("IsRunning", isRunning );
	}

	@Override
	public String getFormattedName() {
		return "Gyro_Turn_";
	}
	
	public void setSetpointRelative(double relativeAngle) {
		relativeSetpoint = relativeAngle;
		initialize();
	}
	
}

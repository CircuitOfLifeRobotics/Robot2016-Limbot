package com.team3925.robot2016.commands.auto;

import com.kauailabs.navx.frc.AHRS;
import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.DriveTrain;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GyroTurn extends PIDCommand implements SmartdashBoardLoggable {
	
	DriveTrain driveTrain = Robot.driveTrain;
	AHRS navx = Robot.navx;
	private double startAngle, targetAngle, currentAngle, lastAngle, errorAngle, /*rotations, */relativeSetpoint, deltaRotation;
	private int dir;
	private boolean isRunning = true;
	private double fwdOutput = 0;
	
	public GyroTurn() {
		this(0, 0);
	}
	
	public GyroTurn(double turnAngle) {
		this(turnAngle, 0);
	}
	
	public GyroTurn(double turnAngle, double forwardSpeed) {
		super(Constants.GYROTURN_P, Constants.GYROTURN_I, Constants.GYROTURN_D);
		relativeSetpoint = turnAngle;
		fwdOutput = forwardSpeed;
		requires(driveTrain);
	}
	
	@Override
	protected void initialize() {
		driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
		driveTrain.setHighGear(false);
		
		startAngle = navx.getFusedHeading();
		targetAngle = startAngle + relativeSetpoint;
		errorAngle = targetAngle-startAngle;
		lastAngle = startAngle;
		dir = (int)Math.signum(errorAngle);
		
		
		setSetpoint(targetAngle);
	}
	
	@Override
	protected double returnPIDInput() {
		return currentAngle/* + rotations*360*/;
	}
	
	@Override
	protected void usePIDOutput(double output) {
		//when positive output, turn clockwise, left side fwd
		driveTrain.arcadeDrive(fwdOutput, output, false);
		putNumberSD("TurnOutput", output);
	}
	
	@Override
	protected void execute() {
		currentAngle = navx.getFusedHeading();
		errorAngle = targetAngle - currentAngle/* - rotations*360*/;
		putNumberSD("Rotations", errorAngle);
		deltaRotation = currentAngle - lastAngle;
		
		if (Math.abs(deltaRotation) > 180) {
			
		}
		
		lastAngle = currentAngle;
		logData();
	}
	
	@Override
	protected boolean isFinished() {
		return Math.abs(currentAngle-targetAngle) < Constants.GYROTURN_POS_TOLERANCE && Math.abs(deltaRotation) < 0.005;
	}
	
	@Override
	protected void end() {
		driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
		isRunning = false;
		driveTrain.setBrakeMode(true);
	}
	
	@Override
	protected void interrupted() {
		driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
		isRunning = false;
		driveTrain.setBrakeMode(true);
	}
	
	@Override
	public void logData() {
		putNumberSD("CurrentAngle", currentAngle);
		putNumberSD("TargetAngle", targetAngle);
		putNumberSD("ErrorAngle", errorAngle);
//		putNumberSD("Rotations", rotations);
		putBooleanSD("IsRunning", isRunning );
		putNumberSD("DeltaRotations", deltaRotation);
		putNumberSD("FwdOutput", fwdOutput);
	}

	@Override
	public String getFormattedName() {
		return "GyroTurn_";
	}
	
	public void setSetpointRelative(double relativeAngle) {
		relativeSetpoint = relativeAngle;
		initialize();
	}
	
}

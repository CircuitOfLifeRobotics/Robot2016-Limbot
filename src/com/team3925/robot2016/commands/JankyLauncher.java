package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.command.Command;

public class JankyLauncher extends Command implements SmartdashBoardLoggable {
	
	private final Launcher launcher = Robot.launcher;
	
	private double TOLERANCE = 5d;
	private double FEEDFWD = 0.4;
	private double SPID_COEFF = (1d/700d) * 0.5;
	private double encoderPos, encoderSetpoint, spid, difference;
	
	public JankyLauncher() {
		requires(Robot.launcher);
	}
	
	@Override
	protected void initialize() {
		launcher.changeAimControlMode(TalonControlMode.PercentVbus);
		
		encoderPos = launcher.getAimMotorPosition();
		encoderSetpoint = encoderPos;
	}

	@Override
	protected void execute() {
		logData(); // LOG THE DATUMS
//		encoderSetpoint = -XboxHelper.getShooterAxis(XboxHelper.AXIS_LEFT_Y) * Constants.LAUNCHER_MAX_HEIGHT;
//		encoderPos = launcher.getAimMotorPosition();
//		
//		spid = FEEDFWD;
//		
//		difference = encoderSetpoint - encoderPos;
//		if (Math.abs(difference) >= TOLERANCE) {
//			spid = spid + difference * SPID_COEFF;
//		}
//		
//		spid = Math.abs(XboxHelper.getShooterAxis(XboxHelper.AXIS_LEFT_Y));
		
		spid = XboxHelper.getShooterAxis(XboxHelper.AXIS_RIGHT_Y);
		
		launcher.setAimMotorSpeed(spid, true);
		
		boolean isPunch = XboxHelper.getShooterButton(XboxHelper.STICK_RIGHT);
		if (isPunch)
			launcher.setPuncher(true);
		else
			launcher.setPuncher(false);
		
		if (XboxHelper.getShooterButton(XboxHelper.A))
			launcher.setIntakeSpeeds(1);
		else
			launcher.setIntakeSpeeds(0);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		launcher.setAimMotorSpeed(0, false);
	}

	@Override
	protected void interrupted() {
		launcher.setAimMotorSpeed(0, false);
	}

	@Override
	public void logData() {
		putNumberSD("EncoderPos", encoderPos);
		putNumberSD("Setpoint", encoderSetpoint);
		putNumberSD("Difference", difference);
		putNumberSD("Spid", spid);
		putNumberSD("coeff", SPID_COEFF);
		putNumberSD("diff*coeff", difference * SPID_COEFF);
		putBooleanSD("diff < tolerance", Math.abs(difference)<TOLERANCE);
	}
	
	@Override
	public String getFormattedName() {
		return "JankyLauncher_";
	}
}

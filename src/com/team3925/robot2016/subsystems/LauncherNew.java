package com.team3925.robot2016.subsystems;

import static com.team3925.robot2016.Constants.LAUNCHER_AIM_KD;
import static com.team3925.robot2016.Constants.LAUNCHER_AIM_KI;
import static com.team3925.robot2016.Constants.LAUNCHER_AIM_KP;
import static com.team3925.robot2016.Constants.*;

import java.util.TimerTask;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.commands.ZeroLauncher;
import com.team3925.robot2016.util.Loopable;
import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.SynchronousPID;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LauncherNew extends Subsystem implements SmartdashBoardLoggable, Loopable {
	
	private final CANTalon motorArm = RobotMap.launcherMotorArm;
	private final CANTalon motorFar = RobotMap.launcherMotorFar;
	private final CANTalon motorNear = RobotMap.launcherMotorNear;
	
//	private final SynchronousPID pid = new SynchronousPID(LAUNCHER_AIM_KP, LAUNCHER_AIM_KI, LAUNCHER_AIM_KD);
	
	private final DigitalInput limitSwitch = RobotMap.launcherLimitSwitch;
	
//	private final EncoderWatcher encoderWater = new EncoderWatcher();
	
	private double armSetpoint = 0;
	private double motorFarSetpoint = 0;
	private double motorNearSetpoint = 0;
	
	private boolean hasZeroed;
	
	private static ZeroLauncher zeroCommand = new ZeroLauncher();
	
	
	/**
	 * TODO implement class
	 */
	private static class EncoderWatcher extends TimerTask {

		@Override
		public void run() {
		}
		
	}
	
	
	@Override
	public void update() {
		
		motorNear.set(motorNearSetpoint);
		motorFar.set(motorFarSetpoint);
		
		
		// If arm motor has not zeroed, start zero command
		if (!hasZeroed) {
			startZeroCommand();
			return;
		}
		
		if (Math.abs(getArmPosition() - armSetpoint) < LAUNCHER_NEW_ARM_TOLERANCE) {
			setMotorArmSpeed(Math.signum(armSetpoint));
		}
		
	}
	
	@Override
	public void logData() {
		putNumberSD("MotorArmSpeed", motorArm.getSpeed());
		putNumberSD("MotorArmSetpoint", armSetpoint);
		putNumberSD("MotorArmEncoderPos", getArmPosition());

		putNumberSD("MotorNearSpeed", motorNear.getSpeed());
		putNumberSD("MotorFarSpeed", motorFar.getSpeed());
		putNumberSD("MotorNearSetpoint", motorNearSetpoint);
		putNumberSD("MotorFarSetpoint", motorFarSetpoint);
		
		armSetpoint = SmartDashboard.getNumber(getFormattedName() + "MotorArmSetpointSETTER", 0);
	}
	
	public void setMotorArmSpeedRaw(double speed) {
		motorArm.set(MiscUtil.limit(speed) * LAUNCHER_NEW_GLOBAL_POWER);
	}
	
	public void setMotorArmSpeed(double speed) {
		boolean cantRunMotorDown = getArmPosition() <= 0 && speed < 0;
		boolean cantRunMotorUp = getArmPosition() >= LAUNCHER_NEW_MAX_ARM_ANGLE && speed > 0;
		
		if (cantRunMotorDown || cantRunMotorUp) {
			setMotorArmSpeedRaw(0);
		} else {
			setMotorArmSpeedRaw(armSetpoint);
		}
	}
	
	/**
	 * @return true if command was started or false if command was already running
	 */
	public boolean startZeroCommand() {
		if (!zeroCommand.isRunning()) {
			zeroCommand.start();
			return true;
		} else {
			return false;
		}
	}
	
	public void setLauncherZeroed() {
		motorArm.setEncPosition(0);
	}
	
	/**
	 * @param setpoint desired angle in degrees
	 */
	public void setArmSetpoint(double setpoint) {
		if (!Double.isFinite(setpoint)) {
			DriverStation.reportError("Could not receive setpoint! Was not a finite number!", false);
			armSetpoint = 0;
		} else {
			armSetpoint = Math.min(LAUNCHER_NEW_MAX_ARM_ANGLE, Math.max(0, setpoint));
		}
	}
	
	public void setMotorNearSetpoint(double setpoint) {
		motorNearSetpoint = setpoint;
	}
	
	public void setMotorFarSetpoint(double setpoint) {
		motorFarSetpoint = setpoint;
	}
	
	public double getArmPosition() {
		return LAUNCHER_NEW_ENCODER_SCALE_FACTOR * motorArm.getEncPosition();
	}
	
	public boolean getLimitSwitch() {
		return limitSwitch.get();
	}
	
	public boolean getArmEncoderMoving() {
		return true; //TODO implement
	}
	
	@Override
	public String getFormattedName() {
		return null;
	}
	
	@Override
	protected void initDefaultCommand() {
	}



}

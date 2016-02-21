package com.team3925.robot2016.commands;


import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.LimitPIDController;
import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.command.Command;

public class LauncherPID extends Command implements SmartdashBoardLoggable {
	
	private final Launcher launcher = Robot.launcher;
	
	private LimitPIDController pidLoop = new LimitPIDController();
	
	private boolean aimEnabled = true, intakeEnabled = true, doRunAim = true, aimOnTarget = false, intakeOnTarget = false;
	private double aimSetpoint, aimSetpointDiff, aimLastSetpoint, aimLimitedSetpoint, aimPosition, aimDifference, aimOutput, aimAngleMultiplier;
	private double intakeOutput, intakeSpeedLeft, intakeSpeedRight;
	
	public LauncherPID(double setPoint) {
		this.aimLimitedSetpoint = setPoint;
		requires(Robot.launcher);
	}
	
	public LauncherPID(double p, double i, double d, double setPoint) {
		this.aimLimitedSetpoint = setPoint;
		pidLoop.setPID(p, i, d);
		requires(Robot.launcher);
	}
	
	/**
	 * If isPunch is true, the piston will engage and punch the ball
	 * 
	 * @param isPunch
	 */
	public void setPuncher(boolean isPunch) {
		launcher.setPuncher(isPunch);
	}
	
	/**
	 * Returns true if the intake wheels are both at the specified speed
	 * 
	 * @return intakeOnTarget
	 */
	public boolean isIntakeOnSetpoint() {
		return intakeOnTarget;
	}
	
	/**
	 * Returns true if the aim position is at the specified setpoint angle
	 * and the aim is stable (not moving)
	 * 
	 * @return aimOnTarget
	 */
	public boolean isAimOnSetpoint() {
		return aimOnTarget;
	}
	
	/**
	 * Sets the setpoint of the aim motor
	 * In degrees
	 * 
	 * @param setpoint
	 */
	public void setAimSetpoint(double setpoint) {
		setpoint = Math.max(0, Math.min(Constants.LAUNCHER_MAX_HEIGHT, setpoint));
		aimSetpoint = setpoint;
	}
	
	/**
	 * Sets the setpoint of the intake motors
	 * TODO: change to easier units
	 * In native units per 100 ms
	 * 
	 * @param setpoint
	 */
	public void setIntakeSetpoint(double setpoint) {
		intakeOutput = Math.max(-26000, Math.min(26000, setpoint));
	}
	
	protected void initialize() {
		reset();
	}

	protected void execute() {
		if (aimEnabled) {
//			aimSetpoint = XboxHelper.getShooterButton(XboxHelper.TRIGGER_LT) ? MiscUtil.joystickToDegrees(XboxHelper.getShooterAxis(XboxHelper.AXIS_LEFT_Y)):aimJoystickSetpoint;
			aimLimitedSetpoint = aimSetpoint;
			aimPosition = MiscUtil.aimEncoderTicksToDegrees(launcher.getAimMotorPosition());
			aimDifference = aimLimitedSetpoint - aimPosition;
			aimSetpointDiff = aimLimitedSetpoint - aimLastSetpoint;
			
			if (Math.abs(aimSetpointDiff) > Constants.LAUNCHER_AIM_INCREMENT) {
				aimLimitedSetpoint = aimLastSetpoint + Constants.LAUNCHER_AIM_INCREMENT * (aimSetpointDiff>0 ? 1:-1);
			}
			
			aimOnTarget = Math.abs(aimSetpoint) < Constants.LAUNCHER_AIM_TOLERANCE && Math.abs(launcher.getAimMotorSpeed()) < 1000;
			
			doRunAim = (aimPosition>Constants.LAUNCHER_AIM_INCREMENT) || (Math.abs(aimSetpointDiff)>Constants.LAUNCHER_AIM_INCREMENT);
			
			pidLoop.setSetpoint(aimLimitedSetpoint);
			pidLoop.calculate(aimPosition);
			aimOutput = pidLoop.get();
			aimAngleMultiplier = 0.4*Math.cos(Math.toRadians(aimPosition))+0.06;
			aimOutput = aimOutput * aimAngleMultiplier;
			aimOutput = Math.min(Math.max(aimOutput, -0.2), 0.8);
			
			if (doRunAim) {
				launcher.setAim(aimOutput);
			}else {
				launcher.setAim(0);
			}
		} else {
			launcher.setAim(0);
			//launcher.setAim(on vacation);
		}
		
		intakeSpeedLeft = launcher.getIntakeSpeedLeft();
		intakeSpeedRight = launcher.getIntakeSpeedRight();
		
		intakeOnTarget = Math.abs(launcher.getIntakeSpeedLeft()-intakeOutput)<Constants.LAUNCHER_WHEELS_TOLERANCE &&
				Math.abs(launcher.getIntakeSpeedRight()-intakeOutput)<Constants.LAUNCHER_WHEELS_TOLERANCE;
		
		/* TODO: get the acutal number of encoder counts per rev
		 * Encoder counts per rev?
		 * 15266664
		 * 4316 
		 * 7301
		 * ~6000
		 * 4246 -> 7250
		 */
		
		if (intakeEnabled) {
			if (XboxHelper.getShooterButton(XboxHelper.START)) {intakeOutput = 0;}
//			else if (XboxHelper.getShooterButton(XboxHelper.Y)) {intakeOutput = 1;}
//			else if (XboxHelper.getShooterButton(XboxHelper.X)) {intakeOutput = 0.2;}
//			else if (XboxHelper.getShooterButton(XboxHelper.B)) {intakeOutput = -0.2;}
//			else if (XboxHelper.getShooterButton(XboxHelper.A)) {intakeOutput = -1;}
//			else if (XboxHelper.getShooterButton(XboxHelper.Y)) {intakeOutput = 25000;}
//			else if (XboxHelper.getShooterButton(XboxHelper.X)) {intakeOutput = 4000;}
//			else if (XboxHelper.getShooterButton(XboxHelper.B)) {intakeOutput = -4000;}
//			else if (XboxHelper.getShooterButton(XboxHelper.A)) {intakeOutput = -25000;}
			launcher.setIntake(intakeOutput);
		} else {
			launcher.setIntake(0);
			//launcher.setIntake(on vacation);
		}
		
		launcher.setPuncher(XboxHelper.getShooterButton(XboxHelper.TRIGGER_RT));
		
		logData();
		
		aimLastSetpoint = aimLimitedSetpoint;
	}
	
	public void enableAim(boolean isEnable) {
		aimEnabled = isEnable;
	}
	
	public void enableIntake(boolean isEnable) {
		intakeEnabled = isEnable;
	}
	
	public boolean getAimEnabled() {
		return aimEnabled;
	}
	
	public boolean getIntakeEnabled() {
		return intakeEnabled;
	}
	
	public void setAimEnabled(boolean isEnabled) {
		aimEnabled = isEnabled;
	}
	
	public void setIntakeEnabled(boolean isEnabled) {
		intakeEnabled = isEnabled;
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	@Override
	protected void end() {
		launcher.setAim(0);
	}

	@Override
	protected void interrupted() {
		launcher.setAim(0);
	}

	@Override
	public void logData() {
		putNumberSD("Difference", aimDifference);
		putNumberSD("Setpoint", aimLimitedSetpoint);
		putNumberSD("Position", aimPosition);
		putNumberSD("Output", aimOutput);
		putNumberSD("AngleMultiplier", aimAngleMultiplier);
		putNumberSD("Differential", pidLoop.getDValue());
		putNumberSD("Error", pidLoop.getError());
		putNumberSD("PrevError", pidLoop.getPrevError());
		putNumberSD("IntakeSpeedLeft", intakeSpeedLeft);
		putNumberSD("IntakeSpeedRight", intakeSpeedRight);
		putNumberSD("IntakeOutput", intakeOutput);
		putBooleanSD("UporDown", aimDifference>0);
		putBooleanSD("AimEnabled", aimEnabled);
		putBooleanSD("IntakeEnabled", intakeEnabled);
		putBooleanSD("DoRunIntake", doRunAim);
	}

	@Override
	public String getFormattedName() {
		return "Launcher_PID_";
	}
	
	public void reset() {
		launcher.setIntakePID(Constants.LAUNCHER_WHEELS_KP, Constants.LAUNCHER_WHEELS_KI, Constants.LAUNCHER_WHEELS_KD, Constants.LAUNCHER_WHEELS_KF, Constants.LAUNCHER_WHEELS_IZONE, Constants.LAUNCHER_WHEELS_RAMP_RATE, 0);
		launcher.setIntakeProfile(0);
		
		pidLoop.setPIDLimits(10000, 10000, 10000, 10000, -10000, -10000, -10000, -10000);
		
		setAimSetpoint(0);
		setIntakeSetpoint(0);
		intakeOutput = 0;
		aimSetpoint = 0;
		aimLastSetpoint = 0;
		
		intakeSpeedLeft = intakeSpeedRight = 0;
	}
	
}

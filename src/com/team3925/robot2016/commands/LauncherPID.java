package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.LimitPID;
import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LauncherPID extends Command implements SmartdashBoardLoggable {
	
	private final Launcher launcher = Robot.launcher;
	
	private LimitPID pidLoop = new LimitPID();
	
	private boolean aimEnabled = true, intakeEnabled = true, doRunIntake = true;
	private double aimJoystickSetpoint, aimSetpointDiff, aimLastSetpoint, aimSetpoint, aimPosition, aimDifference, aimOutput, aimAngleMultiplier;
	private double intakeOutput, intakeSpeedLeft, intakeSpeedRight, intakeSetpoint;
	
	public LauncherPID(double setPoint) {
		this.aimSetpoint = setPoint;
	}
	
	public LauncherPID(double p, double i, double d, double setPoint) {
		this.aimSetpoint = setPoint;
		pidLoop.setPID(p, i, d);
	}
	
	protected void initialize() {
		launcher.changeAimControlMode(TalonControlMode.PercentVbus);
		launcher.changeIntakeLeftControlMode(TalonControlMode.PercentVbus);
		launcher.changeIntakeRightControlMode(TalonControlMode.PercentVbus);
		
		pidLoop.setPIDLimits(10000, 10000, 10000, 10000, -10000, -10000, -10000, -10000);
		intakeOutput = 0;
		aimJoystickSetpoint = 0;
		aimLastSetpoint = 0;
		
		intakeSpeedLeft = intakeSpeedRight = 0;
	}

	protected void execute() {
		if (aimEnabled) {
			aimJoystickSetpoint = XboxHelper.getShooterButton(XboxHelper.TRIGGER_LT) ? MiscUtil.joystickToDegrees(XboxHelper.getShooterAxis(XboxHelper.AXIS_LEFT_Y)):aimJoystickSetpoint;
			aimSetpoint = aimJoystickSetpoint;
			aimPosition = MiscUtil.aimEncoderTicksToDegrees(launcher.getAimMotorPosition());
			aimDifference = aimSetpoint - aimPosition;
			aimSetpointDiff = aimSetpoint - aimLastSetpoint;
			
			if (Math.abs(aimSetpointDiff) > Constants.LAUNCHER_AIM_INCREMENT) {
				aimSetpoint = aimLastSetpoint + Constants.LAUNCHER_AIM_INCREMENT * (aimSetpointDiff>0 ? 1:-1);
			}
			doRunIntake = (aimPosition>Constants.LAUNCHER_AIM_INCREMENT) || (Math.abs(aimSetpointDiff)>Constants.LAUNCHER_AIM_INCREMENT);
			
			pidLoop.setSetpoint(aimSetpoint);
			pidLoop.calculate(aimPosition);
			aimOutput = pidLoop.get();
			aimAngleMultiplier = (2*Math.cos(Math.toRadians(aimPosition))+0.3)/5;
			aimOutput = aimOutput * aimAngleMultiplier;
			aimOutput = Math.min(Math.max(aimOutput, -0.2), 0.8);
			
			if (doRunIntake) {
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
			else if (XboxHelper.getShooterButton(XboxHelper.Y)) {/*intakeSetpoint = 13000 */intakeOutput = 1    ;}
			else if (XboxHelper.getShooterButton(XboxHelper.X)) {/*intakeSetpoint = 4000  */intakeOutput = 0.5 ;}
			else if (XboxHelper.getShooterButton(XboxHelper.B)) {/*intakeSetpoint = -4000 */intakeOutput = -0.5;}
			else if (XboxHelper.getShooterButton(XboxHelper.A)) {/*intakeSetpoint = -13000*/intakeOutput = -1   ;}
			
			launcher.setIntakeSpeeds(intakeOutput);
		} else {
			launcher.setIntakeSpeeds(0);
			//launcher.setIntake(on vacation);
		}
		
		launcher.setPuncher(XboxHelper.getShooterButton(XboxHelper.TRIGGER_RT));
		
		logData();
		
		aimLastSetpoint = aimSetpoint;
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
		launcher.setAimMotorSpeed(0);
	}

	@Override
	public void logData() {
		putNumberSD("Difference", aimDifference);
		putNumberSD("Setpoint", aimSetpoint);
		putNumberSD("Position", aimPosition);
		putNumberSD("Output", aimOutput);
		putNumberSD("AngleMultiplier", aimAngleMultiplier);
		putNumberSD("Differential", pidLoop.getDValue());
		putNumberSD("Error", pidLoop.getError());
		putNumberSD("PrevError", pidLoop.getPrevError());
		putNumberSD("IntakeSpeedLeft", intakeSpeedLeft);
		putNumberSD("IntakeSpeedRight", intakeSpeedRight);
		putNumberSD("IntakeSpeedSetpoint", intakeSetpoint);
		putNumberSD("IntakeOutput", intakeOutput);
		putBooleanSD("UporDown", aimDifference>0);
		putBooleanSD("AimEnabled", aimEnabled);
		putBooleanSD("IntakeEnabled", intakeEnabled);
		putBooleanSD("DoRunIntake", doRunIntake);
	}

	@Override
	public String getFormattedName() {
		return "Launcher_PID_";
	}
	
}

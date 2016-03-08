package com.team3925.robot2016.subsystems;

import static com.team3925.robot2016.Constants.LAUNCHER_AIM_MOTOR_SPEED_MULTIPLIED;
import static com.team3925.robot2016.util.XboxHelper.A;
import static com.team3925.robot2016.util.XboxHelper.B;
import static com.team3925.robot2016.util.XboxHelper.X;
import static com.team3925.robot2016.util.XboxHelper.Y;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.util.LauncherPose;
import com.team3925.robot2016.util.LimitPIDController;
import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.SynchronousPID;
import com.team3925.robot2016.util.TimeoutAction;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The subsystem that represents the launcher. It is responsible for the
 * keeping the arm at the desired angle and keeping the intake motors at
 * the desired speed.
 * 
 * @author Adam C
 */
public final class Launcher extends Subsystem implements SmartdashBoardLoggable {
	
    private final CANTalon motorLeft = RobotMap.launcherMotorLeft;
    private final CANTalon motorRight = RobotMap.launcherMotorRight;
    private final CANTalon motorAim = RobotMap.launcherMotorAim;
    private final DoubleSolenoid puncherSolenoid = RobotMap.launcherPuncherSolenoid;
    private LimitPIDController pidLoop = new LimitPIDController();
    private TimeoutAction manualPuncherWait = new TimeoutAction();
    private SynchronousPID leftPidLoop = new SynchronousPID(Constants.LAUNCHER_WHEELS_KP, Constants.LAUNCHER_WHEELS_KP, Constants.LAUNCHER_WHEELS_KP);
    private SynchronousPID rightPidLoop = new SynchronousPID(Constants.LAUNCHER_WHEELS_KP, Constants.LAUNCHER_WHEELS_KP, Constants.LAUNCHER_WHEELS_KP);
//    Sensor for ball goes here
    
    private boolean aimEnabled = true, intakeEnabled = true, doRunAim = true, aimOnTarget = false, intakeOnTarget = false;
	private double aimSetpoint, aimSetpointDiff, aimLastSetpoint, aimLimitedSetpoint, aimPosition, aimDifference, aimOutput, aimAngleMultiplier;
	private double intakeSetpoint, intakeLimitedSetpoint, intakeSetpointDiff, intakeLastSetpoint/*, intakeSpeedLeft, intakeSpeedRight*/;
	private LauncherPose cachedPose = new LauncherPose(0d, 0d, 0d, 0d, 0d, 0d);
    
    private boolean hasBall = false;
	
	public void init() {
		pidLoop.setPID(Constants.LAUNCHER_AIM_KP, Constants.LAUNCHER_AIM_KI, Constants.LAUNCHER_AIM_KD);
		pidLoop.setPIDLimits(10000, 10000, 10000, 10000, -10000, -10000, -10000, -10000);
		
		motorLeft.setPID(Constants.LAUNCHER_WHEELS_KP, Constants.LAUNCHER_WHEELS_KI, Constants.LAUNCHER_WHEELS_KD, Constants.LAUNCHER_WHEELS_KF, Constants.LAUNCHER_WHEELS_IZONE, Constants.LAUNCHER_WHEELS_RAMP_RATE, 0);
		motorRight.setPID(Constants.LAUNCHER_WHEELS_KP, Constants.LAUNCHER_WHEELS_KI, Constants.LAUNCHER_WHEELS_KD, Constants.LAUNCHER_WHEELS_KF, Constants.LAUNCHER_WHEELS_IZONE, Constants.LAUNCHER_WHEELS_RAMP_RATE, 0);
		motorLeft.setProfile(0);
		motorRight.setProfile(0);
		
		setIntakeProfile(0);
		setAimSetpoint(0);
		setIntakeSetpoint(0);
		
		intakeSetpoint = 0;
		intakeLimitedSetpoint = 0;
		intakeLastSetpoint = 0;
		aimLimitedSetpoint = 0;
		aimSetpoint = 0;
		aimLastSetpoint = 0;
//		intakeSpeedLeft = intakeSpeedRight = 0;
		
		setPuncher(false);
	}
    
	/**
	 * Sets the setpoint of the aim motor
	 * 
	 * @param setpoint in degrees
	 */
	public void setAimSetpoint(double setpoint) {
//		setpoint = Math.max(0, Math.min(Constants.LAUNCHER_MAX_HEIGHT, setpoint));
		aimSetpoint = setpoint;
		SmartDashboard.putNumber("AimSetpointLauncher", setpoint);
	}
	
	/**
	 * Sets the setpoint of the intake motors. 
	 * Spinning IN is positive, spinning OUT is negative
	 * 
	 * @param setpoint in native units per 100ms
	 */
	public void setIntakeSetpoint(double setpoint) {
		intakeSetpoint = Math.max(-26000, Math.min(26000, setpoint)); //TODO change to easier units
	}
	
	public void enableAim(boolean isEnable) {
		aimEnabled = isEnable;
	}
	
	public void enableIntake(boolean isEnable) {
		intakeEnabled = isEnable;
	}

	
	public void update() {
		hasBall = false; //TODO add sensor for ball
		cachedPose.reset(getIntakePosLeft(), getIntakePosRight(), getIntakeSpeedLeft(), getIntakeSpeedRight(), getAimMotorPosition(), getAimMotorSpeed());
		
		if (aimEnabled) {
//			aimSetpoint = XboxHelper.getShooterButton(XboxHelper.TRIGGER_LT) ? MiscUtil.joystickToDegrees(XboxHelper.getShooterAxis(XboxHelper.AXIS_LEFT_Y)):aimSetpoint;
			aimLimitedSetpoint = aimSetpoint;
			aimPosition = getAimMotorPosition();
			aimDifference = aimLimitedSetpoint - aimPosition;
			aimSetpointDiff = aimLimitedSetpoint - aimLastSetpoint;
			
			if (Math.abs(aimSetpointDiff) > Constants.LAUNCHER_AIM_INCREMENT) {
				aimLimitedSetpoint = aimLastSetpoint + Constants.LAUNCHER_AIM_INCREMENT * (aimSetpointDiff>0 ? 1:-1);
			}
			
			aimOnTarget = Math.abs(aimDifference) < Constants.LAUNCHER_AIM_TOLERANCE && Math.abs(getAimMotorSpeed()) < 1000;
			
			doRunAim = (aimPosition>Constants.LAUNCHER_AIM_INCREMENT) || (Math.abs(aimSetpointDiff)>Constants.LAUNCHER_AIM_INCREMENT);
			
			pidLoop.setSetpoint(aimLimitedSetpoint);
			pidLoop.calculate(aimPosition);
			aimOutput = pidLoop.get();
//			aimAngleMultiplier = 0.4*Math.cos(Math.toRadians(aimPosition))+0.06;
			aimAngleMultiplier = 0.5*Math.cos(Math.toRadians(aimPosition))+0.06;
			aimOutput = aimOutput * aimAngleMultiplier;
			aimOutput = Math.min(Math.max(aimOutput, -0.2), 0.8);
			
			if (doRunAim) {
				setAim(aimOutput);
			} else {
				setAim(0);
			}
		} else {
			setAim(0);
			//launcher.setAim(on vacation);
		}
		
//		intakeSpeedLeft = getIntakeSpeedLeft();
//		intakeSpeedRight = getIntakeSpeedRight();
		
		double intakeDiffLeft = Math.abs(getIntakeLeftError());
		double intakeDiffRight = Math.abs(getIntakeRightError());
		intakeOnTarget = intakeDiffLeft<Constants.LAUNCHER_WHEELS_TOLERANCE &&
				intakeDiffRight<Constants.LAUNCHER_WHEELS_TOLERANCE;
		
		/* TODO: get the acutal number of encoder counts per rev
		 * Encoder counts per rev?
		 * 15266664
		 * 4316 
		 * 7301
		 * ~6000
		 * 4246 -> 7250
		 */
		
		if (intakeEnabled) {
			if (Robot.oi.getLauncher_ResetIntakeSetpoint()) {intakeSetpoint = 0;}
//			else if (XboxHelper.getShooterButton(Y)) {intakeSetpoint = 1;}
//			else if (XboxHelper.getShooterButton(X)) {intakeSetpoint = 0.2;}
//			else if (XboxHelper.getShooterButton(B)) {intakeSetpoint = -0.2;}
//			else if (XboxHelper.getShooterButton(A)) {intakeSetpoint = -1;}
//			else if (XboxHelper.getShooterButton(Y)) {intakeSetpoint = 25000;}
//			else if (XboxHelper.getShooterButton(X)) {intakeSetpoint = 4000;}
//			else if (XboxHelper.getShooterButton(B)) {intakeSetpoint = -4000;}
//			else if (XboxHelper.getShooterButton(A)) {intakeSetpoint = -25000;}
			
//			intakeLimitedSetpoint = intakeSetpoint;
//			intakeSetpointDiff = intakeSetpoint - intakeLastSetpoint;
//			
//			if (Math.abs(intakeSetpointDiff) > Constants.LAUNCHER_INTAKE_INCREMENT) {
//				intakeLimitedSetpoint = intakeLastSetpoint + Constants.LAUNCHER_INTAKE_INCREMENT * (intakeSetpointDiff>0 ? 1:-1);
//			}
			
			setLeftIntakeSetpoint(intakeSetpoint);
			setRightIntakeSetpoint(intakeSetpoint);
			
			leftPidLoop.calculate(getIntakeSpeedLeft());
			rightPidLoop.calculate(getIntakeSpeedRight());
			
			setLeftIntake(leftPidLoop.get());
			setRightIntake(rightPidLoop.get());
		} else {
			setLeftIntakeSetpoint(0);
			setRightIntakeSetpoint(0);
			//launcher.setIntake(on vacation);
		}
		
		aimLastSetpoint = aimLimitedSetpoint;
		intakeLastSetpoint = intakeLimitedSetpoint;
	}
	

	
	public void setPuncher(boolean isHigh) {
		//reverse isHigh for practice bot
		puncherSolenoid.set(!isHigh ? Value.kReverse:Value.kForward);
	}
	
	public void setLeftIntake(double input) {
		motorLeft.set(input);
	}
	
	public void setRightIntake(double input) {
		motorRight.set(input);
	}
	
	public void setLeftIntakeSetpoint(double setpoint) {
//		motorLeft.setSetpoint(setpoint);
		leftPidLoop.setSetpoint(-setpoint);
	}
	
	public void setRightIntakeSetpoint(double setpoint) {
//		motorRight.setSetpoint(setpoint);
		rightPidLoop.setSetpoint(-setpoint);
	}
    
    private void changeAimControlMode(TalonControlMode mode) {
    	motorAim.changeControlMode(mode);
    }
    
    private void changeIntakeControlMode(TalonControlMode mode) {
    	motorLeft.changeControlMode(mode);
    	motorRight.changeControlMode(mode);
    }
    
//    public void setAimProfile(int profile) {
//    	profile = Math.min(1, Math.max(0, profile));
//    	motorAim.setProfile(profile);
//    }
    
//    public void setAimPID(double p, double i, double d, double f, int izone, double closeLoopRampRate, int profile) {
//    	profile = Math.min(1, Math.max(0, profile));
//    	motorAim.setPID(p, i, d, f, izone, closeLoopRampRate, profile);
//    }
    
    public void setIntakePID(double p, double i, double d, double f, int izone, double closeLoopRampRate, int profile) {
    	motorLeft.setPID(p, i, d, f, izone, closeLoopRampRate, profile);
    	motorRight.setPID(p, i, d, f, izone, closeLoopRampRate, profile);
    }
    
    public void setIntakeProfile(int profile) {
    	motorLeft.setProfile(profile);
    	motorRight.setProfile(profile);
    }
    
    private void setAim(double output) {
    	motorAim.set(output);
    }
    
    private void setAimMotorSpeed(double speed, boolean doLimits) {
    	if (doLimits) {
	    	if (getAimMotorPosition() <= 10)
	    		speed = Math.max(speed, 0);
	    	if (getAimMotorPosition() >= (Constants.LAUNCHER_MAX_HEIGHT+10))
	    		speed = Math.min(speed, 0);
    	}
    	
    	motorAim.set(speed * LAUNCHER_AIM_MOTOR_SPEED_MULTIPLIED);
    }
    
    @Override
    public void logData() {
//    	putNumberSD("MotorLeft", motorLeft.get());
//    	putNumberSD("MotorRight", motorRight.get());
//    	putNumberSD("AimMotorSpeed", motorAim.getSpeed());
//    	putNumberSD("MotorAimPosition", getAimMotorPosition());
//    	putStringSD("MotorAimMode", motorAim.getControlMode().toString());
    	putNumberSD("EncoderAimPos", getAimMotorPosition());
    	putNumberSD("AimOutput", aimOutput);
    	
    	putNumberSD("MotorAimError", aimDifference);
    	
    	putNumberSD("AimSetpoint", aimSetpoint);
//    	putNumberSD("IntakeSetpoint", intakeSetpoint);
    	
    	putNumberSD("MotorRightSetpoint", motorRight.getSetpoint());
    	putNumberSD("MotorLeftSetpoint", motorLeft.getSetpoint());
//    	putNumberSD("IntakeSetpointDiff", intakeSetpointDiff);
    	
    	putNumberSD("LeftPIDLoopSetpoint", leftPidLoop.getSetpoint());
    	putNumberSD("RightPIDLoopSetpoint", rightPidLoop.getSetpoint());
    	
    	putNumberSD("LeftPIDLoopError", leftPidLoop.getError());
    	putNumberSD("RightPIDLoopError", rightPidLoop.getError());
    	
    	putNumberSD("LeftSpeedFt/Sec", (getIntakeSpeedLeft()*100/4096) * Constants.LAUNCHER_WHEEL_CIRCUM);
    	putNumberSD("RightSpeedFt/Sec", (getIntakeSpeedRight()*100/4096) * Constants.LAUNCHER_WHEEL_CIRCUM);
    	
//    	putNumberSD("MotorRightEncPos", getIntakePosRight());
//    	putNumberSD("MotorLeftEncPos", getIntakePosLeft());
    	
//    	putNumberSD("MotorRightEncVeloctiy", getIntakeVelRight());
//    	putNumberSD("MotorLeftEncVeloctiy", getIntakeVelLeft());
    	
    	putNumberSD("MotorRightSpeed", getIntakeSpeedRight());
    	putNumberSD("MotorLeftSpeed", getIntakeSpeedLeft());
    	
//    	putNumberSD("MotorRightError", motorRight.getError());
//    	putNumberSD("MotorLeftError", motorLeft.getError());
    	
//    	putNumberSD("MotorRightOutputC", motorRight.getOutputCurrent());
//    	putNumberSD("MotorRightOutputV", motorRight.getOutputVoltage());
//    	putNumberSD("MotorLeftOutputC", motorLeft.getOutputCurrent());
//    	putNumberSD("MotorLeftOutputV", motorLeft.getOutputVoltage());
    	
//    	putNumberSD("MotorRightOutV", motorRight.getOutputVoltage());
//    	putNumberSD("MotorRightOutC", motorRight.getOutputCurrent());
//    	putNumberSD("MotorLeftOutV", motorLeft.getOutputVoltage());
//    	putNumberSD("MotorLeftOutC", motorLeft.getOutputCurrent());
    	
//    	putNumberSD("MotorRightAnalogInRaw", motorRight.getAnalogInRaw());
//    	putNumberSD("MotorLeftAnalogInRaw", motorLeft.getAnalogInRaw());
    	
//    	putNumberSD("MotorRightPinStateQuadA", motorRight.getPinStateQuadA());
//    	putNumberSD("MotorRightPinStateQuadB", motorRight.getPinStateQuadB());
//    	putNumberSD("MotorRightPinStateQuadIdx", motorRight.getPinStateQuadIdx());
//    	putNumberSD("MotorLeftPinStateQuadA", motorLeft.getPinStateQuadA());
//    	putNumberSD("MotorLeftPinStateQuadB", motorLeft.getPinStateQuadB());
//    	putNumberSD("MotorLeftPinStateQuadIdx", motorLeft.getPinStateQuadIdx());
    	
    	//non linear loop gain
//    	putNumberSD("MotorAim_getBusVoltage", motorAim.getBusVoltage()              );
//    	putNumberSD("MotorAim_getClosedLoopError", motorAim.getClosedLoopError()    );
//    	putNumberSD("MotorAim_getCloseLoopRampRate", motorAim.getCloseLoopRampRate());
//    	putNumberSD("MotorAim_getP", motorAim.getP()                                );
//    	putNumberSD("MotorAim_getI", motorAim.getI()                                );
//    	putNumberSD("MotorAim_getD", motorAim.getD()                                );
//    	putNumberSD("MotorAim_getF", motorAim.getF()                                );
//    	putNumberSD("MotorAim_getEncPosition", motorAim.getEncPosition()            );
//    	putNumberSD("MotorAim_getEncVelocity", motorAim.getEncVelocity()            );
//    	putNumberSD("MotorAim_getError", motorAim.getError()                        );
//    	putNumberSD("MotorAim_getIZone", motorAim.getIZone()                        );
//    	putNumberSD("MotorAim_getOutputCurrent", motorAim.getOutputCurrent()        );
//    	putNumberSD("MotorAim_getPosition", motorAim.getPosition()                  );
//    	putNumberSD("MotorAim_getSetpoint", motorAim.getSetpoint()                  );
//    	putNumberSD("MotorAim_getSpeed", motorAim.getSpeed()                        );
//    	putNumberSD("MotorAim_getPidGet", motorAim.pidGet()                         );
//    	putNumberSD("MotorAim_PinStateA", motorAim.getPinStateQuadA());
//    	putNumberSD("MotorAim_PinStateB", motorAim.getPinStateQuadB());
//    	putNumberSD("MotorAim_PinStateIdx", motorAim.getPinStateQuadIdx());
//    	putBooleanSD("MotorAim_getInverted", motorAim.getInverted()                 );
    }
    
	public boolean hasBall() {
		return hasBall;
	}
	
	public boolean getAimEnabled() {
		return aimEnabled;
	}
	
	public boolean getIntakeEnabled() {
		return intakeEnabled;
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
	
	public LauncherPose getPhysicalPose() {
		return cachedPose;
	}
	
	public double getIntakeLeftError() {
		return motorLeft.getError();
	}
	
	public double getIntakeRightError() {
		return motorRight.getError();
	}
	
    public double getIntakeSetpointLeft() {
		return motorLeft.getSetpoint();
	}
	
	public double getIntakeSetpointRight() {
		return motorRight.getSetpoint();
	}
	
	public double getAimMotorPosition() {
		return MiscUtil.aimEncoderTicksToDegrees(-motorAim.getEncPosition());
	}
	
	private double getAimMotorSpeed() {
		return motorAim.getSpeed();
	}
	
	private double getIntakeVelRight() {
		try {
			return motorRight.getEncVelocity();
		} catch (Exception e) {
			DriverStation.reportError(motorRight.getDeviceID() + " says " + e.getMessage(), true);
			return 0;
		}
	}
	
	private double getIntakeVelLeft() {
		try {
			return motorLeft.getEncVelocity();
		} catch (Exception e) {
			DriverStation.reportError(motorLeft.getDeviceID() + " says " + e.getMessage(), true);
			return 0;
		}
	}
	
	private double getIntakePosLeft() {
		try {
			return motorLeft.getEncPosition();
		} catch (Exception e) {
			DriverStation.reportError(motorLeft.getDeviceID() + " says " + e.getMessage(), true);
			return 0;
		}
	}
	
	private double getIntakePosRight() {
		try {
			return motorRight.getEncPosition();
		} catch (Exception e) {
			DriverStation.reportError(motorRight.getDeviceID() + " says " + e.getMessage(), true);
			return 0;
		}
	}
	
	public double getIntakeSpeedLeft() {
		try {
			return motorLeft.getSpeed();
		} catch (Exception e) {
			DriverStation.reportError(motorLeft.getDeviceID() + " says " + e.getMessage(), true);
			return 0;
		}
	}
	
	public double getIntakeSpeedRight() {
		try {
			return motorRight.getSpeed();
		} catch (Exception e) {
			DriverStation.reportError(motorRight.getDeviceID() + " says " + e.getMessage(), true);
			return 0;
		}
	}
    
    @Override
    public String getFormattedName() {
    	return "Launcher_";
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}


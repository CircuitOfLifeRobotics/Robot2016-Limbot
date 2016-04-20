package com.team3925.robot2016.subsystems;

import static com.team3925.robot2016.Constants.LAUNCHER_ENCODER_SCALE_FACTOR;
import static com.team3925.robot2016.Constants.LAUNCHER_GLOBAL_POWER;
import static com.team3925.robot2016.Constants.LAUNCHER_MAX_ARM_ANGLE;

import java.util.Timer;
import java.util.TimerTask;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.util.FixedSizeLinkedList;
import com.team3925.robot2016.util.InputWatcher;
import com.team3925.robot2016.util.Loopable;
import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.SynchronousPID;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Subsystem that runs the new launcher
 */
public final class Launcher extends Subsystem implements SmartdashBoardLoggable, Loopable {
	
	private final CANTalon motorArm;
	private final CANTalon motorFar;
	private final CANTalon motorNear;
	
	private final DigitalInput fwdLimitSwitch;
	private final DigitalInput revLimitSwitch;
	
	// TODO implement after testing basics
	private final SynchronousPID pid = new SynchronousPID(Constants.LAUNCHER_PID_K_P, Constants.LAUNCHER_PID_K_I, Constants.LAUNCHER_PID_K_D);
	
	private final DoubleSolenoid puncherSolenoid;
	
	private final InputWatcher encoderWatcher;
	private final Timer encoderWatcherTimer;
	
	
	private double armSetpoint;
	private double motorFarSetpoint;
	private double motorNearSetpoint;
	
	private boolean hasZeroed;
	
	private ZeroLauncher zeroCommand;
	
	
	// DEBUG STUFF
	
	private final TimeoutAction timeoutAction1 = new TimeoutAction();
	private final TimeoutAction timeoutAction2 = new TimeoutAction();
	
	/**
	 * Testing a new way of getting actuators and sensors into a class
	 */
	public Launcher(CANTalon motorArm, CANTalon motorFlywheelFar, CANTalon motorFlywheelNear, DoubleSolenoid puncherSolenoid,
			DigitalInput fwdSwitch, DigitalInput revSwitch) {
		this.motorArm = motorArm;
		this.motorFar = motorFlywheelFar;
		this.motorNear = motorFlywheelNear;
		this.puncherSolenoid = puncherSolenoid;
		this.fwdLimitSwitch = fwdSwitch;
		this.revLimitSwitch = revSwitch;
		
		setHasZeroed(false);
		
		resetSetpoints();

		encoderWatcher = new InputWatcher(Constants.LAUNCHER_ENCODER_WATCHER_DATA_CACHE_SIZE, Constants.LAUNCHER_ENCODER_WATCHER_TOLERANCE,
				new InputWatcher.InputWatcherInput() {
			@Override
			public double getInputWatcherInput() {
				return getArmPosition();
			}
		});
		encoderWatcherTimer = new Timer(getFormattedName() + "EncoderWatcher", true);
		encoderWatcherTimer.scheduleAtFixedRate(encoderWatcher, 0, Constants.LAUNCHER_ENCODER_WATCHER_PERIOD);
	}
	
	public void init() {
		resetSetpoints();
		
		zeroCommand = new ZeroLauncher();
		setHasZeroed(false);
		startZeroCommand();


		// TEST ANGLE SETPOINT
		SmartDashboard.putNumber(getFormattedName() + "MotorArmSetpointSETTER", 0);

		timeoutAction1.config(10d);
		timeoutAction2.config(20d);

		System.out.println("Launcher.init() called");
	}
	
	
	@Override
	public void update() {
		
		setMotorNearSpeed(motorNearSetpoint);
		setMotorFarSpeed(motorFarSetpoint);
		
//		TEST_ANGLE_SETPOINT
			
			// testing getting input from SmartDashboard
		if (hasZeroed()) {
			setArmSetpoint(SmartDashboard.getNumber(getFormattedName() + "MotorArmSetpointSETTER", 0));
			
			double error = getArmPosError();
			
			if (!getAimOnTarget()) {
				final double MULTIPLIER = 0.5;
				
				setMotorArmSpeed(Math.signum(error) * MULTIPLIER /* * Math.min(Math.abs(error/20),1)*/);
				
//				System.out.println(pid.calculate(getArmPosError()));
				System.out.println("MotorArmSpiid " + Math.signum(error) * MULTIPLIER /** Math.min(Math.abs(error/10),1)*/);
			} else {
				setMotorArmSpeed(0);
			}
			System.out.println();
		}
		
		
		logData();
			
	}

	
	@Override
	public void logData() {
		putNumberSD("MotorArmSpeed", motorArm.getSpeed());
		putNumberSD("MotorArmInternalSetpoint", motorArm.get());
		putNumberSD("MotorArmSetpoint", getArmSetpoint());
		putNumberSD("MotorArmEncoderPos", getArmPosition());
		putNumberSD("MotorArmEncoderVel", getArmVelocity());
		putNumberSD("MotorArmError", getArmPosError());
		putBooleanSD("MotorArmOnTarget", getAimOnTarget());
		putBooleanSD("HasZeroed", hasZeroed());
		putBooleanSD("EncoderWatcher", getArmEncoderMoving());
		putBooleanSD("ZeroCommandRunning", (zeroCommand == null) ? false : zeroCommand.isRunning());
		

		putNumberSD("MotorNearSpeed", motorNear.getSpeed());
		putNumberSD("MotorFarSpeed", motorFar.getSpeed());
		putNumberSD("MotorNearSetpoint", motorNearSetpoint);
		putNumberSD("MotorFarSetpoint", motorFarSetpoint);
		
		
		putBooleanSD("FwdLimitSwitch", getFwdLimitSwitch());
		putBooleanSD("RevLimitSwitch", getRevLimitSwitch());
		
	}
	
	
	// PUBLIC SETPOINT METHODS
	
	/**
	 * @param setpoint desired angle in degrees
	 */
	public void setArmSetpoint(double setpoint) {
		if (!Double.isFinite(setpoint)) {
			DriverStation.reportError("Could not set setpoint! Was not a finite number!", false);
			armSetpoint = 0;
		} else {
			armSetpoint = Math.min(LAUNCHER_MAX_ARM_ANGLE, Math.max(0, setpoint));
		}
	}
	
	/**
	 * @param setpoint positive = spinning in
	 */
	public void setFlywheelNearSetpoint(double setpoint) {
		if (!Double.isFinite(setpoint)) {
			DriverStation.reportError("Could not set setpoint! Was not a finite number!", false);
			motorNearSetpoint = 0;
		} else {
			motorNearSetpoint = MiscUtil.limit(setpoint);
		}
	}
	
	/**
	 * @param setpoint positive = spinning in
	 */
	public void setFlywheelFarSetpoint(double setpoint) {
		if (!Double.isFinite(setpoint)) {
			DriverStation.reportError("Could not set setpoint! Was not a finite number!", false);
			motorFarSetpoint = 0;
		} else {
			motorFarSetpoint = MiscUtil.limit(setpoint);
		}
	}
	
	public void resetSetpoints() {
		armSetpoint = 0;
		motorFarSetpoint = 0;
		motorNearSetpoint = 0;
	}
	
	


	// OTHER PUBLIC METHODS
	
	public void setPuncherSolenoidSetpoint(boolean engaged) {
		puncherSolenoid.set(engaged ? Value.kForward:Value.kReverse);
	}
	
	/**
	 * @return true if command was started or false if command was already running/has already run
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
		setHasZeroed(true);
	}
	
	
	
	
	
	
	// PRIVATE SETTERS
	
	void setMotorArmSpeedRaw(double speed) {
		motorArm.set(MiscUtil.limit(speed) * LAUNCHER_GLOBAL_POWER);
	}
	
	private void setMotorArmSpeed(double speed) {
		if (hasZeroed) {
			boolean cantRunMotorDown = (getArmPosition() <= 0 || getFwdLimitSwitch()) && speed < 0;
			boolean cantRunMotorUp = getArmPosition() >= LAUNCHER_MAX_ARM_ANGLE && speed > 0;
			
			putBooleanSD("Can'tRunMotorDown", cantRunMotorDown);
			putBooleanSD("Can'tRunMotorUp", cantRunMotorUp);
			
			if (cantRunMotorDown || cantRunMotorUp) {
//				System.out.println("Arm motor out of bounds. Set motor speed to zero!");
				setMotorArmSpeedRaw(0);
			} else {
				setMotorArmSpeedRaw(speed);
			}
			
		} else {
			DriverStation.reportWarning("LauncherNew has not zeroed! Arm motor speed not set!", false);
		}
	}
	
	private void setMotorNearSpeed(double speed) {
		if (Double.isFinite(speed)) {
			motorNear.set(MiscUtil.limit(speed));
		} else {
			DriverStation.reportError("Could not set flywheel near speed to " + speed, false);
		}
	}
	
	private void setMotorFarSpeed(double speed) {
		if (Double.isFinite(speed)) {
			motorFar.set(MiscUtil.limit(speed));
		} else {
			DriverStation.reportWarning("Could not set flywheel far speed to " + speed, false);
		}
	}
	
	private void setHasZeroed(boolean hasZeroed) {
		this.hasZeroed = hasZeroed;
	}
	
	
	
	// GETTERS
	
	/**
	 * @return boolean: if the arm has performed the zero routine
	 */
	public boolean hasZeroed() {
		return hasZeroed;
	}
	
	private double getArmPosError() {
		return getArmSetpoint() - getArmPosition();
	}

	public boolean getAimOnTarget() {
		return Math.abs(getArmPosError()) < Constants.LAUNCHER_ARM_TOLERANCE && Math.abs(getArmVelocity()) < Constants.LAUNCHER_PID_VELOCITY_TOLERANCE;
	}
	
	private double getArmSetpoint() {
		return armSetpoint;
	}
	
	public double getArmPosition() {
		return LAUNCHER_ENCODER_SCALE_FACTOR * motorArm.getEncPosition();
	}
	
	public double getArmVelocity() {
		return LAUNCHER_ENCODER_SCALE_FACTOR * motorArm.getEncVelocity();
	}
	
	public boolean getArmEncoderMoving() {
		return encoderWatcher.isChanging();
	}
	
	public boolean getFwdLimitSwitch() {
		return fwdLimitSwitch.get();
	}
	
	public boolean getRevLimitSwitch() {
		return revLimitSwitch.get();
	}
	
	@Override
	public String getFormattedName() {
		return "LauncherNew_";
	}
	
	
	
	@Override
	protected void initDefaultCommand() {
	}

}

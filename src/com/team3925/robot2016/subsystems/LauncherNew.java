package com.team3925.robot2016.subsystems;

import static com.team3925.robot2016.Constants.LAUNCHER_NEW_ARM_TOLERANCE;
import static com.team3925.robot2016.Constants.LAUNCHER_NEW_ENCODER_SCALE_FACTOR;
import static com.team3925.robot2016.Constants.LAUNCHER_NEW_ENCODER_WATCHER_DATA_CACHE_SIZE;
import static com.team3925.robot2016.Constants.LAUNCHER_NEW_ENCODER_WATCHER_TOLERANCE;
import static com.team3925.robot2016.Constants.LAUNCHER_NEW_GLOBAL_POWER;
import static com.team3925.robot2016.Constants.LAUNCHER_NEW_MAX_ARM_ANGLE;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.util.Loopable;
import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
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
 * 
 * @author Bryan "atomic_diamond" S
 */
public final class LauncherNew extends Subsystem implements SmartdashBoardLoggable, Loopable {
	
	private final CANTalon motorArm;
	private final CANTalon motorFar;
	private final CANTalon motorNear;
	
	private final DigitalInput fwdLimitSwitch;
	private final DigitalInput revLimitSwitch;
	
	// TODO implement after testing basics
//	private final SynchronousPID pid = new SynchronousPID(LAUNCHER_AIM_KP, LAUNCHER_AIM_KI, LAUNCHER_AIM_KD);
	
	private final DoubleSolenoid puncherSolenoid;
	
	private final EncoderWatcher encoderWatcher;
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
	public LauncherNew(CANTalon motorArm, CANTalon motorFlywheelFar, CANTalon motorFlywheelNear, DoubleSolenoid puncherSolenoid, DigitalInput fwdSwitch, DigitalInput revSwitch) {
		this.motorArm = motorArm;
		this.motorFar = motorFlywheelFar;
		this.motorNear = motorFlywheelNear;
		this.puncherSolenoid = puncherSolenoid;
		this.fwdLimitSwitch = fwdSwitch;
		this.revLimitSwitch = revSwitch;
		
		setHasZeroed(false);
		
		resetSetpoints();
		
		encoderWatcher = new EncoderWatcher();
		encoderWatcherTimer = new Timer(getFormattedName() + "EncoderWatcher", true);
	}
	
	public void init() {
		resetSetpoints();
		zeroCommand = new ZeroLauncher();
		hasZeroed = false;
			
//			TESTING ENCODER WATCHER
			// TODO Move to constructor after implemented and tested
//			encoderWatcherTimer.scheduleAtFixedRate(encoderWatcher, 0, LAUNCHER_NEW_ENCODER_WATCHER_PERIOD);
			
		
//			TEST ANGLE SETPOINT
			SmartDashboard.putNumber(getFormattedName() + "MotorArmSetpointSETTER", 0);
			startZeroCommand();
//			setArmSetpoint(45d);
			
		
//			TESTING ZERO COMMAND
//			boolean zeroWorked = startZeroCommand();
//			System.out.println("startZeroCommand return " + zeroWorked);
//			timeoutAction1.config(10d);
//			timeoutAction2.config(20d);
//			zeroCommand.start();
			
		
//			TESTING DIRECTIONS
//			timeoutAction1.config(1.5d);
//			timeoutAction2.config(3d);
	}
	
	private class ZeroLauncher extends Command {
		
		public ZeroLauncher() {
			super("Zero Command", 3);
			requires(Robot.launcherNew);
		}
		
		@Override
		protected void initialize() {
		}

		@Override
		protected void execute() {
			Robot.launcherNew.setMotorArmSpeedRaw(-.5);
			System.out.println(getName() + " Called Execute");
		}

		@Override
		protected boolean isFinished() {
			return Robot.launcherNew.getFwdLimitSwitch();
		}

		@Override
		protected void end() {
			Robot.launcherNew.setMotorArmSpeedRaw(0);
			Robot.launcherNew.setLauncherZeroed();
			System.out.println("ZeroLauncher has been cancelled");
		}

		@Override
		protected void interrupted() {
			Robot.launcherNew.setMotorArmSpeedRaw(0);
			System.out.println("ZeroLauncher has been cancelled");
		}
		
	}
	
	/**
	 * TODO implement class
	 */
	private class EncoderWatcher extends TimerTask {
		private ArrayBlockingQueue<Double> queue;
		private boolean isMoving;
		
		public EncoderWatcher() {
			queue = new ArrayBlockingQueue<>(LAUNCHER_NEW_ENCODER_WATCHER_DATA_CACHE_SIZE);
		}
		
		public boolean getIsMoving() {
			return isMoving;
		}
		
		@Override
		public void run() {
			isMoving = true;
			
			try {
				queue.put(Robot.launcherNew.getArmPosition());
			} catch (InterruptedException e) {
				DriverStation.reportError(e.getMessage(), true);
			}
			
			if (queue.size() >= LAUNCHER_NEW_ENCODER_WATCHER_DATA_CACHE_SIZE) {
				queue.remove();
			}
		}
		
		private boolean compareValues(double val1, double val2) {
			return Math.abs(val1 - val2) < LAUNCHER_NEW_ENCODER_WATCHER_TOLERANCE;
		}
		
		
	}
	
	@Override
	public void update() {
//		FULL
		/*
			// If arm motor has not zeroed, start zero command
			if (!hasZeroed()) {
				startZeroCommand();
				return;
			}
			
			setMotorNearSpeed(motorNearSetpoint);
			setMotorFarSpeed(motorFarSetpoint);
			*/
			
		
//		TESTING ENCODER WATCHER
			// NOT IMPLEMENTED
			
		
		setMotorNearSpeed(motorNearSetpoint);
		setMotorFarSpeed(motorFarSetpoint);
		
//		TEST_ANGLE_SETPOINT
//			/*
			// testing getting input from SmartDashboard
			//-16000 is shoot angle approximately
			if (hasZeroed) setArmSetpoint(SmartDashboard.getNumber(getFormattedName() + "MotorArmSetpointSETTER", 0));
			
			// should PID be implemented?
			double error = armSetpoint - getArmPosition();
			System.out.println("Diff = "+Math.abs(error));
			if (Math.abs(error) > LAUNCHER_NEW_ARM_TOLERANCE && hasZeroed) {
				setMotorArmSpeed(Math.signum(error) * 0.3 * Math.min(Math.abs(error/20),1));
				System.out.println("MotorArmSpiid "+Math.signum(error) * 0.3 /** Math.min(Math.abs(error/10),1)*/);
			}
//			*/
			
			
//		TESTING_ZERO_COMMAND
		/*
			// launcher zero command has already been started in init()
			
			if (hasZeroed()) {
				if (!timeoutAction2.isFinished()) {
					if (!timeoutAction1.isFinished()) {
						setMotorArmSpeed(.2d);
						putStringSD("CurrentDirection", "Positive");
					} else {
						setMotorArmSpeed(-.2d);
						putStringSD("CurrentDirection", "Negative");
					}
				} else {
					setMotorArmSpeed(0d);
					putStringSD("CurrentDirection", "None");
				}
			}
			*/
			
		
//		TESTING_DIRECTIONS (done)
		/*
			if (!timeoutAction2.isFinished()) {
				if (!timeoutAction1.isFinished()) {
					setMotorArmSpeedRaw(0.3);
					setMotorFarSpeed(0.5);
					setMotorNearSpeed(0.5);
					putStringSD("CurrentDirection", "Positive");
				} else {
					setMotorArmSpeedRaw(-0.3);
					setMotorFarSpeed(-0.5);
					setMotorNearSpeed(-0.5);
					putStringSD("CurrentDirection", "Negative");
				}
			} else {
				setMotorArmSpeedRaw(0);
				setMotorFarSpeed(0);
				setMotorNearSpeed(0);
				putStringSD("CurrentDirection", "None");
			}
			*/
		
		logData();
			
	}
	
	
	@Override
	public void logData() {
		putNumberSD("Debug_Timeout1_TimeRemaining", timeoutAction1.getTimeRemaining());
		putNumberSD("Debug_Timeout2_TimeRemaining", timeoutAction2.getTimeRemaining());
		
		putNumberSD("MotorArmSpeed", motorArm.getSpeed());
		putNumberSD("MotorArmInternalSetpoint", motorArm.get());
		putNumberSD("MotorArmSetpoint", armSetpoint);
		putNumberSD("MotorArmEncoderPos", getArmPosition());

		putNumberSD("MotorNearSpeed", motorNear.getSpeed());
		putNumberSD("MotorFarSpeed", motorFar.getSpeed());
		putNumberSD("MotorNearSetpoint", motorNearSetpoint);
		putNumberSD("MotorFarSetpoint", motorFarSetpoint);
		
		putBooleanSD("HasZeroed", hasZeroed());
		putBooleanSD("EncoderWatcher", getArmEncoderMoving());
		try {putBooleanSD("ZeroCommandRunning", zeroCommand.isRunning());}
		catch (Exception e) {DriverStation.reportError("Cannot call a method on a null command!", false);}
		
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
			armSetpoint = Math.min(LAUNCHER_NEW_MAX_ARM_ANGLE, Math.max(0, setpoint));
		}
	}
	
	public void setFlywheelNearSetpoint(double setpoint) {
		if (!Double.isFinite(setpoint)) {
			DriverStation.reportError("Could not set setpoint! Was not a finite number!", false);
			motorNearSetpoint = 0;
		} else {
			motorNearSetpoint = MiscUtil.limit(setpoint);
		}
	}
	
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
	
	public void setPuncherSolenoid(boolean engaged) {
		puncherSolenoid.set(engaged ? Value.kForward:Value.kReverse);
	}
	
	/**
	 * @return true if command was started or false if command was already running/has already run
	 */
	public boolean startZeroCommand() {
		if (!zeroCommand.isRunning() && !hasZeroed /*true*/) {
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
	
	private void setMotorArmSpeedRaw(double speed) {
		motorArm.set(MiscUtil.limit(speed) * LAUNCHER_NEW_GLOBAL_POWER);
	}
	
	private void setMotorArmSpeed(double speed) {
		if (hasZeroed) {
			boolean cantRunMotorDown = (getArmPosition() <= 0 && speed < 0) || getFwdLimitSwitch();
			boolean cantRunMotorUp = getArmPosition() >= LAUNCHER_NEW_MAX_ARM_ANGLE && speed > 0;
			
			if (cantRunMotorDown || cantRunMotorUp) {
				setMotorArmSpeedRaw(0);
			} else {
				setMotorArmSpeedRaw(armSetpoint);
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
	
	public double getArmPosition() {
		return LAUNCHER_NEW_ENCODER_SCALE_FACTOR * motorArm.getEncPosition();
	}
	
	public boolean getArmEncoderMoving() {
		return encoderWatcher.getIsMoving();
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

package com.team3925.robot2016.subsystems;

import static com.team3925.robot2016.Constants.LAUNCHER_NEW_ENCODER_SCALE_FACTOR;
import static com.team3925.robot2016.Constants.LAUNCHER_NEW_GLOBAL_POWER;
import static com.team3925.robot2016.Constants.LAUNCHER_NEW_MAX_ARM_ANGLE;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.util.Loopable;
import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem that runs the new launcher
 * 
 * @author Bryan "atomic_diamond" S
 */
public final class LauncherNew extends Subsystem implements SmartdashBoardLoggable, Loopable {
	
	private final CANTalon motorArm;
	private final CANTalon motorFar;
	private final CANTalon motorNear;
	
	// TODO implement after testing basics
//	private final SynchronousPID pid = new SynchronousPID(LAUNCHER_AIM_KP, LAUNCHER_AIM_KI, LAUNCHER_AIM_KD);
	
	private final DigitalInput limitSwitch;
	private final DoubleSolenoid puncherSolenoid;
	
	private final EncoderWatcher encoderWatcher;
	private final Timer encoderWatcherTimer;
	
	private double armSetpoint;
	private double motorFarSetpoint;
	private double motorNearSetpoint;
	
	private boolean hasZeroed = false;
	
	private final ZeroLauncher zeroCommand;
	
	
	// DEBUG STUFF
	
	private final TimeoutAction timeoutAction1 = new TimeoutAction();
	private final TimeoutAction timeoutAction2 = new TimeoutAction();
	
	
	/**
	 * Testing a new way of getting actuators and sensors into a class
	 */
	public LauncherNew(CANTalon motorArm, CANTalon motorFlywheelFar, CANTalon motorFlywheelNear, DigitalInput limitSwitch, DoubleSolenoid puncherSolenoid) {
		this.motorArm = motorArm;
		this.motorFar = motorFlywheelFar;
		this.motorNear = motorFlywheelNear;
		this.limitSwitch = limitSwitch;
		this.puncherSolenoid = puncherSolenoid;
		
		setHasZeroed(false);
		
		resetSetpoints();
		
		zeroCommand = new ZeroLauncher();
		encoderWatcher = new EncoderWatcher();
		encoderWatcherTimer = new Timer(getFormattedName() + "EncoderWatcher", true);
	}
	
	public void init() {
		resetSetpoints();
			
//			TESTING ENCODER WATCHER
			// TODO Move to constructor after implemented and tested
//			encoderWatcherTimer.scheduleAtFixedRate(encoderWatcher, 0, Constants.LAUNCHER_NEW_ENCODER_WATCHER_PERIOD);
//			timeoutAction1.config(-1d);
//			timeoutAction2.config(-1d);
			
		
//			TEST ANGLE SETPOINT
//			startZeroCommand();
//			setArmSetpoint(45d);
//			timeoutAction1.config(-1d);
//			timeoutAction2.config(-1d);
			
		
//			TESTING ZERO COMMAND
//			startZeroCommand();
//			timeoutAction1.config(10d);
//			timeoutAction2.config(20d);

		
//			TESTING DIRECTIONS
//			timeoutAction1.config(1.5d);
//			timeoutAction2.config(3d);
	}
	
	private class ZeroLauncher extends Command {
		
		@Override
		protected void initialize() {
		}

		@Override
		protected void execute() {
			setMotorArmSpeedRaw(-.25);
		}

		@Override
		protected boolean isFinished() {
			return getLimitSwitch();
		}

		@Override
		protected void end() {
			setMotorArmSpeedRaw(0);
			setLauncherZeroed();
		}

		@Override
		protected void interrupted() {
			setMotorArmSpeedRaw(0);
		}
		
	}
	
	/**
	 * TODO implement class
	 */
	private static class EncoderWatcher extends TimerTask {
		private ArrayBlockingQueue<Double> queue;
		private boolean isMoving;
		
		public EncoderWatcher() {
			queue = new ArrayBlockingQueue<>(Constants.LAUNCHER_NEW_ENCODER_WATCHER_DATA_CACHE_SIZE);
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
			
			if (queue.size() >= Constants.LAUNCHER_NEW_ENCODER_WATCHER_DATA_CACHE_SIZE) {
				queue.remove();
			}
		}
		
		private boolean compareValues(double val1, double val2) {
			return Math.abs(val1 - val2) < Constants.LAUNCHER_NEW_ENCODER_WATCHER_TOLERANCE;
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
			
			
//		TEST_ANGLE_SETPOINT
			/*
			// testing getting input from SmartDashboard
			setArmSetpoint(SmartDashboard.getNumber(getFormattedName() + "MotorArmSetpointSETTER", 0));
			
			// should PID be implemented?
			if (Math.abs(armSetpoint - getArmPosition()) > LAUNCHER_NEW_ARM_TOLERANCE) {
				setMotorArmSpeed(Math.signum(armSetpoint) * 0.5);
			}
			*/
			
			
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
			
		
//		TESTING_DIRECTIONS
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
		putNumberSD("MotorArmSetpoint", armSetpoint);
		putNumberSD("MotorArmEncoderPos", getArmPosition());

		putNumberSD("MotorNearSpeed", motorNear.getSpeed());
		putNumberSD("MotorFarSpeed", motorFar.getSpeed());
		putNumberSD("MotorNearSetpoint", motorNearSetpoint);
		putNumberSD("MotorFarSetpoint", motorFarSetpoint);
		
		putBooleanSD("LimitSwitch", getLimitSwitch());
		putBooleanSD("HasZeroed", hasZeroed());
		putBooleanSD("EncoderWatcher", getArmEncoderMoving());
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
		if (zeroCommand.isRunning() == hasZeroed == false) {
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
			boolean cantRunMotorDown = (getArmPosition() <= 0 && speed < 0) || getLimitSwitch();
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
	
	public boolean getLimitSwitch() {
		return limitSwitch.get();
	}
	
	public boolean getArmEncoderMoving() {
		return encoderWatcher.getIsMoving();
	}
	
	@Override
	public String getFormattedName() {
		return "LauncherNew_";
	}
	
	
	
	@Override
	protected void initDefaultCommand() {
	}

}

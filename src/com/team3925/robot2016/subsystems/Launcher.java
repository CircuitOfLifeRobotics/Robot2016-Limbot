package com.team3925.robot2016.subsystems;

import static com.team3925.robot2016.Constants.LAUNCHER_ARM_TOLERANCE;
import static com.team3925.robot2016.Constants.LAUNCHER_ENCODER_SCALE_FACTOR;
import static com.team3925.robot2016.Constants.LAUNCHER_GLOBAL_POWER;
import static com.team3925.robot2016.Constants.LAUNCHER_MAX_ARM_ANGLE;

import java.awt.Robot;
import java.util.Timer;
import java.util.TimerTask;

import com.team3925.robot2016.Constants;
//import com.team3925.robot2016.Robot;
import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.util.Loopable;
import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;
import com.team3925.robot2016.util.hidhelpers.XboxHelper;

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
public final class Launcher extends Subsystem implements SmartdashBoardLoggable, Loopable {
	
	
	private final CANTalon motorArm;
	private final CANTalon motorFar;
	private final CANTalon motorNear;
	
	private final DigitalInput fwdLimitSwitch;
	private final DigitalInput revLimitSwitch;
	
	// TODO implement after testing basics
	//encoderWatcherTimer.scheduleAtFixedRate(encoderWatcher, 0, Constants.LAUNCHER_ENCODER_WATCHER_PERIOD);

//	private final SynchronousPID pid = new SynchronousPID(LAUNCHER_AIM_KP, LAUNCHER_AIM_KI, LAUNCHER_AIM_KD);
	
	private final DoubleSolenoid puncherSolenoid;
	
	private final EncoderWatcher encoderWatcher;
	private final Timer encoderWatcherTimer;
	
	
	
	private double armSetpoint;
	private double motorFarSetpoint;
	private double motorNearSetpoint;
	
	private boolean hasZeroed;
	
//	private ZeroLauncher zeroCommand;
	
	
	// DEBUG STUFF
	
	private final TimeoutAction timeoutAction1 = new TimeoutAction();
	private final TimeoutAction timeoutAction2 = new TimeoutAction();
	private final TimeoutAction launcherSpinupTime = new TimeoutAction();
	private final TimeoutAction launchSequenceTimeout = new TimeoutAction();
	
	RobotMap robotmap = new RobotMap();
	
	/**
	 * Testing a new way of getting actuators and sensors into a class
	 */
	public Launcher(CANTalon motorArm, CANTalon motorFlywheelFar, CANTalon motorFlywheelNear, DoubleSolenoid puncherSolenoid, DigitalInput fwdSwitch, DigitalInput revSwitch) {
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
//		zeroCommand = new ZeroLauncher();
		hasZeroed = false;
		
			
			
		// TODO Move to constructor after implemented and tested
		
//			TEST ANGLE SETPOINT
		SmartDashboard.putNumber(getFormattedName() + "MotorArmSetpointSETTER", 0);
		startZeroCommand();
			
		
//			TESTING ZERO COMMAND
//			boolean zeroWorked = startZeroCommand();
//			System.out.println("startZeroCommand return " + zeroWorked);
			timeoutAction1.config(10d);
			timeoutAction2.config(20d);
			launcherSpinupTime.config(Constants.LAUNCHER_WHEEL_SPIN_UP_TIME);
			
		
//			TESTING DIRECTIONS
//			timeoutAction1.config(1.5d);
//			timeoutAction2.config(3d);
	}
	
	/*private class ZeroLauncher extends Command {
		private boolean waitStarted;
		private final TimeoutAction timeout = new TimeoutAction();
		
		/*public ZeroLauncher() {
			super("Zero Command", 3);
			requires(robotmap.launcherNew);
			waitStarted = false;
		}
		
		@Override
		protected void initialize() {
		}

		@Override
		/*protected void execute() {
			Robot.launcherNew.setMotorArmSpeedRaw(-.5);
			System.out.println(getName() + " Called Execute");
			
			if (!waitStarted && Robot.launcherNew.getFwdLimitSwitch()) {
				timeout.config(Constants.LAUNCHER_ZERO_COMMAND_WAIT);
				waitStarted = true;
			}
		}

		@Override
		protected boolean isFinished() {
			return timeout.isFinished();
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
		
	}*/
	
	/**
	 * TODO implement class
	 */
	private class EncoderWatcher extends TimerTask {
		private boolean isMoving;
		
		public EncoderWatcher() {
		}
		
		public boolean getIsMoving() {
			return isMoving;
		}
		
		@Override
		public void run() {
			isMoving = true;
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
			
			// testing getting input from SmartDashboard
			//-16000 is shoot angle approximately
//			if (hasZeroed) setArmSetpoint(SmartDashboard.getNumber(getFormattedName() + "MotorArmSetpointSETTER", 0));
//			
//			double error = armSetpoint - getArmPosition();
//			System.out.println("Diff = "+Math.abs(error));
//			if (Math.abs(error) > LAUNCHER_NEW_ARM_TOLERANCE && hasZeroed) {
//				setMotorArmSpeed(Math.signum(error) * 0.3 * Math.min(Math.abs(error/20),1));
//				System.out.println("MotorArmSpiid "+Math.signum(error) * 0.3 /** Math.min(Math.abs(error/10),1)*/);
//			}
			
			
			
//		TESTING_ZERO_COMMAND
//		/*

//		if (hasZeroed()) {
//			if (!timeoutAction2.isFinished()) {
//				if (!timeoutAction1.isFinished()) {
//					setMotorArmSpeed(.2d);
//					putStringSD("CurrentDirection", "Positive");
//				} else {
//					setMotorArmSpeed(-.2d);
//					putStringSD("CurrentDirection", "Negative");
//				}
//			} else {
//				setMotorArmSpeed(0d);
//				putStringSD("CurrentDirection", "None");
//			}
//		}
//			*/
			
		
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
		//try {putBooleanSD("ZeroCommandRunning", zeroCommand.isRunning());}
		//catch (Exception e) {DriverStation.reportError("Cannot call a method on a null command!", false);}
		
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
	
	public void setPuncherSolenoid(boolean engaged) {
		RobotMap.launcherPuncherSolenoid.set(engaged ? Value.kForward:Value.kReverse);
	}
	
	/**
	 * @return true if command was started or false if command was already running/has already run
	 */
	public boolean startZeroCommand() {
		/*if (!zeroCommand.isRunning() && !hasZeroed /*true) {
			zeroCommand.start();
			return true;
		} else {
			return false;
		}
		*/
		return false;
	}
	
	public void setLauncherZeroed() {
		motorArm.setEncPosition(0);
		setHasZeroed(true);
	}
	
	
	
	
	
	// PRIVATE SETTERS
	
	private void setMotorArmSpeedRaw(double speed) {
		motorArm.set(MiscUtil.limit(speed) * LAUNCHER_GLOBAL_POWER);
	}
	
	private void setMotorArmSpeed(double speed) {
		if (hasZeroed) {
			boolean cantRunMotorDown = (getArmPosition() <= 0 && speed < 0) || getFwdLimitSwitch();
			boolean cantRunMotorUp = getArmPosition() >= LAUNCHER_MAX_ARM_ANGLE && speed > 0;
			
			if (cantRunMotorDown || cantRunMotorUp) {
				setMotorArmSpeedRaw(0);
			} else {
				setMotorArmSpeedRaw(armSetpoint);
			}
			
		} else {
			//DriverStation.reportWarning("LauncherNew has not zeroed! Arm motor speed not set!", false);
		}
	}
	
	private void setMotorNearSpeed(double speed) {
//		if (Double.isFinite(speed)) {
//			motorNear.set(MiscUtil.limit(speed));
//		} else {
//			DriverStation.reportError("Could not set flywheel near speed to " + speed, false);
//		}
		RobotMap.launcherMotorNear.set(speed);
	}
	
	private void setMotorFarSpeed(double speed) {
		RobotMap.launcherMotorFar.set(speed);
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
		return LAUNCHER_ENCODER_SCALE_FACTOR * motorArm.getEncPosition();
	}
	
	public boolean getArmEncoderMoving() {
		return encoderWatcher.getIsMoving();
	}
	
	public boolean getFwdLimitSwitch() {
		return RobotMap.launcherFwdLimitSwitch.get();
	}
	
	public boolean getRevLimitSwitch() {
		return RobotMap.launcherRevLimitSwitch.get();
	}
	
	@Override
	public String getFormattedName() {
		return "LauncherNew_";
	}
	
	@Override
	protected void initDefaultCommand() {
	}
	/*
	 * Launcher Home Logic
	 * 
	 * 
	 * 1: Save encoder value for reference
	 * 2: Set Speeds slowly down (-0.3 or so)
	 * 3: Wait till fwd_switch is triggered or timeout
	 * 4: Shut off Aim Motor
	 */
	
	
	/* Normal Lauching sequence 
	 * 	
	 * 1: Set the piston to reverse
	 * 2: ****Set motors backwards slowly to knock the ball back into the trough (about 0.5 seconds)****NOT working(OVERHEATING MOTOR)
	 * 3: Spin up the motors
	 * 4: Simulataneously raise the arm for vertical aim sequence (Check for EncoderWatcher)
	 * 5: Wait for arm to finish occisalting (1 second or so)
	 * 6: Retract the piston and launch the ball.
	 * 
	 * 7: Reverse the piston and stop the launcher motors
	 * 8: Set the Arm angle to the travel position
	 * 
	 * 
	 */
	
	
	/* Aim Sequence
	 * Starts with target in frame
	 * 
	 * 1: Pixy code supplies offset angle
	 * 2: Drivetrain will adjust to offset angle. 
	 * 3: Get Pixy offset angle
	 * 4: If offset is outside deadzone, turn robot again
	 * 5: Get Ultrasound to supply range to target
	 * 6: If distance is too far, send message to driverstation and cancel the command
	 * 7: Else use distance to compute launching angle (3+4, 5+6 happen simultaneously per Mike)
	 * 8: Start Launching Sequence
	 * 
	 */
	double RunOnce = 0;
	public void LaunchSequence(){
		if (RunOnce == 0){	//make sure the config only runs once
			launcherSpinupTime.config(Constants.LAUNCHER_WHEEL_SPIN_UP_TIME);	//configure the spinup timer
			launchSequenceTimeout.config(Constants.LAUNCHER_WHEEL_SPIN_UP_TIME + Constants.LAUNCHER_FIRE_TIME);	//configure the timeout timer
			RunOnce ++;		//add one to RunOnce so this if statement never runs again
		}
		RobotMap.launcherPuncherSolenoid.set(Value.kReverse); //set solenoid backwards
		setMotorNearSpeed(-Constants.LAUNCHER_GLOBAL_POWER); //spin up the wheels
		setMotorFarSpeed(-Constants.LAUNCHER_GLOBAL_POWER);			
		if (launcherSpinupTime.isFinished()){				//wait for the spinup time timer to expire (lets the wheels get up to speed)
			RobotMap.launcherPuncherSolenoid.set(Value.kForward); //set the solenoid forward
			if (launchSequenceTimeout.isFinished()){	//wait for the timeout to expire (to make sure ball has left the wheels)
				setMotorFarSpeed(0);	//kill the motors
				setMotorNearSpeed(0);
			}
		}
	
		
			
		
	}
	
	public boolean IsFarWorking(){
		boolean isWork = false;
		while(isWork = false){
			setMotorFarSpeed(1);
			if (XboxHelper.getShooterButton(1)){
				setMotorFarSpeed(0);
				isWork = true;
			}
		}
		return isWork;
	}
	
	public boolean IsNearWorking(){
		boolean isWork = false;
		while(isWork = false){
			setMotorNearSpeed(1);
			if (XboxHelper.getShooterButton(1)){
				setMotorNearSpeed(0);
				isWork = true;
			}
		}
		return isWork;
	}
	public boolean IsAimWorking() {
		boolean isAimWork = false;
		while (isAimWork = false){
			setArmSetpoint(45);
			if (XboxHelper.getShooterButton(1)){
				isAimWork = true;
			}
		}
		return isAimWork;
	}
	public boolean IsLauncherPistonWorking(){
		boolean isAimWork = false;
		boolean isAimWork2 = false;
		while (isAimWork = false){
			robotmap.launcherPuncherSolenoid.set(Value.kForward);
			if (XboxHelper.getShooterButton(1) && !isAimWork2){
				RobotMap.launcherPuncherSolenoid.set(Value.kReverse);
				isAimWork2 = true;
			}
			if (isAimWork2 && XboxHelper.getShooterButton(1)){
				RobotMap.launcherPuncherSolenoid.set(Value.kOff);
				isAimWork = true;
			}
		}
		return isAimWork;
	}
	
	
}

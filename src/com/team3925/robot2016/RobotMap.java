package com.team3925.robot2016;

import static com.team3925.robot2016.Constants.DELTA_TIME;
import static com.team3925.robot2016.Constants.DRIVETRAIN_ENCODER_FACTOR;
import static com.team3925.robot2016.Constants.DRIVETRAIN_LEFT_KD;
import static com.team3925.robot2016.Constants.DRIVETRAIN_LEFT_KI;
import static com.team3925.robot2016.Constants.DRIVETRAIN_LEFT_KP;
import static com.team3925.robot2016.Constants.DRIVETRAIN_ON_TARGET_ERROR;
import static com.team3925.robot2016.Constants.DRIVETRAIN_RIGHT_KD;
import static com.team3925.robot2016.Constants.DRIVETRAIN_RIGHT_KI;
import static com.team3925.robot2016.Constants.DRIVETRAIN_RIGHT_KP;

import com.ni.vision.NIVision.CalibrationThumbnailType;
import com.team3925.robot2016.util.CheesySpeedController;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
    private static SpeedController driveTrainMotorLeftA;
    private static SpeedController driveTrainMotorLeftB;
    private static SpeedController driveTrainMotorLeftC;
    private static SpeedController driveTrainMotorRightA;
    private static SpeedController driveTrainMotorRightB;
    private static SpeedController driveTrainMotorRightC;
	public static CheesySpeedController driveTrainMotorsLeft;
	public static CheesySpeedController driveTrainMotorsRight;
    public static Encoder driveTrainEncoderLeft;
    public static Encoder driveTrainEncoderRight;
    public static DoubleSolenoid driveTrainShifterSolenoid;
    public static PIDController driveTrainPIDLeft;
    public static PIDController driveTrainPIDRight;

    public static CANTalon launcherMotorAim;
    public static CANTalon launcherMotorLeft;
    public static CANTalon launcherMotorRight;
    public static DoubleSolenoid launcherPuncherSolenoid;
    
    public static PowerDistributionPanel pdp;

    public static void init() {
    	
    	boolean invertLeft = true;
    	boolean invertRight = false;
    	
        driveTrainMotorLeftA = new Talon(3);
        LiveWindow.addActuator("DriveTrain", "MotorLeftA", (Talon) driveTrainMotorLeftA);
        driveTrainMotorLeftA.setInverted(invertLeft);
        
        driveTrainMotorLeftB = new Talon(4);
        LiveWindow.addActuator("DriveTrain", "MotorLeftB", (Talon) driveTrainMotorLeftB);
        driveTrainMotorLeftB.setInverted(invertLeft);
        
        driveTrainMotorLeftC = new Talon(5);
        LiveWindow.addActuator("DriveTrain", "MotorLeftC", (Talon) driveTrainMotorLeftC);
        driveTrainMotorLeftC.setInverted(invertLeft);
        
        driveTrainMotorRightA = new Talon(0);
        LiveWindow.addActuator("DriveTrain", "MotorRightA", (Talon) driveTrainMotorRightA);
        driveTrainMotorRightA.setInverted(invertRight);
        
        driveTrainMotorRightB = new Talon(1);
        LiveWindow.addActuator("DriveTrain", "MotorRightB", (Talon) driveTrainMotorRightB);
        driveTrainMotorRightB.setInverted(invertRight);
        
        driveTrainMotorRightC = new Talon(2);
        LiveWindow.addActuator("DriveTrain", "MotorRightC", (Talon) driveTrainMotorRightC);
        driveTrainMotorRightB.setInverted(invertRight);
       
    	int[] pdpLeft = { 1, 2, 3 };
    	int[] pdpRight = { 4, 5, 6 };
        SpeedController[] leftMotors = { driveTrainMotorLeftA, driveTrainMotorLeftB, driveTrainMotorLeftC };
        SpeedController[] rightMotors = { driveTrainMotorRightA, driveTrainMotorRightB, driveTrainMotorRightC };
        
    	driveTrainMotorsLeft = new CheesySpeedController(leftMotors, pdpLeft);
    	driveTrainMotorsRight = new CheesySpeedController(rightMotors, pdpRight);
    	
        driveTrainEncoderLeft = new Encoder(0, 1, false, EncodingType.k4X);
        LiveWindow.addSensor("DriveTrain", "EncoderLeft", driveTrainEncoderLeft);
        driveTrainEncoderLeft.setDistancePerPulse(DRIVETRAIN_ENCODER_FACTOR);
        driveTrainEncoderLeft.setPIDSourceType(PIDSourceType.kRate);
        
        driveTrainEncoderRight = new Encoder(2, 3, true, EncodingType.k4X);
        LiveWindow.addSensor("DriveTrain", "EncoderRight", driveTrainEncoderRight);
        driveTrainEncoderRight.setDistancePerPulse(DRIVETRAIN_ENCODER_FACTOR);
        driveTrainEncoderRight.setPIDSourceType(PIDSourceType.kRate);
        
        driveTrainShifterSolenoid = new DoubleSolenoid(0, 1);
        LiveWindow.addActuator("DriveTrain", "ShifterSolenoid", driveTrainShifterSolenoid);
        
        driveTrainPIDLeft = new PIDController(DRIVETRAIN_LEFT_KP, DRIVETRAIN_LEFT_KI,
        		DRIVETRAIN_LEFT_KD, driveTrainEncoderLeft, driveTrainMotorsLeft, DELTA_TIME / 4);
        LiveWindow.addActuator("DriveTrain", "PIDLeft", driveTrainPIDLeft);
        driveTrainPIDLeft.setAbsoluteTolerance(DRIVETRAIN_ON_TARGET_ERROR);
        
        driveTrainPIDRight = new PIDController(DRIVETRAIN_RIGHT_KP, DRIVETRAIN_RIGHT_KI,
        		DRIVETRAIN_RIGHT_KD, driveTrainEncoderRight, driveTrainMotorsRight, DELTA_TIME / 4);
        LiveWindow.addActuator("DriveTrain", "PIDRight", driveTrainPIDRight);
        driveTrainPIDLeft.setAbsoluteTolerance(DRIVETRAIN_ON_TARGET_ERROR);
        
        
        
        launcherPuncherSolenoid = new DoubleSolenoid(3, 2);
        LiveWindow.addActuator("Launcher", "PuncherSolenoid", launcherPuncherSolenoid);
        
        launcherMotorAim = new CANTalon(2);
        LiveWindow.addActuator("Launcher", "AimMotor", launcherMotorAim);
        launcherMotorAim.setFeedbackDevice(FeedbackDevice.PulseWidth);
        launcherMotorAim.changeControlMode(TalonControlMode.MotionProfile); //TODO change to position and do code to make it work
        launcherMotorAim.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
//        launcherMotorAim.reverseSensor(true); doesn't work, sensor value is still negative
        
        launcherMotorLeft = new CANTalon(0);
        LiveWindow.addActuator("Launcher", "MotorLeft", launcherMotorLeft);
        launcherMotorLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        launcherMotorLeft.changeControlMode(TalonControlMode.PercentVbus); //TODO check if we want to use speed
        launcherMotorLeft.setInverted(false);
        
        launcherMotorRight = new CANTalon(1);
        LiveWindow.addActuator("Launcher", "MotorRight", launcherMotorRight);
        launcherMotorRight.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        launcherMotorRight.changeControlMode(TalonControlMode.PercentVbus);
//        launcherMotorRight.setInverted(true);
//        TODO check if this the correct motor to invert
//        TODO add PID to CANTalons
        
        pdp = new PowerDistributionPanel();
    }
}

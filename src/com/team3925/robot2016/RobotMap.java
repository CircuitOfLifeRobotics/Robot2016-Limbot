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
	
    public static CANTalon driveTrainMotorLeftA;
    public static CANTalon driveTrainMotorLeftB;
    public static CANTalon driveTrainMotorLeftC;
    public static CANTalon driveTrainMotorRightA;
    public static CANTalon driveTrainMotorRightB;
    public static CANTalon driveTrainMotorRightC;
    public static Encoder driveTrainEncoderLeft;
    public static Encoder driveTrainEncoderRight;
    public static DoubleSolenoid driveTrainShifterSolenoid;
    public static PIDController driveTrainPIDLeft;
    public static PIDController driveTrainPIDRight;
    
    public static CANTalon launcherMotorAim;
    public static CANTalon launcherMotorLeft;
    public static CANTalon launcherMotorRight;
    public static DoubleSolenoid launcherPuncherSolenoid;
    
    public static DoubleSolenoid armsPlexiSolenoid;
    public static DoubleSolenoid armsCandyCaneSolenoid;
    public static CANTalon armsMotorClimb;
    
    public static PowerDistributionPanel pdp;
    
    public static void init() {
    	
    	boolean invertLeft = true;
    	boolean invertRight = false;
    	
        driveTrainMotorLeftA = new CANTalon(30);
        LiveWindow.addActuator("DriveTrain", "MotorLeftA", driveTrainMotorLeftA);
        driveTrainMotorLeftA.setInverted(invertLeft);
        driveTrainMotorLeftA.changeControlMode(TalonControlMode.PercentVbus);
        
        driveTrainMotorLeftB = new CANTalon(29);
        LiveWindow.addActuator("DriveTrain", "MotorLeftB", driveTrainMotorLeftB);
        driveTrainMotorLeftB.setInverted(invertLeft);
        driveTrainMotorLeftB.changeControlMode(TalonControlMode.Follower);
        driveTrainMotorLeftB.set(driveTrainMotorLeftA.getDeviceID());
        
        driveTrainMotorLeftC = new CANTalon(28);
        LiveWindow.addActuator("DriveTrain", "MotorLeftC", driveTrainMotorLeftC);
        driveTrainMotorLeftC.setInverted(invertLeft);
        driveTrainMotorLeftC.changeControlMode(TalonControlMode.Follower);
        driveTrainMotorLeftC.set(driveTrainMotorLeftA.getDeviceID());
        
        driveTrainMotorRightA = new CANTalon(27);
        LiveWindow.addActuator("DriveTrain", "MotorRightA", driveTrainMotorRightA);
        driveTrainMotorRightA.setInverted(invertRight);
        driveTrainMotorRightA.changeControlMode(TalonControlMode.PercentVbus);
        
        driveTrainMotorRightB = new CANTalon(26);
        LiveWindow.addActuator("DriveTrain", "MotorRightB", driveTrainMotorRightB);
        driveTrainMotorRightB.setInverted(invertRight);
        driveTrainMotorRightB.changeControlMode(TalonControlMode.Follower);
        driveTrainMotorRightB.set(driveTrainMotorRightA.getDeviceID());
        
        driveTrainMotorRightC = new CANTalon(25);
        LiveWindow.addActuator("DriveTrain", "MotorRightC", driveTrainMotorRightC);
        driveTrainMotorRightC.setInverted(invertRight);
        driveTrainMotorRightC.changeControlMode(TalonControlMode.Follower);
        driveTrainMotorRightC.set(driveTrainMotorRightA.getDeviceID());
       
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
        		DRIVETRAIN_LEFT_KD, driveTrainEncoderLeft, driveTrainMotorLeftA, DELTA_TIME / 4);
        LiveWindow.addActuator("DriveTrain", "PIDLeft", driveTrainPIDLeft);
        driveTrainPIDLeft.setAbsoluteTolerance(DRIVETRAIN_ON_TARGET_ERROR);
        
        driveTrainPIDRight = new PIDController(DRIVETRAIN_RIGHT_KP, DRIVETRAIN_RIGHT_KI,
        		DRIVETRAIN_RIGHT_KD, driveTrainEncoderRight, driveTrainMotorRightA, DELTA_TIME / 4);
        LiveWindow.addActuator("DriveTrain", "PIDRight", driveTrainPIDRight);
        driveTrainPIDLeft.setAbsoluteTolerance(DRIVETRAIN_ON_TARGET_ERROR);
        
        
        
        launcherPuncherSolenoid = new DoubleSolenoid(3, 2);
        LiveWindow.addActuator("Launcher", "PuncherSolenoid", launcherPuncherSolenoid);
        
        launcherMotorAim = new CANTalon(21);
        LiveWindow.addActuator("Launcher", "AimMotor", launcherMotorAim);
        launcherMotorAim.setFeedbackDevice(FeedbackDevice.PulseWidth);
        launcherMotorAim.changeControlMode(TalonControlMode.PercentVbus);
        launcherMotorAim.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
        launcherMotorAim.reverseSensor(true);
//        launcherMotorAim.reverseSensor(true); doesn't work, sensor value is still negative
        
        launcherMotorLeft = new CANTalon(22);
        LiveWindow.addActuator("Launcher", "MotorLeft", launcherMotorLeft);
        launcherMotorLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        launcherMotorLeft.changeControlMode(TalonControlMode.PercentVbus);
        launcherMotorLeft.setInverted(true);
        launcherMotorLeft.configEncoderCodesPerRev(4096);
        
        launcherMotorRight = new CANTalon(23);
        LiveWindow.addActuator("Launcher", "MotorRight", launcherMotorRight);
        launcherMotorRight.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        launcherMotorRight.changeControlMode(TalonControlMode.PercentVbus);
        launcherMotorRight.configEncoderCodesPerRev(4096);
        
        armsPlexiSolenoid = new DoubleSolenoid(4, 5);
        //TODO: correct ports for candy cane solenoid
        armsCandyCaneSolenoid = new DoubleSolenoid(10000,10000);
        
        armsMotorClimb = new CANTalon(24);
        LiveWindow.addActuator("Arms", "MotorClimb", armsMotorClimb);
        armsMotorClimb.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        armsMotorClimb.changeControlMode(TalonControlMode.PercentVbus);
        armsMotorClimb.configEncoderCodesPerRev(4096);
        
//        compressor = new Compressor();
        
        pdp = new PowerDistributionPanel();
    }
}

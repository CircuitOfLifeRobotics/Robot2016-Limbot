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

import java.util.HashMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
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
//    public static DoubleSolenoid armsCandyCaneSolenoid;
    public static CANTalon armsMotorClimb;
    
    public static PowerDistributionPanel pdp;
    
    public static void init() {
    	
    	
    	//PDP
    	
    	pdp = new PowerDistributionPanel();
    	
    	//END PDP
    	
    	
    	//DRIVETRAIN
    	boolean invertLeft = true;
    	boolean invertRight = false;
    	
        driveTrainMotorLeftA = new CANTalon(20);
        LiveWindow.addActuator("DriveTrain", "MotorLeftA", driveTrainMotorLeftA);
        driveTrainMotorLeftA.setInverted(invertLeft);
        driveTrainMotorLeftA.changeControlMode(TalonControlMode.PercentVbus);
        driveTrainMotorLeftA.setVoltageRampRate(Constants.DRIVE_TRAIN_VOLTAGE_RAMP_RATE);
        
        driveTrainMotorLeftB = new CANTalon(19);
        LiveWindow.addActuator("DriveTrain", "MotorLeftB", driveTrainMotorLeftB);
        driveTrainMotorLeftB.setInverted(invertLeft);
        driveTrainMotorLeftB.changeControlMode(TalonControlMode.PercentVbus);
        driveTrainMotorLeftB.setVoltageRampRate(Constants.DRIVE_TRAIN_VOLTAGE_RAMP_RATE);
        
        driveTrainMotorLeftC = new CANTalon(18);
        LiveWindow.addActuator("DriveTrain", "MotorLeftC", driveTrainMotorLeftC);
        driveTrainMotorLeftC.setInverted(invertLeft);
        driveTrainMotorLeftC.changeControlMode(TalonControlMode.PercentVbus);
        driveTrainMotorLeftC.setVoltageRampRate(Constants.DRIVE_TRAIN_VOLTAGE_RAMP_RATE);
        
        driveTrainMotorRightA = new CANTalon(17);
        LiveWindow.addActuator("DriveTrain", "MotorRightA", driveTrainMotorRightA);
        driveTrainMotorRightA.setInverted(invertRight);
        driveTrainMotorRightA.changeControlMode(TalonControlMode.PercentVbus);
        driveTrainMotorRightA.setVoltageRampRate(Constants.DRIVE_TRAIN_VOLTAGE_RAMP_RATE);
        
        driveTrainMotorRightB = new CANTalon(16);
        LiveWindow.addActuator("DriveTrain", "MotorRightB", driveTrainMotorRightB);
        driveTrainMotorRightB.setInverted(invertRight);
        driveTrainMotorRightB.changeControlMode(TalonControlMode.PercentVbus);
        driveTrainMotorRightB.setVoltageRampRate(Constants.DRIVE_TRAIN_VOLTAGE_RAMP_RATE);
        
        driveTrainMotorRightC = new CANTalon(15);
        LiveWindow.addActuator("DriveTrain", "MotorRightC", driveTrainMotorRightC);
        driveTrainMotorRightC.setInverted(invertRight);
        driveTrainMotorRightC.changeControlMode(TalonControlMode.PercentVbus);
        driveTrainMotorRightC.setVoltageRampRate(Constants.DRIVE_TRAIN_VOLTAGE_RAMP_RATE);
       
        
        driveTrainEncoderLeft = new Encoder(0, 1, false, EncodingType.k4X);
        LiveWindow.addSensor("DriveTrain", "EncoderLeft", driveTrainEncoderLeft);
        driveTrainEncoderLeft.setDistancePerPulse(DRIVETRAIN_ENCODER_FACTOR);
        driveTrainEncoderLeft.setPIDSourceType(PIDSourceType.kRate);
        
        driveTrainEncoderRight = new Encoder(2, 3, true, EncodingType.k4X);
        LiveWindow.addSensor("DriveTrain", "EncoderRight", driveTrainEncoderRight);
        driveTrainEncoderRight.setDistancePerPulse(DRIVETRAIN_ENCODER_FACTOR);
        driveTrainEncoderRight.setPIDSourceType(PIDSourceType.kRate);
        
        
        driveTrainShifterSolenoid = new DoubleSolenoid(4, 5);
        LiveWindow.addActuator("DriveTrain", "ShifterSolenoid", driveTrainShifterSolenoid);
        
        
        driveTrainPIDLeft = new PIDController(DRIVETRAIN_LEFT_KP, DRIVETRAIN_LEFT_KI,
        		DRIVETRAIN_LEFT_KD, driveTrainEncoderLeft, driveTrainMotorLeftA, DELTA_TIME / 4);
        LiveWindow.addActuator("DriveTrain", "PIDLeft", driveTrainPIDLeft);
        driveTrainPIDLeft.setAbsoluteTolerance(DRIVETRAIN_ON_TARGET_ERROR);
        
        driveTrainPIDRight = new PIDController(DRIVETRAIN_RIGHT_KP, DRIVETRAIN_RIGHT_KI,
        		DRIVETRAIN_RIGHT_KD, driveTrainEncoderRight, driveTrainMotorRightA, DELTA_TIME / 4);
        LiveWindow.addActuator("DriveTrain", "PIDRight", driveTrainPIDRight);
        driveTrainPIDLeft.setAbsoluteTolerance(DRIVETRAIN_ON_TARGET_ERROR);
        
        //END DRIVETRAIN
        
        
        
        //LAUNCHER
        
        launcherPuncherSolenoid = new DoubleSolenoid(2, 3);
        LiveWindow.addActuator("Launcher", "PuncherSolenoid", launcherPuncherSolenoid);
        
        launcherMotorAim = new CANTalon(13);
        LiveWindow.addActuator("Launcher", "AimMotor", launcherMotorAim);
        launcherMotorAim.setFeedbackDevice(FeedbackDevice.PulseWidth);
        launcherMotorAim.changeControlMode(TalonControlMode.PercentVbus);
        launcherMotorAim.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
        launcherMotorAim.reverseSensor(true);
//        launcherMotorAim.reverseSensor(true); doesn't work, sensor value is still negative
        
        launcherMotorLeft = new CANTalon(12);
        LiveWindow.addActuator("Launcher", "MotorLeft", launcherMotorLeft);
        launcherMotorLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        launcherMotorLeft.changeControlMode(TalonControlMode.Speed);
//        launcherMotorLeft.changeControlMode(TalonControlMode.PercentVbus);
        launcherMotorLeft.setInverted(false);
        launcherMotorLeft.reverseSensor(true);
//        launcherMotorLeft.configEncoderCodesPerRev(4096);
        
        launcherMotorRight = new CANTalon(14);
        LiveWindow.addActuator("Launcher", "MotorRight", launcherMotorRight);
        launcherMotorRight.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        launcherMotorRight.changeControlMode(TalonControlMode.Speed);
//        launcherMotorRight.changeControlMode(TalonControlMode.PercentVbus);
        launcherMotorRight.setInverted(false);
        launcherMotorRight.reverseSensor(true);
//        launcherMotorRight.configEncoderCodesPerRev(4096);
        
        //END LAUNCHER
        
        //4, 5 shifter
        //2, 3 puncher
        //0, 1 arms
        
        //ARMS
        
        armsPlexiSolenoid = new DoubleSolenoid(0, 1);
        //TODO: correct ports for candy cane solenoid
//        armsCandyCaneSolenoid = new DoubleSolenoid();
        
        armsMotorClimb = new CANTalon(11);
        LiveWindow.addActuator("Arms", "MotorClimb", armsMotorClimb);
        armsMotorClimb.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        armsMotorClimb.changeControlMode(TalonControlMode.PercentVbus);
//        armsMotorClimb.configEncoderCodesPerRev(4096);
        
        //END ARMS
    }
}

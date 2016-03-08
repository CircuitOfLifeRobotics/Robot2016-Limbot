package com.team3925.robot2016;

import static com.team3925.robot2016.Constants.DRIVETRAIN_ENCODER_FACTOR;

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
    public static CANTalon driveTrainMotorRightA;
    public static CANTalon driveTrainMotorLeftB;
    public static CANTalon driveTrainMotorRightB;
//    public static CANTalon driveTrainMotorLeftC;
//    public static CANTalon driveTrainMotorRightC;
    public static Encoder driveTrainEncoderLeft;
    public static Encoder driveTrainEncoderRight;
    public static DoubleSolenoid driveTrainShifterSolenoid;
    public static PIDController driveTrainPIDLeft;
    public static PIDController driveTrainPIDRight;
    
    public static CANTalon launcherMotorAim;
    public static CANTalon launcherMotorLeft;
    public static CANTalon launcherMotorRight;
    public static DoubleSolenoid launcherPuncherSolenoid;
    
    public static DoubleSolenoid plexiArmsSolenoid;
    
    public static CANTalon climberArmsMotor;
    public static DoubleSolenoid climberSolenoid;
    
//    public static Solenoid blueLedStrip;
//    public static Solenoid redLedStrip;
    
    public static PowerDistributionPanel pdp;
    
    
    /*
    Practice Robot CAN Configurations (What was IN code)
    
    LeftDriveA = 20
    LeftDriveB = 19
    LeftDriveC = 18
    RightDriveA = 17
    RightDriveB = 16
    RightDriveC = 15
    
    LauncherAimMotor = 13
    LauncherIntakeLeft = 12
    LauncherIntakeRight = 14
    
    */
    
    /*
    Competition Robot CAN Configurations (What was IN code)
    
    LeftDriveA = 18
    LeftDriveB = 19
    LeftDriveC = removed
    RightDriveA = 15
    RightDriveB = 16
    RightDriveC = removed
    
    LauncherAimMotor = 13
    LauncherIntakeLeft = 12
    LauncherIntakeRight = 14
    
     */
    
    /*
    Practice Robot CAN Configurations (What is ON ROBOT as of March 3)
    LeftDriveA = 13
    LeftDriveB = 12
    RightDriveA = 18
    RightDriveB = 19
    
    LauncherAim = 15
    LauncherRight = 10
    LauncherLeft = 11
    
    Unused = 14
    */
    
    public static void init() {
    	
    	
    	//PDP
    	
    	pdp = new PowerDistributionPanel();
    	
    	//END PDP
    	
    	
    	//DRIVETRAIN
    	boolean invertLeft = true;
    	boolean invertRight = false;
    	
        driveTrainMotorLeftA = new CANTalon(13);
        LiveWindow.addActuator("DriveTrain", "MotorLeftA", driveTrainMotorLeftA);
        driveTrainMotorLeftA.setInverted(invertLeft);
        driveTrainMotorLeftA.changeControlMode(TalonControlMode.PercentVbus);
        driveTrainMotorLeftA.setVoltageRampRate(Constants.DRIVE_TRAIN_VOLTAGE_RAMP_RATE);
        driveTrainMotorLeftA.enableBrakeMode(false);
        
        driveTrainMotorLeftB = new CANTalon(12);
        LiveWindow.addActuator("DriveTrain", "MotorLeftB", driveTrainMotorLeftB);
        driveTrainMotorLeftB.setInverted(invertLeft);
//        driveTrainMotorLeftB.reverseOutput(invertLeft);
        driveTrainMotorLeftB.changeControlMode(TalonControlMode.PercentVbus);
//        driveTrainMotorLeftB.set(driveTrainMotorLeftA.getDeviceID());
        driveTrainMotorLeftB.setVoltageRampRate(Constants.DRIVE_TRAIN_VOLTAGE_RAMP_RATE);
        driveTrainMotorLeftB.enableBrakeMode(false);
        
//        driveTrainMotorLeftC = new CANTalon(18);
//        LiveWindow.addActuator("DriveTrain", "MotorLeftC", driveTrainMotorLeftC);
//        driveTrainMotorLeftC.setInverted(invertLeft);
////        driveTrainMotorLeftC.reverseOutput(invertLeft);
//        driveTrainMotorLeftC.changeControlMode(TalonControlMode.Follower);
//        driveTrainMotorLeftC.setVoltageRampRate(Constants.DRIVE_TRAIN_VOLTAGE_RAMP_RATE);
//        driveTrainMotorLeftC.enableBrakeMode(false);
        
        driveTrainMotorRightA = new CANTalon(18); // was 17
        LiveWindow.addActuator("DriveTrain", "MotorRightA", driveTrainMotorRightA);
//        driveTrainMotorRightA.setInverted(invertRight);
//        driveTrainMotorRightA.reverseSensor(invertRight);
        driveTrainMotorRightA.changeControlMode(TalonControlMode.PercentVbus);
        driveTrainMotorRightA.setVoltageRampRate(Constants.DRIVE_TRAIN_VOLTAGE_RAMP_RATE);
        driveTrainMotorRightA.enableBrakeMode(false);
        
        driveTrainMotorRightB = new CANTalon(19);
        LiveWindow.addActuator("DriveTrain", "MotorRightB", driveTrainMotorRightB);
        driveTrainMotorRightB.setInverted(invertRight);
//        driveTrainMotorRightB.reverseOutput(invertRight);
//        driveTrainMotorRightB.reverseSensor(invertRight);
        driveTrainMotorRightB.changeControlMode(TalonControlMode.PercentVbus);
//        driveTrainMotorRightB.set(driveTrainMotorRightA.getDeviceID());
        driveTrainMotorRightB.setVoltageRampRate(Constants.DRIVE_TRAIN_VOLTAGE_RAMP_RATE);
        driveTrainMotorRightB.enableBrakeMode(false);
        
//        driveTrainMotorRightC = new CANTalon(15);
//        LiveWindow.addActuator("DriveTrain", "MotorRightC", driveTrainMotorRightC);
////        driveTrainMotorRightC.reverseOutput(invertRight);
////        driveTrainMotorRightC.reverseSensor(invertRight);
//        driveTrainMotorRightC.changeControlMode(TalonControlMode.Follower);
//        driveTrainMotorRightC.set(driveTrainMotorRightA.getDeviceID());
//        driveTrainMotorRightC.setVoltageRampRate(Constants.DRIVE_TRAIN_VOLTAGE_RAMP_RATE);
//        driveTrainMotorRightC.enableBrakeMode(false);
       
        
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
        
        
//        driveTrainPIDLeft = new PIDController(DRIVETRAIN_LEFT_KP, DRIVETRAIN_LEFT_KI,
//        		DRIVETRAIN_LEFT_KD, driveTrainEncoderLeft, driveTrainMotorLeftA, DELTA_TIME / 4);
//        LiveWindow.addActuator("DriveTrain", "PIDLeft", driveTrainPIDLeft);
//        driveTrainPIDLeft.setAbsoluteTolerance(DRIVETRAIN_ON_TARGET_ERROR);
        
//        driveTrainPIDRight = new PIDController(DRIVETRAIN_RIGHT_KP, DRIVETRAIN_RIGHT_KI,
//        		DRIVETRAIN_RIGHT_KD, driveTrainEncoderRight, driveTrainMotorRightA, DELTA_TIME / 4);
//        LiveWindow.addActuator("DriveTrain", "PIDRight", driveTrainPIDRight);
//        driveTrainPIDLeft.setAbsoluteTolerance(DRIVETRAIN_ON_TARGET_ERROR);
        
        //END DRIVETRAIN
        
        
        
        //LAUNCHER
        
        launcherPuncherSolenoid = new DoubleSolenoid(2, 3);
        LiveWindow.addActuator("Launcher", "PuncherSolenoid", launcherPuncherSolenoid);
        
        launcherMotorAim = new CANTalon(15);
        LiveWindow.addActuator("Launcher", "AimMotor", launcherMotorAim);
        launcherMotorAim.setFeedbackDevice(FeedbackDevice.PulseWidth);
        launcherMotorAim.changeControlMode(TalonControlMode.PercentVbus);
        launcherMotorAim.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
        launcherMotorAim.reverseSensor(true);
//        launcherMotorAim.reverseSensor(true); doesn't work, sensor value is still negative
        
        launcherMotorLeft = new CANTalon(11);
        LiveWindow.addActuator("Launcher", "MotorLeft", launcherMotorLeft);
        launcherMotorLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);
//        launcherMotorLeft.changeControlMode(TalonControlMode.Speed);
        launcherMotorLeft.changeControlMode(TalonControlMode.PercentVbus);
        launcherMotorLeft.reverseOutput(false);
        launcherMotorLeft.reverseSensor(true);
//        launcherMotorLeft.configEncoderCodesPerRev(4096);
        
        launcherMotorRight = new CANTalon(10);
        LiveWindow.addActuator("Launcher", "MotorRight", launcherMotorRight);
        launcherMotorRight.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        launcherMotorRight.changeControlMode(TalonControlMode.PercentVbus);
        launcherMotorLeft.reverseOutput(false);
        launcherMotorRight.reverseSensor(false);
//        launcherMotorRight.configEncoderCodesPerRev(4096);
        
        //END LAUNCHER
        
        //4, 5 shifter
        //2, 3 puncher
        //0, 1 arms
        
        
        
        // PLEXI ARMS
        
        plexiArmsSolenoid = new DoubleSolenoid(0, 1);
        //TODO: correct ports for candy cane solenoid
        
        
        
        // CLIMBER
        
        climberArmsMotor = new CANTalon(11);
        LiveWindow.addActuator("Arms", "MotorClimb", climberArmsMotor);
        climberArmsMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        climberArmsMotor.changeControlMode(TalonControlMode.PercentVbus);
//        armsMotorClimb.configEncoderCodesPerRev(4096);
        
//        climberSolenoid = new DoubleSolenoid(0, 0); TODO Get ports
        

        // LEDS
        
//        blueLedStrip = new Solenoid(7);
//        blueLedStrip.set(true);
//        redLedStrip = new Solenoid(6);
//        redLedStrip.set(true);
    }
}

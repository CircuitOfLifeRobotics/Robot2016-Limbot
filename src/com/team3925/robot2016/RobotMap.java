package com.team3925.robot2016;

import static com.team3925.robot2016.Constants.DRIVETRAIN_ENCODER_FACTOR;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Ultrasonic;
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
    public static DoubleSolenoid driveTrainShifterSolenoid;
    
    public static CANTalon launcherMotorAim;
    public static CANTalon launcherMotorLeft;
    public static CANTalon launcherMotorRight;
    public static DoubleSolenoid launcherPuncherSolenoid;
    public static AnalogInput launcherUltrasonic;
    
    public static CANTalon intakeAssistArmLeft;
    public static CANTalon intakeAssistArmRight;
    public static CANTalon intakeAssistWheels;
    
    
    public static DoubleSolenoid plexiArmsSolenoid;
    
    public static CANTalon climberArmsMotor;
    public static DoubleSolenoid climberSolenoid;
    
//    public static Solenoid blueLedStrip;
//    public static Solenoid redLedStrip;
    
    public static PowerDistributionPanel pdp;
    
    public static void init() {
    	
    	
    	//PDP
    	
    	pdp = new PowerDistributionPanel();
    	
    	//END PDP
    	
    	
    	//DRIVETRAIN
    	boolean invertLeft = true;
    	boolean invertRight = false;
    	
        driveTrainMotorLeftA = new CANTalon(19);
        LiveWindow.addActuator("DriveTrain", "MotorLeftA", driveTrainMotorLeftA);
        driveTrainMotorLeftA.setInverted(invertLeft);
        driveTrainMotorLeftA.changeControlMode(TalonControlMode.PercentVbus);
        driveTrainMotorLeftA.setVoltageRampRate(Constants.DRIVE_TRAIN_VOLTAGE_RAMP_RATE);
        driveTrainMotorLeftA.enableBrakeMode(false);
        
        driveTrainMotorLeftB = new CANTalon(18);
        LiveWindow.addActuator("DriveTrain", "MotorLeftB", driveTrainMotorLeftB);
        driveTrainMotorLeftB.setInverted(invertLeft);
//        driveTrainMotorLeftB.reverseOutput(invertLeft);
        driveTrainMotorLeftB.changeControlMode(TalonControlMode.PercentVbus);
//        driveTrainMotorLeftB.set(driveTrainMotorLeftA.getDeviceID());
        driveTrainMotorLeftB.setVoltageRampRate(Constants.DRIVE_TRAIN_VOLTAGE_RAMP_RATE);
        driveTrainMotorLeftB.enableBrakeMode(false);
        
        driveTrainMotorRightA = new CANTalon(17); // was 17
        LiveWindow.addActuator("DriveTrain", "MotorRightA", driveTrainMotorRightA);
//        driveTrainMotorRightA.setInverted(invertRight);
//        driveTrainMotorRightA.reverseSensor(invertRight);
        driveTrainMotorRightA.changeControlMode(TalonControlMode.PercentVbus);
        driveTrainMotorRightA.setVoltageRampRate(Constants.DRIVE_TRAIN_VOLTAGE_RAMP_RATE);
        driveTrainMotorRightA.enableBrakeMode(false);
        
        driveTrainMotorRightB = new CANTalon(16);
        LiveWindow.addActuator("DriveTrain", "MotorRightB", driveTrainMotorRightB);
        driveTrainMotorRightB.setInverted(invertRight);
//        driveTrainMotorRightB.reverseOutput(invertRight);
//        driveTrainMotorRightB.reverseSensor(invertRight);
        driveTrainMotorRightB.changeControlMode(TalonControlMode.PercentVbus);
//        driveTrainMotorRightB.set(driveTrainMotorRightA.getDeviceID());
        driveTrainMotorRightB.setVoltageRampRate(Constants.DRIVE_TRAIN_VOLTAGE_RAMP_RATE);
        driveTrainMotorRightB.enableBrakeMode(false);
        
        
        driveTrainShifterSolenoid = new DoubleSolenoid(4, 5);
        LiveWindow.addActuator("DriveTrain", "ShifterSolenoid", driveTrainShifterSolenoid);
        
        
        
        //LAUNCHER
        
        launcherPuncherSolenoid = new DoubleSolenoid(2, 3);
        LiveWindow.addActuator("Launcher", "PuncherSolenoid", launcherPuncherSolenoid);
        
        //all the way in	2.8A
        //partially in		1.5A
        //in wheels			0.9A
        //no ball			0.8A
        //pointed at light	1.5A
        launcherUltrasonic = new AnalogInput(0);
        
        launcherMotorAim = new CANTalon(13);
        LiveWindow.addActuator("Launcher", "AimMotor", launcherMotorAim);
        launcherMotorAim.setFeedbackDevice(FeedbackDevice.PulseWidth);
        launcherMotorAim.changeControlMode(TalonControlMode.PercentVbus);
        launcherMotorAim.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
        launcherMotorAim.reverseSensor(true);
//        launcherMotorAim.reverseSensor(true); doesn't work, sensor value is still negative
        launcherMotorAim.setEncPosition(0);
        
        launcherMotorLeft = new CANTalon(12);
        LiveWindow.addActuator("Launcher", "MotorLeft", launcherMotorLeft);
        launcherMotorLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        launcherMotorLeft.changeControlMode(TalonControlMode.PercentVbus);
        launcherMotorLeft.setInverted(false);
        launcherMotorLeft.reverseOutput(false);
        launcherMotorLeft.reverseSensor(true);
//        launcherMotorLeft.configEncoderCodesPerRev(4096);
        
        launcherMotorRight = new CANTalon(14);
        LiveWindow.addActuator("Launcher", "MotorRight", launcherMotorRight);
        launcherMotorRight.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        launcherMotorRight.changeControlMode(TalonControlMode.PercentVbus);
        launcherMotorRight.setInverted(false); // practice = true | comp = false
        launcherMotorRight.reverseOutput(true);
        launcherMotorRight.reverseSensor(true);
//        launcherMotorRight.configEncoderCodesPerRev(4096);

        
        
        
        //  INTAKE ASSIST
        
        intakeAssistArmRight = new CANTalon(11);
        LiveWindow.addActuator("Intake_Assist", "ArmRight", intakeAssistArmRight);
        intakeAssistArmRight.changeControlMode(TalonControlMode.PercentVbus);
//        intakeAssistArmRight.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        intakeAssistArmRight.setInverted(false);
        intakeAssistArmRight.reverseOutput(false);
        intakeAssistArmRight.reverseSensor(false);
//        intakeAssistArmRight.setEncPosition(140);
//        intakeAssistArmRight.setPID(Constants.INTAKE_ASSIST_P, Constants.INTAKE_ASSIST_I, Constants.INTAKE_ASSIST_D);
        
        intakeAssistArmLeft = new CANTalon(15);
        LiveWindow.addActuator("Intake_Assist", "ArmLeft", intakeAssistArmLeft);
        intakeAssistArmLeft.changeControlMode(TalonControlMode.PercentVbus);
//        intakeAssistArmLeft.changeControlMode(TalonControlMode.Follower);
//        intakeAssistArmLeft.set(intakeAssistArmRight.getDeviceID());
//        intakeAssistArmLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        intakeAssistArmLeft.setInverted(true);
        intakeAssistArmLeft.reverseOutput(true);
//        intakeAssistArmLeft.reverseSensor(false);
        
        intakeAssistWheels = new CANTalon(20);
        LiveWindow.addActuator("Intake_Assist", "Wheels", intakeAssistWheels);
        intakeAssistWheels.changeControlMode(TalonControlMode.PercentVbus);
        intakeAssistWheels.setInverted(true);
        intakeAssistWheels.reverseOutput(true);
        
        
        // LEDS
        
//        blueLedStrip = new Solenoid(7);
//        blueLedStrip.set(true);
//        redLedStrip = new Solenoid(6);
//        redLedStrip.set(true);
    }
}

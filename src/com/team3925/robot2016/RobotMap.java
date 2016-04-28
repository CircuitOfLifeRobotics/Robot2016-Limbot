package com.team3925.robot2016;

import static com.team3925.robot2016.Constants.*;

import com.team3925.robot2016.util.PixyCmu5;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
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
    public static DoubleSolenoid driveTrainShifterSolenoid;
    
    public static CANTalon launcherMotorArm;
    public static CANTalon launcherMotorFar;
    public static CANTalon launcherMotorNear;
    public static DigitalInput launcherFwdLimitSwitch;
    public static DigitalInput launcherRevLimitSwitch;
    public static DoubleSolenoid launcherPuncherSolenoid;
    public static AnalogInput launcherUltrasonic;
    
    public static CANTalon intakeAssistArmLeft;
    public static CANTalon intakeAssistArmRight;
    public static CANTalon intakeAssistWheels;
    
    
    public static DoubleSolenoid plexiArmsSolenoidLeft;
    public static DoubleSolenoid plexiArmsSolenoidRight;
    
    public static CANTalon climberArmsMotor;
    public static DoubleSolenoid climberSolenoid;
    
//    public static Solenoid blueLedStrip;
//    public static Solenoid redLedStrip;
    
    public static PowerDistributionPanel pdp;
	public static PixyCmu5 pixyCam;
    
    
    public static void init() {
    	
    	
    	//PDP
    	
    	pdp = new PowerDistributionPanel();
    	
    	//END PDP
    	
    	
    	//DRIVETRAIN
    	boolean invertLeft = true;
    	boolean invertRight = false;
    	
        driveTrainMotorLeftA = new CANTalon(DRIVETRAIN_MOTOR_LEFT_A);
        LiveWindow.addActuator("DriveTrain", "MotorLeftA", driveTrainMotorLeftA);
        driveTrainMotorLeftA.setInverted(invertLeft);
        driveTrainMotorLeftA.changeControlMode(TalonControlMode.PercentVbus);
        driveTrainMotorLeftA.setVoltageRampRate(DRIVETRAIN_VOLTAGE_RAMP_RATE);
        driveTrainMotorLeftA.enableBrakeMode(false);
        driveTrainMotorLeftA.configEncoderCodesPerRev(256);
        
        driveTrainMotorLeftB = new CANTalon(DRIVETRAIN_MOTOR_LEFT_B);
        LiveWindow.addActuator("DriveTrain", "MotorLeftB", driveTrainMotorLeftB);
        driveTrainMotorLeftB.setInverted(invertLeft);
//        driveTrainMotorLeftB.reverseOutput(invertLeft);
        driveTrainMotorLeftB.changeControlMode(TalonControlMode.PercentVbus);
//        driveTrainMotorLeftB.set(driveTrainMotorLeftA.getDeviceID());
        driveTrainMotorLeftB.setVoltageRampRate(DRIVETRAIN_VOLTAGE_RAMP_RATE);
        driveTrainMotorLeftB.enableBrakeMode(false);
        
        driveTrainMotorRightA = new CANTalon(DRIVETRAIN_MOTOR_RIGHT_A);
        LiveWindow.addActuator("DriveTrain", "MotorRightA", driveTrainMotorRightA);
//        driveTrainMotorRightA.setInverted(invertRight);
//        driveTrainMotorRightA.reverseSensor(invertRight);
        driveTrainMotorRightA.changeControlMode(TalonControlMode.PercentVbus);
        driveTrainMotorRightA.setVoltageRampRate(DRIVETRAIN_VOLTAGE_RAMP_RATE);
        driveTrainMotorRightA.enableBrakeMode(false);
        driveTrainMotorRightA.configEncoderCodesPerRev(256);
        
        driveTrainMotorRightB = new CANTalon(DRIVETRAIN_MOTOR_RIGHT_B);
        LiveWindow.addActuator("DriveTrain", "MotorRightB", driveTrainMotorRightB);
        driveTrainMotorRightB.setInverted(invertRight);
//        driveTrainMotorRightB.reverseOutput(invertRight);
//        driveTrainMotorRightB.reverseSensor(invertRight);
        driveTrainMotorRightB.changeControlMode(TalonControlMode.PercentVbus);
//        driveTrainMotorRightB.set(driveTrainMotorRightA.getDeviceID());
        driveTrainMotorRightB.setVoltageRampRate(DRIVETRAIN_VOLTAGE_RAMP_RATE);
        driveTrainMotorRightB.enableBrakeMode(false);
        
        
        driveTrainShifterSolenoid = new DoubleSolenoid(DRIVETRAIN_SOLENOID_FORWARD, DRIVETRAIN_SOLENOID_REVERSE);
        LiveWindow.addActuator("DriveTrain", "ShifterSolenoid", driveTrainShifterSolenoid);
        
        
        
        // LAUNCHER
        
        launcherPuncherSolenoid = new DoubleSolenoid(LAUNCHER_SOLENOID_PUNCHER_FORWARD, LAUNCHER_SOLENOID_PUNCHER_REVERSE);
        LiveWindow.addActuator("Launcher", "PuncherSolenoid", launcherPuncherSolenoid);
        
        //all the way in	2.8A
        //partially in		1.5A
        //in wheels			0.9A
        //no ball			0.8A
        //pointed at light	1.5A
        launcherUltrasonic = new AnalogInput(LAUNCHER_ULTRASONIC);
        
        launcherFwdLimitSwitch = new DigitalInput(LAUNCHER_LIMIT_SWITCH_FORWARD);
        launcherRevLimitSwitch = new DigitalInput(LAUNCHER_LIMIT_SWITCH_REVERSE);
        
        launcherMotorArm = new CANTalon(LAUNCHER_MOTOR_ARM);
        LiveWindow.addActuator("Launcher", "AimMotor", launcherMotorArm);
        launcherMotorArm.changeControlMode(TalonControlMode.PercentVbus);
        launcherMotorArm.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
//        launcherMotorArm.configEncoderCodesPerRev(LAUNCHER_NEW_ENCODER_SCALE_FACTOR);
//        launcherMotorAim.reverseSensor(true); doesn't work, sensor value is still negative
        launcherMotorArm.enableForwardSoftLimit(false);
        launcherMotorArm.enableReverseSoftLimit(false);
        launcherMotorArm.enableLimitSwitch(false, false);
        
        launcherMotorFar = new CANTalon(LAUNCHER_MOTOR_FAR);
        LiveWindow.addActuator("Launcher", "MotorFar", launcherMotorFar);
        launcherMotorFar.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        launcherMotorFar.changeControlMode(TalonControlMode.PercentVbus);
        
        launcherMotorNear = new CANTalon(LAUNCHER_MOTOR_NEAR);
        LiveWindow.addActuator("Launcher", "MotorNear", launcherMotorNear);
        launcherMotorNear.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        launcherMotorNear.changeControlMode(TalonControlMode.PercentVbus);
        
        
        
        plexiArmsSolenoidLeft = new DoubleSolenoid(PLEXIARMS_SOLENOID_LEFT_A, PLEXIARMS_SOLENOID_LEFT_B);
        LiveWindow.addActuator("PlexiArms", "SolenoidLeft", plexiArmsSolenoidLeft);
        
        plexiArmsSolenoidRight = new DoubleSolenoid(PLEXIARMS_SOLENOID_RIGHT_A, PLEXIARMS_SOLENOID_RIGHT_B);
        LiveWindow.addActuator("PlexiArms", "SolenoidRight", plexiArmsSolenoidRight);
        
        
        try {
			pixyCam = new PixyCmu5(0xa8, 0.2);
		}catch(Exception e) {
			DriverStation.reportError("Error instantiating PixyCam!", true);
			pixyCam = null;
		}
    }
}

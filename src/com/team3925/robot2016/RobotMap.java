package com.team3925.robot2016;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
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
    public static SpeedController driveTrainMotorLeftA;
    public static SpeedController driveTrainMotorLeftB;
    public static SpeedController driveTrainMotorLeftC;
    public static SpeedController driveTrainMotorRightA;
    public static SpeedController driveTrainMotorRightB;
    public static SpeedController driveTrainMotorRightC;
    public static Encoder driveTrainEncoderLeft;
    public static Encoder driveTrainEncoderRight;
    
    public static CANTalon launcherMotorLeft;
    public static CANTalon launcherMotorRight;


    public static void init() {
    	
        driveTrainMotorLeftA = new Talon(3);
        LiveWindow.addActuator("DriveTrain", "MotorLeftA", (Talon) driveTrainMotorLeftA);
        
        driveTrainMotorLeftB = new Talon(4);
        LiveWindow.addActuator("DriveTrain", "MotorLeftB", (Talon) driveTrainMotorLeftB);
        
        driveTrainMotorLeftC = new Talon(5);
        LiveWindow.addActuator("DriveTrain", "MotorLeftC", (Talon) driveTrainMotorLeftC);
        
        driveTrainMotorRightA = new Talon(0);
        LiveWindow.addActuator("DriveTrain", "MotorRightA", (Talon) driveTrainMotorRightA);
        
        driveTrainMotorRightB = new Talon(1);
        LiveWindow.addActuator("DriveTrain", "MotorRightB", (Talon) driveTrainMotorRightB);
        
        driveTrainMotorRightC = new Talon(2);
        LiveWindow.addActuator("DriveTrain", "MotorRightC", (Talon) driveTrainMotorRightC);
        
        driveTrainEncoderLeft = new Encoder(0, 1, false, EncodingType.k4X);
        LiveWindow.addSensor("DriveTrain", "EncoderLeft", driveTrainEncoderLeft);
        driveTrainEncoderLeft.setDistancePerPulse(1.0);
        driveTrainEncoderLeft.setPIDSourceType(PIDSourceType.kRate);
        
        driveTrainEncoderRight = new Encoder(2, 3, false, EncodingType.k4X);
        LiveWindow.addSensor("DriveTrain", "EncoderRight", driveTrainEncoderRight);
        driveTrainEncoderRight.setDistancePerPulse(1.0);
        driveTrainEncoderRight.setPIDSourceType(PIDSourceType.kRate);
        
        
        launcherMotorLeft = new CANTalon(0);
        LiveWindow.addActuator("Launcher", "MotorLeft", launcherMotorLeft);
        launcherMotorLeft.changeControlMode(TalonControlMode.Speed); //TODO check if we want to use speed
        
        
        launcherMotorRight = new CANTalon(1);
        LiveWindow.addActuator("Launcher", "MotorRight", launcherMotorRight);
        
        launcherMotorRight.changeControlMode(TalonControlMode.Follower);
        launcherMotorRight.set(launcherMotorLeft.getDeviceID());
        launcherMotorRight.setInverted(true);
//        TODO check if this the correct motor to invert
//        TODO add PID to CANTalons
    }
}

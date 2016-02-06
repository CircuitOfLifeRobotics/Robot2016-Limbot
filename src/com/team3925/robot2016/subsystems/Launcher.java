package com.team3925.robot2016.subsystems;

import static com.team3925.robot2016.Constants.LAUNCHER_AIM_MOTOR_SPEED_MULTIPLIED;

import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.commands.LauncherPID;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class Launcher extends Subsystem implements SmartdashBoardLoggable {

    private final CANTalon motorLeft = RobotMap.launcherMotorLeft;
    private final CANTalon motorRight = RobotMap.launcherMotorRight;
    private final CANTalon motorAim = RobotMap.launcherMotorAim;
    private final DoubleSolenoid puncherSolenoid = RobotMap.launcherPuncherSolenoid;
//    Sensor for ball goes here
    
    private boolean hasBall = false;
    private double setpoint = 0;
    

    /**
     * @param speed
     */
    public void setIntakeSpeeds(double speed) {
    	motorLeft.set(speed);
    	motorRight.set(speed);
    }
    
    public void setAimMotorSpeed(double speed) {
    	motorAim.set(speed * LAUNCHER_AIM_MOTOR_SPEED_MULTIPLIED);
    }
    
    public void setHeight(double heightRelative) {
    	setpoint += heightRelative;
    	if (motorAim.getPosition() < setpoint) {
			setAimMotorSpeed(0.3);
		}
    }
    
    public void update() {
    	hasBall = false; //TODO add sensor for ball
    }
    
    public boolean hasBall() {
    	return hasBall;
    }
    
    public double getAimMotorPosition() {
    	return motorAim.getPosition();
    }
    
    public void setPuncher(boolean isHigh) {
    	puncherSolenoid.set(isHigh ? Value.kForward:Value.kReverse);
    }
    
    @Override
    public void logData() {
    	putNumberSD("MotorLeft", motorLeft.get());
    	putNumberSD("MotorRight", motorRight.get());
    	putNumberSD("AimMotorSpeed", motorAim.getSpeed());
    	putNumberSD("MotorAimPosition", getAimMotorPosition());
    }
    
    @Override
    public String getFormattedName() {
    	return "Launcher_";
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new LauncherPID());
    }
}


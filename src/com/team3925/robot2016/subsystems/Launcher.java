package com.team3925.robot2016.subsystems;

import com.team3925.robot2016.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 */
public class Launcher extends Subsystem {

    private final CANTalon motorLeft = RobotMap.launcherMotorLeft;
    private final CANTalon motorRight = RobotMap.launcherMotorRight;

    public void setIntakeSpeeds(double speed) {
//    	
    	motorLeft.set(speed);
//    	motorRight.set(speed);
//    	^ This should already be handled by the FOLLOWER Talon Control Mode
    }
    
    /**
     * Logs various data to SmartDashboard
     */
    public void logData() {
    	SmartDashboard.putNumber("Launcher_MotorLeft", motorLeft.get());
    	SmartDashboard.putNumber("Launcher_MotorRight", motorRight.get());
    }
    
    public void initDefaultCommand() {

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}


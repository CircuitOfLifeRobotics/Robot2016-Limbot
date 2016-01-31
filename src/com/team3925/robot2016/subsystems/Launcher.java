package com.team3925.robot2016.subsystems;

import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class Launcher extends Subsystem implements SmartdashBoardLoggable {

    private final CANTalon motorLeft = RobotMap.launcherMotorLeft;
    private final CANTalon motorRight = RobotMap.launcherMotorRight;

    public void setIntakeSpeeds(double speed) {
//    	
    	motorLeft.set(speed);
//    	motorRight.set(speed);
//    	^ This should already be handled by the FOLLOWER Talon Control Mode
    }
    
    @Override
    public void logData() {
    	putNumberSD("MotorLeft", motorLeft.get());
    	putNumberSD("MotorRight", motorRight.get());
    }
    
    @Override
    public String getFormattedName() {
    	return "Launcher_";
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}


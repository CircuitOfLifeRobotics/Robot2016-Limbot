package com.team3925.robot2016.subsystems;

import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class PlexiArms extends Subsystem implements SmartdashBoardLoggable {
	private DoubleSolenoid armsSolenoid = RobotMap.armsPlexiSolenoid;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	
	public void setArmUp(boolean engaged) {
		armsSolenoid.set(!engaged ? Value.kForward:Value.kReverse);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

	@Override
	public void logData() {
		putBooleanSD("UpPosition", armsSolenoid.get() == Value.kForward);
	}

	@Override
	public String getFormattedName() {
		return "PlexiArms_";
	}
}


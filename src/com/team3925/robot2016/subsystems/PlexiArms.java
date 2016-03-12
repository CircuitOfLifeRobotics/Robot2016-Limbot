package com.team3925.robot2016.subsystems;

import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.commands.ManualPlexiArms;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *  Subsystem for the plexiglass arms (karate choppers) in the front of the robot.
 *  <p>
 *  Not to be confused with <code>CandyCanes</code>,
 *  the subsystem for the climbing mechanism.
 */
public class PlexiArms extends Subsystem implements SmartdashBoardLoggable {
	private DoubleSolenoid armsSolenoid = RobotMap.plexiArmsSolenoid;

	/**
	 * @param engaged if true
	 */
	public void setArmUp(boolean engaged) {
		armsSolenoid.set(!engaged ? Value.kForward:Value.kReverse);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new ManualPlexiArms());
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


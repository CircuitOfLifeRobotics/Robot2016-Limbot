
package com.team3925.robot2016.subsystems;

import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem for the candy canes :D
 * 
 * @author Adam C
 *
 */
public class CandyCanes extends Subsystem implements SmartdashBoardLoggable {
	
//	private DoubleSolenoid candyCaneSolenoid = RobotMap.armsCandyCaneSolenoid;
	private CANTalon climbMotor = RobotMap.armsMotorClimb;
	
	@Override
	protected void initDefaultCommand() {}
	
//	public void setCandyCaneSolenoid(boolean engaged) {
//		candyCaneSolenoid.set(engaged ? Value.kForward:Value.kReverse);
//	}
	
	public void setClimbMotor(double output) {
		climbMotor.set(output);
	}
	
	public double getClimbEncPos() {
		return climbMotor.getEncPosition();
	}
	
	public double getClimbEncSpeed() {
		return climbMotor.getEncVelocity();
	}
	
	public double getClimbSetpoint() {
		return climbMotor.getSetpoint();
	}
	
	@Override
	public void logData() {
		putNumberSD("ClimbEncPos", getClimbEncPos());
		putNumberSD("ClimbEncVel", getClimbEncSpeed());
		putNumberSD("ClimbMotorGet", climbMotor.get());
		putNumberSD("ClimbMotorVoltage", climbMotor.getOutputVoltage());
		putNumberSD("ClimbMotorCurrent", climbMotor.getOutputCurrent());
	}

	@Override
	public String getFormattedName() {
		return "CandyCanes_";
	}
	
}

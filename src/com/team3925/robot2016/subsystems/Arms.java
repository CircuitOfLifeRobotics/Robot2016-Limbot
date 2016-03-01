
package com.team3925.robot2016.subsystems;

import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem for the plexi arms and the candy canes :D
 * 
 * @author Adam C
 *
 */
public class Arms extends Subsystem implements SmartdashBoardLoggable {
	
	private DoubleSolenoid armsSolenoid = RobotMap.armsPlexiSolenoid;
//	private DoubleSolenoid candyCaneSolenoid = RobotMap.armsCandyCaneSolenoid;
	private CANTalon climbMotor = RobotMap.armsMotorClimb;
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
//	public void setCandyCaneSolenoid(boolean engaged) {
//		candyCaneSolenoid.set(engaged ? Value.kForward:Value.kReverse);
//	}
	
	public void setArm(boolean engaged) {
		armsSolenoid.set(!engaged ? Value.kForward:Value.kReverse);
	}
	
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
		putStringSD("ArmsValue", armsSolenoid.get().toString());
		putNumberSD("ClimbEncPos", getClimbEncPos());
		putNumberSD("ClimbEncVel", getClimbEncSpeed());
		putNumberSD("ClimbMotorGet===============================", climbMotor.get());
		putNumberSD("ClimbMotorVoltage=========================", climbMotor.getOutputVoltage());
		putNumberSD("ClimbMotorCurrent=========================", climbMotor.getOutputCurrent());
	}

	@Override
	public String getFormattedName() {
		return "CandyCanes_";
	}
	
}

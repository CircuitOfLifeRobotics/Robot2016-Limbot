package com.team3925.robot2016.subsystems;

import com.team3925.robot2016.RobotMap;

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
public class Arms extends Subsystem {
	
	private DoubleSolenoid armsSolenoid = RobotMap.armsPlexiSolenoid;
	private CANTalon climbMotor = RobotMap.armsMotorClimb;
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	public void setArm(boolean engaged) {
		armsSolenoid.set(engaged ? Value.kForward:Value.kReverse);
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
	
}

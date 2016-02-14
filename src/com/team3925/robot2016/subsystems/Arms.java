package com.team3925.robot2016.subsystems;

import com.team3925.robot2016.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Arms extends Subsystem {
	
	private DoubleSolenoid arm = RobotMap.armsSolenoid;
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	public void setArm(boolean engaged) {
		arm.set(engaged ? Value.kForward:Value.kReverse);
	}
	
}

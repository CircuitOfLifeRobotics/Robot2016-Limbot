package com.team3925.robot2016.subsystems;

import com.team3925.robot2016.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Arms extends Subsystem{
	//private DoubleSolenoid leftArm = RobotMap.leftArm;
	//private DoubleSolenoid rightArm = RobotMap.rightArm;
	
	@Override
	protected void initDefaultCommand() {
		
		
	}
	
	public void setLeftArm(boolean set){
		if(set){
		//	leftArm.set(Value.kForward);
		}else{
		//	leftArm.set(Value.kReverse);
		}
	}
	public void setRightArm(boolean set){
		if(set){
		//	rightArm.set(Value.kForward);
		}else{
		//	rightArm.set(Value.kReverse);
		}
	}

}

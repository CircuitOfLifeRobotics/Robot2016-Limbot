package com.team3925.robot2016.subsystems.components;

import edu.wpi.first.wpilibj.CANTalon;

public class DriveSide {
	
	private CANTalon motorA, motorB;

	public DriveSide(CANTalon motorA, CANTalon motorB) {
		this.motorA = motorA;
		this.motorB = motorB;
	}
	
	public void setSpeed(double speed) {
		motorA.set(speed);
		motorB.set(speed);
	}
	
	public double getSpeed() {
		return (motorA.get() + motorB.get()) / 2;
	}
	
	public double getEncoderPosition() {
		return motorA.getEncPosition();
	}
	
	public void setBrakeMode(boolean isOn) {
		motorA.enableBrakeMode(isOn);
		motorB.enableBrakeMode(isOn);
	}
	
}

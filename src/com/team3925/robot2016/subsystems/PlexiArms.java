package com.team3925.robot2016.subsystems;

import com.team3925.robot2016.commands.PlexiArmsManual;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class PlexiArms extends Subsystem implements SmartdashBoardLoggable {
	
	private final DoubleSolenoid left;
	
	public PlexiArms(DoubleSolenoid left) {
		this.left = left;
	}
	
	public void setSolenoids(boolean isUp) {
		left.set(isUp ? Value.kForward : Value.kReverse);
	}
	
	@Override
	public void logData() {
		putDataSD("SolenoidLeft", left);
	}
	
	@Override
	public String getFormattedName() {
		return "PlexiArms_";
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new PlexiArmsManual());
	}

}

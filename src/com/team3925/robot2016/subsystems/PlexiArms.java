package com.team3925.robot2016.subsystems;

import com.team3925.robot2016.commands.PlexiArmsManual;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class PlexiArms extends Subsystem implements SmartdashBoardLoggable {
	
	private final DoubleSolenoid solenoid;
	
	public PlexiArms(DoubleSolenoid solenoid) {
		this.solenoid = solenoid;
	}
	
	public void setSolenoids(boolean isUp) {
		solenoid.set(isUp ? Value.kForward : Value.kReverse);
	}
	
	@Override
	public void logData() {
		putBooleanSD("IsUp", solenoid.get() == Value.kForward);
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

package com.team3925.robot2016.subsystems;

import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Climber extends Subsystem implements SmartdashBoardLoggable {
	private final CANTalon climberMotor = RobotMap.climberArmsMotor;
	
	private final DoubleSolenoid raiseSolenoid = RobotMap.climberRaiseSolenoid;
	private final DoubleSolenoid shootSolenoid = RobotMap.climberShootSolenoid;
	
	
	
	@Override
	public void logData() {
		putNumberSD("MotorSpeed", climberMotor.getSpeed());
		putBooleanSD("RaiseSolenoid", raiseSolenoid.get() == Value.kForward);
		putBooleanSD("ShootSolenoid", shootSolenoid.get() == Value.kForward);
	}
	
	public void setClimberMotorSpeed(double speed) {
		climberMotor.set(MiscUtil.limit(speed));
	}
	
	public void setRaiseSolenoid(boolean out) {
		raiseSolenoid.set(out ? Value.kForward : Value.kReverse);
	}
	
	public void setShootSolenoid(boolean out) {
		shootSolenoid.set(out ? Value.kForward : Value.kReverse);
	}

	@Override
	public String getFormattedName() {
		return "Climber_";
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
	

}

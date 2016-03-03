
package com.team3925.robot2016.subsystems;

import static com.team3925.robot2016.Constants.CLIMBER_MAX_VALUE;
import static com.team3925.robot2016.Constants.CLIMBER_ACTIVE_TIME;

import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.commands.Climb;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem for the candy canes :D
 * <p>
 * "Climber" and "Candy Canes" are one and the same.
 */
public final class Climber extends Subsystem implements SmartdashBoardLoggable {
	
//	private DoubleSolenoid climberSolenoid = RobotMap.armsCandyCaneSolenoid;
	private CANTalon climbMotor = RobotMap.climberArmsMotor;
	private TimeoutAction enableFunctionality = new TimeoutAction();
	
	@Override
	protected void initDefaultCommand() { setDefaultCommand(new Climb()); }
	
	public void startTimeOut() {
		enableFunctionality.config(135d - CLIMBER_ACTIVE_TIME);
	}
	
	/**
	 * <i>WARNING: Functional code has been disabled due to lack of physical component!</i>
	 * 
	 * @param engaged engage the solenoid
	 */
	public void setClimberSolenoid(boolean engaged) {
		if (!isEnabled()) { return; }
//		climberSolenoid.set(engaged ? Value.kForward:Value.kReverse);
	}
	
	public void setClimbMotor(double output) {
		if (!isEnabled()) { return; }
		if (getClimbEncPos() >= CLIMBER_MAX_VALUE && output > 0) {
			climbMotor.set(0d);
		}
		climbMotor.set(output);
	}

	
	@Override
	public void logData() {
		putNumberSD("EncPos", getClimbEncPos());
		putNumberSD("EncVel", getClimbEncSpeed());
		putNumberSD("MotorGet", climbMotor.get());
		putNumberSD("MotorVoltage", climbMotor.getOutputVoltage());
		putNumberSD("MotorCurrent", climbMotor.getOutputCurrent());
	}
	
	public boolean isEnabled() {
		return enableFunctionality.isFinished();
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
	public String getFormattedName() {
		return "Climber_";
	}
	
}

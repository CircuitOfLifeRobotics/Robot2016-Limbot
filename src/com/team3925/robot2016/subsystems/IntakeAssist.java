package com.team3925.robot2016.subsystems;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class IntakeAssist extends Subsystem implements SmartdashBoardLoggable {
	
	private final CANTalon wheels = RobotMap.intakeAssistWheels;
	private final CANTalon armLeft = RobotMap.intakeAssistArmLeft;
	private final CANTalon armRight = RobotMap.intakeAssistArmRight;
//	private IntakeAssistState state = IntakeAssistState.UP;
//
//	public static enum IntakeAssistState {
//		UP, DOWN, MOVING, DISABLED;
//	}
	
	/**
	 * In PercentVBus
	 * @param speed double from -1 to 1
	 */
	public void setWheelSpeeds(double speed) {
		wheels.set(speed);
	}
	
	public void setArmSpeed(double speed) {
		armLeft.set(speed);
		armRight.set(speed);
	}
	
//	public void setArmMotorDisabled(boolean isDisabled) {
//		if (isDisabled) {
//			armRight.changeControlMode(TalonControlMode.Disabled);
////			armLeft.changeControlMode(TalonControlMode.Disabled);
//		} else {
//			armRight.changeControlMode(TalonControlMode.Position);
//	        armRight.setPID(Constants.INTAKE_ASSIST_P, Constants.INTAKE_ASSIST_I, Constants.INTAKE_ASSIST_D);
////			armLeft.changeControlMode(TalonControlMode.Disabled);
//		}
//	}
	
	/**
	 * If arm is going beyond zones, disable power to them.
	 */
//	public void update() {
		
		// Turn off motors if at top positions
//		if ((getArmState() == IntakeAssistState.DOWN /*&& armRight.getEncVelocity() < 0*/) ||
//				(getArmState() == IntakeAssistState.UP /*&& armRight.getEncVelocity() > 0*/)) {
//			setArmMotorDisabled(true);
//		}
		
		/*
		// maintain position
		if (!(getArmState() == IntakeAssistState.DISABLED)) {
			if (state == IntakeAssistState.UP) {
				armRight.setPosition(Constants.INTAKE_ASSIST_UP_POSITION);
			} else if (state == IntakeAssistState.DOWN) {
				armRight.setPosition(0);
			}
		}
		*/
		
//	}
	
//	private IntakeAssistState getArmState() {
//		if (armRight.getControlMode() == TalonControlMode.Disabled) {
//			return IntakeAssistState.DISABLED;
//		} else {
//			if (armRight.getEncPosition() >= Constants.INTAKE_ASSIST_UP_POSITION) {
//				return IntakeAssistState.UP;
//			} else if (armRight.getEncPosition() <= 10) {
//				return IntakeAssistState.DOWN;
//			} else {
//				return IntakeAssistState.MOVING;
//			}
//		}
//	}
	
	public void setArmEncPos(int position) {
		armRight.setEncPosition(position);
	}
	
	/**
	 * In Position mode
	 * @param isUp set position to up state or down state
	 */
//	public void setArmPosition(boolean isUp) {
//		// setArmMotorDisabled(false);
//		
//		if (isUp) {
////			if (armRight.getEncPosition() >= Constants.INTAKE_ASSIST_UP_POSITION) {
////				setArmSpeed(0.25);
////				putStringSD("ArmSpeed", "Top + 25%");
////			} else {
//				setArmSpeed(1);
//				putStringSD("ArmSpeed", " 100%");
////			}
//		} else {
////			if (armRight.getEncPosition() < 10) {
////				setArmSpeed(0);
////				putStringSD("ArmSpeed", "Bottom + 0%");
////			} else {
//				setArmSpeed(-.25);
//				putStringSD("ArmSpeed", "-25%");
////			}
//		}
//		
//		/*
//		if (isUp && getArmState() == IntakeAssistState.UP) {
//			state = IntakeAssistState.DISABLED;
//		} else if (!isUp && getArmState() == IntakeAssistState.DOWN) {
//			state = IntakeAssistState.DISABLED;
//		} else {
//			state = isUp ? IntakeAssistState.UP : IntakeAssistState.DOWN;
//		} */
//	}
	
	@Override
	public void logData() {
//		putNumberSD("WheelSpeed", wheels.getSpeed());
//		putStringSD("State", state.toString());
		putNumberSD("ArmRightGet", armRight.get());
		putNumberSD("ArmLeftGet", armLeft.get());
//		putNumberSD("ArmRightPos", armRight.getPosition());
//		putNumberSD("ArmRightEncPos", armRight.getEncPosition());
	}

	@Override
	protected void initDefaultCommand() {
	}

	@Override
	public String getFormattedName() {
		return "IntakeAssist_";
	}
	
	
}

package com.team3925.robot2016.subsystems;

import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.commands.ManualIntakeAssist;
import com.team3925.robot2016.util.Loopable;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class IntakeAssist extends Subsystem implements SmartdashBoardLoggable, Loopable {
	
	private final CANTalon wheels = RobotMap.intakeAssistWheels;
	private final CANTalon armLeft = RobotMap.intakeAssistArmLeft;
	private final CANTalon armRight = RobotMap.intakeAssistArmRight;
	
	private IntakeAssistArmMode mode = IntakeAssistArmMode.NEUTRAL;
	
	private double wheelSpeeds;
	
	public static enum IntakeAssistArmMode {
		UP, DOWN, NEUTRAL;
	}
	
	public IntakeAssist() {
		super("IntakeAssist");
		wheelSpeeds = 0;
	}
	
	/**
	 * In PercentVBus
	 * @param speed double from -1 to 1
	 */
	public void setWheelSpeeds(double speed) {
		wheelSpeeds = speed;
	}
	
	public void setArmMode(IntakeAssistArmMode mode) {
		this.mode = mode;
	}
	
	private void setArmSpeed(double speed) {
		armLeft.set(speed);
		armRight.set(speed);
	}
	
//	public void setArmEncPos(int position) {
//		armRight.setEncPosition(position);
//	}
	
	@Override
	public void update() {
		setWheelSpeeds(wheelSpeeds);
		
		switch (mode) {
		case NEUTRAL:
			setArmSpeed(0d);
			break;

		case UP:
			setArmSpeed(1d);
			break;

		case DOWN:
			setArmSpeed(-0.5);
			break;
		}
	}
	
	@Override
	public void logData() {
		putNumberSD("ArmRightGet", armRight.get());
		putNumberSD("ArmLeftGet", armLeft.get());
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new ManualIntakeAssist());
	}

	@Override
	public String getFormattedName() {
		return getName() + "_";
	}
	
}

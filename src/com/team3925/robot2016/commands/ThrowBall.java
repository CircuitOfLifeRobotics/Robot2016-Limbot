package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.command.Command;

enum Mode {
	WAIT_FOR_AIM, SHOOT, DONE;
}

public class ThrowBall extends Command implements SmartdashBoardLoggable{
	
	Launcher launcher = Robot.launcher;
	TimeoutAction timer = new TimeoutAction();
	TimeoutAction buttonTimer = new TimeoutAction();
	Mode mode;
	
	@Override
	protected void initialize() {
		mode = Mode.WAIT_FOR_AIM;
		
		launcher.setPuncher(false);
		launcher.enableAim(true);
		launcher.enableIntake(true);
		launcher.setAimSetpoint(75);
		launcher.setIntakeSetpoint(30000);
		
		buttonTimer.config(0.5);
		timer.config(3);
	}

	@Override
	protected void execute() {
		switch (mode) {
		case WAIT_FOR_AIM:
			if ((launcher.isAimOnSetpoint()/* && launcher.isIntakeOnSetpoint()*/ || timer.isFinished() || XboxHelper.getDriverButton(XboxHelper.STICK_RIGHT))&&buttonTimer.isFinished()) {
				launcher.setPuncher(true);
				mode = Mode.SHOOT;
				timer.config(0.1);
			}
			break;
		case SHOOT:
			if (timer.isFinished()) {
				launcher.setPuncher(false);
				launcher.setAimSetpoint(0);
				launcher.setIntakeSetpoint(0);
				mode = Mode.DONE;
			}
			break;
		case DONE:
			break;
		}
		
		logData();
	}
	
	@Override
	protected boolean isFinished() {
		return XboxHelper.getShooterButton(XboxHelper.START) || mode == Mode.DONE;
	}
	
	@Override
	protected void end() {
		launcher.setAimSetpoint(0);
		launcher.setIntakeSetpoint(0);
	}
	
	@Override
	protected void interrupted() {
		launcher.setAimSetpoint(0);
		launcher.setIntakeSetpoint(0);
	}

	@Override
	public void logData() {
		putBooleanSD("AimOnSetpoint", launcher.isAimOnSetpoint());
		putBooleanSD("IntakeOnSetpoint", launcher.isIntakeOnSetpoint());
		putNumberSD("AimOnSetpoint", launcher.isAimOnSetpoint() ? 1:0);
		putNumberSD("IntakeOnSetpoint", launcher.isIntakeOnSetpoint() ? 1:0);
		putStringSD("Mode", mode.toString());
	}

	@Override
	public String getFormattedName() {
		return "ThrowBall_";
	}

}

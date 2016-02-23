package com.team3925.robot2016.commands;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.command.Command;

enum Mode {
	AIM, SHOOT, DONE;
}

public class ThrowBall extends Command implements SmartdashBoardLoggable{
	
//	LauncherPID launcherPID = Robot.launcherPID;
	Launcher launcher = Robot.launcher;
	TimeoutAction timer = new TimeoutAction();
	Mode mode;
	
	@Override
	protected void initialize() {
		mode = Mode.AIM;
		
		launcher.setPuncher(false);
		launcher.enableAim(true);
		launcher.enableIntake(true);
		launcher.setAimSetpoint(65);
//		launcher.setIntakeSetpoint(-18000);
		launcher.setIntakeSetpoint(1);
		
		timer.config(2);
	}

	@Override
	protected void execute() {
		switch (mode) {
		case AIM:
			if (launcher.isAimOnSetpoint()/* && launcher.isIntakeOnSetpoint()*/ && timer.isFinished()) {
				launcher.setPuncher(true);
				mode = Mode.SHOOT;
				timer.config(0.2);
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

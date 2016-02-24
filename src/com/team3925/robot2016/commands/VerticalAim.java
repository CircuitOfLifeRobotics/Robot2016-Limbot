package com.team3925.robot2016.commands;

import static com.team3925.robot2016.Constants.CAMERA_AIMED_X;
import static com.team3925.robot2016.Constants.CAMERA_DEGS_PER_PX;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

enum VerticalAimMode {
	WAIT_LOW, CHECK_LOW, WAIT_HIGH, CHECK_HIGH, AIM, DONE;
}

public class VerticalAim extends Command implements SmartdashBoardLoggable{
	
	Launcher launcher = Robot.launcher;
	NetworkTable table = Robot.table;
	VerticalAimMode mode;
	
	private double[] centerX = null, centerY = null, area = null, width = null, height = null;
	private double aimDist;
	private boolean isData = false;
	
	@Override
	protected void initialize() {
		mode = VerticalAimMode.WAIT_LOW;
		
		launcher.setPuncher(false);
		launcher.enableAim(true);
		launcher.enableIntake(true);
		launcher.setAimSetpoint(65);
		
		try {
			new ProcessBuilder("/home/lvuser/grip").inheritIO().start();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void execute() {
		calcData();
		
		logData();
	}
	
	@Override
	protected boolean isFinished() {
		return XboxHelper.getShooterButton(XboxHelper.START);
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
	
	private void calcData() {
		centerX = table.getNumberArray("centerX", new double[0]);
		centerY = table.getNumberArray("centerY", new double[0]);
		area = table.getNumberArray("area", new double[0]);
		width = table.getNumberArray("width", new double[0]);
		height = table.getNumberArray("height", new double[0]);
		isData = centerX.length>0;
		
		if (isData) {
			//units = (      feet                    *        pixels           )/( pixels  *   tan(degrees)                   ))
			aimDist = (Constants.CAMERA_TARGET_WIDTH * Constants.CAMERA_FOV_PIX)/((width[0]-2) * Math.tan(Constants.CAMERA_FOV_DEG));
			aimDist = (14 * 320) / (2*height[0]*Math.tan(45/2));
		}
	}
	
	@Override
	public void logData() {
		putBooleanSD("AimOnSetpoint", launcher.isAimOnSetpoint());
		putBooleanSD("IntakeOnSetpoint", launcher.isIntakeOnSetpoint());
		putNumberSD("AimOnSetpoint", launcher.isAimOnSetpoint() ? 1:0);
		putNumberSD("IntakeOnSetpoint", launcher.isIntakeOnSetpoint() ? 1:0);
		
		putBooleanSD("IsData", isData);
		if (isData) {
			for (int i = 0; i < area.length; i++) {
				putNumberSD("NumContours", area.length);
				putNumberSD("CenterX [" +i+ "]", centerX[i]);
				putNumberSD("CenterY [" +i+ "]", centerY[i]);
				putNumberSD("Area [" +i+ "]", area[i]);
				putNumberSD("Width [" +i+ "]", width[i]);
				putNumberSD("Height [" +i+ "]", height[i]);
				
				putNumberSD("Dist", aimDist);
			}
		}
	}

	@Override
	public String getFormattedName() {
		return "VerticalAim_";
	}

}

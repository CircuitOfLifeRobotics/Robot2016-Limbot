package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;


public class VerticalAim extends Command implements SmartdashBoardLoggable{
	enum VerticalAimMode {
		WAIT_LOW, CHECK_LOW, WAIT_HIGH, CHECK_HIGH, AIM, DONE;
	}
	
	Launcher launcher = Robot.launcher;
	NetworkTable table = Robot.table;
	VerticalAimMode mode;
	
	private double[] centerX = null, centerY = null, area = null, width = null, height = null;
	private double distCamTarget, targetAngleRads, distCamTarget2;
	private boolean isData = false, isConnected = false;
	
	@Override
	protected void initialize() {
		mode = VerticalAimMode.WAIT_LOW;
		
		table.putBoolean("run", true);
		
		launcher.setPuncher(false);
		launcher.enableAim(true);
		launcher.enableIntake(true);
		launcher.setAimSetpoint(45);
		
	}

	@Override
	protected void execute() {
		calcData();
		
		logData();
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.oi.getCommandCancel();
	}
	
	@Override
	protected void end() {
		launcher.setAimSetpoint(0);
		launcher.setIntakeSetpoint(0);
		table.putBoolean("run", false);
	}
	
	@Override
	protected void interrupted() {
		launcher.setAimSetpoint(0);
		launcher.setIntakeSetpoint(0);
		table.putBoolean("run", false);
	}
	
	private void calcData() {
		centerX = table.getNumberArray("centerX", new double[0]);
		centerY = table.getNumberArray("centerY", new double[0]);
		area = table.getNumberArray("area", new double[0]);
		width = table.getNumberArray("width", new double[0]);
		height = table.getNumberArray("height", new double[0]);
		isConnected = table.isConnected();
		isData = centerX.length>0;
		
		if (isData) {
			//units = (      feet                    *        pixels           )/( pixels  *   tan(degrees)                   ))
			distCamTarget = (Constants.CAMERA_TARGET_WIDTH * Constants.CAMERA_FOV_PIX)/((width[0]-4) * Math.tan(Constants.CAMERA_FOV_DEG));
			targetAngleRads = Math.toRadians(width[0]/2 * Constants.CAMERA_DEGS_PER_PX);
			distCamTarget2 = Constants.CAMERA_TARGET_WIDTH/2 / Math.tan(targetAngleRads);
		}
	}
	
	@Override
	public void logData() {
		putBooleanSD("IsData", isData);
		putBooleanSD("IsConnected", isConnected);
		if (isData && isConnected) {
			for (int i = 0; i < area.length; i++) {
				putNumberSD("NumContours", area.length);
				try {putNumberSD("CenterX [" +i+ "]", centerX[i]);}catch (Exception e) {DriverStation.reportError("GRIP Messed UP!", true);}
				try {putNumberSD("CenterY [" +i+ "]", centerY[i]);}catch (Exception e) {DriverStation.reportError("GRIP Messed UP!", true);}
				try {putNumberSD("Area [" +i+ "]", area[i]);      }catch (Exception e) {DriverStation.reportError("GRIP Messed UP!", true);}
				try {putNumberSD("Width [" +i+ "]", width[i]);    }catch (Exception e) {DriverStation.reportError("GRIP Messed UP!", true);}
				try {putNumberSD("Height [" +i+ "]", height[i]);  }catch (Exception e) {DriverStation.reportError("GRIP Messed UP!", true);}
				
				putNumberSD("CamTargetDist", distCamTarget);
				putNumberSD("CamTargetDist2", distCamTarget2);
				putNumberSD("TargetAngleRads", targetAngleRads);
			}
		}
	}

	@Override
	public String getFormattedName() {
		return "VerticalAim_";
	}

}

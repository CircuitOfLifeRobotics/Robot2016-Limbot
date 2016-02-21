package com.team3925.robot2016.commands;

import static com.team3925.robot2016.Constants.CAMERA_AIMED_X;
import static com.team3925.robot2016.Constants.CAMERA_DEGS_PER_PX;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 * Shoots ball into high goal
 */
//TODO: add target selection, not just first value in data arrays
public class LaunchBallHigh extends Command implements SmartdashBoardLoggable {
	private NetworkTable table;
//	private GyroTurn turnCommand;
	private TableListener tableListener = new TableListener();
	private final LauncherPID launcherPID = Robot.launcherPID;
	
	private double[] centerX, centerY, area, width, height;
	private boolean isData = true;
	private double xOffset, xSize, yawOffset;
	
	public LaunchBallHigh() {
		
	}
	
	// Called just before this Command runs the first time
	protected void initialize() {
		table = Robot.table;
		table.addTableListener(tableListener, true);
		calcData();
		launcherPID.setAimEnabled(true);
//		turnCommand = new GyroTurn();
	}
	
	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		logData();
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return XboxHelper.getShooterButton(XboxHelper.START);
	}

	// Called once after isFinished returns true
	protected void end() {}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {}

	private void calcData() {
		centerX = table.getNumberArray("centerX", new double[0]);
		centerY = table.getNumberArray("centerY", new double[0]);
		area = table.getNumberArray("area", new double[0]);
		width = table.getNumberArray("width", new double[0]);
		height = table.getNumberArray("height", new double[0]);
		isData = centerX.length>0;
		if (isData) {
			xOffset = centerX[0] - CAMERA_AIMED_X;
			xSize = width[0];
			yawOffset = CAMERA_DEGS_PER_PX * xOffset;
		}
	}
	
	@Override
	public void logData() {
		putBooleanSD("IsData", isData);
		putNumberSD("NumContours", area.length);
		for (int i = 0; i < area.length; i++) {
			putNumberSD("CenterX [" +i+ "]", centerX[i]);
			putNumberSD("CenterY [" +i+ "]", centerY[i]);
			putNumberSD("Area [" +i+ "]", area[i]);
			putNumberSD("Width [" +i+ "]", width[i]);
			putNumberSD("Height [" +i+ "]", height[i]);
		}
		if (isData) {
			putNumberSD("XOffset [0]", xOffset);
			putNumberSD("XSize [0]", xSize);
			putNumberSD("YawOffset [0]", yawOffset);
		}
	}
	
	@Override
	public String getFormattedName() {
		return "LaunchBallHigh_";
	}
	
	private class TableListener implements ITableListener {
		@Override
		public void valueChanged(ITable source, String key, Object value, boolean isNew) {
			DriverStation.reportError("TableListener: Called calcData\n", false);
			calcData();
//			turnCommand.setSetpointRelative(yawOffset);
//			if (!turnCommand.isRunning()) {
//				turnCommand.start();
//			}
		}
		
	}
}

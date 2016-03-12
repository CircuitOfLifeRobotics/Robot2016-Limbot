package com.team3925.robot2016.commands;

import static com.team3925.robot2016.Constants.CAMERA_AIMED_X;
import static com.team3925.robot2016.Constants.CAMERA_DEGS_PER_PX;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 * Shoots ball into high goal
 */
//TODO: add target selection, not just first value in data arrays
public class LaunchBallHigh extends Command implements SmartdashBoardLoggable {
	enum Mode {
		ROUGH_AIM, HORIZ_AIM;
	}
	private NetworkTable table;
	private TableListener tableListener = new TableListener();
	private Launcher launcher = Robot.launcher;
	private TimeoutAction timeout;
	private GyroTurn gyroTurn;
	private Mode mode;
	
	private double[] centerX, centerY, area, width, height;
	private boolean isData = true;
	private double camDist, yawOffsetDegs, pixCenter;
	
	public LaunchBallHigh() {
		timeout = new TimeoutAction();
	}
	
	// Called just before this Command runs the first time
	protected void initialize() {
		camDist = yawOffsetDegs = pixCenter = 0;
		mode = Mode.ROUGH_AIM;
		
		table.addTableListener(tableListener, true);
		calcData();
		
		launcher.setAimSetpoint(45);
		launcher.setIntakeSetpoint(0);
		launcher.enableAim(true);
		launcher.enableIntake(true);
		
		timeout.config(2);
	}
	
	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		switch (mode) {
		case ROUGH_AIM:
			if (launcher.isAimOnSetpoint() || timeout.isFinished()) {
				gyroTurn = new GyroTurn(yawOffsetDegs);
				gyroTurn.start();
				timeout.config(3);
			}
			break;
		case HORIZ_AIM:
			if (!gyroTurn.isRunning()) {
				end();
			}
			break;
		}
		
		logData();
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Robot.oi.getCommandCancel();
	}

	// Called once after isFinished returns true
	protected void end() {
		table.putBoolean("run", false);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		table.putBoolean("run", false);
	}

	private void calcData() {
		centerX = table.getNumberArray("centerX", new double[0]);
		centerY = table.getNumberArray("centerY", new double[0]);
		area = table.getNumberArray("area", new double[0]);
		width = table.getNumberArray("width", new double[0]);
		height = table.getNumberArray("height", new double[0]);
		isData = centerX.length>0;
		if (isData) {
			camDist = Constants.CAMERA_TARGET_WIDTH/2 / Math.tan(Math.toRadians(width[0]/2 * Constants.CAMERA_DEGS_PER_PX));
			pixCenter = -Math.atan(Constants.CAMERA_MID_OFFSET/camDist)/Constants.CAMERA_DEGS_PER_PX + Constants.CAMERA_FOV_PIX/2;
			yawOffsetDegs = (pixCenter - centerX[0]) * Constants.CAMERA_DEGS_PER_PX;
		}
	}
	
	@Override
	public void logData() {
		putStringSD("Mode", mode.toString());
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
			putNumberSD("DistanceAway", camDist);
			putNumberSD("CenterPx", pixCenter);
			putNumberSD("DegsOffset", yawOffsetDegs);
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
		}
		
	}
}

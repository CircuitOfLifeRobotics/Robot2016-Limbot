package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * Shoots ball into high goal
 */
//TODO: add target selection, not just first value in data arrays
public class LaunchBallHigh extends Command implements SmartdashBoardLoggable {
	private final Launcher launcher = Robot.launcher;
	private NetworkTable table;
	
	private double[] centerX, centerY, area, width, height;
	private boolean isData = true;
	private double xOffset, xSize, yawOffset;
	
    public LaunchBallHigh() {
        requires(Robot.launcher);
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	table = Robot.table;
    	calcData();
    	logData();
    }
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	calcData();
    	logData();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return XboxHelper.getShooterButton(XboxHelper.START) || !isData;
    }

    // Called once after isFinished returns true
    protected void end() {
    	launcher.setIntakeSpeeds(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	launcher.setIntakeSpeeds(0);
    }
    
    private void calcData() {
    	centerX = table.getNumberArray("centerX", new double[0]);
    	centerY = table.getNumberArray("centerY", new double[0]);
    	area = table.getNumberArray("area", new double[0]);
    	width = table.getNumberArray("width", new double[0]);
    	height = table.getNumberArray("height", new double[0]);
    	isData = centerX.length>0;
    	if (isData) {
    		xOffset = centerX[0] - Constants.CAMERA_AIMED_X;
			xSize = width[0];
			yawOffset = Constants.CAMERA_DEGS_PER_PIX * xOffset;
    	}
    }

	@Override
	public void logData() {
		putBooleanSD("IsData", isData);
		putNumberSD("NumContours", area.length);
		if (isData) {
			putNumberSD("CenterX", centerX[0]);
			putNumberSD("CenterY", centerY[0]);
			putNumberSD("Area", area[0]);
			putNumberSD("Width", width[0]);
			putNumberSD("Height", height[0]);
			putNumberSD("XOffset", xOffset);
			putNumberSD("XSize", xSize);
			putNumberSD("YawOffset", yawOffset);
		}
	}

	@Override
	public String getFormattedName() {
		return "LaunchBallHigh_";
	}
}

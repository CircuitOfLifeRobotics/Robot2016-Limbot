package com.team3925.robot2016.commands;

import java.util.List;
import java.util.TimerTask;

import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.util.PixyCmu5;
import com.team3925.robot2016.util.PixyCmu5.PixyFrame;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class CameraHelper extends TimerTask {
	
	private static CameraHelper instance;
	
	private final PixyCmu5 cam;
	
	private static final double TARGET_WIDTH = 0.508;//meters
	
	private List<PixyFrame> frames;
	private PixyFrame activeFrame;
	public boolean isOnTarget;
	private boolean doDebug;
	private int numFrames;
	public double dataAge;
	public double camToTarget;
	public double timeSinceLastRun, lastTime;
	
	public CameraHelper(PixyCmu5 cam, boolean doDebug) {
		this.cam = cam;
		this.doDebug = doDebug;
		numFrames = 0;
		dataAge = timeSinceLastRun = lastTime = 0;
	}
	
	public static CameraHelper getInstance() {
		if (instance == null) {
			instance = new CameraHelper(RobotMap.pixyCam, true);
		}
		return instance;
	}
	
	public void logData(boolean doLog) {
		doDebug = doLog;
	}
	
	public double calcData() {
		frames = cam.getCurrentframes();
		dataAge = cam.getDataAge();
		numFrames = frames.size();
		activeFrame = calcBiggestFrame(frames);
		
		try {
			/* Derivation of logic:
			 * 
			 * tan(theta) = y/x
			 * tan(angle of target in FOV) = (half of target width)/(distance to target)
			 * tan((width of target px)/2 * (degs per px)) = (half of target width)/(distance to target)
			 * (distance to target) * tan((width of target px)/2 * (degs per px)) = (half of target width)
			 * (distance to target) = (half of target width) / tan((width of target px)/2 * (degs per px))
			 * 
			 * Notes:
			 * 
			 * only works at right angle to target
			 */
			camToTarget = (TARGET_WIDTH/2) / Math.tan(Math.toRadians(activeFrame.width/2 * PixyCmu5.PIXY_X_DEG_PER_PIXEL));
			
			
		}catch(Exception e) {
			DriverStation.reportError("Error calculating distance to target, frame cannot be null!", true);
		}
		return camToTarget;
	}

	private PixyFrame calcBiggestFrame(List<PixyFrame> frames) {
		int size = frames.size();
		
		if (size<=0)
			return null;
		else {
			int maxWidth = 0, activeIdx = -1;
			//loops through all the frames
			for (int i=1; i<=size; i++) {
				//stores the current frame and width of that frame (temporary)
				PixyFrame tempFrame = frames.get(i-1);
				int tempWidth = tempFrame.width;
				//compares the width of the current frame to the biggest width so far
				if (tempWidth > maxWidth) {
					//sets the new biggest width
					maxWidth = tempWidth;
					//sets the new index of the biggest frame
					activeIdx = i-1;
				}
			}
			//returns the biggest frame
			return frames.get(activeIdx);
		}
	}
	
	public void logData() {
		SmartDashboard.putNumber("TimeSinceLastRun", timeSinceLastRun);
		SmartDashboard.putNumber("DataAge", dataAge);
		SmartDashboard.putNumber("NumFrames", numFrames);
		
		//logs all frames
//		for (int i=1; i<=frames.size(); i++) {
//			PixyFrame tempFrame = frames.get(i-1);
//			SmartDashboard.putNumber("Frame["+i+"]Timestamp", tempFrame.timestamp);
//			SmartDashboard.putNumber("Frame["+i+"]Width", tempFrame.width);
//			SmartDashboard.putNumber("Frame["+i+"]Height", tempFrame.height);
//			SmartDashboard.putNumber("Frame["+i+"]CenterX", tempFrame.xCenter);
//			SmartDashboard.putNumber("Frame["+i+"]CenterY", tempFrame.yCenter);
//			SmartDashboard.putNumber("Frame["+i+"]Angle", tempFrame.angle);
//			SmartDashboard.putNumber("Frame["+i+"]Area", tempFrame.area);
//		}
		
		//logs active frame
		if (activeFrame != null) {
			SmartDashboard.putNumber("activeFrame_Timestamp", activeFrame.timestamp);
			SmartDashboard.putNumber("activeFrame_Width", activeFrame.width);
			SmartDashboard.putNumber("activeFrame_Height", activeFrame.height);
			SmartDashboard.putNumber("activeFrame_CenterX", activeFrame.xCenter);
			SmartDashboard.putNumber("activeFrame_CenterY", activeFrame.yCenter);
			SmartDashboard.putNumber("activeFrame_Angle", activeFrame.angle);
			SmartDashboard.putNumber("activeFrame_Area", activeFrame.area);
			SmartDashboard.putNumber("activeFrame_xDegOffset", cam.degreesXFromCenter(activeFrame));
			SmartDashboard.putNumber("activeFrame_CamToTarget", camToTarget);
		}
	}

	@Override
	public void run() {
		timeSinceLastRun = Timer.getFPGATimestamp()-lastTime;
		calcData();
		if (doDebug) logData();
		lastTime = Timer.getFPGATimestamp();
	}
	
}
package com.team3925.robot2016.commands;

import static com.team3925.robot2016.Constants.*;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;


public class VerticalAim extends Command implements SmartdashBoardLoggable{
	enum Mode {
		GOTO_AIM, PROCESS_DATA, GOTO_ANLGE, DONE;
	}
	
	Launcher launcher = Robot.launcher;
	NetworkTable table = Robot.table;
	GyroTurn gyroTurn = new GyroTurn();
	ThrowBall throwBall = new ThrowBall();
	TimeoutAction timeoutAction;
	Mode mode;
	
	private double[] centerX = null, centerY = null, area = null, width = null, height = null;
	private double /*distCamTarget, */targetAngleRads;
	private double camToTarget, aimPivotToTargetGnd, robotYawErrDegs;
	private double shootAngle, shootSpeed;
	private int contourIndex = 0, targetCenterPx;
	private boolean isData = false, isConnected = false;
	
	@Override
	protected void initialize() {
		table.putBoolean("run", true);
		
		launcher.setPuncher(false);
		launcher.enableAim(true);
		launcher.enableIntake(false);
		launcher.setAimSetpoint(45);
		
		shootSpeed = 25000;
		shootAngle = 45;
		
		timeoutAction = new TimeoutAction();
		
		mode = Mode.GOTO_AIM;
		timeoutAction.config(0.1);
	}

	@Override
	protected void execute() {
		switch (mode) {
		case GOTO_AIM:
			if (launcher.isAimOnSetpoint() && timeoutAction.isFinished()) {
				mode = Mode.PROCESS_DATA;
				timeoutAction.config(0.1);
			}
			break;
		case PROCESS_DATA:
			calcData();
			if (timeoutAction.isFinished()) {
				if (!isData) {
					if (launcher.getAimMotorPosition()<50)
						launcher.setAimSetpoint(70);
					else
						interrupted();
				}else if (robotYawErrDegs < GYROTURN_POS_TOLERANCE) {
					mode = Mode.DONE;
				}else {
					mode = Mode.GOTO_ANLGE;
					gyroTurn.setSetpointRelative(robotYawErrDegs);
					gyroTurn.start();
					timeoutAction.config(0.5);
				}
			}
			break;
		case GOTO_ANLGE:
			if (gyroTurn.isFinished() || timeoutAction.isFinished()) {
				mode = Mode.PROCESS_DATA;
			}
			break;
		case DONE:
			end();
			break;
		default:
			interrupted();
			break;
		}
		
		calcData();
		
		logData();
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.oi.getCommandCancel();
	}
	
	@Override
	protected void end() {
		table.putBoolean("run", false);
		
		throwBall.setAngle(shootAngle);
		throwBall.setIntakeSpeed(shootSpeed);
		throwBall.start();
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
		
		contourIndex = 0;
		
		if (isData) {
			double lastMaxWidth = 0;
			double maxWidth = 0;
			for (int i=0; i<centerX.length; i++) {
				maxWidth = Math.max(width[i], maxWidth);
				contourIndex = maxWidth==lastMaxWidth ? contourIndex:i;
				lastMaxWidth = maxWidth;
			}
			
			//units = (      feet                    *        pixels           )/( pixels  *   tan(degrees)                   ))
//			distCamTarget = (CAMERA_TARGET_WIDTH * CAMERA_FOV_PIX)/((width[0]-4) * Math.tan(CAMERA_FOV_DEG));
			targetAngleRads = Math.toRadians(width[contourIndex]/2 * CAMERA_DEGS_PER_PX);
			camToTarget = CAMERA_TARGET_WIDTH/2 / Math.tan(targetAngleRads);
			aimPivotToTargetGnd = Math.sin(Math.toRadians(launcher.getAimMotorPosition())) * (camToTarget + CAMERA_PIVOT_DIST);
			
			targetCenterPx = (int) (-Math.atan(CAMERA_MID_OFFSET/camToTarget)/CAMERA_DEGS_PER_PX + CAMERA_FOV_PIX/2);
			robotYawErrDegs = (targetCenterPx - centerX[contourIndex]) * CAMERA_DEGS_PER_PX;
			
			double discriminant = Math.pow(MAX_BALL_EXIT_VELOCITY, 4) - 
					GRAVITY*(GRAVITY*aimPivotToTargetGnd*aimPivotToTargetGnd + 
					2*CAMERA_TARGET_HEIGHT_GROUND*Math.pow(MAX_BALL_EXIT_VELOCITY, 2));
			if (discriminant>0) {
				double plusAngle = Math.toDegrees(Math.atan( (Math.pow(MAX_BALL_EXIT_VELOCITY, 2) + Math.sqrt(discriminant)) / (GRAVITY*aimPivotToTargetGnd) ));
				double minusAngle = Math.toDegrees(Math.atan( (Math.pow(MAX_BALL_EXIT_VELOCITY, 2) - Math.sqrt(discriminant)) / (GRAVITY*aimPivotToTargetGnd) ));
				shootAngle = minusAngle;
			} else {
				shootAngle = -1;
			}
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
				
//				putNumberSD("CamTargetDist", distCamTarget);
				putNumberSD("CamTargetDist", camToTarget);
				putNumberSD("RobotTargetDist", aimPivotToTargetGnd);
				putNumberSD("TargetAngleRads", targetAngleRads);
				
				putNumberSD("YawOffset", robotYawErrDegs);
				putNumberSD("PixelCenter", targetCenterPx);
				
				putNumberSD("ContourIndex", contourIndex);
			}
		}
	}

	@Override
	public String getFormattedName() {
		return "VerticalAim_";
	}

}

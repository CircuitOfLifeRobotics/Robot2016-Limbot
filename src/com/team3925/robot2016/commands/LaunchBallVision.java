package com.team3925.robot2016.commands;

import static com.team3925.robot2016.Constants.*;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.PixyCmu5;
import com.team3925.robot2016.util.PixyCmu5.PixyFrame;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

enum State {
	BEGIN_WAIT, HORIZ_AIMING, VERT_AIMING, SHOOTING, DONE;
}

public class LaunchBallVision extends Command implements SmartdashBoardLoggable {
	
	GyroTurn turnCommand;
	Command throwball;
	Launcher launcher = Robot.launcher;
	TimeoutAction timeout = new TimeoutAction();
	State state;
	private final PixyCmu5 pixy = Robot.pixy;
	
	double vertSetpoint, degFromCenter;
	
	private int centerX, centerY, area, width, height;
	private double camDist, pixCenter;
	private PixyFrame frame;
	private boolean hasTurned;
	
	public LaunchBallVision() {
		turnCommand = new GyroTurn();
		throwball = Robot.oi.throwBallFar;
	}
	
	public void reset() {
		launcher.setAimSetpoint(0);
		launcher.setIntakeSetpoint(0);
	}
	
	@Override
	protected void initialize() {
		reset();
		state = State.BEGIN_WAIT;
		hasTurned = false;
	}
	
	@Override
	protected void execute() {
		
		try {
			frame = pixy.getFrames().get(pixy.getFrames().size()-1);
			calcData();
		} catch (Exception e) {
			DriverStation.reportError("No Camera! Cannot do Vision!", true);
			this.cancel();
		}
		
//		if (!turnCommand.isRunning()) {
//			turnCommand.setSetpointRelative(degFromCenter + Constants.CAMERA_OFFSET_COMP);
//			turnCommand.start();
//			hasTurned = true;
//		}
//		
//		if (!turnCommand.isRunning() && hasTurned == true) {
//			end();
//		}
		
		/*
		switch (state) {
		case BEGIN_WAIT:
			if (timeout.isFinished()) {
				timeout.config(0);
				state = State.HORIZ_AIMING;
			}
			break;
		case HORIZ_AIMING:
			if (!turnCommand.isRunning() || timeout.isFinished()) {
				launcher.enableAim(true);
				launcher.enableIntake(true);
				launcher.setAimSetpoint(Constants.LAUNCHER_THROWBALL_NEAR_ANGLE);
				timeout.config(5);
				state = State.VERT_AIMING;
			}
			break;
		case VERT_AIMING:
			if ((launcher.isAimOnSetpoint() && launcher.isIntakeOnSetpoint()) || timeout.isFinished()) {
				launcher.setIntakeSetpoint(1);
				launcher.setPuncher(true);
				timeout.config(0.1);
				state = State.SHOOTING;
			}
			break;
		case SHOOTING:
			if (timeout.isFinished()) {
				launcher.setPuncher(false);
				launcher.setIntakeSetpoint(0);
				launcher.setAimSetpoint(0);
				state = State.DONE;
			}
			break;
		case DONE:
			end();
			break;
		}*/
		
		
		
		logData();
	}
	
	private void calcData() {
		/*
		camDist = CAMERA_TARGET_WIDTH/2 / Math.tan(Math.toRadians(width/2 * CAMERA_DEGS_PER_PX));
		pixCenter = -Math.atan(CAMERA_MID_OFFSET/camDist)/CAMERA_DEGS_PER_PX + PIXY_FOV/2;
		yawOffsetDegs = (pixCenter - centerX) * CAMERA_DEGS_PER_PX;
		*/
		
		if (!pixy.getFrames().isEmpty()) {
			centerX = frame.xCenter;
			centerY = frame.yCenter;
			area = frame.area;
			width = frame.width;
			height = frame.height;
			
			camDist = CAMERA_TARGET_WIDTH/2 / Math.tan(Math.toRadians(width/2 * PixyCmu5.PIXY_X_DEG_PER_PIXEL));
			pixCenter = -Math.atan(CAMERA_MID_OFFSET/camDist)/PixyCmu5.PIXY_X_DEG_PER_PIXEL + PixyCmu5.PIXY_X_FOV/2;
			degFromCenter = PixyCmu5.degreesXFromCenter(pixy.getCurrentframes().get(0));
		}
		
	}
	
	@Override
	protected boolean isFinished() {
		return state == State.DONE;
	}
	
	@Override
	protected void end() {
		throwball.cancel();
	}
	
	@Override
	protected void interrupted() {
		throwball.cancel();
	}

	@Override
	public void logData() {
		putStringSD("State", state.toString());
		putBooleanSD("TurnCommandRunning", turnCommand.isRunning());
		
		putNumberSD("CamDist", camDist);
		putNumberSD("PixCenter", pixCenter);
		
		putBooleanSD("Pixy_ObjectDetected", pixy.isObjectDetected());
		putBooleanSD("Pixy_ObjectDetected", pixy.isDetectedAndCentered());
		putNumberSD("Pixy_DegFromCenter", degFromCenter);
	}

	@Override
	public String getFormattedName() {
		return "LaunchBall_";
	}
	
}

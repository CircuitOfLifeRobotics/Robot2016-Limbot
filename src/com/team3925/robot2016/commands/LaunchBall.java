package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.PixyCmu5;
import com.team3925.robot2016.util.PixyCmu5.PixyFrame;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

enum State {
	BEGIN_WAIT, HORIZ_AIMING, VERT_AIMING, SHOOTING, DONE;
}

public class LaunchBall extends Command implements SmartdashBoardLoggable {
	
	GyroTurn turnCommand;
	Launcher launcher = Robot.launcher;
	TimeoutAction timeout = new TimeoutAction();
	State state;
	private final PixyCmu5 pixy = Robot.pixy;
	
	double horizDeltaSetpoint, horizDelta, vertSetpoint;
	
	private int centerX, centerY, area, width, height;
	private double camDist, yawOffsetDegs, pixCenter;
	private PixyFrame frame;
	
	public LaunchBall() {
		this(0, 0);
	}
	
	public LaunchBall(double horizAimDeltaSetpoint, double vertAimSetpoint) {
		horizDeltaSetpoint = horizAimDeltaSetpoint;
		vertSetpoint = vertAimSetpoint;
	}
	
	public void reset() {
		launcher.setAimSetpoint(0);
		launcher.setIntakeSetpoint(0);
		timeout.config(0.2);
	}
	
	@Override
	protected void initialize() {
		turnCommand = new GyroTurn(horizDeltaSetpoint);
		reset();
		state = State.BEGIN_WAIT;
		turnCommand.start();
	}
	
	@Override
	protected void execute() {
		
		frame = pixy.getFrames().get(pixy.getFrames().size()-1);
		calcData();
		
		switch (state) {
		case BEGIN_WAIT:
			if (timeout.isFinished()) {
				timeout.config(5);
				state = State.HORIZ_AIMING;
			}
			break;
		case HORIZ_AIMING:
			if (!turnCommand.isRunning() || timeout.isFinished()) {
				launcher.enableAim(true);
				launcher.enableIntake(true);
				launcher.setAimSetpoint(vertSetpoint);
				launcher.setIntakeSetpoint(20000);
				timeout.config(5);
				state = State.VERT_AIMING;
			}
			break;
		case VERT_AIMING:
			if ((launcher.isAimOnSetpoint() && launcher.isIntakeOnSetpoint()) || timeout.isFinished()) {
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
		}
		
		logData();
	}
	
	private void calcData() {
		centerX = frame.xCenter;
		centerY = frame.yCenter;
		area = frame.area;
		width = frame.width;
		height = frame.height;
		
		camDist = Constants.CAMERA_TARGET_WIDTH/2 / Math.tan(Math.toRadians(width/2 * Constants.CAMERA_DEGS_PER_PX));
		pixCenter = -Math.atan(Constants.CAMERA_MID_OFFSET/camDist)/Constants.CAMERA_DEGS_PER_PX + Constants.PIXY_FOV/2;
		yawOffsetDegs = (pixCenter - centerX) * Constants.CAMERA_DEGS_PER_PX;
	}
	
	@Override
	protected boolean isFinished() {
		return state == State.DONE;
	}
	
	@Override
	protected void end() {}
	
	@Override
	protected void interrupted() {
		launcher.setAimSetpoint(0);
		launcher.setIntakeSetpoint(0);
	}

	@Override
	public void logData() {
		putStringSD("State", state.toString());
		putBooleanSD("TurnCommandRunning", turnCommand.isRunning());
		
		putNumberSD("CamDist", camDist);
		putNumberSD("PixCenter", pixCenter);
		putNumberSD("YawOffsetDegs", yawOffsetDegs);
	}

	@Override
	public String getFormattedName() {
		return "LaunchBall_";
	}
	
}

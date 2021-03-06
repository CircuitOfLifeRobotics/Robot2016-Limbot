package com.team3925.robot2016.subsystems;

import static com.team3925.robot2016.Constants.GLOBAL_MAX_DRIVE_TRAIN_PWR;

import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.commands.ManualDrive;
import com.team3925.robot2016.subsystems.components.DriveSide;
import com.team3925.robot2016.util.DriveTrainPose;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.Loopable;
import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;


public class DriveTrain extends Subsystem implements SmartdashBoardLoggable, Loopable {
	public static interface DriveController extends SmartdashBoardLoggable /*SDL extention may be removed*/ {
		DriveTrainSignal update(DriveTrainPose pose);
		
		public boolean isFinished();
	}

	
	private final DriveSide sideLeft, sideRight;
    private final DoubleSolenoid shifterSolenoid = RobotMap.driveTrainShifterSolenoid;
    
    private DriveTrainPose cached_pose = new DriveTrainPose(0, 0, 0, 0, 0, 0);
    private DriveController controller = null;
    
    public DriveTrain() {
    	sideLeft = new DriveSide(RobotMap.driveTrainMotorLeftA, RobotMap.driveTrainMotorLeftB);
    	sideRight = new DriveSide(RobotMap.driveTrainMotorRightA, RobotMap.driveTrainMotorRightB);
    }
    
    public void setMotorSpeeds(DriveTrainSignal input) {
    	sideLeft.setSpeed(MiscUtil.limit(input.left) * GLOBAL_MAX_DRIVE_TRAIN_PWR);
    	sideRight.setSpeed(MiscUtil.limit(input.right) * GLOBAL_MAX_DRIVE_TRAIN_PWR);
    }
    
    public void setHighGear(boolean highGear) {
    	shifterSolenoid.set(highGear ? Value.kReverse : Value.kForward);
    }
    
    public void resetEncoders() {
    	// TODO: implement encoders
//    	encoderLeft.reset();
//    	encoderRight.reset();
    }
    
    public boolean isHighGear() {
    	return shifterSolenoid.get() == Value.kReverse;
    }
    
    public void startController(DriveTrain gyroDrive) {
    }
    
    public void setBrakeMode(boolean enabled) {
    	sideLeft.setBrakeMode(enabled);
    	sideRight.setBrakeMode(enabled);
    }
    
	@Override
	public void update() {
		if (controller == null) { return; }
		controller.update(getPhysicalPose());
		controller.logData();
	}
    
    /**
     * @return The pose according to the current sensor state <p>
     * rate + heading are in degrees
     */
    public DriveTrainPose getPhysicalPose() {
    	// TODO: cached_pose is not updated ever
//    	cached_pose.reset(encoderLeft.getDistance(), encoderRight.getDistance(),
//    			encoderLeft.getRate(), encoderRight.getRate(),
//    			navx.getFusedHeading(),
//    			navx.getRate());
    	return cached_pose;
    }
    
	public void arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs) {
		double leftMotorSpeed;
		double rightMotorSpeed;

		moveValue = MiscUtil.limit(moveValue);
		rotateValue = MiscUtil.limit(rotateValue);

		if (squaredInputs) {
			// square the inputs (while preserving the sign) to increase fine control
			// while permitting full power
			if (moveValue >= 0.0) {
				moveValue = (moveValue * moveValue);
			} else {
				moveValue = -(moveValue * moveValue);
			}
			if (rotateValue >= 0.0) {
				rotateValue = (rotateValue * rotateValue);
			} else {
				rotateValue = -(rotateValue * rotateValue);
			}
		}

		if (moveValue > 0.0) {
			if (rotateValue > 0.0) {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = Math.max(moveValue, rotateValue);
			} else {
				leftMotorSpeed = Math.max(moveValue, -rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			}
		} else {
			if (rotateValue > 0.0) {
				leftMotorSpeed = -Math.max(-moveValue, rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			} else {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
			}
		}
		
		setMotorSpeeds(new DriveTrainSignal(leftMotorSpeed, rightMotorSpeed));
	}
    
	@Override
	public String getFormattedName() {
		return "DriveTrain_";
	}
	
	@Override
	public void logData() {
		// Temporary
		putNumberSD("MotorLeft_Speed", sideLeft.getSpeed());
		putNumberSD("MotorRight_Speed", sideRight.getSpeed());
		
		putDriveTrainPoseSD(getPhysicalPose());
		
		putStringSD("Controller", (controller == null) ? "None" : MiscUtil.formattedNameToNormalName(controller.getFormattedName()));
	}
	
    public void initDefaultCommand() {
    	setDefaultCommand(new ManualDrive());
    }

}

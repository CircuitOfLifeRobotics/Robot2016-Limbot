package com.team3925.robot2016.subsystems;

import static com.team3925.robot2016.Constants.GLOBAL_MAX_DRIVE_TRAIN_PWR;

import com.kauailabs.navx.frc.AHRS;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.util.CheesySpeedController;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.Pose;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Subsystem;


public class DriveTrain extends Subsystem implements SmartdashBoardLoggable {

	private final AHRS navx = Robot.navx;
	private final CheesySpeedController motorsLeft = RobotMap.driveTrainMotorsLeft;
	private final CheesySpeedController motorsRight = RobotMap.driveTrainMotorsRight;
    private final Encoder encoderLeft = RobotMap.driveTrainEncoderLeft;
    private final Encoder encoderRight = RobotMap.driveTrainEncoderRight;
    private final DoubleSolenoid shifterSolenoid = RobotMap.driveTrainShifterSolenoid;
    private final PIDController pidLeft = RobotMap.driveTrainPIDLeft;
    private final PIDController pidRight = RobotMap.driveTrainPIDRight;
    
    private Pose cached_pose = new Pose(0, 0, 0, 0, 0, 0);
    
    
    public void setMotorSpeeds(DriveTrainSignal input) {
    	motorsLeft.set(MiscUtil.limit(input.left * GLOBAL_MAX_DRIVE_TRAIN_PWR));
    	motorsRight.set(MiscUtil.limit(input.right * GLOBAL_MAX_DRIVE_TRAIN_PWR));
    }
    
    public void setSetpoint(DriveTrainSignal setpoints) {
    	pidLeft.setSetpoint(setpoints.left);
    	pidRight.setSetpoint(setpoints.right);
    }
    
    public void setHighGear(boolean highGear) {
    	shifterSolenoid.set(highGear ? Value.kReverse : Value.kForward);
    }
    
    public void resetEncoders() {
    	encoderLeft.reset();
    	encoderRight.reset();
    }
    
    public boolean isHighGear() {
    	return shifterSolenoid.get() == Value.kReverse;
    }
    
    public void setPIDEnabled(boolean enabled) {
    	if (enabled) {
			pidLeft.enable();
			pidRight.enable();
		} else {
			pidLeft.disable();
			pidRight.disable();
		}
    }
    
    public boolean getPIDEnabled() {
    	return pidLeft.isEnabled() == pidRight.isEnabled() == true;
    }
    
	public boolean onTarget() {
		return pidLeft.onTarget() && pidRight.onTarget();
	}
    
    public DriveTrainSignal getEncoderRates() {
    	return new DriveTrainSignal(encoderLeft.getRate(), encoderRight.getRate());
    }
    
    /**
     * @return The pose according to the current sensor state
     */
    public Pose getPhysicalPose() {
    	cached_pose.reset(encoderLeft.getDistance(), encoderRight.getDistance(),
    			encoderLeft.getRate(), encoderRight.getRate(),
    			Math.toRadians(navx.getFusedHeading()),
    			Math.toRadians(navx.getRate()));
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
    
	public String getFormattedName() {
		return "DriveTrain_";
	}
	
	@Override
	public void logData() {
		putNumberSD("MotorsLeft_Speed", motorsLeft.get());
		putNumberSD("MotorsRight_Speed", motorsRight.get());
		
		putNumberSD("PIDLeftSetpoint", pidLeft.get());
		putNumberSD("PIDRightSetpoint", pidRight.get());
		putBooleanSD("PIDEnabled", getPIDEnabled());
		
//		maxCurLeftAbs = Math.max( Math.abs(motorsLeft.getCurrent()), maxCurLeftAbs );
//		maxCurRightAbs = Math.max( Math.abs(motorsRight.getCurrent()), maxCurRightAbs );
//		
//		putNumberSD("LeftMotors_SignedCurent", motorsLeft.getSignedCurrent());
//		putNumberSD("RightMotors_SignedCurent", motorsRight.getSignedCurrent());
//		putNumberSD("LeftMotors_MaxAbsCurrent", maxCurLeftAbs);
//		putNumberSD("RightMotors_MaxAbxCurrent", maxCurRightAbs);
		
		putBooleanSD("HighGear", isHighGear());
		
		MiscUtil.putPoseSD(getFormattedName() + "PhysicalState_", getPhysicalPose());
		
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

}


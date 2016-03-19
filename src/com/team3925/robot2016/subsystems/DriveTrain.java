package com.team3925.robot2016.subsystems;

import static com.team3925.robot2016.Constants.GLOBAL_MAX_DRIVE_TRAIN_PWR;

import com.kauailabs.navx.frc.AHRS;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.commands.ManualDrive;
import com.team3925.robot2016.util.CheesySpeedController;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.DrivetrainPose;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class DriveTrain extends Subsystem implements SmartdashBoardLoggable {

	private final AHRS navx = Robot.navx;
	private final CANTalon motorLeftA = RobotMap.driveTrainMotorLeftA;
	private final CANTalon motorRightA = RobotMap.driveTrainMotorRightA;
	private final CANTalon motorLeftB = RobotMap.driveTrainMotorLeftB;
	private final CANTalon motorRightB = RobotMap.driveTrainMotorRightB;
//	private final CANTalon motorLeftC = RobotMap.driveTrainMotorLeftC;
//	private final CANTalon motorRightC = RobotMap.driveTrainMotorRightC;
    private final Encoder encoderLeft = RobotMap.driveTrainEncoderLeft;
    private final Encoder encoderRight = RobotMap.driveTrainEncoderRight;
    private final DoubleSolenoid shifterSolenoid = RobotMap.driveTrainShifterSolenoid;
    //DELETE ON COMP BOT
    private final PIDController pidLeft = RobotMap.driveTrainPIDLeft;
    private final PIDController pidRight = RobotMap.driveTrainPIDRight;
    //END DELETE ON COMP BOT
    
    private DrivetrainPose cached_pose = new DrivetrainPose(0, 0, 0, 0, 0, 0);
    private double maxErrorLeft = 0;
    private double maxErrorRight = 0;
    
    
    
    public void setMotorSpeeds(DriveTrainSignal input) {
    	//DELETE ON COMP BOT
    	motorLeftA.set(MiscUtil.limit(input.left * GLOBAL_MAX_DRIVE_TRAIN_PWR));
    	motorRightA.set(MiscUtil.limit(input.right * GLOBAL_MAX_DRIVE_TRAIN_PWR));
    	//END DELETE ON COMP BOT
    	motorLeftB.set(MiscUtil.limit(input.left * GLOBAL_MAX_DRIVE_TRAIN_PWR));
    	motorRightB.set(MiscUtil.limit(input.right * GLOBAL_MAX_DRIVE_TRAIN_PWR));
//    	motorLeftC.set(MiscUtil.limit(input.left * GLOBAL_MAX_DRIVE_TRAIN_PWR));
//    	motorRightC.set(MiscUtil.limit(input.right * GLOBAL_MAX_DRIVE_TRAIN_PWR));
    }
    
//    public void setSetpoint(DriveTrainSignal setpoints) {
//    	pidLeft.setSetpoint(setpoints.left);
//    	pidRight.setSetpoint(setpoints.right);
//    }
    
    public void setHighGear(boolean highGear) {
//    	shifterSolenoid.set(highGear ? Value.kReverse : Value.kForward);
    }
    
    public void resetEncoders() {
    	encoderLeft.reset();
    	encoderRight.reset();
    }
    
    public boolean isHighGear() {
//    	return shifterSolenoid.get() == Value.kReverse;
    	return false;
    }
    
//    public void setPIDEnabled(boolean enabled) {
//    	if (enabled) {
//			pidLeft.reset();
//			pidRight.reset();
//			maxErrorLeft = maxErrorRight = 0;
//		} else {
//			pidLeft.disable();
//			pidRight.disable();
//		}
//    }
    
//    public boolean getPIDEnabled() {
//    	return pidLeft.isEnabled() == pidRight.isEnabled() == true;
//    }
    
//	public boolean onTarget() {
//		return pidLeft.onTarget() && pidRight.onTarget();
//	}
    
    /**
     * @return The pose according to the current sensor state
     */
    public DrivetrainPose getPhysicalPose() {
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
		putNumberSD("MotorLeft_Speed", motorLeftA.get());
		putNumberSD("MotorRight_Speed", motorRightA.get());
		
		putNumberSD("MotorLeftA_V", motorLeftA.getOutputVoltage());
		putNumberSD("MotorLeftB_V", motorLeftB.getOutputVoltage());
//		putNumberSD("MotorLeftC_V", motorLeftC.getOutputVoltage());
		putNumberSD("MotorRightA_V", motorRightA.getOutputVoltage());
		putNumberSD("MotorRightB_V", motorRightB.getOutputVoltage());
//		putNumberSD("MotorRightC_V", motorRightC.getOutputVoltage());
		
//		putNumberSD("MotorLeftA_C", motorLeftA.getOutputCurrent());
//		putNumberSD("MotorLeftB_C", motorLeftB.getOutputCurrent());
//		putNumberSD("MotorLeftC_C", motorLeftC.getOutputCurrent());
//		putNumberSD("MotorRightA_C", motorRightA.getOutputCurrent());
//		putNumberSD("MotorRightB_C", motorRightB.getOutputCurrent());
//		putNumberSD("MotorRightC_C", motorRightC.getOutputCurrent());
		
		//Commented out due to not using PID with drive train yet
//		putDataSD("PIDControllerLeft", pidLeft);
//		putDataSD("PIDControllerRight", pidRight);
		
//		putBooleanSD("PIDEnabled", getPIDEnabled());
//		putNumberSD("PIDLeftSetpoint", pidLeft.get());
//		putNumberSD("PIDRightSetpoint", pidRight.get());
//		putNumberSD("PIDLeftError", pidLeft.getError());
//		putNumberSD("PIDRightError", pidRight.getError());
//		putNumberSD("PIDLeftDeltaSetpoint", pidLeft.getDeltaSetpoint());
//		putNumberSD("PIDRightDeltaSetpoint", pidRight.getDeltaSetpoint());
//		putNumberSD("PIDLeftAverageError", pidLeft.getAvgError());
//		putNumberSD("PIDRightAverageError", pidRight.getAvgError());
//		maxErrorLeft = Math.max(maxErrorLeft, pidLeft.getError());
//		maxErrorRight = Math.max(maxErrorRight, pidRight.getError());
//		putNumberSD("PIDLeftMaxError", maxErrorLeft);
//		putNumberSD("PIDRightMaxError", maxErrorRight);
		
		
//		Commented out due to bugs with PDP and Null Pointers
//		maxCurLeftAbs = Math.max( Math.abs(motorsLeft.getCurrent()), maxCurLeftAbs );
//		maxCurRightAbs = Math.max( Math.abs(motorsRight.getCurrent()), maxCurRightAbs );
//		
//		putNumberSD("LeftMotors_SignedCurent", motorsLeft.getSignedCurrent());
//		putNumberSD("RightMotors_SignedCurent", motorsRight.getSignedCurrent());
//		putNumberSD("LeftMotors_MaxAbsCurrent", maxCurLeftAbs);
//		putNumberSD("RightMotors_MaxAbxCurrent", maxCurRightAbs);
		
//		putBooleanSD("HighGear", isHighGear());
		
		MiscUtil.putPoseSD(getFormattedName() + "PhysicalState_", getPhysicalPose());
		
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new ManualDrive());
    }

}


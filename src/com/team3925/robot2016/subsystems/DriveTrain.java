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
    private final DoubleSolenoid shifterSolenoid = RobotMap.driveTrainShifterSolenoid;
    
    private DrivetrainPose cached_pose = new DrivetrainPose(0, 0, 0, 0, 0, 0);
    private double maxErrorLeft = 0;
    private double maxErrorRight = 0;
    
    
    
    public void setMotorSpeeds(DriveTrainSignal input) {
    	motorLeftA.set(MiscUtil.limit(input.left * GLOBAL_MAX_DRIVE_TRAIN_PWR));
    	motorRightA.set(MiscUtil.limit(input.right * GLOBAL_MAX_DRIVE_TRAIN_PWR));
    	motorLeftB.set(MiscUtil.limit(input.left * GLOBAL_MAX_DRIVE_TRAIN_PWR));
    	motorRightB.set(MiscUtil.limit(input.right * GLOBAL_MAX_DRIVE_TRAIN_PWR));
    }
    
    public void setHighGear(boolean highGear) {
    	shifterSolenoid.set(highGear ? Value.kReverse : Value.kForward);
    }
    
    public void resetEncoders() {
//    	encoderLeft.reset();
//    	encoderRight.reset();
    }
    
    public boolean isHighGear() {
    	return shifterSolenoid.get() == Value.kReverse;
//    	return false;
    }
    
    
    public void setBrakeMode(boolean enabled) {
    	motorLeftA.enableBrakeMode(enabled);
    	motorLeftB.enableBrakeMode(enabled);
    	motorRightA.enableBrakeMode(enabled);
    	motorRightB.enableBrakeMode(enabled);
    }
    
    /**
     * @return The pose according to the current sensor state <p>
     * rate + heading are in degrees
     */
    public DrivetrainPose getPhysicalPose() {
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
    
	public String getFormattedName() {
		return "DriveTrain_";
	}
	
	@Override
	public void logData() {
		putNumberSD("MotorLeft_Speed", motorLeftA.get());
		putNumberSD("MotorRight_Speed", motorRightA.get());
		
		putNumberSD("EncoderLeft", motorRightA.getEncPosition());
		
		MiscUtil.putPoseSD(getFormattedName() + "PhysicalState_", getPhysicalPose());
		
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new ManualDrive());
    }

}


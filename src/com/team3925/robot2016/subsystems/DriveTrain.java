package com.team3925.robot2016.subsystems;

import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.MiscUtil;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 */
public class DriveTrain extends Subsystem {

    private final SpeedController motorLeftA = RobotMap.driveTrainMotorLeftA;
    private final SpeedController motorLeftB = RobotMap.driveTrainMotorLeftB;
    private final SpeedController motorLeftC = RobotMap.driveTrainMotorLeftC;
    private final SpeedController motorRightA = RobotMap.driveTrainMotorRightA;
    private final SpeedController motorRightB = RobotMap.driveTrainMotorRightB;
    private final SpeedController motorRightC = RobotMap.driveTrainMotorRightC;
    
    private final Encoder encoderLeft = RobotMap.driveTrainEncoderLeft;
    private final Encoder encoderRight = RobotMap.driveTrainEncoderRight;
    
    
    public void setMotorSpeeds(DriveTrainSignal input) {
    	setLeftMotorSpeeds(input.left);
    	setRightMotorSpeeds(input.right);
    }
    
    private void setLeftMotorSpeeds(double speed) {
    	motorLeftA.set(speed);
    	motorLeftB.set(speed);
    	motorLeftC.set(speed);
    }
    
    private void setRightMotorSpeeds(double speed) {
    	motorRightA.set(speed);
    	motorRightB.set(speed);
    	motorRightC.set(speed);
    }
    
    public void resetEncoders() {
    	encoderLeft.reset();
    	encoderRight.reset();
    }
    
    public DriveTrainSignal getEncoderRates() {
    	return new DriveTrainSignal(encoderLeft.getRate(), encoderRight.getRate());
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
    
    /**
     * Logs various data to SmartDashboard
     */
    public void logData() {
    	SmartDashboard.putNumber("DriveTrain_MotorLeftA", motorLeftA.get());
    	SmartDashboard.putNumber("DriveTrain_MotorLeftB", motorLeftB.get());
    	SmartDashboard.putNumber("DriveTrain_MotorLeftC", motorLeftC.get());
    	
    	SmartDashboard.putNumber("DriveTrain_MotorRightA", motorRightA.get());
    	SmartDashboard.putNumber("DriveTrain_MotorRightB", motorRightB.get());
    	SmartDashboard.putNumber("DriveTrain_MotorRightC", motorRightC.get());
    	
    	
    	SmartDashboard.putNumber("DriveTrain_EncoderLeftRate", encoderLeft.getRate());
    	SmartDashboard.putNumber("DriveTrain_EncoderRightRate", encoderRight.getRate());
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}


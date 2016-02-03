package com.team3925.robot2016.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.Pose;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class DriveTrain extends Subsystem implements SmartdashBoardLoggable {
	
    /**
     * Interface for the drivetrain controllers (straight drive, turn in place, etc)
     */
    public interface DriveController {
        DriveTrainSignal update(Pose pose);

        Pose getCurrentSetpoint();

        public boolean onTarget();

    }

	private final AHRS navx = Robot.navx;
    private final SpeedController motorLeftA = RobotMap.driveTrainMotorLeftA;
    private final SpeedController motorLeftB = RobotMap.driveTrainMotorLeftB;
    private final SpeedController motorLeftC = RobotMap.driveTrainMotorLeftC;
    private final SpeedController motorRightA = RobotMap.driveTrainMotorRightA;
    private final SpeedController motorRightB = RobotMap.driveTrainMotorRightB;
    private final SpeedController motorRightC = RobotMap.driveTrainMotorRightC;
    private final Encoder encoderLeft = RobotMap.driveTrainEncoderLeft;
    private final Encoder encoderRight = RobotMap.driveTrainEncoderRight;
    
//    private Controller controller = null;
    private Pose cached_pose = new Pose(0, 0, 0, 0, 0, 0);
    
    
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
    
    public Pose getPhysicalPose() {
    	cached_pose.reset(encoderLeft.getDistance(), encoderRight.getDistance(),
    			encoderLeft.getRate(), encoderRight.getRate(),
    			Math.toRadians(navx.getYaw()), 0); //get and check navx heading values
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
		putNumberSD("MotorLeftA", motorLeftA.get());
		putNumberSD("MotorLeftB", motorLeftB.get());
		putNumberSD("MotorLeftC", motorLeftC.get());
		
		putNumberSD("MotorRightA", motorRightA.get());
		putNumberSD("MotorRightB", motorRightB.get());
		putNumberSD("MotorRightC", motorRightC.get());
		
		putNumberSD("EncoderLeftRate", encoderLeft.getRate());
		putNumberSD("EncoderRightRate", encoderRight.getRate());
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

}


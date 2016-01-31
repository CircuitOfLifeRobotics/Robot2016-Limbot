package com.team3925.robot2016.commands;

import static com.team3925.robot2016.Constants.TRAJECTORY_FOLLOWER_KA;
import static com.team3925.robot2016.Constants.TRAJECTORY_FOLLOWER_KD;
import static com.team3925.robot2016.Constants.TRAJECTORY_FOLLOWER_KI;
import static com.team3925.robot2016.Constants.TRAJECTORY_FOLLOWER_KP;
import static com.team3925.robot2016.Constants.TRAJECTORY_FOLLOWER_KV;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.DriveTrain;
import com.team3925.robot2016.trajectory.LegacyTrajectoryFollower;
import com.team3925.robot2016.trajectory.Path;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.command.Command;

/**
 * A simpler command that is capable of moving the robot in a trajectory
 * 
 * @author Bryan
 */
public class TrajectoryFollowSimple extends Command implements SmartdashBoardLoggable {
	private final DriveTrain driveTrain = Robot.driveTrain;
	private LegacyTrajectoryFollower followerLeft = new LegacyTrajectoryFollower("left");
	private LegacyTrajectoryFollower followerRight = new LegacyTrajectoryFollower("right");
	
    public TrajectoryFollowSimple(Path path) {
        requires(Robot.driveTrain);
        
        followerLeft.configure(TRAJECTORY_FOLLOWER_KP, TRAJECTORY_FOLLOWER_KI,
        		TRAJECTORY_FOLLOWER_KD, TRAJECTORY_FOLLOWER_KV, TRAJECTORY_FOLLOWER_KA);
        followerRight.configure(TRAJECTORY_FOLLOWER_KP, TRAJECTORY_FOLLOWER_KI,
        		TRAJECTORY_FOLLOWER_KD, TRAJECTORY_FOLLOWER_KV, TRAJECTORY_FOLLOWER_KA);
        
        followerLeft.setTrajectory(path.getLeftWheelTrajectory());
        followerRight.setTrajectory(path.getRightWheelTrajectory());
        reset();
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	logData();
    }
    
    private void reset() {
    	followerLeft.reset();
    	followerRight.reset();
    }
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return followerLeft.isFinishedTrajectory() && followerRight.isFinishedTrajectory();
    }

    // Called once after isFinished returns true
    protected void end() {
    	driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
    }

	@Override
	public void logData() {
		putBooleanSD("leftIsFinished", followerLeft.isFinishedTrajectory());
		putBooleanSD("rightIsFinished", followerRight.isFinishedTrajectory());
	}

	@Override
	public String getFormattedName() {
		return "TrajectoryFollowSimple_";
	}
}

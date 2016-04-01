package com.team3925.robot2016.commands.auto;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.commands.IntakeAssistArmControl;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *	Drives the robot from the center of the arena, over a defense,
 *	and shoots the preloaded ball at the high goal
 */
public class AutoRoutineCenter extends CommandGroup {

    public AutoRoutineCenter(DefenseCrossBase initCrossCommand, int robotPos, boolean putArmsDown) {
    	//Adam's trig:
    	// atan( (13.5 + (pos-2)*4.5) / 12.5 )
    	// Math.atan( (13.5 + (robotPos-2)*4.5) / 12.5 ))
    	
    	addParallel(new IntakeAssistArmControl(putArmsDown), 15);
    	addSequential(new WaitCommand(2));
    	
    	addSequential(initCrossCommand, Constants.AUTONOMOUS_CROSS_DEFENSE_DRIVE_TIME + 0.5);
    	addSequential(new WaitCommand(.5));
    	Robot.driveTrain.setBrakeMode(false);
    	
//    	addSequential(new GyroDrive(0, true, 2, -1));
//    	addSequential(new WaitCommand(.5));
//    	Robot.driveTrain.setBrakeMode(false);
    	
    	
//    	double distToDrive = 0;
//    	double angleToTurn = 0;
//    	
//    	switch (robotPos) { // TODO Add calculations
//		case 0: // far right
//			
//			break;
//		case 1: // right
//			
//			break;
//		case 2: // center
//			
//			break;
//		case 3: // left
//			
//			break;
//		case 4: // far left
//			
//			break;
//
//		default:
//			break;
//		}
    	
//    	addSequential(new GyroDrive(distToDrive), 3);
//    	Robot.driveTrain.setBrakeMode(false);
//    	addSequential(new GyroTurn(angleToTurn));
//    	Robot.driveTrain.setBrakeMode(false);
//    	addSequential(new ThrowBall(Constants.AUTONOMOUS_SHOOT_ANGLE, 1));
    }
    
}

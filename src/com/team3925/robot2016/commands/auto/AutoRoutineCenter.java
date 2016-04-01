package com.team3925.robot2016.commands.auto;

import com.team3925.robot2016.Constants;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *	Drives the robot from the center of the arena, over a defense,
 *	and shoots the preloaded ball at the high goal
 */
public class AutoRoutineCenter extends CommandGroup {

    public AutoRoutineCenter(DefenseCrossBase initCrossCommand, int robotPos, boolean putArmsDown) {
    	addParallel(new IntakeAssistArmControl(putArmsDown), 15);
    	addSequential(new WaitCommand(2));
    	
    	addSequential(initCrossCommand, Constants.AUTONOMOUS_CROSS_DEFENSE_DRIVE_TIME);
    }
}

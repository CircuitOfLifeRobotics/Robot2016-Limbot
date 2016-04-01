package com.team3925.robot2016.commands.auto;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.commands.auto.defensecross.DefenseCrossBase;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *	Drives the robot from the center of the arena, over a defense,
 *	and shoots the preloaded ball at the high goal!
 * <p>
 * That's that plan at least
 */
public class AutoRoutineCenter extends CommandGroup {

    public AutoRoutineCenter(DefenseCrossBase initCrossCommand, int robotPos, boolean putArmsDown) {
    	addParallel(new IntakeAssistMovePosition(!putArmsDown, 15d));
    	
    	addSequential(new WaitCommand(2d)); // wait for arms to get to position
    	addSequential(initCrossCommand, Constants.AUTONOMOUS_CROSS_DEFENSE_DRIVE_TIME + 0.5 /* Buffer time */);
    	
    	// logic for different positions goes here
    }
}

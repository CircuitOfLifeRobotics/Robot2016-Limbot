package com.team3925.robot2016.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 * Auto routine that starts in the courtyard, and shoots a goal
 */
public class AutoRoutineCourtyard extends CommandGroup {

    public AutoRoutineCourtyard(double angle) {
    	addSequential(new WaitCommand(2));
    	
//    	addSequential(new ThrowBall(Constants.AUTONOMOUS_SHOOT_ANGLE, 1));
    } 
}

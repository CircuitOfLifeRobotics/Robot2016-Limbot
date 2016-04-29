package com.team3925.robot2016.commands.auto;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.commands.LaunchBall;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 * Auto routine that starts in the courtyard, and shoots a goal
 */
public class AutoRoutineCourtyard extends CommandGroup {

    public AutoRoutineCourtyard(double angle) {
    	super("AutoRoutineCourtyard");
    	
    	addSequential(new WaitCommand(3)); // wait for zero command
    	addSequential(new LaunchBall(Constants.AUTONOMOUS_SHOOT_ANGLE));
    } 
}

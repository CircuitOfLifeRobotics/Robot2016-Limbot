package com.team3925.robot2016.commands.auto;

import com.team3925.robot2016.commands.LaunchBall;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 * Auto routine that starts in the courtyard, and shoots a goal
 */
public class AutoRoutineCourtyard extends CommandGroup {

    public AutoRoutineCourtyard(double angle) {
    	super("AutoRoutine-Courtyard");
    	addSequential(new WaitCommand(4));
    	addSequential(new LaunchBall(angle));
    } 
}

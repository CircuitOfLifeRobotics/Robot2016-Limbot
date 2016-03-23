package com.team3925.robot2016.commands;

import com.team3925.robot2016.Constants;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Auto routine that starts in the courtyard, and shoots a goal
 */
public class AutoRoutineCourtyard extends CommandGroup {

    public AutoRoutineCourtyard(double angle) {
    	addSequential(new ThrowBall(Constants.AUTONOMOUS_SHOOT_ANGLE, 1));
    } 
}

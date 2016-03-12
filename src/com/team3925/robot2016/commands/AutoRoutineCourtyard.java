package com.team3925.robot2016.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Auto routine that starts in the courtyard, and shoots a goal
 */
public class AutoRoutineCourtyard extends CommandGroup {

    public AutoRoutineCourtyard() {
    	//Adam's trig:
    	//     atan( (13.5 + (pos-2)*4.5) / 12.5 )
    	
    	addSequential(new ThrowBall(75, 1));
//    	addSequential(new VerticalAim());
    } 
}

package com.team3925.robot2016.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *	Drives the robot from the center of the arena, over a defense,
 *	and shoots the preloaded ball at the high goal
 */
public class AutoRoutineCenter extends CommandGroup {


    public AutoRoutineCenter(Command initCommand, int robotPos) {
    	//Adam's trig:
    	//     atan( (13.5 + (pos-2)*4.5) / 12.5 )
    	addSequential(initCommand, 7d);
    	addSequential(new GyroTurn(Math.atan( (13.5 + (robotPos-2)*4.5) / 12.5 )));
    	addSequential(new ThrowBall(75, 1));
//    	addSequential(new VerticalAim());
    }
    
}

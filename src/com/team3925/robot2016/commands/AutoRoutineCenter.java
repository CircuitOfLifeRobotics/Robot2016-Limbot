package com.team3925.robot2016.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *	Drives the robot from the center of the arena, over a defense,
 *	and shoots the preloaded ball at the high goal
 */
public class AutoRoutineCenter extends CommandGroup {


	public AutoRoutineCenter() {

		// Add Commands here:
		// e.g. addSequential(new Command1());
		//      addSequential(new Command2());
		// these will run in order.

		// To run multiple commands at the same time,
		// use addParallel()
		// e.g. addParallel(new Command1());
		//      addSequential(new Command2());
		// Command1 and Command2 will run in parallel.

		// A command group will require all of the subsystems that each member
		// would require.
		// e.g. if Command1 requires chassis, and Command2 requires arm,
		// a CommandGroup containing them would require both the chassis and the
		// arm.

	} 
}

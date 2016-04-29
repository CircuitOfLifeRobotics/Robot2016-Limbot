package com.team3925.robot2016.commands.auto;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.commands.PickUpShootPlate;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *	Drives the robot from the center of the arena, over a defense,
 *	and shoots the preloaded ball at the high goal!
 * <p>
 * That's that plan at least
 */
public class AutoRoutineCenter extends CommandGroup {
	
    public AutoRoutineCenter(CommandGroup initCrossCommand, int robotPos, String name) {
    	super("AutoRoutineCenter-" + name);
    	
    	addSequential(initCrossCommand, Constants.AUTONOMOUS_CROSS_DEFENSE_DRIVE_TIME + 0.5 /* Buffer time */);
    	addSequential(new WaitCommand(0.5));
    	
    	// logic for different positions goes here
    	
    	addSequential(new PickUpShootPlate(true));
    
    }
	
	@Override
	protected void initialize() {
		System.out.println("[" + Timer.getFPGATimestamp() + "] " + getName() + ".init()");
	}
	
	@Override
	protected void end() {
		System.out.println("[" + Timer.getFPGATimestamp() + "] " + getName() + ".end()");
	}
    
}

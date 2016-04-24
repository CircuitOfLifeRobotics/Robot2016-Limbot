package com.team3925.robot2016.commands;

import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.command.Command;

public class Climber extends Command{
	TimeoutAction raiseTimeout = new TimeoutAction();
	com.team3925.robot2016.subsystems.Climber climber = new com.team3925.robot2016.subsystems.Climber();
	@Override
	protected void initialize() {
		climber.setRaiseSolenoid(true);
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}
	

}

package com.team3925.robot2016.commands.auto;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.util.DriveTrainSignal;

import edu.wpi.first.wpilibj.command.Command;

public class QuickDrive extends Command {
	private DriveTrainSignal signal;
	
	public QuickDrive(double timeout, DriveTrainSignal signal) {
		super("JankyDrive", timeout);
		requires(Robot.driveTrain);
		this.signal = signal;
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Robot.driveTrain.setMotorSpeeds(signal);
	}

	@Override
	protected boolean isFinished() {
		return isTimedOut();
	}

	@Override
	protected void end() {
		Robot.driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
	}

	@Override
	protected void interrupted() {
		end();
	}

}

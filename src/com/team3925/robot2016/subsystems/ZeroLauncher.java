package com.team3925.robot2016.subsystems;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.util.TimeoutAction;

import edu.wpi.first.wpilibj.command.Command;

public class ZeroLauncher extends Command {
		private boolean waitStarted;
		private final TimeoutAction timeout = new TimeoutAction();
		
		public ZeroLauncher() {
			super("Zero Command", 8);
			super.setInterruptible(false);
			requires(Robot.launcher);
		}
		
		@Override
		protected void initialize() {
			waitStarted = false;
			System.out.println("ZeroCommand.init()");
		}

		@Override
		protected void execute() {
			System.out.println("Wait Started: " + waitStarted);
			if (!waitStarted && Robot.launcher.getFwdLimitSwitch()) {
				timeout.config(Constants.LAUNCHER_ZERO_COMMAND_WAIT);
				waitStarted = true;
			}
			if (waitStarted) {
				Robot.launcher.setMotorArmSpeedRaw(0);
			} else {
				Robot.launcher.setMotorArmSpeedRaw(-.5);
			}
		}

		@Override
		protected boolean isFinished() {
			return timeout.isFinished() && waitStarted;
		}

		@Override
		protected void end() {
			Robot.launcher.setMotorArmSpeedRaw(0);
			Robot.launcher.setLauncherZeroed();
			System.out.println("Zero command ended");
		}

		@Override
		protected void interrupted() {
			// Code should never reach here. super.setInterruptible(false) is invoked
			Robot.launcher.setMotorArmSpeedRaw(0);
			System.out.println("Zero command interrupted!");
		}
		
	}
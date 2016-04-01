package com.team3925.robot2016.commands.auto.defensecross;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

public class CrossDefault extends DefenseCrossBase implements SmartdashBoardLoggable {
	
	public CrossDefault() {
		super(true);
		requires(Robot.intakeAssist);
	}

	@Override
	protected void initialize() {
		super.initialize();
	}
	
	@Override
	protected void routine() {
	}
	
	@Override
	public String getFormattedName() {
		return "CrossDefault_";
	}
	
	public void logData() {
	}
}

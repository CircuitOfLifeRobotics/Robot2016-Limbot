package com.team3925.robot2016.commands.auto.defensecross;

import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

public class CrossDefault extends DefenseCrossBase implements SmartdashBoardLoggable {
	
	public CrossDefault() {
		super(true);
	}

	@Override
	protected void initialize() {
		super.initialize();
		System.out.println(MiscUtil.formattedNameToNormalName(getFormattedName()));
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

package com.team3925.robot2016.commands.defensecommands;

import com.team3925.robot2016.util.DriveTrainSignal;

public class CrossRoughTerrain extends DefenseCrossBase {
	private DriveTrainSignal fullSpeed = new DriveTrainSignal(1d, 1d);
	
	@Override
	protected void routine() {
		driveTrain.setMotorSpeeds(fullSpeed);
	}

	@Override
	public String getFormattedName() {
		return "CrossRoughTerrain_";
	}

}

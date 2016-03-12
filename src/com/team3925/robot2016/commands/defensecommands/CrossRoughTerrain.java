package com.team3925.robot2016.commands.defensecommands;

import com.team3925.robot2016.util.DriveTrainSignal;

public class CrossRoughTerrain extends DefenseCrossBase {
	private final DriveTrainSignal MAAAXXXPOOOOOWWWWWEEEERRRRRR = new DriveTrainSignal(1d, 1d);
	
	@Override
	protected void routine() {
		driveTrain.setMotorSpeeds(MAAAXXXPOOOOOWWWWWEEEERRRRRR);
	}

	@Override
	public String getFormattedName() {
		return "CrossRoughTerrain_";
	}

}

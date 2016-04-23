package com.team3925.robot2016.subsystems;

import java.util.TimerTask;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class EncoderCheck extends TimerTask {

	Launcher launcher = new Launcher(null, null, null, null, null, null);
	double startValue = launcher.getArmPosition(); // TODO refine
	double start = Timer.getFPGATimestamp();
	private final double TOLERANCE = 100; //TODO refine tolerance
	
	public void run() {
		
		if (Timer.getFPGATimestamp() - start >= 40){
			if (compareEncoders()){
				SmartDashboard.putBoolean("THE ENCODER IS OFF!!!!! STOP BEFORE IT'S TOO LATE!!!!!!", true);
			}
			start = Timer.getFPGATimestamp();
		}
			
	}// end of run()

	private boolean compareEncoders(){
		if (startValue - launcher.getArmPosition() >= TOLERANCE){
			return true;
		} else {
			startValue = launcher.getArmPosition();
			return false;
		}
	}// end of compareEncoders()
	
}

package com.team3925.robot2016.commands;

import java.util.concurrent.BlockingQueue;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;

import edu.wpi.first.wpilibj.DriverStation;

public class EncoderGet implements Runnable{
	
	protected BlockingQueue<Double> queue = null;
	private Launcher launcher = Robot.launcher;
	private final double TOLERANCE = 50.5;
	
	public EncoderGet(BlockingQueue<Double> queue) {
		this.queue = queue;
	}

	public void init(){
		try {
			queue.put(launcher.getEncoderDistance());
			Thread.sleep(20);
			queue.put(launcher.getEncoderDistance());
			Thread.sleep(20);
			queue.put(launcher.getEncoderDistance());
			Thread.sleep(20);
			queue.put(launcher.getEncoderDistance());
		} catch (InterruptedException e) {
			DriverStation.reportError(e.getMessage(), true);;
		}		
	}// end of init
	
	public boolean checkEncoderValues() {
		try {
			if (queue.take() - launcher.getEncoderDistance() < TOLERANCE 
					|| queue.take() - launcher.getEncoderDistance() > TOLERANCE){
				queue.put(launcher.getEncoderDistance());
				return true;
			}else{
				return false;
			}	
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}// end of checkEncoderValues
	
	public void run() {
		init();
		
		while(checkEncoderValues()){
			checkEncoderValues();
		}
		
		
	}// end of run

}// end of EncoderGet class

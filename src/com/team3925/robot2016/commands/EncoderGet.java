package com.team3925.robot2016.commands;

import java.util.concurrent.BlockingQueue;

import com.team3925.robot2016.Robot;
import com.team3925.robot2016.subsystems.Launcher;

public class EncoderGet implements Runnable{
	
	protected BlockingQueue<Double> queue = null;
	private Launcher launcher = Robot.launcher;
	private final double TOLERANCE = 50.5;
	
	public EncoderGet(BlockingQueue<Double> queue) {
		this.queue = queue;
	}

	public void run() {
		try {
			queue.put(launcher.getEncoderDistance());
			Thread.sleep(20);
			queue.put(launcher.getEncoderDistance());
			Thread.sleep(20);
			queue.put(launcher.getEncoderDistance());
			Thread.sleep(20);
			queue.put(launcher.getEncoderDistance());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
	public boolean checkEncoderValues() {
		
		return false;
	}
	
}

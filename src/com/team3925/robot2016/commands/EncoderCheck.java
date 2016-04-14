package com.team3925.robot2016.commands;

import java.util.concurrent.BlockingQueue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class EncoderCheck implements Runnable{

	protected BlockingQueue<?> queue = null;

	public EncoderCheck(BlockingQueue<?> queue){
		this.queue = queue;
	}
	
	
	public void run() {
		try {
			System.out.println(queue.take());
			System.out.println(queue.take());
			System.out.println(queue.take());
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
}

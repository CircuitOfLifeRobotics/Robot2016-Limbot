package com.team3925.robot2016.commands;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueEncoder {

	public static void checkEncoders() throws Exception{
		
		BlockingQueue<Double> queue = new ArrayBlockingQueue<>(5);
		
		EncoderGet   input  = new EncoderGet(queue)  ;
		new Thread(input).start(); 
		
	}// end of checkEncoders
	
}// end of class

package com.team3925.robot2016.commands;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueEncoder {

	public static void checkEncoders() throws Exception{
		
		BlockingQueue<Double> queue = new ArrayBlockingQueue<>(7);
		
		EncoderGet   input  = new EncoderGet(queue)  ;
		EncoderCheck output = new EncoderCheck(queue);
		
		new Thread(input).start(); 
		new Thread(output).start();
		
	}
	
}// end of class

package com.team3925.robot2016.util;

public class RobotPosition {
	enum Obstacle {
		LOW_BAR, PORTCULLIS, 
	}
	
	private int fieldPos;
	private Obstacle obstacle;
	
	public RobotPosition() {
		this(0);
	}
	
	public RobotPosition(int fieldPos) {
		this(fieldPos, Obstacle.LOW_BAR);
	}
	
	public RobotPosition(Obstacle obstacle) {
		this(0, obstacle);
	}
	
	public RobotPosition(int fieldPos, Obstacle obstacle) {
		this.fieldPos = fieldPos;
		this.obstacle = obstacle;
	}
	
	public void setFieldPosition(int position) {
		fieldPos = Math.min(Math.max(position, 0), 4);
	}
	
	public void setObstacle(Obstacle obstacle) {
		this.obstacle = obstacle;
	}
	
	public int getFieldPosition() {
		return fieldPos;
	}
	
	public Obstacle getObstacle() {
		return obstacle;
	}
}

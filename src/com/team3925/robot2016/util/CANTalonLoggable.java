package com.team3925.robot2016.util;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

public class CANTalonLoggable extends CANTalon implements LiveWindowSendable {
	
	/**
	 * Live Window code, only does anything if live window is activated
	 */
	private ITable table;
	
	public CANTalonLoggable(int deviceNumber) {
		super(deviceNumber);
		// TODO Auto-generated constructor stub
	}
	
	public CANTalonLoggable(int deviceNumber, int controlPeriodMs) {
		super(deviceNumber, controlPeriodMs);
		// TODO Auto-generated constructor stub
	}
	
	public CANTalonLoggable(int deviceNumber, int controlPeriodMs, int enablePeriodMs) {
		super(deviceNumber, controlPeriodMs, enablePeriodMs);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void initTable(ITable subtable) {
		table = subtable;
		updateTable();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateTable() {
		
	}
}

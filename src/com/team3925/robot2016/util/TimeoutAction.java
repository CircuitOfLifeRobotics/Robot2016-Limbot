package com.team3925.robot2016.util;

import edu.wpi.first.wpilibj.Timer;

public class TimeoutAction {
	private double m_timeout;
    private double m_time_start;

    public TimeoutAction() {
    }

    public boolean isFinished() {
        return Timer.getFPGATimestamp() >= m_time_start + m_timeout;
    }

    public void config(double timeOut) {
    	m_timeout = timeOut;
        m_time_start = Timer.getFPGATimestamp();
    }

}

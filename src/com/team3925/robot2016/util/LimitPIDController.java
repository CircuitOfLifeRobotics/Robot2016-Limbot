package com.team3925.robot2016.util;

import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.util.BoundaryException;

public class LimitPIDController implements LiveWindowSendable {
	private double m_P; // factor for "proportional" control
	private double m_I; // factor for "integral" control
	private double m_D; // factor for "derivative" contro
	private double m_P_max;
	private double m_I_max;
	private double m_D_max;
	private double m_totalError_max;
	private double m_P_min;
	private double m_I_min;
	private double m_D_min;
	private double m_totalError_min;
	private double m_maximumOutput = 1.0; // |maximum output|
	private double m_minimumOutput = -1.0; // |minimum output|
	private double m_maximumInput = 0.0; // maximum input - limit setpoint to
											// this
	private double m_minimumInput = 0.0; // minimum input - limit setpoint to
											// this
	private boolean m_continuous = false; // do the endpoints wrap around? eg.
											// Absolute encoder
	private double m_prevError = 0.0; // the prior sensor input (used to compute
										// velocity)
	private double m_totalError = 0.0; // the sum of the errors for use in the
										// integral calc
	private double m_setpoint = 0.0;
	private double m_error = 0.0;
	private double m_P_value = 0.0;
	private double m_I_value = 0.0;
	private double m_D_value = 0.0;
	private double prev_m_D_value;
	private double avg_m_D_value;
	private double m_result = 0.0;
	private double m_last_input = Double.NaN;
	
	private ITable m_table;
	
	public LimitPIDController() {
    }

	/**
     * Allocate a PID object with the given constants for P, I, D, P limit, I limit, D limit, Total Error limit
     * If P, I, D, or total error limits are less than 0, P, I, D, and total error values are not limited
     *
     * @param Kp the proportional coefficient
     * @param Ki the integral coefficient
     * @param Kd the derivative coefficient
     */
    public LimitPIDController(double Kp, double Ki, double Kd, double maxP, double maxI, double maxD, double maxError, double minP, double minI, double minD, double minError) {
        m_P = Kp;
        m_I = Ki;
        m_D = Kd;
        m_P_max = maxP;
        m_I_max = maxI;
        m_D_max = maxD;
        m_totalError_max = maxError;
        m_P_min = minP;
        m_I_min = minI;
        m_D_min = minD;
        m_totalError_min = minError;
    }

	/**
	 * Read the input, calculate the output accordingly, and write to the
	 * output. This should be called at a constant rate by the user (ex. in a
	 * timed thread)
	 *
	 * @param input
	 *            the input
	 */
	public double calculate(double input) {
		m_last_input = input;
		m_error = m_setpoint - input;
		if (m_continuous) {
			if (Math.abs(m_error) > (m_maximumInput - m_minimumInput) / 2) {
				if (m_error > 0) {
					m_error = m_error - m_maximumInput + m_minimumInput;
				} else {
					m_error = m_error + m_maximumInput - m_minimumInput;
				}
			}
		}

		if ((m_error * m_P < m_maximumOutput) && (m_error * m_P > m_minimumOutput)) {
			m_totalError += m_error;
		} else {
			m_totalError = 0;
		}
		
		
		m_totalError = Math.max(m_totalError_min, Math.min(m_totalError_max, m_totalError));
		m_P_value = Math.max(m_P_min, Math.min(m_P_max, m_P * m_error));
		m_I_value = Math.max(m_I_min, Math.min(m_I_max, m_I * m_totalError));
		m_D_value = Math.max(m_D_min, Math.min(m_D_max, m_D * (m_error - m_prevError)));
		
		avg_m_D_value = m_D_value*0.5 + prev_m_D_value*0.5;
		
		m_result = (m_P_value + m_I_value + m_D_value);
		m_prevError = m_error;
		prev_m_D_value = avg_m_D_value;
		
//		SmartDashboard.putNumber("Launcher_PID_Avg_D_value", avg_m_D_value);
		
		if (m_result > m_maximumOutput) {
			m_result = m_maximumOutput;
		} else if (m_result < m_minimumOutput) {
			m_result = m_minimumOutput;
		}
		return m_result;
	}

	/**
	 * Set the PID controller gain parameters. Set the proportional, integral,
	 * and differential coefficients.
	 *
	 * @param p
	 *            Proportional coefficient
	 * @param i
	 *            Integral coefficient
	 * @param d
	 *            Differential coefficient
	 */
	public void setPID(double p, double i, double d) {
		m_P = p;
		m_I = i;
		m_D = d;
	}
	
	public void setPIDLimits(double pMax, double iMax, double dMax, double totalErrorMax, double pMin, double iMin, double dMin, double totalErrorMin) {
		m_P_max = (double) pMax;
		m_I_max = (double) iMax;
		m_D_max = (double) dMax;
		m_totalError_max = (double) totalErrorMax;
		m_P_min = (double) pMin;
		m_I_min = (double) iMin;
		m_D_min = (double) dMin;
		m_totalError_min = (double) totalErrorMin;
	}
	
	/**
	 * Get the total accumulated error
	 * 
	 * @return totalError
	 */
	public double getTotalError() {
		return m_totalError;
	}
	
	/**
	 * Get the Proportional coefficient
	 *
	 * @return proportional coefficient
	 */
	public double getP() {
		return m_P;
	}

	/**
	 * Get the Integral coefficient
	 *
	 * @return integral coefficient
	 */
	public double getI() {
		return m_I;
	}

	/**
	 * Get the Differential coefficient
	 *
	 * @return differential coefficient
	 */
	public double getD() {
		return m_D;
	}
	
	/**
	 * Get the Proportional limit value
	 * @return proportional limit value
	 */
	public double getPLimit() {
		return m_P_max;
	}
	
	/**
	 * Get the Integral limit value
	 * @return integral limit value
	 */
	public double getILimit() {
		return m_I_max;
	}
	
	/**
	 * Get the Derivative limit value
	 * @return derivative limit value
	 */
	public double getDLimit() {
		return m_D_max;
	}
	
	/**
	 * Get the Proportional part of the output
	 * 
	 * @return p_value
	 */
	public double getPValue() {
		return m_P_value;
	}
	
	/**
	 * Get the Integral part of the output
	 * 
	 * @return i_value
	 */
	public double getIValue() {
		return m_I_value;
	}
	
	/**
	 * Get the Derivative part of the output
	 * 
	 * @return d_value
	 */
	public double getDValue() {
		return m_D_value;
	}
	
	/**
	 * Return the current PID result This is always centered on zero and
	 * constrained the the max and min outs
	 *
	 * @return the latest calculated output
	 */
	public double get() {
		return m_result;
	}

	/**
	 * Set the PID controller to consider the input to be continuous, Rather
	 * then using the max and min in as constraints, it considers them to be the
	 * same point and automatically calculates the shortest route to the
	 * setpoint.
	 *
	 * @param continuous
	 *            Set to true turns on continuous, false turns off continuous
	 */
	public void setContinuous(boolean continuous) {
		m_continuous = continuous;
	}

	/**
	 * Set the PID controller to consider the input to be continuous, Rather
	 * then using the max and min in as constraints, it considers them to be the
	 * same point and automatically calculates the shortest route to the
	 * setpoint.
	 */
	public void setContinuous() {
		this.setContinuous(true);
	}

	/**
	 * Sets the maximum and minimum values expected from the input.
	 *
	 * @param minimumInput
	 *            the minimum value expected from the input
	 * @param maximumInput
	 *            the maximum value expected from the output
	 */
	public void setInputRange(double minimumInput, double maximumInput) {
		if (minimumInput > maximumInput) {
			throw new BoundaryException("Lower bound is greater than upper bound");
		}
		m_minimumInput = minimumInput;
		m_maximumInput = maximumInput;
		setSetpoint(m_setpoint);
	}

	/**
	 * Sets the minimum and maximum values to write.
	 *
	 * @param minimumOutput
	 *            the minimum value to write to the output
	 * @param maximumOutput
	 *            the maximum value to write to the output
	 */
	public void setOutputRange(double minimumOutput, double maximumOutput) {
		if (minimumOutput > maximumOutput) {
			throw new BoundaryException("Lower bound is greater than upper bound");
		}
		m_minimumOutput = minimumOutput;
		m_maximumOutput = maximumOutput;
	}

	/**
	 * Set the setpoint for the PID controller
	 *
	 * @param setpoint
	 *            the desired setpoint
	 */
	public void setSetpoint(double setpoint) {
		if (m_maximumInput > m_minimumInput) {
			if (setpoint > m_maximumInput) {
				m_setpoint = m_maximumInput;
			} else if (setpoint < m_minimumInput) {
				m_setpoint = m_minimumInput;
			} else {
				m_setpoint = setpoint;
			}
		} else {
			m_setpoint = setpoint;
		}
	}

	/**
	 * Returns the current setpoint of the PID controller
	 *
	 * @return the current setpoint
	 */
	public double getSetpoint() {
		return m_setpoint;
	}
	
	/**
	 * Returns the previous difference of the input from the setpoint
	 *
	 * @return the previous error
	 */
	public double getPrevError() {
		return m_prevError;
	}
	
	/**
	 * Returns the current difference of the input from the setpoint
	 *
	 * @return the current error
	 */
	public double getError() {
		return m_error;
	}

	/**
	 * Return true if the error is within the tolerance
	 *
	 * @return true if the error is less than the tolerance
	 */
	public boolean onTarget(double tolerance) {
		return m_last_input != Double.NaN && Math.abs(m_last_input - m_setpoint) < tolerance;
	}

	/**
	 * Reset all internal terms.
	 */
	public void reset() {
		m_last_input = Double.NaN;
		m_prevError = 0;
		m_totalError = 0;
		m_result = 0;
		m_setpoint = 0;
	}

	public void resetIntegrator() {
		m_totalError = 0;
	}

	public String getState() {
		String lState = "";

		lState += "Kp: " + m_P + "\n";
		lState += "Ki: " + m_I + "\n";
		lState += "Kd: " + m_D + "\n";

		return lState;
	}

	public String getType() {
		return "PIDController";
	}
	
	@Override
	public void initTable(ITable subtable) {
		m_table = subtable;
		m_table.putNumber("P_Constant", m_P);
		m_table.putNumber("I_Constant", m_I);
		m_table.putNumber("D_Constant", m_D);
		m_table.putNumber("Setpoint", m_setpoint);
		updateTable();
	}

	@Override
	public ITable getTable() {
		return m_table;
	}

	@Override
	public String getSmartDashboardType() {
		return "SynchronousPID";
	}

	@Override
	public void updateTable() {
		if (m_table != null) {
			m_table.putNumber("P_Constant", m_P);
			m_table.putNumber("I_Constant", m_I);
			m_table.putNumber("D_Constant", m_D);
			m_P = m_table.getNumber("P_Constant", 0);
			m_I = m_table.getNumber("I_Constant", 0);
			m_D = m_table.getNumber("D_Constant", 0);
		}
	}

	@Override
	public void startLiveWindowMode() {}

	@Override
	public void stopLiveWindowMode() {}
}

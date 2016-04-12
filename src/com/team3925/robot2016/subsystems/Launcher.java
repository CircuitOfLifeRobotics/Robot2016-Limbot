package com.team3925.robot2016.subsystems;

import static com.team3925.robot2016.Constants.LAUNCHER_AIM_KD;
import static com.team3925.robot2016.Constants.LAUNCHER_AIM_KI;
import static com.team3925.robot2016.Constants.LAUNCHER_AIM_KP;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.util.Loopable;
import com.team3925.robot2016.util.MiscUtil;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.SynchronousPID;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The subsystem that represents the launcher. It is responsible for the
 * keeping the arm at the desired angle and keeping the intake motors at
 * the desired speed.
 * 
 * @author Adam C
 */
public final class Launcher extends Subsystem implements SmartdashBoardLoggable, Loopable {
	
    private final CANTalon motorLeft = RobotMap.launcherMotorFar;
    private final CANTalon motorRight = RobotMap.launcherMotorNear;
    private final CANTalon motorAim = RobotMap.launcherMotorArm;
    private final DoubleSolenoid puncherSolenoid = RobotMap.launcherPuncherSolenoid;
    private SynchronousPID aimPidLoop = new SynchronousPID();
    private double turnAngle = Double.NaN;
    
    private boolean aimEnabled = false,
    		aimOnTarget = false;
	private double aimSetpoint,
			aimSetpointDiff,
			aimLastSetpoint,
			aimPosition,
			aimDifference,
			aimOutput;
	private double intakeSpeed;
    
	public void init() {
		aimPidLoop.setPID(LAUNCHER_AIM_KP, LAUNCHER_AIM_KI, LAUNCHER_AIM_KD);
		resetPID();
		
		// TODO: If the limits are this high, can this be replaced with a normal PID controller?
//		aimPidLoop.setPIDLimits(10000, 10000, 10000, 10000, -10000, -10000, -10000, -10000);
		
		setAimSetpoint(0);
		
		intakeSpeed = 0;
		aimSetpoint = 0;
		aimLastSetpoint = 0;
		
		setPuncher(false);
	}
    
	public void initDefaultCommand() {
		// a default command should not be run
		// only shoot when requested
	}
	
	@Override
	public void update() {
	
		// AIM MOTOR
		
			// TODO: refactor so this isn't needed
			if (aimEnabled) {
				aimPosition = getAimMotorPosition();
	
				aimDifference = aimSetpoint - aimPosition;
				aimSetpointDiff = aimSetpoint - aimLastSetpoint;
	
				double aimLimitedSetpoint = aimSetpoint;
				// TODO: this effectively does nothing
	//			if (Math.abs(aimSetpointDiff) > Constants.LAUNCHER_AIM_INCREMENT) {
	//				aimLimitedSetpoint = aimLastSetpoint + Constants.LAUNCHER_AIM_INCREMENT * (aimSetpointDiff>0 ? 1:-1);
	//			}
				
				aimOnTarget = Math.abs(aimDifference) < Constants.LAUNCHER_AIM_TOLERANCE
						// TODO: 2 IS MAGIC
						&& Math.abs(motorAim.getSpeed()) < 2;
				
				boolean isAtBottom = Math.abs(aimPosition - 0 /* bottom */) < Constants.LAUNCHER_AIM_TOLERANCE;
				boolean isNearTarget = Math.abs(aimDifference) < Constants.LAUNCHER_AIM_TOLERANCE;
				
				boolean doRunAim = !isAtBottom || !isNearTarget;
				putBooleanSD("DoRunAim", doRunAim);
				
				aimPidLoop.setSetpoint(aimLimitedSetpoint);
				aimOutput = aimPidLoop.calculate(aimPosition);
	
				// this scales the pid output due to the effects of gravity at different angles
				aimOutput *= 1 - (aimPosition/150)*(aimPosition/150);
	
				aimOutput = Math.min(Math.max(aimOutput, -0.2), 0.8);
	//			aimOutput = Math.min(Math.max(aimOutput, -0.2), 1);
				
				aimLastSetpoint = aimLimitedSetpoint;
				
				// TODO: fix this somehow to remove
				if (doRunAim) {
					motorAim.set(aimOutput);
				} else {
					motorAim.set(0d);
				}
			} else {
				motorAim.set(0d);
			}
			
			
			// FLYWHEELS
			
			motorLeft.set(-intakeSpeed);
			motorRight.set(intakeSpeed);
			
		}

	/**
	 * Sets the setpoint of the aim motor
	 * 
	 * @param setpoint in degrees
	 */
	public void setAimSetpoint(double setpoint) {
//		setpoint = Math.max(0, Math.min(Constants.LAUNCHER_MAX_HEIGHT, setpoint));
		aimSetpoint = setpoint;
		putNumberSD("AimSetpoint", setpoint);
	}
	
	public void enableAim(boolean isEnable) {
		aimEnabled = isEnable;
	}
	
	/**
	 * Launcher maintains intake speeds
	 * 
	 * @param speed PercentVBus; positive = out, negative = in
	 */
	public void setIntakeSpeed(double speed) {
		intakeSpeed = speed;
	}
	
	
	public void resetPID() {
		aimPidLoop.reset();
	}
	
	public void setPuncher(boolean isHigh) {
		//reverse isHigh for practice bot
		puncherSolenoid.set(isHigh ? Value.kReverse:Value.kForward);
	}
	
	public boolean getAimEnabled() {
		return aimEnabled;
	}
	
	/**
	 * Returns true if the aim position is at the specified setpoint angle
	 * and the aim is stable (not moving)
	 * 
	 * @return aimOnTarget
	 */
	public boolean isAimOnSetpoint() {
		return aimOnTarget;
	}
	
	public double getTurnAngle() {
		return turnAngle;
	}

	public double getAimMotorPosition() {
		return MiscUtil.aimEncoderTicksToDegrees(-motorAim.getEncPosition());
	}
	
	@Override
    public String getFormattedName() {
    	return "Launcher_";
    }
    
    @Override
	public void logData() {
		
		SmartDashboard.putData("Launcher_Aim_PID_L00000000oop", aimPidLoop);
		putNumberSD("AimMotorSpeed", motorAim.getSpeed());
		
		putNumberSD("EncoderAimPos", getAimMotorPosition());
		
		putNumberSD("AimDifference", aimDifference);
		putNumberSD("AimSetpoint", aimSetpoint);
		putNumberSD("AimOutput", aimOutput);
		
		putBooleanSD("PuncherSolenoid", puncherSolenoid.get() == Value.kForward);
		
		putNumberSD("MotorRightSetpoint", motorRight.getSetpoint());
		putNumberSD("MotorLeftSetpoint", motorLeft.getSetpoint());
		
		putNumberSD("WheelsSetpoint", intakeSpeed);
		
		putNumberSD("MotorAim_getOutputCurrent", motorAim.getOutputCurrent());
		putNumberSD("MotorAim_getPosition", motorAim.getPosition());
		putNumberSD("MotorAim_getSetpoint", motorAim.getSetpoint());
		
	}
}


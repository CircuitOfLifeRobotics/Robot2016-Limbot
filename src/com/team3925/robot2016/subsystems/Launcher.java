package com.team3925.robot2016.subsystems;

import static com.team3925.robot2016.Constants.LAUNCHER_AIM_MOTOR_SPEED_MULTIPLIED;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;
import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.util.LimitPIDController;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;


/**
 *
 */
public class Launcher extends Subsystem implements SmartdashBoardLoggable {
	
    private final CANTalon motorLeft = RobotMap.launcherMotorLeft;
    private final CANTalon motorRight = RobotMap.launcherMotorRight;
    private final CANTalon motorAim = RobotMap.launcherMotorAim;
    private final DoubleSolenoid puncherSolenoid = RobotMap.launcherPuncherSolenoid;
    private LimitPIDController pidLoop = new LimitPIDController();
//    Sensor for ball goes here
    
    private boolean hasBall = false;
	
	public void init() {}
    
	public double getIntakeSpeedRight() {
		return motorRight.getEncVelocity();
	}
	
	public double getIntakeSpeedLeft() {
		return motorLeft.getEncVelocity();
	}
	
	public double getIntakePosLeft() {
		return motorLeft.getEncPosition();
	}
	
	public double getIntakePosRight() {
		return motorRight.getEncPosition();
	}
	
	public void update() {
		hasBall = false; //TODO add sensor for ball
	}
	
	public boolean hasBall() {
		return hasBall;
	}
	
	public double getAimMotorPosition() {
		return -motorAim.getEncPosition();
	}
	
	public double getAimMotorSpeed() {
		return motorAim.getSpeed();
	}
	
	public void setPuncher(boolean isHigh) {
		puncherSolenoid.set(isHigh ? Value.kForward:Value.kReverse);
	}
	
    public void setIntake(double speed) {
    	motorLeft.set(speed);
    	motorRight.set(speed);
    }
    
    private void changeAimControlMode(TalonControlMode mode) {
    	motorAim.changeControlMode(mode);
    }
    
    private void changeIntakeLeftControlMode(TalonControlMode mode) {
    	motorLeft.changeControlMode(mode);
    }
    
    private void changeIntakeRightControlMode(TalonControlMode mode) {
    	motorRight.changeControlMode(mode);
    }
    
//    public void setAimProfile(int profile) {
//    	profile = Math.min(1, Math.max(0, profile));
//    	motorAim.setProfile(profile);
//    }
    
//    public void setAimPID(double p, double i, double d, double f, int izone, double closeLoopRampRate, int profile) {
//    	profile = Math.min(1, Math.max(0, profile));
//    	motorAim.setPID(p, i, d, f, izone, closeLoopRampRate, profile);
//    }
    
    public void setIntakePID(double p, double i, double d, double f, int izone, double closeLoopRampRate, int profile) {
    	motorLeft.setPID(p, i, d, f, izone, closeLoopRampRate, profile);
    	motorRight.setPID(p, i, d, f, izone, closeLoopRampRate, profile);
    }
    
    public void setIntakeProfile(int profile) {
    	motorLeft.setProfile(profile);
    	motorRight.setProfile(profile);
    }
    
    public void setAim(double output) {
    	motorAim.set(output);
    }
    
    private void setAimMotorSpeed(double speed, boolean doLimits) {
    	if (doLimits) {
	    	if (getAimMotorPosition() <= 10)
	    		speed = Math.max(speed, 0);
	    	if (getAimMotorPosition() >= (Constants.LAUNCHER_MAX_HEIGHT+10))
	    		speed = Math.min(speed, 0);
    	}
    	
    	motorAim.set(speed * LAUNCHER_AIM_MOTOR_SPEED_MULTIPLIED);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Override
    public void logData() {
//    	putNumberSD("MotorLeft", motorLeft.get());
//    	putNumberSD("MotorRight", motorRight.get());
//    	putNumberSD("AimMotorSpeed", motorAim.getSpeed());
//    	putNumberSD("MotorAimPosition", getAimMotorPosition());
//    	putStringSD("MotorAimMode", motorAim.getControlMode().toString());
    	putNumberSD("EncoderAimPos", getAimMotorPosition());
    	
    	putNumberSD("MotorRightSetpoint", motorRight.getSetpoint());
    	putNumberSD("MotorLeftSetpoint", motorLeft.getSetpoint());
    	
    	putNumberSD("MotorRightEncPos", motorRight.getEncPosition());
    	putNumberSD("MotorLeftEncPos", motorLeft.getEncPosition());
    	
    	putNumberSD("MotorRightEncVeloctiy", motorRight.getEncVelocity());
    	putNumberSD("MotorLeftEncVeloctiy", motorLeft.getEncVelocity());
    	
    	putNumberSD("MotorRightSpeed", motorRight.getSpeed());
    	putNumberSD("MotorLeftSpeed", motorLeft.getSpeed());
    	
    	putNumberSD("MotorRightError", motorRight.getError());
    	putNumberSD("MotorLeftError", motorLeft.getError());
    	
    	putNumberSD("MotorRightOutV", motorRight.getOutputVoltage());
    	putNumberSD("MotorRightOutC", motorRight.getOutputCurrent());
    	putNumberSD("MotorLeftOutV", motorLeft.getOutputVoltage());
    	putNumberSD("MotorLeftOutC", motorLeft.getOutputCurrent());
    	
//    	putNumberSD("MotorRightAnalogInRaw", motorRight.getAnalogInRaw());
//    	putNumberSD("MotorLeftAnalogInRaw", motorLeft.getAnalogInRaw());
    	
//    	putNumberSD("MotorRightPinStateQuadA", motorRight.getPinStateQuadA());
//    	putNumberSD("MotorRightPinStateQuadB", motorRight.getPinStateQuadB());
//    	putNumberSD("MotorRightPinStateQuadIdx", motorRight.getPinStateQuadIdx());
//    	putNumberSD("MotorLeftPinStateQuadA", motorLeft.getPinStateQuadA());
//    	putNumberSD("MotorLeftPinStateQuadB", motorLeft.getPinStateQuadB());
//    	putNumberSD("MotorLeftPinStateQuadIdx", motorLeft.getPinStateQuadIdx());
    	
    	//non linear loop gain
//    	putNumberSD("MotorAim_getBusVoltage", motorAim.getBusVoltage()              );
//    	putNumberSD("MotorAim_getClosedLoopError", motorAim.getClosedLoopError()    );
//    	putNumberSD("MotorAim_getCloseLoopRampRate", motorAim.getCloseLoopRampRate());
//    	putNumberSD("MotorAim_getP", motorAim.getP()                                );
//    	putNumberSD("MotorAim_getI", motorAim.getI()                                );
//    	putNumberSD("MotorAim_getD", motorAim.getD()                                );
//    	putNumberSD("MotorAim_getF", motorAim.getF()                                );
//    	putNumberSD("MotorAim_getEncPosition", motorAim.getEncPosition()            );
//    	putNumberSD("MotorAim_getEncVelocity", motorAim.getEncVelocity()            );
//    	putNumberSD("MotorAim_getError", motorAim.getError()                        );
//    	putNumberSD("MotorAim_getIZone", motorAim.getIZone()                        );
//    	putNumberSD("MotorAim_getOutputCurrent", motorAim.getOutputCurrent()        );
//    	putNumberSD("MotorAim_getPosition", motorAim.getPosition()                  );
//    	putNumberSD("MotorAim_getSetpoint", motorAim.getSetpoint()                  );
//    	putNumberSD("MotorAim_getSpeed", motorAim.getSpeed()                        );
//    	putNumberSD("MotorAim_getPidGet", motorAim.pidGet()                         );
//    	putNumberSD("MotorAim_PinStateA", motorAim.getPinStateQuadA());
//    	putNumberSD("MotorAim_PinStateB", motorAim.getPinStateQuadB());
//    	putNumberSD("MotorAim_PinStateIdx", motorAim.getPinStateQuadIdx());
//    	putBooleanSD("MotorAim_getInverted", motorAim.getInverted()                 );
    }
    
    @Override
    public String getFormattedName() {
    	return "Launcher_";
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}


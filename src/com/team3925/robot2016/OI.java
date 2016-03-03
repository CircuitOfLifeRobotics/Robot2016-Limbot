package com.team3925.robot2016;

import static com.team3925.robot2016.util.XboxHelper.*;

import javax.sound.midi.ControllerEventListener; // Too Legit 4 Me

import com.team3925.robot2016.commands.CollectBall;
import com.team3925.robot2016.commands.GyroTurn;
import com.team3925.robot2016.commands.ThrowBall;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public final class OI {
    /* CREATING BUTTONS
     One type of button is a joystick button which is any button on a joystick.
     You create one by telling it which joystick it's on and which button
     number it is.
     Joystick stick = new Joystick(port);
     Button button = new JoystickButton(stick, buttonNumber);

     There are a few additional built in buttons you can use. Additionally,
     by subclassing Button you can create custom triggers and bind those to
     commands the same as any other Button.

    // TRIGGERING COMMANDS WITH BUTTONS
     Once you have a button, it's trivial to bind it to a button in one of
     three ways:

     Start the command when the button is pressed and let it run the command
     until it is finished as determined by it's isFinished method.
     button.whenPressed(new ExampleCommand());

     Run the command while the button is being held down and interrupt it once
     the button is released.
     button.whileHeld(new ExampleCommand());

     Start the command when the button is released  and let it run the command
     until it is finished as determined by it's isFinished method.
     button.whenReleased(new ExampleCommand()); */

    public Joystick xboxDriver;
    public Joystick xboxShooter;
    public Button startCollectBall;
    public Button startThrowBall;
    public Button startGyroTurn;
    public Button cancelCommands;
    public Command collectBall;
    public Command throwBall;
    public Command gyroTurn;
    
    public OI() {
    	
    	xboxDriver = new Joystick(0);
    	xboxShooter = new Joystick(1);
    	
    	collectBall = new CollectBall();
    	throwBall = new ThrowBall();
    	gyroTurn = new GyroTurn(45);
    	
    	startCollectBall = new JoystickButton(xboxDriver, XboxHelper.A);
    	startCollectBall.whenPressed(collectBall);
    	startCollectBall.cancelWhenPressed(throwBall);
    	startCollectBall.cancelWhenPressed(gyroTurn);
		
    	startThrowBall = new JoystickButton(xboxDriver, XboxHelper.Y);
    	startThrowBall.whenPressed(throwBall);
    	startThrowBall.cancelWhenPressed(collectBall);
    	startThrowBall.cancelWhenPressed(gyroTurn);
    	
    	startGyroTurn = new JoystickButton(xboxDriver, XboxHelper.X);
    	startGyroTurn.whenPressed(gyroTurn);
    	startGyroTurn.cancelWhenActive(throwBall);
    	startGyroTurn.cancelWhenActive(collectBall);
    	
    	cancelCommands = new JoystickButton(xboxDriver, XboxHelper.START);
    	cancelCommands.cancelWhenPressed(collectBall);
    	cancelCommands.cancelWhenPressed(throwBall);
    	cancelCommands.cancelWhenActive(gyroTurn);
    	
        // SmartDashboard Buttons
//        SmartDashboard.putData("ManualDrive", new ManualDrive());
//        SmartDashboard.putData("LaunchBall", new LaunchBallHigh());
//        SmartDashboard.putData("CollectBall", new CollectBall());
//        SmartDashboard.putData("AutoRoutineCenter", new AutoRoutineCenter());
//        SmartDashboard.putData("AutoRoutineCourtyard", new AutoRoutineCourtyard());
        
//    	SmartDashboard.putData("ThrowBall", new ThrowBall());
//    	SmartDashboard.putData("FeedBall", new FeedBall());
    	
    }
    
    
    
    // ROBOT BEHAVIOR
    
    public double getManualDrive_ForwardValue() {
    	return XboxHelper.getDriverAxis(AXIS_LEFT_Y);
    }

	public double getManualDrive_RotateValue() {
		return XboxHelper.getDriverAxis(AXIS_RIGHT_X);
	}
	
	public boolean getManualDrive_HighGearToggle() {
		return XboxHelper.getDriverButton(XboxHelper.TRIGGER_LT) || XboxHelper.getDriverButton(XboxHelper.TRIGGER_RT);
	}
	
	public boolean getStartCandyCanes() {
		return XboxHelper.getShooterButton(XboxHelper.START);
	}
	
	public boolean getLauncher_ResetIntakeSetpoint() {
		return XboxHelper.getShooterButton(START);
	}
	
	public boolean getThrowBall_LaunchBallOverride() {
		return XboxHelper.getDriverButton(XboxHelper.STICK_RIGHT);
	}
	
	public double getManualArms_ClimberValue() {
		return XboxHelper.getShooterAxis(XboxHelper.AXIS_RIGHT_Y);
	}
	
	public boolean getCandyCanes_GoUp() { // TODO check
		return XboxHelper.getShooterPOV()>270 || XboxHelper.getShooterPOV()<90;
	}
	
	public boolean getCandyCanes_GoDown() {
		return XboxHelper.getShooterPOV()>90 && XboxHelper.getShooterPOV()<270;
	}
	
	public boolean getManualArms_GetArmValue() {
		return XboxHelper.getDriverAxis(XboxHelper.AXIS_TRIGGER_LEFT)>0.5 || XboxHelper.getDriverAxis(XboxHelper.AXIS_TRIGGER_RIGHT)>0.5;
	}
	
	public boolean getCommandCancel() {
		return XboxHelper.getShooterButton(START) || XboxHelper.getDriverButton(START);
	}
	
}


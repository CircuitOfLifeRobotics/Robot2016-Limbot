package com.team3925.robot2016;

import static com.team3925.robot2016.util.XboxHelper.AXIS_LEFT_Y;
import static com.team3925.robot2016.util.XboxHelper.AXIS_RIGHT_X;
import static com.team3925.robot2016.util.XboxHelper.START;

import java.text.DecimalFormat;

import com.team3925.robot2016.commands.AutoRoutineCenter;
import com.team3925.robot2016.commands.AutoRoutineCourtyard;
import com.team3925.robot2016.commands.AutoRoutineDoNothing;
import com.team3925.robot2016.commands.CollectBall;
import com.team3925.robot2016.commands.GyroTurn;
import com.team3925.robot2016.commands.LaunchBallLow;
import com.team3925.robot2016.commands.ThrowBall;
import com.team3925.robot2016.commands.VerticalAim;
import com.team3925.robot2016.commands.defensecommands.CrossDefault;
import com.team3925.robot2016.commands.defensecommands.CrossRoughTerrain;
import com.team3925.robot2016.commands.defensecommands.DefenseCrossBase;
import com.team3925.robot2016.util.RobotPosition;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


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
	public Button startThrowBallFar;
	public Button startThrowBallNear;
	public Button startLow;
	public Button startVision;
	public Button cancelCommands;
	
	public Command visionShoot;
	public Command collectBall;
	public Command throwBallFar;
	public Command throwBallNear;
	public Command throwBallLow;
//	public Command gyroTurn;

	public SendableChooser autoChooser;
	public SendableChooser throwBallTesting;
	private static DecimalFormat df = new DecimalFormat("#.##");

	public SendableChooser positionChooser;

	public OI() {

		xboxDriver = new Joystick(0);
		xboxShooter = new Joystick(1);

		collectBall = new CollectBall();
		throwBallFar = new ThrowBall(65, 1);
		throwBallNear = new ThrowBall(80, 1);
		throwBallLow = new ThrowBall(20, 1);
		visionShoot = new VerticalAim();


		startCollectBall = new JoystickButton(xboxShooter, XboxHelper.A);
		startCollectBall.whenPressed(collectBall);
		startCollectBall.cancelWhenPressed(throwBallNear);
		startCollectBall.cancelWhenPressed(throwBallFar);
		startCollectBall.cancelWhenPressed(throwBallLow);
		startCollectBall.cancelWhenPressed(visionShoot);

		startThrowBallFar = new JoystickButton(xboxShooter, XboxHelper.Y);
		startThrowBallFar.whenPressed(throwBallFar);
		startThrowBallFar.cancelWhenPressed(collectBall);
		startThrowBallFar.cancelWhenPressed(throwBallNear);
		startThrowBallFar.cancelWhenPressed(throwBallLow);
		startThrowBallFar.cancelWhenPressed(visionShoot);
		
		startThrowBallNear = new JoystickButton(xboxShooter, XboxHelper.X);
		startThrowBallNear.whenPressed(throwBallNear);
		startThrowBallNear.cancelWhenPressed(throwBallFar);
		startThrowBallNear.cancelWhenPressed(collectBall);
		startThrowBallNear.cancelWhenPressed(throwBallLow);
		startThrowBallNear.cancelWhenPressed(visionShoot);
		
		startLow = new JoystickButton(xboxShooter, XboxHelper.B);
		startLow.whenPressed(throwBallLow);
		startLow.cancelWhenPressed(throwBallFar);
		startLow.cancelWhenPressed(collectBall);
		startLow.cancelWhenPressed(throwBallNear);
		startLow.cancelWhenPressed(visionShoot);
		
//		startVision = new JoystickButton(xboxDriver, XboxHelper.BACK);
//		startVision.whenPressed(visionShoot);
//		startVision.cancelWhenPressed(throwBallFar);
//		startVision.cancelWhenPressed(collectBall);
//		startVision.cancelWhenPressed(throwBallNear);
//		startVision.cancelWhenPressed(throwBallLow);
		
		cancelCommands = new JoystickButton(xboxShooter, XboxHelper.START);
		cancelCommands.cancelWhenPressed(collectBall);
		cancelCommands.cancelWhenPressed(throwBallFar);
		cancelCommands.cancelWhenPressed(throwBallNear);
		cancelCommands.cancelWhenPressed(throwBallLow);
		cancelCommands.cancelWhenPressed(visionShoot);

		positionChooser = new SendableChooser();
		positionChooser.addDefault("1 - Far Right", new Integer(0));
		positionChooser.addObject("2 - Near Right", new Integer(1));
		positionChooser.addObject("3 - Middle", new Integer(2));
		positionChooser.addObject("4 - Near Left", new Integer(3));
		positionChooser.addObject("5 - Far Left", new Integer(4));
		
		//NATHAN IS THE BEST
		autoChooser = new SendableChooser();
		
		autoChooser.addDefault("DO NOTHING", new AutoRoutineDoNothing());
		autoChooser.addObject("Courtyard Freebie Shot", new AutoRoutineCourtyard());
		
		autoChooser.addObject("Low Bar", new CrossDefault());
//		autoChooser.addObject("Portcullis", new CrossLowBar());
//		autoChooser.addObject("Chival De Frise", new CrossLowBar());
//		autoChooser.addObject("Draw Bridge", new CrossLowBar());
		autoChooser.addObject("Moat", new CrossDefault());
		autoChooser.addObject("Rock Wall", new CrossDefault());
		autoChooser.addObject("Rough Terrain", new CrossDefault());
//		autoChooser.addObject("Sally Port", new CrossLowBar());
		autoChooser.addObject("Ramparts", new CrossDefault());


//		SmartDashboard.putData("Position Chooser", positionChooser);
		SmartDashboard.putData("VisionShoot", visionShoot);


		// SmartDashboard Buttons
		//        SmartDashboard.putData("ManualDrive", new ManualDrive());
		//        SmartDashboard.putData("LaunchBall", new LaunchBallHigh());
		//        SmartDashboard.putData("CollectBall", new CollectBall());
		//        SmartDashboard.putData("AutoRoutineCenter", new AutoRoutineCenter());
		//        SmartDashboard.putData("AutoRoutineCourtyard", new AutoRoutineCourtyard());

		//    	SmartDashboard.putData("ThrowBall", new ThrowBall());
		//    	SmartDashboard.putData("FeedBall", new FeedBall());

	}

	private double calcThrowBallSpeed(double percentage) {
		return Constants.LAUNCHER_MAX_INTAKE_SPEED * (double) percentage / 100d;
	}

	private void addThrowBallValue(double percentage) {
		throwBallTesting.addObject("ThrowBall (" + df.format(calcThrowBallSpeed(percentage)) + ", " + percentage + "%)",
				new ThrowBall(calcThrowBallSpeed(percentage)));
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
	
	public double getCandyCanes_Set() {
		return -XboxHelper.getShooterAxis(XboxHelper.AXIS_LEFT_Y);
	}

	public boolean getManualArms_GetArmValue() {
		return XboxHelper.getDriverAxis(XboxHelper.AXIS_TRIGGER_LEFT)>0.5 || XboxHelper.getDriverAxis(XboxHelper.AXIS_TRIGGER_RIGHT)>0.5;
	}

	public boolean getCommandCancel() {
		return XboxHelper.getShooterButton(START) || XboxHelper.getDriverButton(START);
	}

	public CommandGroup setAutonomous() {
//		return new RobotPosition(((RobotPosition)positionChooser.getSelected()).getFieldPosition(), ((RobotPosition)obstacleChooser.getSelected()).getObstacle());
//		if (autoChooser.getSelected() instanceof AutoRoutineDoNothing) {
//			return (CommandGroup) autoChooser.getSelected();
//			
//		} else if (autoChooser.getSelected() instanceof AutoRoutineCourtyard) {
//			return (CommandGroup) autoChooser.getSelected();
//			
//		} else if (autoChooser.getSelected() instanceof DefenseCrossBase) {
//			return new AutoRoutineCenter((Command) autoChooser.getSelected(), (int) positionChooser.getSelected());
//			
//		} else {
//			DriverStation.reportError("Defaulted in autonomous selection!", false);
//			return new AutoRoutineDoNothing();
//		}
		
		CommandGroup janky = new CommandGroup();
		janky.addSequential(new  CrossDefault(), 6);
		janky.addSequential(new GyroTurn(45));
		janky.addSequential(new GyroTurn(-45));
		
		return janky;
		
	}

}


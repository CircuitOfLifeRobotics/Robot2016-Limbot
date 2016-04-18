package com.team3925.robot2016;

import static com.team3925.robot2016.util.hidhelpers.XboxHelper.START;

import com.team3925.robot2016.commands.CollectBall;
import com.team3925.robot2016.commands.LaunchBall;
import com.team3925.robot2016.commands.auto.AutoRoutineCenter;
import com.team3925.robot2016.commands.auto.defensecross.CrossDefault;
import com.team3925.robot2016.util.hidhelpers.FlightStickHelper;
import com.team3925.robot2016.util.hidhelpers.ThrustmasterHelper;
import com.team3925.robot2016.util.hidhelpers.XboxHelper;

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

	public Joystick driverFlightstick;
	public Joystick driverWheel;
	public Joystick shooterXbox;

	public Button startCollectBall;
	public Button startThrowBallFar;
	public Button startThrowBallNear;
	public Button startThrowBallLow;
	public Button cancelCommands;

	public Command collectBall;
	public Command throwBallFar;
//	public Command throwBallNear;
//	public Command throwBallLow;
//	public GyroTurn gyroTurn;

	public SendableChooser autoChooser;
	public SendableChooser throwBallTesting;

	public SendableChooser positionChooser;

	public OI() {

		driverFlightstick = new Joystick(0);
		driverWheel = new Joystick(1);
		shooterXbox = new Joystick(2);

		collectBall = new CollectBall();
		throwBallFar = new LaunchBall();
//		throwBallNear = new ThrowBall(Constants.LAUNCHER_THROWBALL_NEAR_ANGLE, 1, 5);
//		throwBallLow = new ThrowBall(0, 1, 1);

		startCollectBall = new JoystickButton(shooterXbox, XboxHelper.A);
		startCollectBall.whenPressed(collectBall);
		startCollectBall.cancelWhenPressed(throwBallFar);
//		startCollectBall.cancelWhenPressed(throwBallNear);
//		startCollectBall.cancelWhenPressed(throwBallLow);

		startThrowBallFar = new JoystickButton(shooterXbox, XboxHelper.Y);
		startThrowBallFar.whenPressed(throwBallFar);
		startThrowBallFar.cancelWhenPressed(collectBall);
//		startThrowBallFar.cancelWhenPressed(throwBallNear);
//		startThrowBallFar.cancelWhenPressed(throwBallLow);

		startThrowBallNear = new JoystickButton(shooterXbox, XboxHelper.X);
//		startThrowBallNear.whenPressed(throwBallNear);
		startThrowBallNear.cancelWhenPressed(collectBall);
		startThrowBallNear.cancelWhenPressed(throwBallFar);
//		startThrowBallNear.cancelWhenPressed(throwBallLow);
		
		startThrowBallLow = new JoystickButton(shooterXbox, XboxHelper.B);
//		startThrowBallLow.whenPressed(throwBallLow);
		startThrowBallLow.cancelWhenPressed(collectBall);
		startThrowBallLow.cancelWhenPressed(throwBallFar);
//		startThrowBallLow.cancelWhenPressed(throwBallNear);
		
		cancelCommands = new JoystickButton(shooterXbox, XboxHelper.START);
		cancelCommands.cancelWhenPressed(collectBall);
		cancelCommands.cancelWhenPressed(throwBallFar);
//		cancelCommands.cancelWhenPressed(throwBallNear);
//		cancelCommands.cancelWhenPressed(throwBallLow);

		positionChooser = new SendableChooser();
		positionChooser.addDefault("1 - Far Right", new Integer(0));
		positionChooser.addObject("2 - Near Right", new Integer(1));
		positionChooser.addObject("3 - Middle", new Integer(2));
		positionChooser.addObject("4 - Near Left", new Integer(3));
		positionChooser.addObject("5 - Far Left", new Integer(4));

		//NATHAN IS THE BEST
		autoChooser = new SendableChooser();

//		autoChooser.addDefault("DO NOTHING", new AutoRoutineDoNothing());
		autoChooser.addDefault("Cross-ArmsUP", new AutoRoutineCenter(new CrossDefault(), 0));
		autoChooser.addObject("Cross-ArmsDOWN", new AutoRoutineCenter(new CrossDefault(), 0));
//		autoChooser.addObject("Courtyard Freebie Shot", new AutoRoutineCourtyard(Constants.AUTONOMOUS_SHOOT_ANGLE));
//
//				autoChooser.addObject("Portcullis", new CrossDefault());
//		//		autoChooser.addObject("Chival De Frise", new CrossLowBar());
//		//		autoChooser.addObject("Draw Bridge", new CrossLowBar());
//		autoChooser.addObject("Moat", new CrossDefault());
//		autoChooser.addObject("Rock Wall", new CrossDefault());
//		autoChooser.addObject("Rough Terrain", new CrossDefault());
//		//		autoChooser.addObject("Sally Port", new CrossLowBar());
//		autoChooser.addObject("Ramparts", new CrossDefault());


		SmartDashboard.putData("Position Chooser", positionChooser);
		SmartDashboard.putData("Autonomous Chooser", autoChooser);


		// SmartDashboard Buttons
		// SmartDashboard.putData("ManualDrive", new ManualDrive());
		// SmartDashboard.putData("LaunchBall", new LaunchBallHigh());
		// SmartDashboard.putData("CollectBall", new CollectBall());
		// SmartDashboard.putData("AutoRoutineCenter", new AutoRoutineCenter());
		// SmartDashboard.putData("AutoRoutineCourtyard", new AutoRoutineCourtyard());

		// SmartDashboard.putData("ThrowBall", new ThrowBall());
		// SmartDashboard.putData("FeedBall", new FeedBall());

	}
	

	public CommandGroup setAutonomous() {
		// return new RobotPosition(((RobotPosition)positionChooser.getSelected()).getFieldPosition(), ((RobotPosition)obstacleChooser.getSelected()).getObstacle());
		
//		Object selected = autoChooser.getSelected();
//		SendableChooser selected = ((SendableChooser) SmartDashboard.getData("Autonomous Chooser")).getSelected();
//		SmartDashboard.putString("AutoSelected", selected.toString());
//		return (CommandGroup) selected;
		
		return new AutoRoutineCenter(new CrossDefault(), 0);
		
//		if (selected instanceof AutoRoutineDoNothing) {
//			return (CommandGroup) selected;
//
//		} else if (selected instanceof AutoRoutineCourtyard) {
//			return (CommandGroup) selected;
//
//		} else if (selected instanceof DefenseCrossBase) {
//			return new AutoRoutineCenter((DefenseCrossBase) selected, (int) positionChooser.getSelected());
//
//		} else {
//			DriverStation.reportError("Defaulted in autonomous selection!", false);
//			return new AutoRoutineDoNothing();
//		}
		
		// CommandGroup janky = new CommandGroup();
		// janky.addSequential(new  CrossDefault(), 6);
		// janky.addSequential(new GyroTurn(45));
		// janky.addSequential(new GyroTurn(-45));
		
		// return janky;

	}
	
	

	// ROBOT BEHAVIOR

	public double getManualDrive_ForwardValue() {
		return -FlightStickHelper.getAxis(FlightStickHelper.AXIS_Y);
	}
	
	public boolean getVisionShoot_GyroTurnEnable() {
		return false; //TODO add user input
	}

	public double getManualDrive_RotateValue() {
		return -ThrustmasterHelper.getAxis(ThrustmasterHelper.AXIS_WHEEL);
	}

	public boolean getManualDrive_HighGearToggle() {
		return false; //TODO Figure out what to do with this
	}

	public boolean getManualDrive_QuickTurn() {
		return ThrustmasterHelper.getButton(ThrustmasterHelper.PADDLE_LEFT) ||
				ThrustmasterHelper.getButton(ThrustmasterHelper.PADDLE_RIGHT);
	}

	public boolean getStartCandyCanes() {
		return XboxHelper.getShooterButton(XboxHelper.START);
	}

	public boolean getLauncher_ResetIntakeSetpoint() {
		return XboxHelper.getShooterButton(START);
	}

	/**
	 * Prevents <code>ThrowBall</code> from launching ball until released
	 */
	public boolean getThrowBall_LaunchBallOverride() {
		return !XboxHelper.getShooterButton(XboxHelper.TRIGGER_LT);
	}

	public double getManualArms_ClimberValue() {
		return XboxHelper.getShooterAxis(XboxHelper.AXIS_RIGHT_Y);
	}

	public double getCandyCanes_Set() {
		return -XboxHelper.getShooterAxis(XboxHelper.AXIS_LEFT_Y);
	}
	
	public boolean getIntakeAssist_ArmValue_Up() {
		return XboxHelper.getShooterButton(XboxHelper.TRIGGER_RT);
	}
	
	public boolean getIntakeAssist_ArmValue_Down() {
		return XboxHelper.getShooterButton(XboxHelper.TRIGGER_LT);
	}
	
	public boolean getManualArms_GetArmValue() {
//		return xboxDriver.getRawButton(1);
		return FlightStickHelper.getButton(FlightStickHelper.TRIGGER);
	}

	public boolean getCommandCancel() {
		return XboxHelper.getShooterButton(START);
	}


}
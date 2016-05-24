package com.team3925.robot2016;

import static com.team3925.robot2016.util.hidhelpers.XboxHelper.START;

import com.team3925.robot2016.commands.CollectBall;
import com.team3925.robot2016.commands.LaunchBall;
import com.team3925.robot2016.commands.LowGoal;
import com.team3925.robot2016.commands.PlexiMove;
import com.team3925.robot2016.commands.ResetArms;
import com.team3925.robot2016.commands.SetArmSetpointTemporary;
import com.team3925.robot2016.commands.auto.AutoRoutineCourtyard;
import com.team3925.robot2016.commands.auto.AutoRoutineDoNothing;
import com.team3925.robot2016.commands.auto.GyroDrive;
import com.team3925.robot2016.commands.auto.QuickDrive;
import com.team3925.robot2016.subsystems.ZeroLauncher;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.hidhelpers.FlightStickHelper;
import com.team3925.robot2016.util.hidhelpers.ThrustmasterHelper;
import com.team3925.robot2016.util.hidhelpers.XboxHelper;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
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

	private Button startCollectBall;
	private Button startThrowBallFar;
	private Button startThrowBallNear;
	private Button startThrowBallSide;
	private Button cancelCommands;
	private Button startDropAngle;
	private Button startResetArms;
	private Button startLowGoal;
	

	private Command collectBall;
	private Command launcherBallHigh;
	private Command launcherBallLow;
	private Command launcherBallSide;
	private Command launcherDropAngle;
	private Command resetArms;
	private Command lowGoal;
	
	public SendableChooser autoChooser;
	public SendableChooser throwBallTesting;

	public SendableChooser positionChooser;

	public OI() {

		driverFlightstick = new Joystick(0);
		driverWheel = new Joystick(1);
		shooterXbox = new Joystick(2);
		
		collectBall = new CollectBall();
		launcherBallHigh = new LaunchBall(Constants.LAUNCHER_LAUNCHER_BALL_HIGH_ANGLE);
		launcherBallLow = new LaunchBall(Constants.LAUNCHER_LAUNCHER_BALL_LOW_ANGLE);
		launcherBallSide = new LaunchBall(Constants.LAUNCHER_LAUNCHER_BALL_SIDE_ANGLE);
		launcherDropAngle = new SetArmSetpointTemporary(Constants.LAUNCHER_LAUNCH_BALL_MIDZONE_ANGLE);
		resetArms = new ResetArms();
		lowGoal = new LowGoal();
		
		
		// TODO: MAKE THIS SETUP NOT BRITTLE, DELICATE, FRAGILE, AND ALL OTHER SYNONYMS
		
		startCollectBall = new JoystickButton(shooterXbox, XboxHelper.A);
		startCollectBall.whenPressed(collectBall);
		startCollectBall.cancelWhenPressed(launcherBallHigh);
		startCollectBall.cancelWhenPressed(launcherBallLow);
		startCollectBall.cancelWhenPressed(launcherBallSide);
		startCollectBall.cancelWhenPressed(launcherDropAngle);
		startCollectBall.cancelWhenPressed(lowGoal);

		
		startThrowBallFar = new JoystickButton(shooterXbox, XboxHelper.Y);
		startThrowBallFar.whenPressed(launcherBallHigh);
		startThrowBallFar.cancelWhenPressed(collectBall);
		startThrowBallFar.cancelWhenPressed(launcherBallLow);
		startThrowBallFar.cancelWhenPressed(launcherBallSide);
		startThrowBallFar.cancelWhenPressed(launcherDropAngle);
		startThrowBallFar.cancelWhenPressed(lowGoal);
		
		
		startThrowBallNear = new JoystickButton(shooterXbox, XboxHelper.X);
		startThrowBallNear.whenPressed(launcherBallLow);
		startThrowBallNear.cancelWhenPressed(collectBall);
		startThrowBallNear.cancelWhenPressed(launcherBallHigh);
		startThrowBallNear.cancelWhenPressed(launcherBallSide);
		startThrowBallNear.cancelWhenPressed(launcherDropAngle);
		startThrowBallNear.cancelWhenPressed(lowGoal);
		
		
		startThrowBallSide = new JoystickButton(shooterXbox, XboxHelper.B);
		startThrowBallSide.whenPressed(launcherBallSide);
		startThrowBallSide.cancelWhenPressed(collectBall);
		startThrowBallSide.cancelWhenPressed(launcherBallHigh);
		startThrowBallSide.cancelWhenPressed(launcherBallLow);
		startThrowBallSide.cancelWhenPressed(launcherDropAngle);
		startThrowBallSide.cancelWhenPressed(lowGoal);
		
		
		startDropAngle = new JoystickButton(driverFlightstick, FlightStickHelper.TRIGGER);
		startDropAngle.whileHeld(launcherDropAngle);        	
		startDropAngle.cancelWhenPressed(collectBall);
		startDropAngle.cancelWhenPressed(launcherBallHigh);
		startDropAngle.cancelWhenPressed(launcherBallLow);
		startDropAngle.cancelWhenPressed(launcherBallSide);
		startDropAngle.cancelWhenPressed(lowGoal);
		
		
		cancelCommands = new JoystickButton(shooterXbox, XboxHelper.START);
		cancelCommands.cancelWhenPressed(collectBall);
		cancelCommands.cancelWhenPressed(launcherBallHigh);
		cancelCommands.cancelWhenPressed(launcherBallLow);
		cancelCommands.cancelWhenPressed(launcherBallSide);
		cancelCommands.cancelWhenPressed(launcherDropAngle);
		cancelCommands.cancelWhenPressed(resetArms);
		cancelCommands.cancelWhenPressed(lowGoal);
		
		
		startLowGoal = new JoystickButton(shooterXbox, XboxHelper.BUMPER_LT);
		startLowGoal.whenPressed(lowGoal);
		startLowGoal.cancelWhenPressed(collectBall);
		startLowGoal.cancelWhenPressed(launcherBallHigh);
		startLowGoal.cancelWhenPressed(launcherBallLow);
		startLowGoal.cancelWhenPressed(launcherBallSide);
		startLowGoal.cancelWhenPressed(launcherDropAngle);

		
		startResetArms = new JoystickButton(driverFlightstick, FlightStickHelper.BOTTOM_RIGHT_DOWN);
		startResetArms.whenPressed(resetArms);
		startResetArms.cancelWhenPressed(collectBall);
		startResetArms.cancelWhenPressed(launcherBallHigh);
		startResetArms.cancelWhenPressed(launcherBallLow);
		startResetArms.cancelWhenPressed(launcherBallSide);
		startResetArms.cancelWhenPressed(launcherDropAngle);
		startResetArms.cancelWhenPressed(lowGoal);
		
		
		
		positionChooser = new SendableChooser();
		positionChooser.addDefault("1 - Far Right", new Integer(1));
		positionChooser.addObject("2 - Near Right", new Integer(2));
		positionChooser.addObject("3 - Middle", new Integer(3));
		positionChooser.addObject("4 - Near Left", new Integer(4));
		positionChooser.addObject("5 - Far Left", new Integer(5));

		
		//NATHAN IS THE BEST
		
		autoChooser = new SendableChooser();

		autoChooser.addDefault("DO NOTHING", new AutoRoutineDoNothing());
		autoChooser.addObject("Courtyard Freebie Shot", new AutoRoutineCourtyard(Constants.AUTONOMOUS_SHOOT_ANGLE));
//		autoChooser.addObject("DefenseCross-ArmsUP", new AutoRoutineCrossDefault(true));
//		autoChooser.addObject("DefenseCross-ArmsDOWN", new AutoRoutineCrossDefault(false));

		
		SmartDashboard.putData(Constants.AUTONOMOUS_POSITION_CHOOSER_NAME, positionChooser);
		SmartDashboard.putData(Constants.AUTONOMOUS_CHOOSER_NAME, autoChooser);

	}
	
	

	public CommandGroup getAutonomous() {
		/*
		Object selected = autoChooser.getSelected();
		
		if (selected instanceof AutoRoutineDoNothing) {
			return (CommandGroup) selected;

		} else if (selected instanceof AutoRoutineCourtyard) {
			return (CommandGroup) selected;

		} else if (selected instanceof DefenseCrossCommandGroup && selected instanceof CommandGroup) {
			CommandGroup command = (CommandGroup) selected;
			int pos = (int) positionChooser.getSelected();
			
			return new AutoRoutineCenter(command, pos, command.getName() + "-Pos" + pos);
		} else {
			DriverStation.reportError("Defaulted in autonomous selection!", false);
			return new AutoRoutineDoNothing();
		}
		//*/
		
		// BACKUP AUTO
		//*
		CommandGroup janky = new CommandGroup();
		
		janky.addParallel(new PlexiMove(true));
//		janky.addParallel(new ZeroLauncher());
//		janky.addSequential(new GyroDrive(0, true, Constants.AUTONOMOUS_CROSS_DEFENSE_DRIVE_TIME, 0.4d));
		janky.addSequential(new QuickDrive(Constants.AUTONOMOUS_CROSS_DEFENSE_DRIVE_TIME, DriveTrainSignal.FULL_FORWARD));

		return janky;
		//*/

	}
	
	

	// ROBOT BEHAVIOR
	public boolean getJankyFire(){
		return XboxHelper.getShooterButton(XboxHelper.B);
	}
	
	public double getJankyLauncherUp(){
		return XboxHelper.getShooterAxis(XboxHelper.AXIS_TRIGGER_LEFT);
	}
	
	public double getJankyLauncherDown(){
		return XboxHelper.getShooterAxis(XboxHelper.AXIS_TRIGGER_RIGHT);
	}

	public boolean getCollectBall_Continue() {
		return XboxHelper.getShooterButton(XboxHelper.BUMPER_RT);
	}
	
	public boolean getPlexiArms_Control() {
		return !FlightStickHelper.getButton(FlightStickHelper.TOP_DOWN);
	}
	
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
		return (!FlightStickHelper.getButton(FlightStickHelper.TOP_UP)); //TODO Get driver preference
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
		return !XboxHelper.getShooterButton(XboxHelper.BUMPER_LT);
	}

	public double getManualArms_ClimberValue() {
		return XboxHelper.getShooterAxis(XboxHelper.AXIS_RIGHT_Y);
	}

	public double getCandyCanes_Set() {
		return -XboxHelper.getShooterAxis(XboxHelper.AXIS_LEFT_Y);
	}
	
	public boolean getManualArms_GetArmValue() {
		return FlightStickHelper.getButton(FlightStickHelper.TRIGGER);
	}

	public boolean getCommandCancel() {
		return XboxHelper.getShooterButton(START);
	}


}
package com.team3925.robot2016;

import com.team3925.robot2016.commands.AutoRoutineCenter;
import com.team3925.robot2016.commands.AutoRoutineCourtyard;
import com.team3925.robot2016.commands.CollectBall;
import com.team3925.robot2016.commands.LaunchBall;
import com.team3925.robot2016.commands.ManualDrive;
import com.team3925.robot2016.commands.TrajectoryFollow;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
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
    public Joystick launchPad;

    public OI() {

    	xboxDriver = new Joystick(0);
    	xboxShooter = new Joystick(1);
        launchPad = new Joystick(2);
        
        // SmartDashboard Buttons
        SmartDashboard.putData("ManualDrive", new ManualDrive());
        SmartDashboard.putData("TrajectoryFollow", new TrajectoryFollow());
        SmartDashboard.putData("LaunchBall", new LaunchBall());
        SmartDashboard.putData("CollectBall", new CollectBall());
        SmartDashboard.putData("AutoRoutineCenter", new AutoRoutineCenter());
        SmartDashboard.putData("AutoRoutineCourtyard", new AutoRoutineCourtyard());
        
    }

}


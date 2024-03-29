package frc.robot.AutoRoutines;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.DriveTrain;
import frc.robot.HatchIntake;
import frc.robot.framework.Waiter;
import frc.robot.lib.BetterJoystick;

public class RightForward { //drives to the right side of the cargo ship, from level 1

    public static final void run(DriveTrain train, HatchIntake hatch,BetterJoystick joystick){
        Waiter.isAllowed = true;
        hatch.hold();
        train.setLeft();
        DriverStation.reportError("deploying is working",false);
        train.driveTo(-9);
        Waiter.waitFor(train::atDistance,joystick::triggerPressed,5000,10);
        train.arcadeDrive(0, 0);
        train.turnTo(30);
        Waiter.waitFor(train::atAngle,joystick::triggerPressed,1000,10);
        train.arcadeDrive(0,0);
        hatch.forward();
        train.driveTo(-8.1);
        Waiter.waitFor(train::atDistance,joystick::triggerPressed,2500,10);
        train.arcadeDrive(0,0);
        train.turnTo(57);
        Waiter.waitFor(train::atAngle,joystick::triggerPressed,1000,10);
        train.arcadeDrive(0,0);
           
        Waiter.isAllowed = true; 
    
    }
}
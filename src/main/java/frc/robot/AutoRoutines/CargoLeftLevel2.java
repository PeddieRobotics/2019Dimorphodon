package frc.robot.AutoRoutines;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.DriveTrain;
import frc.robot.HatchIntake;
import frc.robot.framework.Waiter;
import frc.robot.lib.BetterJoystick;

public class CargoLeftLevel2 {  //drives to the left side of the cargo ship, from level 2
    
    public static final void run(DriveTrain train, HatchIntake hatch,BetterJoystick joystick){
        Waiter.isAllowed = true;
        hatch.hold();
        train.setRight();
        DriverStation.reportError("deploying is working",false);
        train.driveTo(-13.2);
        Waiter.waitFor(train::atDistance,joystick::triggerPressed,5000,10);
        train.arcadeDrive(0, 0);
        train.turnTo(-30);
        Waiter.waitFor(train::atAngle,joystick::triggerPressed,1000,10);
        train.arcadeDrive(0,0);
        hatch.forward();
        train.driveTo(-8.1);
        Waiter.waitFor(train::atDistance,joystick::triggerPressed,5000,10);
        train.arcadeDrive(0,0);
        train.turnTo(-57);
        Waiter.waitFor(train::atAngle,joystick::triggerPressed,1000,10);
        train.arcadeDrive(0,0);
           
        Waiter.isAllowed = true; 
    
    }
}
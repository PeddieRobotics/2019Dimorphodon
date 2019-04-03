package frc.robot.AutoRoutines;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.DriveTrain;
import frc.robot.HatchIntake;
import frc.robot.framework.Waiter;
import frc.robot.lib.BetterJoystick;
public class CargoRightLevel2{
    public static final void run(DriveTrain train, HatchIntake hatch,BetterJoystick joystick){
        Waiter.isAllowed = true;
        hatch.hold();
        train.setLeft();
        DriverStation.reportError("deploying is working",false);
        train.driveTo(-12);
        Waiter.waitFor(train::atDistance,5000);
        train.arcadeDrive(0, 0);
        train.turnTo(30);
        Waiter.waitFor(train::atAngle,1000);
        train.arcadeDrive(0,0);
        hatch.forward();
        train.driveTo(-8.1);
        Waiter.waitFor(train::atDistance,5000);
        train.arcadeDrive(0,0);
        train.turnTo(58);
        Waiter.waitFor(train::atAngle,1000);
        train.arcadeDrive(0,0);
           
        Waiter.isAllowed = true; 
    
    }
}
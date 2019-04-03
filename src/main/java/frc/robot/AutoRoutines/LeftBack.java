package frc.robot.AutoRoutines;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.lib.BetterJoystick;
import frc.robot.DriveTrain;
import frc.robot.HatchIntake;
import frc.robot.framework.Waiter;
public class LeftBack {
    public static final void run(DriveTrain train, HatchIntake hatch, BetterJoystick joystick){
        Waiter.isAllowed = true;
        Waiter.waitFor(50);
       
        train.driveTo(-5);
        Waiter.waitFor(train::atDistance,joystick::triggerPressed,5000,10);
        train.arcadeDrive(0, 0);
        train.turnTo(95);
        Waiter.waitFor(train::atAngle, joystick::triggerPressed,1000,10);
         train.arcadeDrive(0,0);
        
        Waiter.isAllowed = true;      
    
    }
}
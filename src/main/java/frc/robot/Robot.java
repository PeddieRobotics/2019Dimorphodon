package frc.robot;

import frc.robot.framework.Looper;
import frc.robot.lib.BetterJoystick;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {

  DriveTrain drivetrain;
  CargoIntake cIntake;
  HatchIntake hIntake;
  Looper loop;

  boolean isDown = false;
  
  BetterJoystick  leftJoystick, rightJoystick;

  public void robotInit() {
    leftJoystick = new BetterJoystick(0);
    rightJoystick = new BetterJoystick(1);
    drivetrain = new DriveTrain();
    cIntake = new CargoIntake();
    hIntake = new HatchIntake();
    
    loop = new Looper(10);
    loop.add(drivetrain::update);
    loop.add(cIntake::update);
    loop.add(hIntake::update);
    loop.start();

  }

  public void robotPeriodic() {
  }

  public void autonomousInit() {

  }

  public void autonomousPeriodic() {

  }

  public void teleopPeriodic() {
    drivetrain.arcadeDrive(leftJoystick.getRawAxis(1), -rightJoystick.getRawAxis(0));

    //left joystick controls
    if(leftJoystick.getRisingEdge(1)) {
      if(cIntake.isDown) {
        cIntake.clawUp();
      }else {
        cIntake.clawDown();
      }


    }

  }

  public void testPeriodic() {
  }
}

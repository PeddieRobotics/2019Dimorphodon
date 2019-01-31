package frc.robot;

import frc.robot.framework.Looper;
import frc.robot.lib.BetterJoystick;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {

  DriveTrain drivetrain;
  CargoIntake cIntake;
  HatchIntake hIntake;
  ShoulderPivot shoulder;
  Looper loop;
  LimeLight lime;
  Vision vision;

  boolean isDown = false;

  BetterJoystick leftJoystick, rightJoystick;

  public void robotInit() {
    leftJoystick = new BetterJoystick(0);
    rightJoystick = new BetterJoystick(1);
    drivetrain = new DriveTrain();
    cIntake = new CargoIntake();
    hIntake = new HatchIntake();
    shoulder = new ShoulderPivot();
    lime = new LimeLight();
    vision = new Vision();

    loop = new Looper(10);
    loop.add(drivetrain::update);
    loop.add(cIntake::update);
    loop.add(hIntake::update);
    loop.add(shoulder::update);
    loop.add(lime::update);
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

    // left joystick controls - Will control hatch intake
    if (leftJoystick.getRisingEdge(1)) {
    }

    // right joystick controls - Will control cargo intake
    if (rightJoystick.getRisingEdge(1)) {
      cIntake.ejectFast();
    } else if (rightJoystick.getRisingEdge(2)) {
      if (shoulder.getTargetPosition() == 0.0) {
        // Move up
      } else {
        // Move down
      }
    } else if (rightJoystick.getRisingEdge(3)) {
      cIntake.intake();
    } else if (rightJoystick.getRisingEdge(4)) {
      cIntake.ejectSlow();
    }
  }

  public void testPeriodic() {
  }
}

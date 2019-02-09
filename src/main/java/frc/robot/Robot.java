package frc.robot;

import frc.robot.framework.Looper;
import frc.robot.lib.BetterJoystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Robot extends TimedRobot {

  DriveTrain drivetrain;
  CargoIntake cIntake;
  HatchIntake hIntake;
  FloorIntake fIntake;
  ShoulderPivot shoulder;
  Looper loop;
  LimeLight lime;
  Vision vision;

  boolean isDown = false;
  boolean frontSide = true; // Toggles "front" of robot: true = cargo side, false = hatch side

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
    updateDash();

    if (rightJoystick.getRisingEdge(2)) {
      frontSide = frontSide ? false : true;
    }
    if (frontSide) {
      drivetrain.arcadeDrive(leftJoystick.getRawAxis(1), -rightJoystick.getRawAxis(0));
    } else {
      drivetrain.arcadeDrive(-leftJoystick.getRawAxis(1), rightJoystick.getRawAxis(0));
    }

    // left joystick controls - Will control hatch intake, speed
    if (leftJoystick.getRisingEdge(1)) {
      hIntake.pushOut();
    } else if (leftJoystick.getRisingEdge(2)) {
      fIntake.hIntake();
    } else if (leftJoystick.getRisingEdge(3)) {
      fIntake.hHold();
    } else if (leftJoystick.getRisingEdge(4)) {
      hIntake.hLock();
    }

    // right joystick controls - Will control cargo intake, turning
    if (rightJoystick.getRisingEdge(1)) {
      cIntake.ejectFast();
    } else if (rightJoystick.getRisingEdge(4)) {
      if (shoulder.getTargetPosition() == 0.0) {
        // Move up
      } else {
        // Move down
      }
    } else if (rightJoystick.getRisingEdge(3)) {
      cIntake.intake();
    }

  }

  public void testPeriodic() {
  }

  public void updateDash() {
    SmartDashboard.putBoolean("Arm Up", shoulder.getLimitSwitchTop());
    SmartDashboard.putBoolean("Arm Down", shoulder.getLimitSwitchBottom());
    SmartDashboard.putBoolean("Shoulder Brake", shoulder.getBrake());
    SmartDashboard.putBoolean("Hatch", hIntake.hasHatch());
    SmartDashboard.putBoolean("Cargo", cIntake.hasCargo());
    // Output Drive Dist Left
    // Output Drive Dist Right
  }
}

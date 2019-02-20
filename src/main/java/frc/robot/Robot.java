package frc.robot;

import frc.robot.framework.Looper;
import frc.robot.lib.BetterJoystick;
import frc.robot.lib.BetterXbox;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Robot extends TimedRobot {

  DriveTrain drivetrain;
  CargoIntake cIntake;
  HatchIntake hIntake;
  Shoulder shoulder;
  Looper loop;
  LimeLight lime;
  Vision vision;
  BetterXbox xbox;
  // BetterJoystick leftJoystick, rightJoystick;

  boolean isDown = false;
  boolean frontSide = true; // Toggles "front" of robot: true = cargo side, false = hatch side

  double speedDeadBand = 0.05;
  double turnDeadBand = 0.05;

  public void robotInit() {
    xbox = new BetterXbox(0);
    drivetrain = new DriveTrain();
    cIntake = new CargoIntake();
    hIntake = new HatchIntake();
    lime = new LimeLight();
    vision = new Vision();
    shoulder = new Shoulder();

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

    // double speed = Math.pow(leftJoystick.getRawAxis(1), 3);
    // double turn = Math.pow(rightJoystick.getRawAxis(0), 3);
    // double speed = leftJoystick.getRawAxis(1);
    // double turn = rightJoystick.getRawAxis(0);

    double fspeed = xbox.getRawAxis(2) - xbox.getRawAxis(3);
    double fturn = xbox.getRawAxis(0);

    if (Math.abs(fspeed) < speedDeadBand) {
      fspeed = 0;
    }
    if (Math.abs(fturn) < turnDeadBand) {
      fturn = 0;
    }
    drivetrain.arcadeDrive(fspeed, fturn);// cap speed in driveTrain

    // if(leftJoystick.getRisingEdge(2)) {
    // hatchSide = !hatchSide;
    // }

    if (!frontSide) {
      if (xbox.getRisingEdge(ElectricalLayout.xboxLeftTrigger)) {
        hIntake.eject();
      } else if (xbox.getRisingEdge(ElectricalLayout.xboxRightTrigger)) {
        hIntake.hold();
      } else if (xbox.getRisingEdge(ElectricalLayout.xboxStart)) {
        hIntake.pushOut();
      } else if (xbox.getRisingEdge(ElectricalLayout.xboxBack)) {
        hIntake.pullBack();
      }
    } else {
      if (xbox.getRisingEdge(ElectricalLayout.xboxLeftTrigger)) {
        cIntake.intake();
      } else if (xbox.getRisingEdge(ElectricalLayout.xboxRightTrigger)) {
        cIntake.eject();
      } else if (xbox.getRisingEdge(ElectricalLayout.xboxLeftBumper)) {
        cIntake.disabled();
      } else if (xbox.getRisingEdge(ElectricalLayout.xboxStart)) {
        shoulder.setShoulder(0);
      }

      else if (xbox.getRisingEdge(ElectricalLayout.xboxXButton)) { // X
        shoulder.setShoulder(25);
        cIntake.ejectSpeed = -1.0;
      }

      else if (xbox.getRisingEdge(ElectricalLayout.xboxYButton)) { // Y
        shoulder.setShoulder(-20);
        cIntake.ejectSpeed = -0.5;
      }

      else if (xbox.getRisingEdge(ElectricalLayout.xboxBButton)) { // B
        shoulder.setShoulder(70);
        cIntake.ejectSpeed = -0.5;
      }

      else if (xbox.getRisingEdge(ElectricalLayout.xboxAButton)) { // A
        shoulder.setShoulder(105);
        cIntake.ejectSpeed = -0.5;
      }
    }

    /*
     * else if (rightJoystick.getRisingEdge(4)) if (shoulder.getTargetPosition() ==
     * 0.0) { // Move up } else { // Move down } }
     */

  }

  public void testPeriodic() {
  }

  public void updateDash() {
    SmartDashboard.putBoolean("Hatch", hIntake.hasHatch());
    SmartDashboard.putBoolean("Cargo", cIntake.hasCargo());
    // Output Drive Dist Left
    // Output Drive Dist Right
  }
}

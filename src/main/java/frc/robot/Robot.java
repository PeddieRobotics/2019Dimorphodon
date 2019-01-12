package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
  Joystick steeringWheel;
  Joystick leftJoystick;
  Joystick rightJoystick;
  DriveTrain driveTrain;

  public void robotInit() {
    steeringWheel = new Joystick(0);
    leftJoystick = new Joystick(1);
    rightJoystick = new Joystick(2);
    driveTrain = new DriveTrain();
  }

  public void robotPeriodic() {
  }

  public void autonomousInit() {

  }

  public void autonomousPeriodic() {

  }

  public void teleopPeriodic() {
    driveTrain.arcadeDrive(steeringWheel.getZ(), steeringWheel.getX());
    driveTrain.update();

  }

  public void testPeriodic() {
  }
}

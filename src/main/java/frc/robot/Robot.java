package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
  Joystick leftJoystick;
  Joystick rightJoystick;
  DriveTrain driveTrain;
  
  public void robotInit() {
    leftJoystick = new Joystick(0);
    rightJoystick = new Joystick(1);
    driveTrain = new DriveTrain();
  }

  public void robotPeriodic() {
  }

  public void autonomousInit() {

  }

  public void autonomousPeriodic() {

  }

  public void teleopPeriodic() {
  
    driveTrain.update();

  }

  public void testPeriodic() {
  }
}

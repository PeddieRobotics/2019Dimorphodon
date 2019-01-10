package frc.robot;

/*
  Drivetrain is where we control how the robot drives
*/
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Talon;
import frc.robot.lib.PID;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Joystick;

/**
 * leftSpeed: controls the speed of our left side motors, we want them to move
 * as a unit rightSpeed: controls the speed of our right side motors Pid: used
 * to callibrate the speed that our left and right motors should run at mode: an
 * enum that tells us what state we are in(an enum assigns numerical values to
 * states that can be accesed) rightMotor: our rightmotor leftMotor: our left
 * motor
 */
public class DriveTrain extends Subsystem {
  private double leftSpeed;
  private double rightSpeed;
  private PID pid;

  private enum Mode_Types {
    TELEOP
  };

  Mode_Types mode;
  Talon rightMotor;
  Talon leftMotor;

  /**
   * set our starting mode to TELEOP set our motor ports
   */
  public DriveTrain() {
    mode = Mode_Types.TELEOP;

    rightMotor = new Talon(1);
    leftMotor = new Talon(0);

    pid = new PID(0.1, 0, 0, 0);
  }

  /**
   * 
   * @param speed the speed that we should be running at if we want to drive
   *              striaght
   * @param turn  offsets the speed so that we can turn, basically if our left
   *              motors are running faster and our right motors are running
   *              slower we will turn right
   * 
   */
  public void arcadeDrive(double speed, double turn) {
    leftSpeed = speed - turn;
    rightSpeed = speed + turn;
    mode = Mode_Types.TELEOP;
  }

  /**
   * The main robot program will call this function It updates our speed and
   * turning
   */
  public void update() {
    switch (mode) {
    case TELEOP:
      rightMotor.set(rightSpeed);
      leftMotor.set(leftSpeed);
    }
  }
}

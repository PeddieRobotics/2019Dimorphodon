package frc.robot;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.lib.PID;
import edu.wpi.first.wpilibj.Talon;import edu.wpi.first.wpilibj.

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

  public DriveTrain() {
    mode = Mode_Types.TELEOP;

    rightMotor = new Talon(1);
    leftMotor = new Talon(0);

    pid = new PID()
  }

  public void arcadeDrive(double speed, double turn) {
    leftSpeed = speed - turn;
    rightSpeed = speed + turn;
    mode = Mode_Types.TELEOP;
  }

  public void update() {
    rightMotor.set(rightSpeed);
    leftMotor.set(leftSpeed);
  }

  public void initDefaultCommand() {

  }
}
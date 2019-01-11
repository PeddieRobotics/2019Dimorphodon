package frc.robot;

import org.usfirst.frc.team5895.robot.lib.NavX;
import org.usfirst.frc.team5895.robot.lib.PID;
import org.usfirst.frc.team5895.robot.lib.TrajectoryDriveController;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;


public class DriveTrain extends Subsystem {
  private Talon leftMotor;
  private Talon rightMotor;
  double leftSpeed, rightSpeed;
  private NavX NavX;
  private Encoder leftEncoder, rightEncoder;
  
  private static final double turn_KP = 0;
  private static final double turn_KI = 0;

  private static final double drive_KP = 0;
  private static final double drive_KI = 0;

  private PID turnPID;
  private PID drivePID;
  
  public void initDefaultCommand() {

  }


}

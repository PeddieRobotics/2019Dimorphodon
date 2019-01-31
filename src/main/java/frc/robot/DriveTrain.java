package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;

import frc.robot.lib.NavX;
import frc.robot.lib.PID;

import frc.robot.lib.pathfinder.*;
import frc.robot.lib.PathfinderFollower;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.sensors.*;

public class DriveTrain {

  private CANSparkMax leftDriveMaster, rightDriveMaster, leftDriveFollower1, leftDriveFollower2, rightDriveFollower1,
      rightDriveFollower2;
  private double leftspeed, rightspeed;

  private PigeonIMU pigeon;
  double angle = 0;

  private CANPIDController turnPID;
  private boolean turning;
  private CANPIDController drivePID;

  private CANEncoder leftEncoder, rightEncoder;

  private enum Mode_Type {
    TELEOP, AUTO_PATHFINDER
  }

  private Mode_Type mode = Mode_Type.TELEOP;

  public static final double TURN_P = 0.02549;
  public static final double TURN_I = 0.0;
  private static final double DRIVE_KP = 0.081;
  private static final double DRIVE_KI = 0.000001;
  private static final double DRIVE_KD = 0.11;
  private static final double capSpeed = 0.5;

  // tracking
  private double posX, posY; // feet
  private double lastDistance = 0d; // distance traveled the last time update() was called

  private PathfinderGenerator pathMaster;
  private PathfinderFollower p_straight;
  private PathfinderFollower p_in_use;

  public DriveTrain() {

    // initialize drive motors
    leftDriveMaster = new CANSparkMax(0, MotorType.kBrushless);
    leftDriveFollower1 = new CANSparkMax(1, MotorType.kBrushless);
    leftDriveFollower2 = new CANSparkMax(2, MotorType.kBrushless);
    rightDriveMaster = new CANSparkMax(3, MotorType.kBrushless);
    rightDriveFollower1 = new CANSparkMax(4, MotorType.kBrushless);
    rightDriveFollower2 = new CANSparkMax(5, MotorType.kBrushless);

    leftDriveFollower1.follow(leftDriveMaster);
    leftDriveFollower2.follow(leftDriveMaster);
    rightDriveFollower1.follow(rightDriveMaster);
    rightDriveFollower2.follow(rightDriveMaster);

    // initialize pigeonIMU
    pigeon = new PigeonIMU(leftDriveMaster.getDeviceId());
    pigeon.setFusedHeading(angle);

    // initialize encoders
    leftEncoder = leftDriveMaster.getEncoder();
    rightEncoder = rightDriveMaster.getEncoder();

    // initialize PID
    drivePID = leftDriveMaster.getPIDController();
    drivePID.setP(DRIVE_KP);
    drivePID.setI(DRIVE_KI);
    drivePID.setD(DRIVE_KD);
    drivePID.setFF(0.0);
    drivePID.setOutputRange(-1.0, 1.0);

    turnPID = rightDriveMaster.getPIDController();
    turnPID.setP(TURN_P);
    turnPID.setI(TURN_I);
    turnPID.setD(0.0);
    turnPID.setFF(0.0);
    turnPID.setOutputRange(-1.0, 1.0);

    pathMaster = new PathfinderGenerator(false);

    try {
      DriverStation.reportError("Start generating paths with pathfinder", false);
      // kp ki kd kv ka kturn
      p_straight = new PathfinderFollower(pathMaster.Straight(), 0.1, 0, 0, 1.0 / 13.75, 1.0 / 75.0, -0.009);
    } catch (Exception e) {
      DriverStation.reportError("Pathfinder: error generate paths", false);
    }
  }

  public void auto_straight() {
    p_in_use = p_straight;
    p_in_use.reset();
    mode = Mode_Type.AUTO_PATHFINDER;
  }

  public void resetPigeon() {
    pigeon.setFusedHeading(0);
  }

  public void turnTo(double angle) {
    pigeon.setFusedHeading(angle); // need to change this part with turnPIDs involved?
    turning = true;
    mode = Mode_Type.TELEOP;
  }

  /**
   * makes the robot stop turning using the PID
   */
  public void stopTurning() {
    turning = false;
  }

  /**
   * returns if the robot is turning to an angle using the PID
   * 
   * @return if the robot is turning to an angle using the PID
   */
  public boolean isTurning() {
    return turning;
  }

  public double getAngle() {
    PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
    pigeon.getFusedHeading(fusionStatus);
    return fusionStatus.heading;
  }

  public void arcadeDrive(double speed, double turn) {
    leftspeed = speed - turn;
    rightspeed = speed + turn;
    mode = Mode_Type.TELEOP;
    turning = false;
  }

  /**
   * @return the average velocity in feet per second from the left and right
   *         encoders.
   */
  public double getVelocity() {
    return (leftEncoder.getVelocity() + rightEncoder.getVelocity()) / 2;
  }

  /**
   * @return the average distance traveled in feet from the left and right
   *         encoders.
   */
  public double getDistanceTraveled() {
    return (leftEncoder.getPosition() + rightEncoder.getPosition()) / 2;
  }

  /**
   * gets the x position of the drivetrain
   * 
   * @return the x position of the drivetrain in feet
   */
  public double getXPosition() {
    return posX;
  }

  public double getYPosition() {
    return posY;
  }

  public void update() {
    double distance = getDistanceTraveled() - lastDistance;

    // posX += distance*Math.cos(Math.toRadians(getAngle()));
    // posY += distance*Math.sin(Math.toRadians(getAngle()));

    distance = getDistanceTraveled();

    switch (mode) {
    case TELEOP:
      if (turning) {

        leftspeed = leftDriveMaster.get(); // current set speed of a speed controller
        rightspeed = rightDriveMaster.get();

        if (leftspeed > capSpeed) {
          leftspeed = capSpeed;
        }
        if (rightspeed > capSpeed) {
          rightspeed = capSpeed;
        }
        if (leftspeed < -capSpeed) {
          leftspeed = -capSpeed;
        }
        if (rightspeed < -capSpeed) {
          rightspeed = -capSpeed;
        }
      }

      double setpoint = 2000.0;
      drivePID.setReference(setpoint, ControlType.kVelocity);
      break;

    case AUTO_PATHFINDER:
      double[] p = p_in_use.getOutput(rightEncoder.getPosition(), leftEncoder.getPosition(),
          getAngle() * Math.PI / 180);

      leftDriveMaster.set(p[1]);
      rightDriveMaster.set(-p[0]);

      DriverStation.reportError("right: " + p[0], false);
      DriverStation.reportError("left: " + p[1], false);
      break;
    }

  }
}

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;

import frc.robot.lib.NavX;
import frc.robot.lib.PID;

import frc.robot.lib.pathfinder.*;
import frc.robot.lib.PathfinderFollower;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class DriveTrain {

  private CANSparkMax leftDriveMaster, rightDriveMaster, leftDriveFollower1, leftDriveFollower2, rightDriveFollower1,
      rightDriveFollower2;
  private double leftspeed, rightspeed;
  private NavX navX;

  private PID turnPID;
  private boolean turning;
  private PID drivePID;

  private CANEncoder leftEncoder, rightEncoder;

  private enum Mode_Type {
    TELEOP, AUTO_PATHFINDER, TELEOP_REVERSE, AUTO_PATHFINDER_REVERSE
  };

  private Mode_Type mode = Mode_Type.TELEOP;

  public static final double TURN_P = 0.0;
  public static final double TURN_I = 0.0;
  private static final double DRIVE_KP = 0.081;
  private static final double DRIVE_KI = 0.000001;
  private static final double capSpeed = 0.5;

  private PathfinderGenerator pathMaster;
  private PathfinderFollower p_straight;
  private PathfinderFollower p_in_use;

  // tracking
  private double posX, posY; // feet
  private double lastDistance = 0d; // distance traveled the last time update() was called

  public DriveTrain() {

    // initialize drive motors
    leftDriveMaster = new CANSparkMax(ElectricalLayout.MOTOR_DRIVE_LEFT_MASTER, MotorType.kBrushless);
    leftDriveFollower1 = new CANSparkMax(ElectricalLayout.MOTOR_DRIVE_LEFT_FOLLOWER1, MotorType.kBrushless);
    leftDriveFollower2 = new CANSparkMax(ElectricalLayout.MOTOR_DRIVE_LEFT_FOLLOWER2, MotorType.kBrushless);
    rightDriveMaster = new CANSparkMax(ElectricalLayout.MOTOR_DRIVE_RIGHT_MASTER, MotorType.kBrushless);
    rightDriveFollower1 = new CANSparkMax(ElectricalLayout.MOTOR_DRIVE_RIGHT_FOLLOWER1, MotorType.kBrushless);
    rightDriveFollower2 = new CANSparkMax(ElectricalLayout.MOTOR_DRIVE_RIGHT_FOLLOWER2, MotorType.kBrushless);

    leftDriveFollower1.follow(leftDriveMaster);
    leftDriveFollower2.follow(leftDriveMaster);
    rightDriveFollower1.follow(rightDriveMaster);
    rightDriveFollower2.follow(rightDriveMaster);

    // initialize encoders
    leftEncoder = leftDriveMaster.getEncoder();
    rightEncoder = rightDriveMaster.getEncoder();

    // initialize navX
    navX = new NavX();

    // initialize PID
    turnPID = new PID(TURN_P, TURN_I, 0, 6);
    drivePID = new PID(DRIVE_KP, DRIVE_KI, 0, 6);

    // initialize pathfinder
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

  /**
   * resets the NavX to 0
   */
  public void resetNavX() {
    navX.reset();
  }

  public void turnTo(double angle) {
    turnPID.set(angle);
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
   * @return if the robot is turning to an angle using the PID
   */
  public boolean isTurning() {
    return turning;
  }

  public double getAngle() {
    return navX.getAngle();
  }

  public void arcadeDrive(double speed, double turn) {
    leftspeed = speed - turn;
    rightspeed = speed + turn;
    mode = Mode_Type.TELEOP;
    turning = false;
  }

  public void arcadeDriveReverse(double speed, double turn) {
    leftspeed = speed - turn;
    rightspeed = speed + turn;
    mode = Mode_Type.TELEOP_REVERSE;
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

  /**
   * gets the y position of the drivetrain
   * 
   * @return the y position of the drivetrain in feet
   */
  public double getYPosition() {
    return posY;
  }

  /**
   * stop turnPID when robot is at correct angle
   * 
   * @return whether PID is at correct angle
   */
  public boolean atAngle() {
    return (Math.abs(turnPID.getSetpoint() - getAngle()) <= 5);
  }

  /**
   * stop drivePID when robot is at correct distance
   * 
   * @return whether PID is at correct distance
   */
  public boolean atDistance() {
    return (Math.abs(drivePID.getSetpoint() - getDistanceTraveled()) <= 2);
  }

  public void update() {
    double distance = getDistanceTraveled() - lastDistance;

    posX += distance * Math.cos(Math.toRadians(getAngle()));
    posY += distance * Math.sin(Math.toRadians(getAngle()));

    distance = getDistanceTraveled();

    switch (mode) {

    case AUTO_PATHFINDER:
      double[] p = p_in_use.getOutput(rightEncoder.getPosition(), leftEncoder.getPosition(),
          getAngle() * Math.PI / 180);

      leftDriveMaster.set(p[1]);
      rightDriveMaster.set(-p[0]);
      // DriverStation.reportError("right: " + p[0], false);
      // DriverStation.reportError("left: " + p[1], false);
      break;

    case TELEOP:
      if (turning) {
        leftspeed = -turnPID.getOutput(navX.getAngle());
        rightspeed = turnPID.getOutput(navX.getAngle());

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
      leftDriveMaster.set(-leftspeed);
      rightDriveMaster.set(rightspeed);
      break;

    case TELEOP_REVERSE:
      if (turning) {
        leftspeed = turnPID.getOutput(navX.getAngle());
        rightspeed = -turnPID.getOutput(navX.getAngle());

        if (leftspeed > -capSpeed) {
          leftspeed = -capSpeed;
        }
        if (rightspeed > -capSpeed) {
          rightspeed = -capSpeed;
        }
        if (leftspeed < capSpeed) {
          leftspeed = capSpeed;
        }
        if (rightspeed < capSpeed) {
          rightspeed = capSpeed;
        }
      }
      leftDriveMaster.set(leftspeed);
      rightDriveMaster.set(-rightspeed);
      break;
    }
  }
}

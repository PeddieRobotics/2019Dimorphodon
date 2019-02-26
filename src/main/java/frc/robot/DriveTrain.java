package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;

import frc.robot.lib.NavX;
import frc.robot.lib.PID;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class DriveTrain {

  private CANSparkMax leftDriveMaster, rightDriveMaster, leftDriveFollower1, leftDriveFollower2, rightDriveFollower1,
      rightDriveFollower2;
  private double leftspeed, rightspeed;
  private NavX navX;

  private PID turnPID;
  private PID drivePID;
  private PID txPID;
  private PID yDistPID;
  private CANEncoder leftEncoder, rightEncoder;

  private enum Mode_Type {
    DRIVE_STRAIGHT, TURNING, TELEOP, AUTO
  };

  private Mode_Type mode = Mode_Type.TELEOP;

  public static final double TURN_P = 0.00558;
  public static final double TURN_I = 0.0;
  private static final double DRIVE_KP = 0.081;
  private static final double DRIVE_KI = 0.000001;
  private static final double capSpeed = 0.5;
  private static final double TX_I = 0.000;//will have to tune these
  private static final double TX_P=0.0;
  private static final double YDIST_I=0.0;
  private static final double YDIST_P=0.0; 
  // tracking
  private double lastDistance = 0d; // distance traveled the last time update() was called
  LimeLight lime;
  LookupTable verticalTable;
  private static final double yDistSetPoint =0;
  private static final double txSetPoint = 0;
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
    txPID = new PID(TX_P, TX_I,0,6);
    yDistPID = new PID(YDIST_P, YDIST_I, 0,6);
    turnPID = new PID(TURN_P, TURN_I, 0, 6);
    drivePID = new PID(DRIVE_KP, DRIVE_KI, 0, 6);
    lime = new LimeLight();
    double[] vertPixelDist  = {79,55,24,18,29,36,48,72,25,16,20,58,93,53,42};
    double[] vertDist = {21.5,30.5,73.5,98,60,48,36,24,72,108,84,30,18,32,40};
    try{
      verticalTable = new LookupTable(vertDist, vertPixelDist);
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  /**
   * resets the NavX to 0
   */
  public void resetNavX() {
    navX.reset();
  }
  public void startAuto(){
    txPID.set(txSetPoint);
    yDistPID.set(yDistSetPoint);
    mode = Mode_Type.AUTO;
  }
  public void turnTo(double angle) {
    leftDriveMaster.setOpenLoopRampRate(0.0);
    rightDriveMaster.setOpenLoopRampRate(0.0);
    turnPID.set(angle);
    mode = Mode_Type.TURNING;
  }

  public double getAngle() {
    return navX.getAngle();
  }

  public void arcadeDrive(double speed, double turn) {
    leftDriveMaster.setOpenLoopRampRate(0.5);
    rightDriveMaster.setOpenLoopRampRate(0.5);
    leftspeed = speed + turn;
    rightspeed = speed - turn;
    mode = Mode_Type.TELEOP;
  }

  /**
   * @return the average velocity in feet per second from the left and right
   *         encoders.
   */
  public double getVelocity() {
    return (leftEncoder.getVelocity() + rightEncoder.getVelocity()) / 2;
  }

  /**
   * @return the distance traveled in feet from the left encoder
   */
  public double getLeftDistanceTraveled() {
    return leftEncoder.getPosition();
  }

  /**
   * @return the distance traveled in feet from the right encoder
   */
  public double getRightDistanceTraveled() {
    return rightEncoder.getPosition();
  }

  /**
   * @return the average distance traveled in feet from left and right encoders
   */
  public double getAvgDistanceTraveled() {
    return (leftEncoder.getPosition() + rightEncoder.getPosition()) / 2.0;
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
    return (Math.abs(drivePID.getSetpoint() - getAvgDistanceTraveled()) <= 2);
  }

  public void update() {
    double distance = getAvgDistanceTraveled() - lastDistance;

    distance = getAvgDistanceTraveled();

    switch (mode) {

    case DRIVE_STRAIGHT:
      leftspeed = drivePID.getOutput(leftEncoder.getPosition());
      rightspeed = drivePID.getOutput(rightEncoder.getPosition());

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
      break;

    case TURNING:
      leftspeed = turnPID.getOutput(navX.getAngle());
      rightspeed = -turnPID.getOutput(navX.getAngle());

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
      break;

    case TELEOP:
      leftDriveMaster.set(-leftspeed);
      rightDriveMaster.set(rightspeed);
      break;
    case AUTO:
      double turn = turnPID.getOutput(lime.tx());
      leftspeed = -yDistPID.getOutput(verticalTable.get(lime.currentTvert))+turn;
      rightspeed = yDistPID.getOutput(verticalTable.get(lime.currentTvert)) + turn;
    }
  }
}

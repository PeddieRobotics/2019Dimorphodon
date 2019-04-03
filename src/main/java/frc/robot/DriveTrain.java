package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;

import frc.robot.lib.NavX;
import frc.robot.lib.PID;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.lib.BetterCANEncoder;
public class DriveTrain {

  private CANSparkMax leftDriveMaster, rightDriveMaster, leftDriveFollower1, leftDriveFollower2, rightDriveFollower1,
      rightDriveFollower2;
  private double leftspeed, rightspeed;
//  private NavX navX;

  private PID turnPID;
  private PID drivePID;
  private PID txPID;
  private PID yDistPID;
  private BetterCANEncoder leftEncoder, rightEncoder;
  private boolean atDistances; 
  private boolean atAngle;
  private enum Mode_Type {
    DRIVE_STRAIGHT, TURNING, TELEOP, AUTO_DRIVE
  };
  
  private Mode_Type mode = Mode_Type.TELEOP;

  public static final double TURN_P = 0.00925;
  public static final double TURN_I =0.0;//0.0000005;
  private static final double DRIVE_KP = 0.04;//00.081;
  private static final double DRIVE_KI = 0.0;//0.000001;
  private static final double turnCapSpeed = 0.4;
  private static final double driveCapSpeed = 0.45;
  private static final double TX_I = 0.000;//will have to tune these
  private static final double TX_P=0.00558;
  private static final double YDIST_I=0.0;
  private static final double YDIST_P=0.0; 
  // tracking
  private double lastDistance = 0d; // distance traveled the last time update() was called
  LimeLight lime;
  LookupTable verticalTable;
  private static final double yDistSetPoint =0;
  private static final double txSetPoint = 0;
  private double distancePerPulse = 0.141;

  NavX navX; 
  public DriveTrain() {
    navX = new NavX();
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
    leftEncoder = new BetterCANEncoder(leftDriveMaster);
    rightEncoder = new BetterCANEncoder(rightDriveMaster);
    leftEncoder.setDistancePerPulse(1.0); //6.0*3.1415926535/12.0/24.0*9.0/100.0
    rightEncoder.setDistancePerPulse(1.0); //0.138 experimental
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
    // initialize navX
//    navX = new NavX();
//leftEncoder.setDistancePerPulse(-0.137); //6.0*3.1415926535/12.0/24.0*9.0/100.0
//rightEncoder.setDistancePerPulse(.137); //0.138 experimental
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

 
   public void resetNavX() {
    navX.reset();
   }
  
  public void setLimePIDOn(){
    lime.setPID();
    //DriverStation.reportError("On",true);
  }
  public void turnTo(double angle) {
    resetEncoders();
    resetNavX();
    leftDriveMaster.setOpenLoopRampRate(0.0);
    rightDriveMaster.setOpenLoopRampRate(0.0);
    turnPID.set(angle);
    mode = Mode_Type.TURNING;

    
  }
  
  // public double getAngle() {
  //   return navX.getAngle();
  // }
  
  public boolean atAngle(){
    return(Math.abs(navX.getAngle()-turnPID.getSetpoint())<3&&leftspeed<0.1);
  }

  public void driveTo(double distance){
    resetEncoders();
    resetNavX();
    turnPID.set(0);
    leftDriveMaster.setOpenLoopRampRate(0.0);
    rightDriveMaster.setOpenLoopRampRate(0.0);
    drivePID.set(distance/distancePerPulse);
    mode = Mode_Type.DRIVE_STRAIGHT;
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
  public double getLeftDistance() {
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
    return (leftEncoder.getPosition() + rightEncoder.getPosition() / 2.0);
  }
  
  /**
   * stop turnPID when robot is at correct angle
   * 
   * @return whether PID is at correct angle
   */
  // public boolean atAngle() {
  //   return (Math.abs(turnPID.getSetpoint() - getAngle()) <= 5);
  // }

  /**
   * stop drivePID when robot is at correct distance
   * 
   * @return whether PID is at correct distance
   */
  public boolean atDistance() {
    return (Math.abs(-leftEncoder.getPosition() - drivePID.getSetpoint()) <= 2&&leftspeed<0.08);
  }

  public void resetEncoders(){
    
    leftEncoder.setPosition(0.0);
    rightEncoder.setPosition(0.0);
  }

  public void update() {
    double distance = getAvgDistanceTraveled() - lastDistance;
    lime.update();
    distance = getAvgDistanceTraveled();
//    DriverStation.reportError("" + (leftEncoder.getPosition()), false);
//    DriverStation.reportError(""+navX.getAngle(),false);
//    DriverStation.reportError("" + leftEncoder.getPosition(), false);
    switch (mode) {

    case DRIVE_STRAIGHT:
      leftspeed = drivePID.getOutput(-leftEncoder.getPosition());
      rightspeed = drivePID.getOutput(rightEncoder.getPosition());
      double leftturn = turnPID.getOutput(navX.getAngle());
      double rightturn = -turnPID.getOutput(navX.getAngle());
     DriverStation.reportError("how far " + drivePID.getSetpoint()*distancePerPulse,false);
      DriverStation.reportError("how far" + leftEncoder.getPosition()*distancePerPulse,false);
      if (leftspeed > driveCapSpeed) {
        leftspeed = driveCapSpeed;
      }
      if (rightspeed > driveCapSpeed) {
        rightspeed = driveCapSpeed;
      }
      if (leftspeed < -driveCapSpeed) {
        leftspeed = -driveCapSpeed;
      }
      if (rightspeed < -driveCapSpeed) {
        rightspeed = -driveCapSpeed;
      }
      leftDriveMaster.set(-leftspeed+leftturn);
      rightDriveMaster.set(rightspeed+rightturn);

      break;

    case TURNING:
      leftspeed = turnPID.getOutput(navX.getAngle());
      rightspeed = -turnPID.getOutput(navX.getAngle());
    //DriverStation.reportError(""+navX.getAngle(),false);
      if (leftspeed > turnCapSpeed) {
        leftspeed = turnCapSpeed;
      }
      if (rightspeed > turnCapSpeed) {
        rightspeed = turnCapSpeed;
      }
      if (leftspeed < -turnCapSpeed) {
        leftspeed = -turnCapSpeed;
      }
      if (rightspeed < -turnCapSpeed) {
        rightspeed = -turnCapSpeed;
      }
      leftDriveMaster.set(-leftspeed);
      rightDriveMaster.set(rightspeed);
      // if(leftspeed<0.01){
      //   mode = Mode_Type.TELEOP;
      // }
      break;
    case AUTO_DRIVE:
    double driveOutput = drivePID.getOutput(getLeftDistance());

    leftDriveMaster.set(-driveOutput);
    rightDriveMaster.set(driveOutput);
//    DriverStation.reportError("" + getLeftDistance(), false);
    break;
    case TELEOP:
     // DriverStation.reportError("post turn"+navX.getAngle(),false);
      leftDriveMaster.set(-leftspeed);
      rightDriveMaster.set(rightspeed);
      break;
    
    }
  }
  public void setLeft(){
    lime.leftPipeline();
  }
  public void setRight(){
    lime.rightPipeline();
  }
}


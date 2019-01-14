package frc.robot;

// /*
  Drivetrain is where we control how the robot drives
*/
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.lib.PID;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.lib.NavX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DriverStation;
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
  private NavX NavX;
  private Encoder leftEncoder, rightEncoder;
 
  private PID drivePID;
  private PID turnPID;
  private boolean turning;
  private static final double DRIVE_KP = 0.08;
  private static final double DRIVE_KI = 0.0000001;
  public static final double TURN_P = 0.03;
  public static final double TURN_I = 0.00000001;
  private static final double capSpeed = 0.5;

 // private TrajectoryDriveController ...;
  private enum Mode_Type {AUTO_DRIVE, TELEOP, AUTO_TURN};
  private Mode_Type mode = Mode_Type.TELEOP;

  private Talon leftMotor;
   private Talon rightMotor;

  private enum Mode_Types {
    TELEOP, AUTO_DRIVE, AUTO_TURN
  };

  Mode_Types mode;
  Talon rightMotor;
  Talon leftMotor;

  /**
   * set our starting mode to TELEOP set our motor ports
   */
  public DriveTrain() {
    mode = Mode_Types.TELEOP;
    NavX = new NavX(); //angle that we are at.
    leftEncoder.setDistancePerPulse(4/12.0*3.14/360);
    rightEncoder.setDistancePerPulse(4/12.0*3.14/360);
    drivePID = new PID(DRIVE_KP, DRIVE_KI, 0, 6);
		turnPID = new PID(TURN_P, TURN_I, 0, 6, true, 1.0, true);
    rightMotor = new Talon(1);
    leftMotor = new Talon(0);

    pid = new PID(0.1, 0, 0, 0);
  }

  public double getDistance() {
    return leftEncoder.getDistance();
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
  
  public void driveStraight(double distance){
    resetEncodersAndNavX();
    drivePID.set(distance);
    mode = Mode_Type.AUTO_DRIVE;
    DriverStation.reportError("" + getDistance(), false);
  }
  
  public boolean atAngle() {
		return (Math.abs(turnPID.getSetpoint() - getDistance()) <= 2);
	}
  
  public void turnTo(double angle) {
		turnPID.set(angle);
		mode = Mode_Type.AUTO_TURN;
  }
  
  public boolean atDistance() {
		return (Math.abs(drivePID.getSetpoint() - getDistance()) <= 2);
  }
  
  public void  resetEncodersAndNavX(){
    leftEncoder.reset();
    rightEncoder.reset();
    NavX.reset();
  }
 
  /**
   * The main robot program will call this function It updates our speed and
   * turning
   */
  public void update() {
    switch(mode){
      case AUTO_DRIVE:
        leftSpeed = drivePID.getOutput(getDistance());
        rightSpeed = drivePID.getOutput(getDistance());
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
				break;	
      case TELEOP:
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
        break;
      case AUTO_TURN:
        leftSpeed = -turnPID.getOutput(NavX.getAngle());
        rightSpeed = -turnPID.getOutput(NavX.getAngle());
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);	
      break;
    }
      
  }

  /**
   * 
   */
  public void initDefaultCommand() {

  }
}


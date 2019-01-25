
package frc.robot;

public class ElectricalLayout {
 
  //motors
  public static final int MOTOR_DRIVE_LEFT_MASTER = 1;
  public static final int MOTOR_DRIVE_LEFT_FOLLOWER1 = 2;
  public static final int MOTOR_DRIVE_LEFT_FOLLOWER2 = 3;
  public static final int MOTOR_DRIVE_RIGHT_MASTER = 4;
  public static final int MOTOR_DRIVE_RIGHT_FOLLOWER1 = 5;
  public static final int MOTOR_DRIVE_RIGHT_FOLLOWER2 = 6;
  public static final int MOTOR_CARGO_SHOULDER = 7;
  public static final int MOTOR_CARGO_CLAW = 8;

  //solenoids
  public static final int SOLENOID_FLOOR_INTAKE = 0;  //brings intake up/down
  public static final int SOLENOID_FLOOR_CLAMP = 1;   //clamps intake
  public static final int SOLENOID_HATCH_PUNCHER = 2; //puncher
  public static final int SOLENOID_HATCH_GRABBER = 3; //middle claw locking mechanism
  public static final int SOLENOID_HATCH_DEPLOY = 4;  //pushes the whole thing out
  public static final int SOLENOID_CLIMB_STILT = 5;

}


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
  public static final int SOLENOID_FLOOR_PIVOT = 0;  //brings intake up/down
  public static final int SOLENOID_FLOOR_CLAMP = 1;   //clamps intake
  public static final int SOLENOID_HATCH_PUNCHER = 2; //puncher
  public static final int SOLENOID_HATCH_GRABBER = 3; //middle claw locking mechanism
  public static final int SOLENOID_HATCH_DEPLOY = 4;  //pushes the whole thing out
  public static final int SOLENOID_CARGO_CLAMP = 5;
  
  //xbox "axes", anything that has a value that can change 

  public static final int xboxForwardThrottle = 2;//left trigger 
  public static final int xboxBackwardThrottle = 3; //right trigger
  public static final int xboxLeftJoystickX = 0; 
  public static final int xboxLeftJoystickY = 1;
  public static final int xboxRightJoystickX = 4; 
  public static final int xboxRightJoystickY =5;
  //xbox buttons 
  public static final int xboxAButton = 1; 
  public static final int xboxBButton = 2; 
  public static final int xboxYButton = 3; 
  public static final int xboxXButton = 4; 
  public static final int xboxLeftTrigger = 5;
  public static final int xboxRightTrigger = 6;
  public static final int xboxBack = 7;
  public static final int xboxStart  =8;
  public static final int xboxLeftBumper = 9; 
  public static final int xboxRightBumper = 10;
  //xbox "menu" buttons, 8 states
  public static final int xBoxMenuLeft = 0;
  public static final int xBoxMenuRight =0;
  public static final int xBoxMenuUp= 0;
  public static final int xBoxMenuDown = 0;
  public static final int xBoxMenuLeftDown = 0;
  public static final int xBoxMenuLeftUp = 0;
  public static final int xBoxMenuRightDown = 0;
  public static final int xBoxMenuRightUp = 0;


  //sensors 
  public static final int SENSOR_HATCH_INTAKE = 0; 

  public static final int SENSOR_RIGHT_CLAW_INTAKE = 1;
  public static final int SENSOR_LEFT_CLAW_INTAKE = 2;

}

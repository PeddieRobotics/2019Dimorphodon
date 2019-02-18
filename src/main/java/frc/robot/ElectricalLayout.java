
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
  public static final int MOTOR_CARGO_LEFT_CLAW = 8;
  public static final int MOTOR_CARGO_RIGHT_CLAW = 9;

  //solenoids
  public static final int SOLENOID_HATCH_PUNCHER = 1; //puncher
  public static final int SOLENOID_HATCH_GRABBER = 2; //middle claw locking mechanism
  public static final int SOLENOID_HATCH_DEPLOY = 3;  //pushes the whole thing out
  public static final int SOLENOID_SHOULDER_BRAKE = 0; //check what port this actually is

  
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


  //analog sensors 
  public static final int SENSOR_RIGHT_HATCH_INTAKE = 2; 
  public static final int SENSOR_LEFT_HATCH_INTAKE = 3;

  public static final int SENSOR_RIGHT_CLAW_INTAKE = 1;
  public static final int SENSOR_LEFT_CLAW_INTAKE = 0;

  //check these too
  public static final int SENSOR_ARM_UP = 3;
  public static final int SENSOR_ARM_DOWN = 4;
  public static final int SENSOR_ARM_BRAKE = 5;

}

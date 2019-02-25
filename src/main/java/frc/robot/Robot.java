package frc.robot;

import frc.robot.framework.Looper;
import frc.robot.lib.BetterJoystick;
import frc.robot.lib.BetterXbox;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Robot extends TimedRobot {

  DriveTrain drivetrain;
  CargoIntake cIntake;
  HatchIntake hIntake;
  Shoulder shoulder;
  Looper loop;
  LimeLight lime;
  Vision vision;
  BetterJoystick leftJoystick;
  BetterJoystick rightJoystick;
//  Blinkin blinkin;
  //BetterJoystick leftJoystick, rightJoystick;

  boolean isDown = false;
  boolean frontSide = true; // Toggles "front" of robot: true = cargo side, false = hatch side

  double speedDeadBand=0.07;
  double turnDeadBand = 0.07;

  public void robotInit() {
    leftJoystick = new BetterJoystick(0);
    rightJoystick = new BetterJoystick(1);
    drivetrain = new DriveTrain();
    cIntake = new CargoIntake();
    hIntake = new HatchIntake();
    lime = new LimeLight();
    vision = new Vision();
    shoulder = new Shoulder();
//    blinkin = new Blinkin();

    loop = new Looper(10);
    loop.add(drivetrain::update);
    loop.add(cIntake::update);
    loop.add(hIntake::update);
    loop.add(shoulder::update);
    loop.add(lime::update);
    loop.start();

  }

  public void robotPeriodic() {
  }

  public void autonomousInit() {

  }

  public void autonomousPeriodic() {

  }

  public void teleopPeriodic() {

    // double speed = Math.pow(leftJoystick.getRawAxis(1), 3);
    // double turn = Math.pow(rightJoystick.getRawAxis(0), 3);
    double speed = deadband(leftJoystick.getRawAxis(1), speedDeadBand);
    double turn = deadband(rightJoystick.getRawAxis(0), turnDeadBand);

    //double fspeed = xbox.getRawAxis(2)-xbox.getRawAxis(3);
    //double fturn = xbox.getRawAxis(0);

   //cap speed in driveTrain

     if(rightJoystick.getRisingEdge(2)) {
       frontSide = !frontSide;
    }

    if (!frontSide) {
      drivetrain.arcadeDrive(-speed, turn);
//      blinkin.solidBlue();
      if (rightJoystick.getRisingEdge(2)) {
        hIntake.hold();
      } else if (leftJoystick.getRisingEdge(2)) {
        frontSide = !frontSide;
      } else if (leftJoystick.getRisingEdge(3)) {
        hIntake.pullBack();
      } else if (leftJoystick.getRisingEdge(4)) {
        hIntake.pushOut();
      }
    } else {
      drivetrain.arcadeDrive(speed, turn);
//      blinkin.solidWhite();
      if(rightJoystick.getRisingEdge(1))  { 
        cIntake.eject();
      } 
      else if (leftJoystick.getRisingEdge(3)) {
        shoulder.setShoulder(0);
        cIntake.setEjectSpeed(0.0);
      }
      
      else if (rightJoystick.getRisingEdge(4)) { //X
     
      shoulder.setShoulder(-20);
      cIntake.setEjectSpeed(-0.5);
      } 
      
      else if (leftJoystick.getRisingEdge(3)) { //Y
      shoulder.setShoulder(-20);
      cIntake.setEjectSpeed(-0.5);
      } 
    
      else if (rightJoystick.getRisingEdge(2)) { //B
        shoulder.setShoulder(20);
        cIntake.setEjectSpeed(-0.7);
      } 
      else if (leftJoystick.getRisingEdge(1)) { //B
        
       shoulder.setShoulder(65);
      cIntake.setEjectSpeed(-0.5);
      } 
      
      else if (leftJoystick.getRisingEdge(4)) { //A
        shoulder.setShoulder(15);
         cIntake.setEjectSpeed(-1.0);
      } 
      else if(rightJoystick.getRisingEdge(3)){
        shoulder.setShoulder(105);
      cIntake.intake();
      
      }
    }
  }

  public void testPeriodic() {
  }

  public void updateDash() {
    SmartDashboard.putBoolean("Hatch", hIntake.hasHatch());
    SmartDashboard.putBoolean("Cargo", cIntake.hasCargo());
    // Output Drive Dist Left
    // Output Drive Dist Right
  }

  public double deadband(double JoystickValue, double DeadbandCutoff) {
    double deadbandreturn;
    if (JoystickValue<DeadbandCutoff&&JoystickValue>(DeadbandCutoff*(-1))) {
      deadbandreturn = 0;
    }
    else {
      deadbandreturn= (JoystickValue-(Math.abs(JoystickValue)/JoystickValue*DeadbandCutoff))/(1-DeadbandCutoff);
    }
    return deadbandreturn;
  }
}

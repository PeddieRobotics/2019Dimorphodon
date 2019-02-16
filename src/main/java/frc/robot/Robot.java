package frc.robot;

import frc.robot.framework.Looper;
import frc.robot.lib.BetterJoystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Robot extends TimedRobot {

  DriveTrain drivetrain;
  CargoIntake cIntake;
  HatchIntake hIntake;
  FloorIntake fIntake;
  ShoulderPivot shoulder;
  Looper loop;
  LimeLight lime;
  Vision vision;
  BetterJoystick xbox;
  boolean isDown = false;
  boolean frontSide = true; // Toggles "front" of robot: true = cargo side, false = hatch side

  BetterJoystick leftJoystick, rightJoystick;
  double xDeadBand=0.10;
  double yDeadBand = 0.10;
  public void robotInit() {
    xbox = new BetterJoystick(0);
    drivetrain = new DriveTrain();
    cIntake = new CargoIntake();
    hIntake = new HatchIntake();
    shoulder = new ShoulderPivot();
    lime = new LimeLight();
    vision = new Vision();

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
    updateDash();

    
    //add drive train code here 

    // left joystick controls - Will control hatch intake, speed
    if (xbox.getRawButton(ElectricalLayout.xboxLeftBumper)){
      hIntake.pushOut();
    }/* else if (leftJoystick.getRisingEdge(2)) { no floor intake as of now
      fIntake.hIntake();
    } 
    else if (leftJoystick.getRisingEdge(3)) {
      fIntake.hHold();
    } */
    else if (xbox.getRawButton(ElectricalLayout.xboxXButton)) {
      hIntake.hold();
    }else if(xbox.getRawButton(ElectricalLayout.xboxStart)){//switch what state we are in 
      if(hIntake.pushedOut){
        hIntake.pullBack();
      }else{
        hIntake.pushOut();
      }
    }

    // right joystick controls - Will control cargo intake, turning
    if (xbox.getRawButton(ElectricalLayout.xboxRightBumper)){
      cIntake.eject( -1.0 );
    } 
    /*else if (rightJoystick.getRisingEdge(4)) { don't know what this means 
      if (shoulder.getTargetPosition() == 0.0) {
        // Move up
      } else {
        // Move down
      }
    }*/
     else if (xbox.getRawButton(ElectricalLayout.xboxAButton)) {
      cIntake.intake();
    }else if(xbox.getRawButton(ElectricalLayout.xboxYButton)){
      cIntake.hasCargo();
    }else if(xbox.getRawButton(ElectricalLayout.xboxBButton)){

      cIntake.eject( -0.5 );
    }

  }

  public void testPeriodic() {
  }

  public void updateDash() {
    SmartDashboard.putBoolean("Arm Up", shoulder.getLimitSwitchTop());
    SmartDashboard.putBoolean("Arm Down", shoulder.getLimitSwitchBottom());
    SmartDashboard.putBoolean("Shoulder Brake", shoulder.getBrake());
    SmartDashboard.putBoolean("Hatch", hIntake.hasHatch());
    SmartDashboard.putBoolean("Cargo", cIntake.hasCargo());
    // Output Drive Dist Left
    // Output Drive Dist Right
  }
}

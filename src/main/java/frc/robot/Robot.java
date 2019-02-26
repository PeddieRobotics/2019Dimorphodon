package frc.robot;

import frc.robot.framework.Looper;
import frc.robot.lib.BetterJoystick;
// import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Robot extends TimedRobot {

  public static enum Mode_Type {
    HATCH, CARGO, CLIMB
  };
  private Mode_Type mode;

  DriveTrain drivetrain;
  CargoIntake cIntake;
  HatchIntake hIntake;
  Shoulder shoulder;
  Looper loop;
  Lights hatch;
  LimeLight lime;
  Vision vision;
  BetterJoystick leftJoystick;
  BetterJoystick rightJoystick;
  BetterJoystick opJoystick;
  //BetterJoystick leftJoystick, rightJoystick;

  boolean isDown = false;

  boolean sensorState;
  boolean brakeState;

  double speedDeadBand = 0.07;
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

    hatch = new Lights(7);

    loop = new Looper(10);
    loop.add(drivetrain::update);
    loop.add(cIntake::update);
    loop.add(hIntake::update);
    loop.add(shoulder::update);
    loop.add(lime::update);
    loop.start();

    mode = Mode_Type.HATCH;

    shoulder.setBrakes( true );
    hIntake.setSensors( true );

    sensorState = true; //are using brakes and sensors
    brakeState = true;

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

    if(leftJoystick.getRisingEdge(2)) {
      if( mode == Mode_Type.CARGO ) {
        mode = Mode_Type.HATCH;
      } else if ( mode == Mode_Type.HATCH ) {
        mode = Mode_Type.CARGO;
      }
    }
    if ( opJoystick.getRisingEdge(1) ) {
      mode = Mode_Type.CLIMB;
    } else if ( opJoystick.getRisingEdge(2) ) {
      cIntake.setIntakeSpeed(0.25);
      cIntake.intake();
    } else if ( opJoystick.getRisingEdge(5) ) {
      mode = Mode_Type.HATCH;
    } else if ( opJoystick.getRisingEdge(6) ) {
      mode = Mode_Type.CARGO;
    } else if ( opJoystick.getRisingEdge(3) ) {
      sensorState = !sensorState;
      hIntake.setSensors( sensorState );
      // DriverStation.reportError( "sensorState: " + sensorState, false );
    } else if ( opJoystick.getRisingEdge(4) ) {
      brakeState = !brakeState;
      shoulder.setBrakes( brakeState ); 
      // sDriverStation.reportError( "brakeState: " + brakeState, false );
    }

    switch (mode) {

      case HATCH:

        drivetrain.arcadeDrive(-speed, turn);
        if (rightJoystick.getRisingEdge(2)) {
          hIntake.hold();
        } else if (leftJoystick.getRisingEdge(3)) {
          hIntake.pullBack();
        } else if (leftJoystick.getRisingEdge(4)) {
          hIntake.pushOut();
        }
        else if(rightJoystick.getRisingEdge(1))  { 
          cIntake.eject();
        } 

      break;

      case CARGO:

      /**
       * Now, instead of using a setNoBrake method to set the shoulder,
       * simply change the angle but then set setBrakes to false. It
       * changes to NO_BRAKE_DISENGAGING and etc automatically.
       */

        drivetrain.arcadeDrive(speed, turn);
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
        else if (rightJoystick.getRisingEdge(2)) { //B
          shoulder.setShoulder(20);
          cIntake.setEjectSpeed(-0.7);
        } 
        else if (rightJoystick.getRisingEdge(3)) { //B
          shoulder.setShoulder(65);
          cIntake.setEjectSpeed(-0.5);
        }
        else if (leftJoystick.getRisingEdge(4)) { //A
          shoulder.setShoulder(15);
          cIntake.setEjectSpeed(-1.0);
        } 
        else if(leftJoystick.getRisingEdge(1)){
          shoulder.setShoulder(110);
          cIntake.setIntakeSpeed(0.5);
          cIntake.intake();
        }

      break;

      case CLIMB:
      break;
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

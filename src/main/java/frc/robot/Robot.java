package frc.robot;

import frc.robot.framework.Looper;
import frc.robot.lib.BetterJoystick;
// import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Counter.Mode;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Robot extends TimedRobot {

  public static enum Mode_Type {
    HATCH, CARGO, CLIMB
  };
  private Mode_Type mode;

  DriveTrain drivetrain;
  CargoIntake cargo;
  Climber climber;
  HatchIntake hatch;
  Shoulder shoulder;
  Looper loop;
  Lights hatchLights;
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
    cargo = new CargoIntake();
    hatch = new HatchIntake();
    lime = new LimeLight();
    vision = new Vision();
    shoulder = new Shoulder();
    climber = new Climber();

    hatchLights = new Lights(7);

    loop = new Looper(10);
    loop.add(drivetrain::update);
    loop.add(cargo::update);
    loop.add(hatch::update);
    loop.add(shoulder::update);
    loop.add(lime::update);
    loop.add(climber::update);
    loop.start();

    mode = Mode_Type.HATCH;

    shoulder.setBrakes( true );
    //hIntake.setSensors( true );
    
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
      cargo.setIntakeSpeed(0.25);
      cargo.intake();
    } else if ( opJoystick.getRisingEdge(5) ) {
      mode = Mode_Type.HATCH;
    } else if ( opJoystick.getRisingEdge(6) ) {
      mode = Mode_Type.CARGO;
    } else if ( opJoystick.getRisingEdge(3) ) {
      sensorState = !sensorState;
  //    hIntake.setSensors( sensorState );
      // DriverStation.reportError( "sensorState: " + sensorState, false );
    } else if ( opJoystick.getRisingEdge(4) ) {
      brakeState = !brakeState;
      shoulder.setBrakes( brakeState ); 
      // sDriverStation.reportError( "brakeState: " + brakeState, false );
    }

    switch (mode) {

      case HATCH:
        updateLights();
        drivetrain.arcadeDrive(-speed, turn);
        if (rightJoystick.getRisingEdge(2)) {
          hatch.hold();
        } else if (leftJoystick.getRisingEdge(3)) {
          hatch.pullBack();
        } else if (leftJoystick.getRisingEdge(4)) {
          hatch.pushOut();
        }
        else if(rightJoystick.getRisingEdge(1))  { 
          cargo.eject();
        } 

      break;

      case CARGO:

      /**
       * Now, instead of using a setNoBrake method to set the shoulder,
       * simply change the angle but then set setBrakes to false. It
       * changes to NO_BRAKE_DISENGAGING and etc automatically.
       */
        updateLights();
        drivetrain.arcadeDrive(speed, turn);
        if(rightJoystick.getRisingEdge(1))  { 
          cargo.eject();
        } 
        else if (leftJoystick.getRisingEdge(3)) {
          shoulder.setShoulder(0);
          cargo.setEjectSpeed(0.0);
        }
        else if (rightJoystick.getRisingEdge(4)) { //X
          shoulder.setShoulder(-20);
          cargo.setEjectSpeed(-0.5);
        } 
        else if (rightJoystick.getRisingEdge(3)) { //B
          shoulder.setShoulder(20);
          cargo.setEjectSpeed(-0.7);
        } 
        else if (rightJoystick.getRisingEdge(2)) { //B
          shoulder.setShoulder(65);
          cargo.setEjectSpeed(-0.5);
        }
        else if (leftJoystick.getRisingEdge(4)) { //A
          shoulder.setShoulder(15);
          cargo.setEjectSpeed(-1.0);
        } 
        else if(leftJoystick.getRisingEdge(1)){
          shoulder.setShoulder(110);
          cargo.setIntakeSpeed(0.5);
          cargo.intake();
        }

      break;

      case CLIMB:
        lime.off();//turn the lights off if we are climbing 
        hatchLights.set(false);
        if (rightJoystick.getRisingEdge(1)) 
        {
          shoulder.setShoulder(-20);
          hatch.pushOut();
          climber.fireFront();
        }
        else if (rightJoystick.getRisingEdge(2))
        {
          climber.frontUp();
        }
        else if (leftJoystick.getRisingEdge(1))
        {
          shoulder.setShoulder(110);
          hatch.pullBack();
          climber.fireBack();
        }
        else if (leftJoystick.getRisingEdge(2))
        {
          climber.backUp();
        }
    
      break;
    }
  }

  public void testPeriodic() {
    
  }

  public void updateDash() {
    SmartDashboard.putBoolean("Hatch", hatch.hasHatch());
    SmartDashboard.putBoolean("Cargo", cargo.hasCargo());
    // Output Drive Dist Left
    // Output Drive Dist Right
  }
  public void updateLights(){
    boolean isHatch = (mode == Mode_Type.HATCH);
    hatchLights.update(hatch.hasHatch(),isHatch);//if we have a hatch it will blink, otherwise will be equal to is hatch
    if(!isHatch){
      if(cargo.hasCargo()){
        lime.blink();
      }else{
        lime.solid();
      }
    }else{//so if we aren't in cargo mode we want to be in hatch mode
      lime.defaultValue();
    }
    
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

package frc.robot;

import frc.robot.framework.Looper;
import frc.robot.lib.BetterJoystick;
import edu.wpi.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
// import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Counter.Mode;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.Timer;
public class Robot extends TimedRobot {

  public static enum Mode_Type {
    HATCH, CARGO, CLIMB
  };
  private Mode_Type mode;

  DriveTrain drivetrain;
  CargoIntake cargo;
  Climber climber;
  HatchIntake hatch;
  //Shoulder shoulder;
  ShoulderV2 shoulder;
  Looper loop;
  Lights hatchLights;
  LimeLight lime;
  Vision vision;
  BetterJoystick leftJoystick;
  BetterJoystick rightJoystick;
  BetterJoystick opJoystick;
  //BetterJoystick leftJoystick, rightJoystick;
  private double startTime;
  private double currentScore;
  private double lastScore;
  private double bestScoreTime;
  private String currentMode;
  private double points;
  private double cycles; 
  boolean isDown = false;

  boolean sensorState;
  boolean brakeState;
  boolean lastHadCargo;
  boolean intaking;

  double speedDeadBand = 0.07;
  double turnDeadBand = 0.07;
  double systemsDelay = 0;
  boolean autoStarted = false;

  PowerDistributionPanel pdp;
  
  public void robotInit() {
    leftJoystick = new BetterJoystick(0);
    rightJoystick = new BetterJoystick(1);
    opJoystick = new BetterJoystick(2);
    drivetrain = new DriveTrain();
    cargo = new CargoIntake();
    hatch = new HatchIntake();
    lime = new LimeLight();
    vision = new Vision();
   // shoulder = new Shoulder();
   shoulder = new ShoulderV2();
    climber = new Climber();

    pdp = new PowerDistributionPanel();

    hatchLights = new Lights(7);
    points = 0;
    cycles =0;
    currentScore = -1000;//random numbers
    lastScore = -10000;
    bestScoreTime = currentScore - lastScore;
  
    loop = new Looper(10);
    loop.add(drivetrain::update);
    loop.add(cargo::update);
    loop.add(hatch::update);
    loop.add(shoulder::update);
    loop.add(lime::update);
    loop.add(climber::update);
    loop.start();
    
    mode = Mode_Type.HATCH;

    shoulder.setBrakes(true);
    //hIntake.setSensors( true );
    
    sensorState = true; //are using brakes and sensors
    brakeState = true;
    systemsDelay = Timer.getFPGATimestamp();

    updateDash();
  }

  public void robotPeriodic() {
    
  }

  public void autonomousInit() {

    hatch.hold();

  }

  public void autonomousPeriodic() {
    double time = Timer.getFPGATimestamp();
    if(time-systemsDelay>0.1){
      // double speed = Math.pow(leftJoystick.getRawAxis(1), 3);
      // double turn = Math.pow(rightJoystick.getRawAxis(0), 3);
      double speed = deadband(leftJoystick.getRawAxis(1), speedDeadBand);
      double turn = deadband(rightJoystick.getRawAxis(0), turnDeadBand);

      //double fspeed = xbox.getRawAxis(2)-xbox.getRawAxis(3);
      //double fturn = xbox.getRawAxis(0);

    //cap speed in driveTrain
      updateLights();
      double limecommand = 0;
      if(leftJoystick.getRawButton(1)){
        limecommand = -drivetrain.lime.generateOutput();
      }
      if(rightJoystick.getRisingEdge(1))  { 
        hatch.eject();
      } else if (rightJoystick.getRisingEdge(2)) {
        hatch.hold();
      } else if (rightJoystick.getRisingEdge(3)) {
        hatch.pullBack();
      } else if (rightJoystick.getRisingEdge(4)) {
        hatch.pushOut();
      }
      drivetrain.arcadeDrive(-speed, turn+limecommand);  
      if(opJoystick.getRisingEdge(2)){
        drivetrain.lime.center();
      }
      if(opJoystick.getRisingEdge(5)){
        drivetrain.lime.leftPipeline();
      }
      if(opJoystick.getRisingEdge(6)){
        drivetrain.lime.rightPipeline();
      }
      updateDash();
    }

  }

  public void teleopPeriodic() {

//    DriverStation.reportError( "PDP: " + pdp.getCurrent(12), false);

    double time = Timer.getFPGATimestamp();
    if(time-systemsDelay>0.1){
      // double speed = Math.pow(leftJoystick.getRawAxis(1), 3);
      // double turn = Math.pow(rightJoystick.getRawAxis(0), 3);
      double speed = deadband(leftJoystick.getRawAxis(1), speedDeadBand);
      double turn = deadband(rightJoystick.getRawAxis(0), turnDeadBand);

      //double fspeed = xbox.getRawAxis(2)-xbox.getRawAxis(3);
      //double fturn = xbox.getRawAxis(0);

    //cap speed in driveTrain

      if(leftJoystick.getRisingEdge(2)) {
        if( mode == Mode_Type.CARGO ) {
          shoulder.setShoulder(0);
          cargo.setIntakeSpeed(0);
          hatch.pushOut();
          mode = Mode_Type.HATCH;
        } else if ( mode == Mode_Type.HATCH ) {
          hatch.pullBack();
          mode = Mode_Type.CARGO;
        }
      }
      if ( opJoystick.getRisingEdge(1) ) {
        mode = Mode_Type.CLIMB;
      }
      if(opJoystick.getRisingEdge(2)){
        drivetrain.lime.center();
      }
      if(opJoystick.getRisingEdge(5)){
        drivetrain.lime.leftPipeline();
      }
      if(opJoystick.getRisingEdge(6)){
        drivetrain.lime.rightPipeline();
      }
      
       else if ( opJoystick.getRisingEdge(3) ) {
        mode = Mode_Type.HATCH;
      } else if ( opJoystick.getRisingEdge(4) ) {
        mode = Mode_Type.CARGO;
      }
      
      switch (mode) {

        case HATCH:
          updateLights();
          double limecommand = 0;
          if(leftJoystick.getRawButton(1)){
            limecommand = -drivetrain.lime.generateOutput();
          }
          if(rightJoystick.getRisingEdge(1))  { 
            hatch.eject();
          } else if (rightJoystick.getRisingEdge(2)) {
            hatch.hold();
          } else if (rightJoystick.getRisingEdge(3)) {
            hatch.pullBack();
          } else if (rightJoystick.getRisingEdge(4)) {
            hatch.pushOut();
          }
          drivetrain.arcadeDrive(-speed, turn+limecommand); 
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
            cargo.setIntakeSpeed(0.0);
            intaking = false;
          }
          else if (rightJoystick.getRisingEdge(4)) { //X
            shoulder.setShoulder(-35);
            cargo.setEjectSpeed(-0.4);
            intaking = false;
          } 
          else if (rightJoystick.getRisingEdge(3)) { //B
            shoulder.setShoulder(35);
            cargo.setEjectSpeed(-0.4);
            intaking = false;
          } 
          else if (rightJoystick.getRisingEdge(2)) { //B
            shoulder.setShoulder(68); //was 66
            cargo.setEjectSpeed(-0.5);
            intaking = false;
          }
          else if (leftJoystick.getRisingEdge(4)) { //A
            shoulder.setShoulder(15);
            cargo.setEjectSpeed(-1.0);
            intaking = false;
          } 
          else if(leftJoystick.getRisingEdge(1)){
            shoulder.setShoulder(108);  //was 111
            cargo.setIntakeSpeed(0.5);
            intaking = true;
            cargo.intake();
            
          }
          if (cargo.hasCargo() && !lastHadCargo && intaking) {
            shoulder.setShoulder(35);
            cargo.setEjectSpeed(-0.4);
          }
        break;

        case CLIMB:
  
          lime.off();//turn the lights off if we are climbing 
          hatchLights.set(false);
          hatch.disable();
          drivetrain.arcadeDrive(speed, turn);
          if (rightJoystick.getRisingEdge(4)) 
          {
//            shoulder.setShoulder(-20);
//            hatch.pushOut();
            climber.fireFront();
          }
          else if (rightJoystick.getRisingEdge(3))
          {
            climber.frontUp();
          }
          else if (leftJoystick.getRisingEdge(4))
          {
            shoulder.setShoulder(67.5);
//            hatch.pullBack();
            climber.fireBack();
          }
          else if (leftJoystick.getRisingEdge(3))
          {
            climber.backUp();
          }
      
        break;
      }
      if(rightJoystick.getRawButton(6)){
        
        drivetrain.setLimePIDOn();
      }
      
      lastHadCargo = cargo.hasCargo();
      updateDash();
    }
  }
  public void testInit(){
    
  }
  public void testPeriodic() {
  //  if(leftJoystick.getRawButton(1)){
  //    drivetrain.lime.leftPipeline();
  //    SmartDashboard.putBoolean("pressed",true);
  //  }
  //  if(leftJoystick.getRawButtonPressed(2)){
  //    drivetrain.lime.rightPipeline();
  //  }
  if(leftJoystick.getRawButton(1)) {
    hatch.forward();
  } else if(leftJoystick.getRawButton(2)) {
    hatch.pullBack();
  }
  }
  public void disabledInit(){
    drivetrain.arcadeDrive(0,0);
    hatch.disable();
    lime.off();
  }
  public void updateDash() {
    SmartDashboard.putBoolean("Have Hatch", hatch.hasHatch());
    SmartDashboard.putBoolean("Have Cargo ", cargo.hasCargo());
    SmartDashboard.putBoolean("Cargo Mode",(mode == Mode_Type.CARGO));
    SmartDashboard.putBoolean("Hatch Mode",(mode == Mode_Type.HATCH));
    SmartDashboard.putBoolean("Climb Mode",(mode == Mode_Type.CLIMB));
  }
  public void updateLights(){
    boolean isHatch = (mode == Mode_Type.HATCH);
    hatchLights.update(hatch.hasHatch());//if we have a hatch it will blink, otherwise will be equal to is hatch
    /*if(!isHatch){
      lime.solid();
    }else{//so if we aren't in cargo mode we want to be in hatch mode
      lime.off();
    }
    */
    lime.solid();
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

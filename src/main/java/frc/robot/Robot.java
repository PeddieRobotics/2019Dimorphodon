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
import frc.robot.AutoRoutines.*;

public class Robot extends TimedRobot {

  public static enum Mode_Type {
    HATCH, CARGO, CLIMB
  };

  private Mode_Type mode;

  DriveTrain drivetrain;
  CargoIntake cargo;
  Climber climber;
  HatchIntake hatch;
  // Shoulder shoulder;
  ShoulderV2 shoulder;
  Looper loop;
  Lights hatchLights;
  LimeLight lime;
  Vision vision;
  BetterJoystick leftJoystick;
  BetterJoystick rightJoystick;
  BetterJoystick opJoystick;
  // BetterJoystick leftJoystick, rightJoystick;
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
  boolean autoRun = false;
  PowerDistributionPanel pdp;
  private static final String kDefaultAuto = "Default";
  private static final String leftRocket = "Left Rocket";

  private static final String rightRocket = "Right Rocket";

  private static final String leftCargo = "Left Cargo";

  private static final String rightCargo = "Right Cargo";
  private static final String leftCargoLevel2 = "Left Cargo L2";
  private static final String leftRocketLevel2 = "Left Rocket L2";

  private static final String rightCargoLevel2 = "Right Cargo L2";
  private static final String rightRocketLevel2 = "Right Rocket L2";

  private static final String doNothing = "Do Nothing!!!";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  boolean goBackLeftCargo = false;
  boolean goBackRightCargo = false;

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
    cycles = 0;
    currentScore = -1000;// random numbers
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
    // hIntake.setSensors( true );

    sensorState = true; // are using brakes and sensors
    brakeState = true;
    systemsDelay = Timer.getFPGATimestamp();
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("Right Cargo Level 1", rightCargo);
    m_chooser.addOption("Right Rocket Level 1", rightRocket);
    m_chooser.addOption("Left Cargo Level 1", leftCargo);
    m_chooser.addOption("Left Rocket Level 1", leftRocket);
    m_chooser.addOption("Left Cargo Level 2", leftCargoLevel2);
    m_chooser.addOption("Left Rocket Level 2 ", leftRocketLevel2);
    m_chooser.addOption("Right Cargo Level 2", rightCargoLevel2);
    m_chooser.addOption("Right Rocket Level 2", rightRocketLevel2);
    m_chooser.addOption("Do Nothing!!!", doNothing);

    SmartDashboard.putData("Auto choices", m_chooser);
    shoulder.setIdleBrakeMode(false);
    updateDash();
  }

  public void robotPeriodic() {

  }

  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    DriverStation.reportError("Auto selected: " + m_autoSelected, false);
    // hatch.hold();
    // OtherTest.run(drivetrain, hatch);
    String AutoSelected = m_autoSelected;
    if (m_autoSelected.equals(leftCargo)) {
      LeftForward.run(drivetrain, hatch, opJoystick);
      goBackLeftCargo = true;
    }
    if (m_autoSelected.equals(leftRocket)) {
      RocketLeft.run(drivetrain, hatch, opJoystick);
    }
    if (m_autoSelected.equals(leftRocketLevel2)) {
      RocketLeftLevel2.run(drivetrain, hatch, opJoystick);
    }
    if (m_autoSelected.equals(leftCargoLevel2)) {
      CargoLeftLevel2.run(drivetrain, hatch, opJoystick);
      goBackLeftCargo = true;
      DriverStation.reportError("Left cargo selected", false);
    }
    if (m_autoSelected.equals(rightCargo)) {
      RightForward.run(drivetrain, hatch, opJoystick);
      goBackRightCargo = true;
    }
    if (m_autoSelected.equals(rightCargoLevel2)) {
      CargoRightLevel2.run(drivetrain, hatch, opJoystick);
      goBackRightCargo = true;
    }
    if (m_autoSelected.equals(rightRocket)) {
      RocketRight.run(drivetrain, hatch, opJoystick);
    }
    if (m_autoSelected.equals(rightRocketLevel2)) {
      RocketRightLevel2.run(drivetrain, hatch, opJoystick);
    }
  }

  public void autonomousPeriodic() {

    double time = Timer.getFPGATimestamp();
    if (time - systemsDelay > 0.1) {
      // double speed = Math.pow(leftJoystick.getRawAxis(1), 3);
      // double turn = Math.pow(rightJoystick.getRawAxis(0), 3);
      double speed = deadband(leftJoystick.getRawAxis(1), speedDeadBand);
      double turn = deadband(rightJoystick.getRawAxis(0), turnDeadBand);

      // double fspeed = xbox.getRawAxis(2)-xbox.getRawAxis(3);
      // double fturn = xbox.getRawAxis(0);

      // cap speed in driveTrain
      updateLights();
      double limecommand = 0;
      if (leftJoystick.getRawButton(1)) {
        limecommand = -drivetrain.lime.generateOutput();
        if (drivetrain.lime.currentTvert > 50) {
          if (speed < -0.1) {
            speed = -0.1;
          }
        } else if (drivetrain.lime.currentTvert > 30) {
          if (speed < -0.2) {
            speed = -0.3;

          }
        }
      }
      if (rightJoystick.getRisingEdge(1)) {
        hatch.eject();
        if (!autoRun) {
          if (goBackLeftCargo) {
            LeftBack.run(drivetrain, hatch, opJoystick);
            goBackLeftCargo = false;
            autoRun = true;
          }
          if (goBackRightCargo) {
            RightBack.run(drivetrain, hatch, opJoystick);
            goBackRightCargo = false;
          }
        }
      } else if (rightJoystick.getRisingEdge(2)) {
        hatch.hold();
      } else if (rightJoystick.getRisingEdge(3)) {
        hatch.pullBack();
      } else if (rightJoystick.getRisingEdge(4)) {
        hatch.pushOut();
      }
      drivetrain.arcadeDrive(-speed, turn + limecommand);
      if (opJoystick.getRisingEdge(2)) {
      }
      if (opJoystick.getRisingEdge(3)) {
        drivetrain.lime.leftPipeline();
      }
      if (opJoystick.getRisingEdge(4)) {
        drivetrain.lime.rightPipeline();
      }

      updateDash();
    }

    // DriverStation.reportError("has hatch" + hatch.hasHatch(),false);

  }

  public void teleopPeriodic() {

    // DriverStation.reportError( "PDP: " + pdp.getCurrent(12), false);

    double time = Timer.getFPGATimestamp();
    if (time - systemsDelay > 0.1) {
      // double speed = Math.pow(leftJoystick.getRawAxis(1), 3);
      // double turn = Math.pow(rightJoystick.getRawAxis(0), 3);
      double speed = deadband(leftJoystick.getRawAxis(1), speedDeadBand);
      double turn = deadband(rightJoystick.getRawAxis(0), turnDeadBand);

      // double fspeed = xbox.getRawAxis(2)-xbox.getRawAxis(3);
      // double fturn = xbox.getRawAxis(0);

      // cap speed in driveTrain

      if (leftJoystick.getRisingEdge(2)) {
        if (mode == Mode_Type.CARGO) {
          shoulder.setShoulder(0);
          cargo.setIntakeSpeed(0);
          hatch.pushOut();
          mode = Mode_Type.HATCH;
        } else if (mode == Mode_Type.HATCH) {
          hatch.pullBack();
          mode = Mode_Type.CARGO;
        }
      }
      if (opJoystick.getRisingEdge(1)) {
        mode = Mode_Type.CLIMB;
      }
      if (opJoystick.getRisingEdge(2)) {
        drivetrain.lime.center();
      }
      if (opJoystick.getRisingEdge(3)) {
        drivetrain.lime.leftPipeline();
      }
      if (opJoystick.getRisingEdge(4)) {
        drivetrain.lime.rightPipeline();
      }

      else if (opJoystick.getRisingEdge(5)) {
        mode = Mode_Type.HATCH;
      } else if (opJoystick.getRisingEdge(6)) {
        mode = Mode_Type.CARGO;
      } else if (opJoystick.getRawButton(7)) {
        vision.camera1.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
        vision.camera2.setConnectionStrategy(ConnectionStrategy.kKeepOpen);

      } else if (opJoystick.getRawButton(8)) {
        vision.camera1.setConnectionStrategy(ConnectionStrategy.kForceClose);
        vision.camera2.setConnectionStrategy(ConnectionStrategy.kForceClose);
      }

      switch (mode) {

      case HATCH:
        updateLights();
        double limecommand = 0;
        if (leftJoystick.getRawButton(1)) {
          limecommand = -drivetrain.lime.generateOutput();
          if (drivetrain.lime.currentTvert > 50) {
            if (speed < -0.1) {

              speed = -0.1;
            }
          } else if (drivetrain.lime.currentTvert > 30) {
            if (speed < -0.2) {
              speed = -0.3;

            }
          }
        }
        if (rightJoystick.getRisingEdge(1)) {
          hatch.eject();
        } else if (rightJoystick.getRisingEdge(2)) {
          hatch.hold();
        } else if (rightJoystick.getRisingEdge(3)) {
          hatch.pullBack();
        } else if (rightJoystick.getRisingEdge(4)) {
          hatch.pushOut();
        }
        drivetrain.arcadeDrive(-speed, turn + limecommand);
        break;

      case CARGO:

        /**
         * Now, instead of using a setNoBrake method to set the shoulder, simply change
         * the angle but then set setBrakes to false. It changes to NO_BRAKE_DISENGAGING
         * and etc automatically.
         */
        updateLights();
        drivetrain.arcadeDrive(speed, turn);
        if (rightJoystick.getRisingEdge(1)) {
          cargo.eject();
        } else if (leftJoystick.getRisingEdge(3)) {
          shoulder.setBrakes(true);
          shoulder.setShoulder(0);
          cargo.setEjectSpeed(0.0);
          cargo.setIntakeSpeed(0.0);
          intaking = false;
        } else if (rightJoystick.getRisingEdge(4)) { // X
          shoulder.setBrakes(true);
          shoulder.setShoulder(-35);
          cargo.setEjectSpeed(-0.4);
          intaking = false;
        } else if (rightJoystick.getRisingEdge(3)) { // B
          shoulder.setBrakes(true);
          shoulder.setShoulder(35);
          cargo.setEjectSpeed(-0.4);
          intaking = false;
        } else if (rightJoystick.getRisingEdge(2)) { // B
          shoulder.setBrakes(true);
          shoulder.setShoulder(68); // was 66
          cargo.setEjectSpeed(-0.5);
          intaking = false;
        } else if (leftJoystick.getRisingEdge(4)) { // A
          shoulder.setBrakes(true);
          shoulder.setShoulder(15);
          cargo.setEjectSpeed(-1.0);

          intaking = false;
        } else if (leftJoystick.getRisingEdge(1)) {
          shoulder.setBrakes(true);
          shoulder.setShoulder(108); // was 111
          cargo.setIntakeSpeed(0.5);
          intaking = true;
          cargo.intake();

        }
        if (cargo.hasCargo() && !lastHadCargo && intaking) {
          shoulder.setBrakes(true);
          shoulder.setShoulder(35);
          cargo.setEjectSpeed(-0.4);
        }
        break;

      case CLIMB:

        lime.off();// turn the lights off if we are climbing
        hatchLights.set(false);
        hatch.disable();
        drivetrain.arcadeDrive(speed, turn);
        if (rightJoystick.getRisingEdge(1)) {

          // shoulder.setShoulder(-20);
          // hatch.pushOut();
          shoulder.setBrakes(true);
          shoulder.setShoulder(0);
          intaking = false;
          climber.fireFront();

        } else if (rightJoystick.getRisingEdge(2)) {

          climber.frontUp();
          shoulder.setBrakes(true);
          shoulder.setShoulder(67.5);
          // hatch.pullBack();
          climber.fireBack();

        } else if (leftJoystick.getRisingEdge(1)) {

          climber.backUp();

        } else if (leftJoystick.getRisingEdge(3)) {

          shoulder.setBrakes(true);
          shoulder.setShoulder(0);
          cargo.setEjectSpeed(0.0);
          cargo.setIntakeSpeed(0.0);
          intaking = false;

        } else if (opJoystick.getRisingEdge(8)) {

          climber.frontUp();
          climber.backUp();
          shoulder.setBrakes(true);
          shoulder.setShoulder(0);

        }

        /*
         * else if (leftJoystick.getRisingEdge(3)) { }
         */

        break;
      }
      if (rightJoystick.getRawButton(6)) {

        drivetrain.setLimePIDOn();
      }

      lastHadCargo = cargo.hasCargo();
      updateDash();
    }
  }

  public void testInit() {

  }

  public void testPeriodic() {
    // if(leftJoystick.getRawButton(1)){
    // drivetrain.lime.leftPipeline();
    // SmartDashboard.putBoolean("pressed",true);
    // }
    // if(leftJoystick.getRawButtonPressed(2)){
    // drivetrain.lime.rightPipeline();
    // }
    if (leftJoystick.getRawButton(1)) {
      hatch.forward();
    }
    if (leftJoystick.getRawButton(2)) {
      hatch.pullBack();
    }
  }

  public void disabledInit() {
    drivetrain.arcadeDrive(0, 0);
    hatch.disable();
    lime.off();
    shoulder.setIdleBrakeMode(false);
    autoRun = false;
  }

  public void updateDash() {
    SmartDashboard.putBoolean("Have Hatch", hatch.hasHatch());
    SmartDashboard.putBoolean("Have Cargo ", cargo.hasCargo());
    SmartDashboard.putBoolean("Cargo Mode", (mode == Mode_Type.CARGO));
    SmartDashboard.putBoolean("Hatch Mode", (mode == Mode_Type.HATCH));
    SmartDashboard.putBoolean("Climb Mode", (mode == Mode_Type.CLIMB));
  }

  public void updateLights() {
    boolean isHatch = (mode == Mode_Type.HATCH);
    hatchLights.update(hatch.hasHatch());// if we have a hatch it will blink, otherwise will be equal to is hatch
    /*
     * if(!isHatch){ lime.solid(); }else{//so if we aren't in cargo mode we want to
     * be in hatch mode lime.off(); }
     */
    lime.solid();
  }

  public double deadband(double JoystickValue, double DeadbandCutoff) {
    double deadbandreturn;
    if (JoystickValue < DeadbandCutoff && JoystickValue > (DeadbandCutoff * (-1))) {
      deadbandreturn = 0;
    } else {
      deadbandreturn = (JoystickValue - (Math.abs(JoystickValue) / JoystickValue * DeadbandCutoff))
          / (1 - DeadbandCutoff);
    }
    return deadbandreturn;
  }
}

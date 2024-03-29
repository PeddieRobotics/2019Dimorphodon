package frc.robot;


import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HatchIntake extends Subsystem {

  private static enum ModeType {
    INTAKING, HOLDING, EJECTING, DISABLED, DISENGAGING, BACK, FORWARD
  };

  private ModeType mode = ModeType.DISABLED;

  public boolean pushedOut;
  private Solenoid pushOut; // pushes the entire mechanism out

  private Solenoid grabberS; // controls the middle claw grabber
  private boolean grabbing;

  private Solenoid puncherS; // punches the hatch off the middle claw grabber
  private boolean punching;

  public boolean hasHatch;
  private AnalogInput leftSensor;
  private AnalogInput rightSensor;
  private double currentRawValue;
  private double averageRawValue;
  private double displayRawValue;
  private int numberOfLoops = 5;// basically the amount of times we want to look at a hatch sensor
  private int loopsDone = 0;
  private double ejectTime = 0;

  public HatchIntake() {
    pushOut = new Solenoid(ElectricalLayout.SOLENOID_HATCH_DEPLOY);
    puncherS = new Solenoid(ElectricalLayout.SOLENOID_HATCH_PUNCHER);
    grabberS = new Solenoid(ElectricalLayout.SOLENOID_HATCH_GRABBER);
    leftSensor = new AnalogInput(ElectricalLayout.SENSOR_LEFT_HATCH_INTAKE);
    rightSensor = new AnalogInput(ElectricalLayout.SENSOR_RIGHT_HATCH_INTAKE);
  }

  public void initDefaultCommand() {
  }

  /**
   * push the intake out
   */
  public void pushOut() {
    mode = ModeType.INTAKING;
  }

  /**
   * hold a hatch
   */
  public void hold() {
    mode = ModeType.HOLDING;
  }

  /**
   * eject a hatch
   */
  public void eject() {
    ejectTime = Timer.getFPGATimestamp();
    mode = ModeType.EJECTING;
  }

  /**
   * intake a hatch
   */
  public void intake() {
    mode = ModeType.INTAKING;
  }

  /**
   * push the intake forward but don't be ready to intake
   */
  public void forward() {
    mode = ModeType.FORWARD;
  }

  /**
   * pull the intake back but don't disable it
   */
  public void pullBack() {
    mode = ModeType.BACK;
  }

  /**
   * disable intake
   */
  public void disable() {
    mode = ModeType.DISABLED;
  }

  /**
   * @return whether the intake has a hatch or not
   */
  public boolean hasHatch() {
    return (leftSensor.getValue() < 3700 && rightSensor.getValue() < 3700);
  }

  public void update() {
    // updateCalculations();
    DriverStation.reportError("" + hasHatch(), false);
    switch (mode) {

    case INTAKING: // Holds panel up to grabber
      pushedOut = true;
      grabbing = false; // middle grabber open
      punching = false; // puncher back
      if (hasHatch() == true) {
        mode = ModeType.HOLDING; // if it has been waiting for 200ms, it begins to hold
      } else {
        mode = ModeType.INTAKING; // continues to intake
      }
    break;

    case FORWARD: // to keep grabbing but start moving
      punching = false; // puncher back
      pushedOut = true;
      if (hasHatch() == true) {
        mode = ModeType.HOLDING; // if it has been waiting for 200ms, it begins to hold
      }
    break;

    case DISENGAGING:
      grabbing = false; // middle grabber open/not holding hatch panel
      punching = false; // punches

      if (Timer.getFPGATimestamp() - ejectTime > 1.2) { // compares the time we started waiting to current time
        mode = ModeType.INTAKING; // if it has been waiting for 200ms, it begins to hold
      }
    break;

    case HOLDING: // Holds panel up to grabber

      grabbing = true; // middle grabber locks/holding on a hatch panel
      punching = false; // puncher back

    break;

    case EJECTING: // Punches panel out

      grabbing = false; // middle grabber open/not holding hatch panel
      punching = true; // punches

      if (Timer.getFPGATimestamp() - ejectTime > 0.6) { // compares the time we started waiting to current time
        mode = ModeType.DISENGAGING; // if it has been waiting for 200ms, it begins to hold
      }

    break;

    case BACK:

      punching = false;
      pushedOut = false;
    break;

    case DISABLED:

      grabbing = true;
      punching = false;
      pushedOut = false; // pushes out

    break;

    }

    pushOut.set(pushedOut);
    grabberS.set(!grabbing);
    puncherS.set(punching);

  }

  /**
   * The point of this is to do an average of the values incase Of some random
   * spike
   */
  // to delete
  public void updateCalculations() {
    
    if (loopsDone < numberOfLoops) {
      currentRawValue = leftSensor.getValue();
      averageRawValue += currentRawValue;
      loopsDone++;
    } else {
      averageRawValue = averageRawValue / numberOfLoops;
      displayRawValue = averageRawValue;
      loopsDone = 0;
      if (displayRawValue < 3700) {
        hasHatch = true;
      } else {
        hasHatch = false;
      }
    }
    SmartDashboard.putBoolean("hasHatch", hasHatch);
    SmartDashboard.putNumber("DisplayRawValue", displayRawValue);

  }

}

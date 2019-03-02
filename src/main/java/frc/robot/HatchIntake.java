package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HatchIntake extends Subsystem {

//  Blinkin blinkin;

  private double lastTime;

  private static enum ModeType {
    INTAKING, HOLDING, EJECTING, DISABLED, DISENGAGING, BACK
  };

  private ModeType mode = ModeType.DISABLED;

  private boolean intaking; // Used to set lastTime
  private boolean ejecting; // Used to set lastTime

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
   
//    blinkin = new Blinkin();
  }

  public void initDefaultCommand() {
  }

  public void pushOut() {
    mode = ModeType.INTAKING;
  }

  public void hold() {
    mode = ModeType.HOLDING;
  }

  public void eject() {
    ejectTime = Timer.getFPGATimestamp();
    mode = ModeType.EJECTING;
  }

  public void intake() {
    mode = ModeType.INTAKING;
  }

  public void pullBack() {
    mode = ModeType.BACK;
  }

  public void disable() {
    mode = ModeType.DISABLED;
  }

  public boolean hasHatch() {
    return (leftSensor.getValue() < 3700 && rightSensor.getValue() < 3700);
  }

  public void update() {
//    updateCalculations();
    switch (mode) {
      case INTAKING: // Holds panel up to grabber
      pushedOut = true;
      grabbing = false;  // middle grabber open
      punching = false; // puncher back
          if ( hasHatch() == true ) {
//            blinkin.strobeBlue();
            mode = ModeType.HOLDING; //if it has been waiting for 200ms, it begins to hold
          } else {
            mode = ModeType.INTAKING; //continues to intake
          }
    break;

    case DISENGAGING:
    grabbing = false; // middle grabber open/not holding hatch panel
    punching = false; // punches

      if (Timer.getFPGATimestamp() - ejectTime > 1.2) { //compares the time we started waiting to current time
        mode = ModeType.INTAKING; //if it has been waiting for 200ms, it begins to hold
      }
    break;
    case HOLDING: // Holds panel up to grabber

      grabbing = true;  // middle grabber locks/holding on a hatch panel
      punching = false; // puncher back

    break;

    case EJECTING: // Punches panel out

      grabbing = false; // middle grabber open/not holding hatch panel
      punching = true;  // punches

        if (Timer.getFPGATimestamp() - ejectTime > 0.6) { //compares the time we started waiting to current time
        	mode = ModeType.DISENGAGING; //if it has been waiting for 200ms, it begins to hold
        } 

    break;

    case BACK:

        punching = false;
        pushedOut = false;
    break;

    case DISABLED:

      grabbing = false;
      punching = false;
      pushedOut = false; // pushes out

//      blinkin.fireLarge();

      break;

    }

    pushOut.set(pushedOut);
    grabberS.set(grabbing);
    puncherS.set(punching);

  }

  /**
   * The point of this is to do an average of the values incase Of some random
   * spike
   */
  //to delete
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

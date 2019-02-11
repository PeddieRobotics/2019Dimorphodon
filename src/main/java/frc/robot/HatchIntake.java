package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HatchIntake extends Subsystem {

  private double lastTime;

  private static enum ModeType {
    INTAKING, HOLDING, EJECTING, DISABLED, ENABLED, CALCULATING
  };

  private ModeType mode = ModeType.DISABLED;

  private boolean pushedOut;
  private Solenoid pushOut; // pushes the entire mechanism out

  private Solenoid grabberS; // controls the middle claw grabber
  private boolean grabbing;

  private Solenoid puncherS; // punches the hatch off the middle claw grabber
  private boolean punching;

  public boolean hasHatch;
  private AnalogInput hatchSensor;
  private double currentRawValue;
  private double averageRawValue;
  private double displayRawValue;
  private int numberOfLoops = 5;//basically the amount of times we want to look at a hatch sensor 
  private int loopsDone = 0;
  public HatchIntake() {
    pushOut = new Solenoid(ElectricalLayout.SOLENOID_HATCH_DEPLOY);
    puncherS = new Solenoid(ElectricalLayout.SOLENOID_HATCH_PUNCHER);
    grabberS = new Solenoid(ElectricalLayout.SOLENOID_HATCH_GRABBER);
    hatchSensor = new AnalogInput(ElectricalLayout.SENSOR_HATCH_INTAKE);
  }

  public void initDefaultCommand() {
  }

  public void pushOut() {
    mode = ModeType.ENABLED;
  }

  public void hLock() {
    mode = ModeType.HOLDING;
  }

  public void hEject() {
    mode = ModeType.EJECTING;
  }


  public void pullBack() {
    mode = ModeType.DISABLED;
  }

  public boolean hasHatch() {
    mode = ModeType.INTAKING;
    return (hatchSensor.getValue() < 3700 );
  }


  public void update() {
    updateCalculations();
    switch (mode) {
    case INTAKING: // Holds panel up to grabber
     
      grabbing = false; // middle grabber open
      punching = false; // puncher back

      double waitTimeIntake = Timer.getFPGATimestamp(); // stamps current time
      if (waitTimeIntake - lastTime > 0.6) { // compares the time we started waiting to current time
        if (hasHatch() == true) {
          mode = ModeType.HOLDING; // if it has been waiting for 200ms, it begins to hold
        } else {
          mode = ModeType.INTAKING; // continues to intake
        }
      } else {
        mode = ModeType.EJECTING; // if not, it keeps waiting
      }

      break;

    case HOLDING: // Holds panel up to grabber

      grabbing = true; // middle grabber locks/holding on a hatch panel
      punching = false; // puncher back

      break;

    case EJECTING: // Punches panel out

      grabbing = false; // middle grabber open/not holding hatch panel
      punching = true; // punches

      double waitTimeEject = Timer.getFPGATimestamp(); // stamps current time
      if (waitTimeEject - lastTime > 0.6) { // compares the time we started waiting to current time
        mode = ModeType.HOLDING; // if it has been waiting for 200ms, it begins to hold
      } else {
        mode = ModeType.EJECTING; // if not, it keeps waiting
      }

      break;

    case DISABLED:

      grabbing = false;
      punching = false;
      pushedOut = false; // pushes out

      break;

    case ENABLED:

      grabbing = false;
      punching = false;
      pushedOut = true; // pushes out

      break;

    }

    pushOut.set(pushedOut);

    grabberS.set(grabbing);
    puncherS.set(punching);

  }
  /**
   * The point of this is to do an average of the values incase 
   * Of some random spike 
   */
  public void updateCalculations(){
    if(loopsDone<numberOfLoops){
        currentRawValue = hatchSensor.getValue();
        averageRawValue+=currentRawValue;
        loopsDone++;
    }
    else{
        averageRawValue = averageRawValue/numberOfLoops;
        displayRawValue = averageRawValue;
        loopsDone = 0;
        if(displayRawValue<3700){
            hasHatch = true;
        }else{
            hasHatch = false;
        }
    }
    SmartDashboard.putBoolean("hasHatch",hasHatch);
    SmartDashboard.putNumber("DisplayRawValue",displayRawValue);

  }
  
}

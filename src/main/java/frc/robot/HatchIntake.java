package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class HatchIntake extends Subsystem {

  private static enum ModeType {
    LOCKING, PUNCHING, DISABLED, ENABLED
  };
  private ModeType mode = ModeType.DISABLED;

  private boolean pushedOut;
  private Solenoid pushOut; // pushes the entire mechanism out

  private Solenoid grabberS; // controls the middle claw grabber
  private boolean grabbing;

  private Solenoid puncherS; // punches the hatch off the middle claw grabber
  private boolean punching;

  public boolean hasHatch;

  public HatchIntake() {
    pushOut = new Solenoid(ElectricalLayout.SOLENOID_HATCH_DEPLOY);
    puncherS = new Solenoid(ElectricalLayout.SOLENOID_HATCH_PUNCHER);
    grabberS = new Solenoid(ElectricalLayout.SOLENOID_HATCH_GRABBER);
  }

  public void initDefaultCommand() {
  }

  public void pushOut() {
    mode = ModeType.ENABLED;
  }

  public void hLock() {
    mode = ModeType.LOCKING;
  }

  public void hEject() {
    mode = ModeType.PUNCHING;
  }

  public void pullBack() {
    mode = ModeType.DISABLED;
  }

  public boolean hasHatch() {
    return hasHatch;
  }

  // public boolean hasHatch() {
  // //will use if we have a sensor
  // }

  public void update() {

    switch (mode) {

    case LOCKING: // Holds panel up to grabber

      grabbing = true; // middle grabber locks/holding on a hatch panel
      punching = false; // puncher back

      hasHatch = true;

      break;

    case PUNCHING: // Punches panel out

      grabbing = false; // middle grabber open/not holding hatch panel
      punching = true; // punches

      hasHatch = false;

      break;

    case DISABLED:

      grabbing = false;
      punching = false;
      pushedOut = false; // pushes out

      hasHatch = false;

      break;

    case ENABLED:

      grabbing = false;
      punching = false;
      pushedOut = true; // pushes out

      hasHatch = false;

      break;

    }

    pushOut.set(pushedOut);

    grabberS.set(grabbing);
    puncherS.set(punching);

  }

}
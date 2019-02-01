package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class HatchIntake extends Subsystem {

  private static enum ModeType {
    INTAKING, HOLDING, LOCKING, PUNCHING, DISABLED, ENABLED
  };

  private ModeType mode = ModeType.DISABLED;

  private boolean pushedOut;

  private Solenoid pushOut; // pushes the entire mechanism out

  private Solenoid intakeS; // brings the intake up/down
  private boolean intaking;

  private Solenoid clampS; // clamps the hatch to the floor intake
  private boolean clamping;

  private Solenoid grabberS; // controls the middle claw grabber
  private boolean grabbing;

  private Solenoid puncherS; // punches the hatch off the middle claw grabber
  private boolean punching;

  public boolean hasHatch;

  public HatchIntake() {
    pushOut = new Solenoid(ElectricalLayout.SOLENOID_HATCH_DEPLOY);
    puncherS = new Solenoid(ElectricalLayout.SOLENOID_HATCH_PUNCHER);
    grabberS = new Solenoid(ElectricalLayout.SOLENOID_HATCH_GRABBER);
    clampS = new Solenoid(ElectricalLayout.SOLENOID_FLOOR_CLAMP);
    intakeS = new Solenoid(ElectricalLayout.SOLENOID_FLOOR_PIVOT);
  }

  public void initDefaultCommand() {
  }

  public void pushOut() {
    mode = ModeType.ENABLED;
  }

  public void hIntake() {
    mode = ModeType.INTAKING;
  }

  public void hHold() {
    mode = ModeType.HOLDING;
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

    case INTAKING: // Picks panel off floor

      intaking = true; // brings the panel down
      clamping = false; // unclamps
      grabbing = false; // middle grabber ready to grab
      punching = false; // puncher back

      hasHatch = false;

      break;

    case HOLDING: // Clamps the panel to the floor intake and lifts to grabber

      intaking = true; // brings the panel up
      clamping = true; // clamps
      grabbing = false; // middle grabber ready to grab
      punching = false; // puncher back

      hasHatch = true;

      break;

    case LOCKING: // Holds panel up to grabber

      grabbing = true; // middle grabber locks/holding on a hatch panel
      clamping = false; // unclamps
      intaking = true; // puts the panel down
      punching = false; // puncher back

      hasHatch = true;

      break;

    case PUNCHING: // Punches panel out

      clamping = false; // unclamping
      intaking = true; // panel down
      grabbing = false; // middle grabber open/not holding hatch panel
      punching = true; // punches

      hasHatch = false;

      break;

    case DISABLED:

      intaking = false;
      clamping = false;
      grabbing = false;
      punching = false;
      pushedOut = false; // pushes out

      hasHatch = false;

      break;

    case ENABLED:

      intaking = false;
      clamping = false;
      grabbing = false;
      punching = false;
      pushedOut = true; // pushes out

      hasHatch = false;

      break;

    }

    pushOut.set(pushedOut);

    intakeS.set(intaking);
    clampS.set(clamping);
    grabberS.set(grabbing);
    puncherS.set(punching);

  }

}
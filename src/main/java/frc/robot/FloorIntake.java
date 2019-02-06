package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class FloorIntake extends Subsystem {

  private static enum ModeType {
    INTAKING, HOLDING, DISABLED, ENABLED
  };

  private ModeType mode = ModeType.DISABLED;

  private Solenoid intakeS; // brings the intake up/down
  private boolean intaking;

  private Solenoid clampS; // clamps the hatch to the floor intake
  private boolean clamping;

  public void initDefaultCommand() {
  }

  public FloorIntake() {
    clampS = new Solenoid(ElectricalLayout.SOLENOID_FLOOR_CLAMP);
    intakeS = new Solenoid(ElectricalLayout.SOLENOID_FLOOR_PIVOT);
  }

  public void hIntake() {
    mode = ModeType.INTAKING;
  }

  public void hHold() {
    mode = ModeType.HOLDING;
  }

  public void update() {

    switch (mode) {

     case INTAKING: // Picks panel off floor
    
        intaking = true; // brings the panel down
        clamping = false; // unclamps

      break;

      case HOLDING: // Clamps the panel to the floor intake and lifts to grabber

        intaking = true; // brings the panel up
        clamping = true; // clamps

      break;

     case DISABLED:

        intaking = false;
        clamping = false;

      break;

      case ENABLED:

        intaking = false;
        clamping = false;

      break;

    }

    intakeS.set(intaking);
    clampS.set(clamping);

  }
}

package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class FloorIntake extends Subsystem {

  private double lastTime;

  private static enum ModeType {
    INTAKING, HOLDING, EJECTING, DISABLED, ENABLED
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

  public void hEject() {
    mode = ModeType.EJECTING;
    lastTime = Timer.getFPGATimestamp();
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

      case EJECTING: // Clamps the panel to the floor intake and lifts to grabber

        intaking = true; // panel up
        clamping = false; // unclamped

        double waitTime = Timer.getFPGATimestamp(); //stamps current time 
        if (waitTime - lastTime > 0.6) { //compares the time we started waiting to current time
        	mode = ModeType.INTAKING; //if it has been waiting for 200ms, it begins to intake
        } else {
        	mode = ModeType.EJECTING; //if not, it keeps waiting
        }

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

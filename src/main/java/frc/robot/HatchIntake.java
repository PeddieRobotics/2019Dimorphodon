package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class HatchIntake extends Subsystem {  

  private boolean pushedOut;

  private Solenoid pushTheWholeThingOut;  //pushes the entire mechanism out

  private Solenoid intakeS;     //brings the intake up/down
  private boolean intaking;

  private Solenoid clampS;      //clamps on the hatch
  private boolean clamping;

  private Solenoid grabberS;    //controls the middle claw grabber
  private boolean grabbing;

  private Solenoid puncherS;    //punches the hatch off the middle claw grabber
  private boolean punching;
  
  public void initDefaultCommand() {

    pushTheWholeThingOut = new Solenoid(ElectricalLayout.SOLENOID_HATCH_DEPLOY);
    puncherS = new Solenoid(ElectricalLayout.SOLENOID_HATCH_PUNCHER);
    grabberS = new Solenoid(ElectricalLayout.SOLENOID_HATCH_GRABBER);
    clampS = new Solenoid (ElectricalLayout.SOLENOID_FLOOR_CLAMP);
    intakeS = new Solenoid(ElectricalLayout.SOLENOID_FLOOR_PIVOT);

    pushedOut = true;   //when competition starts, it pushes the thing out right away

  }

  public void hIntake() {
    intaking = true;     //brings the panel down
    clamping = false;    //unclamps
    grabbing = false;    //middle grabber ready to grab
    punching = false;    //puncher back
  }

  public void hHold() {
    intaking = false;    //brings the panel up
    clamping = true;     //unclamps
    grabbing = false;    //middle grabber ready to grab
    punching = false;    //puncher back
  }

  public void hLock() {
    grabbing = true;     //middle grabber locks/holding on a hatch panel
    clamping = false;    //unclamps
    intaking = true;     //puts the panel down
    punching = false;    //puncher back
  }

  public void hEject() {
    clamping = false;    //unclamping
    intaking = true;     //panel down
    grabbing = false;    //middle grabber open/not holding hatch panel
    punching = true;     //punches
  }

  public void pullBack() {
    intaking = false;   //panel up
    clamping = false;   //unclamped
    grabbing = false;   //not grabbing
    punching = false;   //puncher back
    pushedOut = false;    //pulls the whole thing back
  }

//  public boolean hasHatch() {
//    //will use if we have a sensor
//  }

  public void update(){

    pushTheWholeThingOut.set(pushedOut);

    intakeS.set(intaking);
    clampS.set(clamping);
    grabberS.set(grabbing);
    puncherS.set(punching);

	}

}
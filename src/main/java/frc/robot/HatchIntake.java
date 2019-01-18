package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class HatchIntake extends Subsystem {  

  private boolean intaking;
  private boolean locking;


  private Solenoid pushTheWholeThingOut;  //pushes the entire mechanism out
  private boolean out;

  private Solenoid intakeS;     //brings the intake up/down
  private boolean down;

  private Solenoid clampS;      //clamps on the hatch
  private boolean clamped;

  private Solenoid grabberS;    //controls the middle claw grabber
  private boolean grabbed;

  private Solenoid puncherS;    //punches the hatch off the middle claw grabber
  private boolean punched;
  
  public void initDefaultCommand() {

    pushTheWholeThingOut = new Solenoid(1);
    puncherS = new Solenoid(2);
    grabberS = new Solenoid(3);
    clampS = new Solenoid (4);
    intakeS = new Solenoid(5);

    out = true;   //when competition starts, it pushes the thing out right away

  }

  public void hIntake() {
    intaking = true;  //hatch panel intake thing down
  }

  public void hHold() {
    intaking = false;   //hatch panel intake thing up
  }

  public void hLock() {
    locking = true;      //middle grabber UP a hatch panel
  }

  public void hEject() {
    locking = false;    //middle grabber open/not UP hatch panel
  }

  public boolean hasHatch() {
    //will use if we have a sensor
  }

  public void update(){

    intakeS.set(intaking);
    
	}

}
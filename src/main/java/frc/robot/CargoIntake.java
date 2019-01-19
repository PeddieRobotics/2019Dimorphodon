
package frc.robot;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DigitalInput;


public class CargoIntake extends Subsystem {

  private static enum ModeType {INTAKING, EJECTING, HOLDING, DISABLED};
  private ModeType mode = ModeType.DISABLED;

  private CANSparkMax shoulder;
  private CANSparkMax wrist;

  private CANPIDController spid;
  private CANPIDController wpid;

  public boolean isDown;
  private double ejectSpeed;
  private double speed;
  private double encoderOffset;

  DigitalInput shoulderLimitSwitch;
//  private CANEncoder sEncoder;
//  private CANEncoder wEncoder;

  public CargoIntake() {
    shoulder = new CANSparkMax( 0, MotorType.kBrushless );
    wrist = new CANSparkMax( 1, MotorType.kBrushless );

    spid = shoulder.getPIDController();
    wpid = wrist.getPIDController();

    shoulderLimitSwitch = new DigitalInput(8);
  //  sEncoder = shoulder.getEncoder();
  //  wEncoder = wrist.getEncoder();
  }

  public void home() {
    if (shoulderLimitSwitch.get()) {
      shoulder.set(0.1);
    }
    else { 
      shoulder.set(0);
      encoderOffset = shoulder.getEncoder().getPosition();
    }
  }
  
  public void intake() {
    mode = ModeType.INTAKING;  //intakes
  }

  public void ejectFast() {
    ejectSpeed = 1.0;
    mode = ModeType.EJECTING;
  }

  public void ejectSlow() {
    ejectSpeed = 0.5;
    mode = ModeType.EJECTING;
  }

  public void disable() {
    mode = ModeType.DISABLED;
  }

  public void clawUp() {
    isDown = false;
    mode = ModeType.HOLDING;
  }

  public void clawDown() {
    isDown = true;
    mode = ModeType.INTAKING;
  }

  /*
  will use if we have a sensor
  public boolean hasCargo() {
  }
  */
  public void update(){
		
		switch(mode) {
		
		case INTAKING:
		     
		  speed = 1.0;
		     
      /*
      if(hasCargo()) { 
			 	mode = ModeType.HOLDING; 
       }
       */
       
			break;
		
    case HOLDING:

			speed = 0.001;
    //	DriverStation.reportError("holding", false);
    
			break;
		
		case EJECTING:
      speed = -ejectSpeed;
			break; 
			
		case DISABLED:
			speed = 0;
			break;
		}
		
		wrist.set(speed);
		
	}

  
  public void initDefaultCommand() {
  }

}

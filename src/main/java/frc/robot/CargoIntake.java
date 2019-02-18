
package frc.robot;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class CargoIntake extends Subsystem {

  private double lastTime;
  
  private static enum Mode_Type{
    INTAKING,EJECTING,HOLDING,DISABLED
  };

  private Mode_Type mode = Mode_Type.DISABLED;

  TalonSRX rightClaw, leftClaw;
  AnalogInput rightSensor, leftSensor;

  double speed;
  double ejectSpeed;

  public CargoIntake(){
    //initialize things here
    rightClaw = new TalonSRX(ElectricalLayout.MOTOR_CARGO_LEFT_CLAW);
    leftClaw = new TalonSRX(ElectricalLayout.MOTOR_CARGO_RIGHT_CLAW);

    rightSensor = new AnalogInput(ElectricalLayout.SENSOR_LEFT_CLAW_INTAKE);
    leftSensor = new AnalogInput(ElectricalLayout.SENSOR_RIGHT_CLAW_INTAKE);

  }

  public boolean hasCargo(){
    return (rightSensor.getVoltage() < 4.5 && leftSensor.getVoltage() < 4.5);
  }

  public void intake(){
    mode = Mode_Type.INTAKING;
  }

  public void hold() {
    mode = Mode_Type.HOLDING;
  }

  public void eject(){
    mode = Mode_Type.EJECTING;
  }

  public void disabled(){
    mode = Mode_Type.DISABLED;
  }

  public void update() {
    switch (mode) {

      case INTAKING:
        speed = 0.5;
         if(hasCargo()) { 
           mode = Mode_Type.HOLDING; 
          }
        break;
  
      case HOLDING:
        speed = 0.1;
        break;
  
      case EJECTING:
        speed = ejectSpeed;

        double waitTimeEject = Timer.getFPGATimestamp();
        if (waitTimeEject - lastTime > 0.6) { 
        	mode = Mode_Type.INTAKING;
        } 
        break;
  
      case DISABLED:
        speed = 0;
        break;
      }
      leftClaw.set(ControlMode.PercentOutput, speed);
      rightClaw.set(ControlMode.PercentOutput, -speed);
  }

  @Override
  public void initDefaultCommand() {
    
  }
}

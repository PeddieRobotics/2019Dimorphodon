
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

  private double ejectTime;

  private static enum Mode_Type {
    INTAKING, EJECTING, HOLDING, DISABLED
  };

  private Mode_Type mode = Mode_Type.DISABLED;

  TalonSRX rightClaw, leftClaw;
  AnalogInput rightSensor;

  private double speed;
  private double ejectSpeed;
  private double intakeSpeed;

  public CargoIntake() {
    // initialize things here
    rightClaw = new TalonSRX(ElectricalLayout.MOTOR_CARGO_RIGHT_CLAW);
    leftClaw = new TalonSRX(ElectricalLayout.MOTOR_CARGO_LEFT_CLAW);

    rightSensor = new AnalogInput(ElectricalLayout.SENSOR_RIGHT_CLAW_INTAKE);
    // leftSensor = new AnalogInput(ElectricalLayout.SENSOR_LEFT_CLAW_INTAKE);
  }

  public boolean hasCargo() {
    return (rightSensor.getVoltage() < 1.0);
  }

  public void intake() {
    mode = Mode_Type.INTAKING;
  }

  public void hold() {
    mode = Mode_Type.HOLDING;
  }

  public void eject() {
    ejectTime = Timer.getFPGATimestamp();
    mode = Mode_Type.EJECTING;
  }

  public void disabled() {
    mode = Mode_Type.DISABLED;
  }

  public void setEjectSpeed(double eject) {
    ejectSpeed = eject;
  }

  public void setIntakeSpeed(double intake) {
    intakeSpeed = intake;
  }

  public void update() {
    switch (mode) {

    case INTAKING:
      speed = intakeSpeed;
      if (hasCargo()) {
        mode = Mode_Type.HOLDING;
      }
    break;

    case HOLDING:
      speed = 0.1;

      if (!hasCargo()) {
        mode = Mode_Type.INTAKING;
      }
    break;

    case EJECTING:
      speed = ejectSpeed;

      if (Timer.getFPGATimestamp() - ejectTime > 2.0) {
        mode = Mode_Type.INTAKING;
      }
    break;

    case DISABLED:
      speed = 0;
    break;
    
    }

    leftClaw.set(ControlMode.PercentOutput, -speed);
    rightClaw.set(ControlMode.PercentOutput, speed);
  }

  @Override
  public void initDefaultCommand() {

  }
}

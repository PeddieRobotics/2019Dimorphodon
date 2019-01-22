
package frc.robot;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Subsystem;

public class CargoIntake extends Subsystem {

  private static enum ModeType {
    INTAKING, EJECTING, HOLDING, DISABLED
  };

  private ModeType mode = ModeType.DISABLED;

  private CANSparkMax wrist;
  private CANPIDController wpid;
  private CANEncoder wEncoder;

  public boolean isDown;
  private double ejectSpeed;
  private double speed;

  public void initDefaultCommand() {
    wrist = new CANSparkMax(1, MotorType.kBrushless);
    wpid = wrist.getPIDController();
    wEncoder = wrist.getEncoder();

    wpid.setP(0.0);
    wpid.setI(0.0);
    wpid.setD(0.0);
    wpid.setFF(0.0);
    wpid.setOutputRange(-1.0, 1, 0);

    wrist.setSmartCurrentLimit(50);
  }

  public void intake() {
    mode = ModeType.INTAKING; // intakes
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
   * will use if we have a sensor public boolean hasCargo() { }
   */
  public void update() {

    switch (mode) {

    case INTAKING:

      speed = 1.0;

      /*
       * if(hasCargo()) { mode = ModeType.HOLDING; }
       */

      break;

    case HOLDING:

      speed = 0.001;
      // DriverStation.reportError("holding", false);

      break;

    case EJECTING:
      speed = -ejectSpeed;
      break;

    case DISABLED:
      speed = 0;
      break;
    }

    wpid.setReference(speed, ControlType.kVelocity);

  }

}
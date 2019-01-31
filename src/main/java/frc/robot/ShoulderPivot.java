package frc.robot;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANDigitalInput;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj.command.Subsystem;

public class ShoulderPivot extends Subsystem{
    private CANSparkMax shoulderMotor;
    private CANPIDController spid;
    private CANEncoder sEncoder;
    private CANDigitalInput forwardLimitSwitch, reverseLimitSwitch;

    private Solenoid brakeSolenoid;
    private DigitalInput brakeSensor, limitSwitch;

    boolean brakeOn = false;

    private enum Mode_Type {MOVING, BRAKING, DISENGAGING, DISABLED, HOMING};
    private Mode_Type mode = Mode_Type.DISABLED;

    private double targetPos;
    private double setDistance;
    private double brakeTimestamp;
    private double conversion; //need to figure out this number
    private double shoulderSpeed;

    public void initDefaultCommand() {
        limitSwitch = new DigitalInput(8);
        shoulderMotor = new CANSparkMax(ElectricalLayout.MOTOR_CARGO_SHOULDER, MotorType.kBrushless);
        spid = shoulderMotor.getPIDController();
        sEncoder = shoulderMotor.getEncoder();
        forwardLimitSwitch = shoulderMotor.getForwardLimitSwitch(LimitSwitchPolarity.kNormallyOpen);
        reverseLimitSwitch = shoulderMotor.getReverseLimitSwitch(LimitSwitchPolarity.kNormallyClosed);

        brakeSensor = new DigitalInput(1);
        brakeSolenoid = new Solenoid(0);

        //initialize PIDs
        spid.setP(0.0);
        spid.setI(0.0);
        spid.setD(0.0);
        spid.setFF(0.0);
        spid.setOutputRange(-1.0, 1.0);

        shoulderMotor.setSmartCurrentLimit(50);
    }

    public void home(){
        mode = Mode_Type.HOMING;
    }
    public double getPosition() {
        return sEncoder.getPosition()*conversion; //in rotations
    }

    public void setTargetPosition(double targetPosition) {
        targetPos = targetPosition/conversion;
        if(targetPos < 0.0) {
            targetPos = 0.0;
        }
        brakeTimestamp = Timer.getFPGATimestamp();
        mode = Mode_Type.DISENGAGING;
    }

    public void kPosition(double position) {
        setDistance = position;
        mode = Mode_Type.MOVING;
    }

    public void brake() {
		mode = Mode_Type.BRAKING;
	}
	
	public double getSpeed() {
		return sEncoder.getVelocity();
	}
    
    public boolean atTarget() {
        return ((Math.abs(sEncoder.getPosition()-targetPos) < 200.0)) 
        && (Math.abs(sEncoder.getVelocity()) < 1.0);
    }

    public double getState() {
		return mode.ordinal();
    }
    
    public void disable() {
		mode = Mode_Type.DISABLED;
    }
    
    public void update() {
        if(forwardLimitSwitch.isLimitSwitchEnabled()) {
            spid.setOutputRange(0.0, 0.0);
        } else {
            spid.setOutputRange(0.0, 1.0);
        }
        if(reverseLimitSwitch.isLimitSwitchEnabled()) {
            spid.setOutputRange(0.0, 0.0);
        } else {
            spid.setOutputRange(-1.0, 0.0);
        }
        
        switch(mode) {
            case HOMING:
            if (limitSwitch.get()) {
                shoulderSpeed = 0.1;
           //     shoulderMotor.set(0.1);
              }
              else { 
                  shoulderSpeed = 0;
              //  shoulderMotor.set(0);
              }
            break;
            case DISENGAGING:
                brakeOn = false;
                if (brakeSensor.get()) {
                    mode = Mode_Type.MOVING;
                }
              /*  if(Timer.getFPGATimestamp() - brakeTimestamp > 0.1) {
                    mode = Mode_Type.MOVING;
                }
                break;
            */
            case MOVING:
                brakeOn = false;
                spid.setReference(targetPos, ControlType.kPosition);

                if(atTarget()) {
                    mode = Mode_Type.BRAKING;
                }
                break;
            
            case BRAKING:
                brakeOn = true;
                spid.setReference(0.0, ControlType.kVelocity);

                break;
            
            case DISABLED:
                brakeOn = false;
                spid.setReference(0.0, ControlType.kVelocity);
                break;
        }
        brakeSolenoid.set(brakeOn);
        shoulderMotor.set(shoulderSpeed);

    }
}

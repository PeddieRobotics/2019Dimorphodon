package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
public class Climber {
    Solenoid front, back;
    private double startTime1 = 0;
    private double currentTime = 0;
    private double startTime2 = 0;
    private double delay1 = 2;
    private double delay2 = 2; 
    private boolean frontState = false;
    private boolean backState = false;
    //CargoIntake cIntake = new CargoIntake();
    //HatchIntake hIntake = new HatchIntake();
    public Climber(){
        front = new Solenoid(ElectricalLayout.SOLENOID_CLIMB_FRONT);
        back = new Solenoid(ElectricalLayout.SOLENOID_CLIMB_BACK);
    }

    public void fireFront(){
        frontState = true;
    }

    public void fireBack(){
        backState = true;
    }

    public void backUp(){
        backState = false;
    }
    
    public void frontUp(){
        frontState = false;
    }
    
    public void update(){
        //DriverStation.reportError("....." + frontState, false);
        front.set(frontState);
        back.set(backState);
   }
   /**
    * In case of some random error
    */
   
}

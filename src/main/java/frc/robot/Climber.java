package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
public class Climber {
    Solenoid front = new Solenoid(4);
    Solenoid back = new Solenoid(5);
    private double startTime1 = 0;
    private double currentTime = 0;
    private double startTime2 = 0;
    private double delay1 = 2;
    private double delay2 = 2; 
    private boolean startedOne = false;
    private boolean startedTwo = false;
    //CargoIntake cIntake = new CargoIntake();
    //HatchIntake hIntake = new HatchIntake();
    //Shoulder shoulder = new Shoulder();
    public Climber(CargoIntake cIntake, HatchIntake hIntake, Shoulder shoulder){
        //this.cIntake = cIntake;
        //this.hIntake = hIntake;
        //this.shoulder = shoulder;
    }
    public void fireFront(){
        startedOne = true;
        //shoulder.setShoulder(-20);
        //hIntake.pushOut();
        startTime1 = Timer.getFPGATimestamp();
        
    }
    public void fireBack(){
        //shoulder.setShoulder(110);
        //hIntake.pullBack();
        startedTwo = true;
        startTime2 = Timer.getFPGATimestamp();
    }
    public void backUp(){
        back.set(false);
    }
    public void frontUp(){
        front.set(false);
    }
    
    public void update(){
        
        currentTime = Timer.getFPGATimestamp();
        
        if(startedOne && (currentTime - startTime1) > delay1){
            front.set(true);
            startedOne = false;
        }
        //if we have started second and front is fired retract it
        else if(startedTwo && (currentTime - startTime2) > delay2){
            back.set(true);
            startedTwo = false;
        }
   }
   /**
    * In case of some random error
    */
   
}

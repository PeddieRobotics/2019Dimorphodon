package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class Climber {
    Solenoid front = new Solenoid(4);
    Solenoid back = new Solenoid(5);
    private BetterTimer oneDelay = new BetterTimer(2000);
    private BetterTimer twoDelay = new BetterTimer(2000);
    private boolean first = false;
    private boolean second = false;
    private boolean startedOne = false;
    private boolean startedTwo = false;
    CargoIntake cIntake = new CargoIntake();
    HatchIntake hIntake = new HatchIntake();
    Shoulder shoulder = new Shoulder();
    public Climber(CargoIntake cIntake, HatchIntake hIntake, Shoulder shoulder){
        this.cIntake = cIntake;
        this.hIntake = hIntake;
        this.shoulder = shoulder;
    }
    public void fireFront(){
        startedOne = true;
        shoulder.setShoulder(-20);
        hIntake.pushOut();
        oneDelay.start();
    }
    public void fireBack(){
        shoulder.setShoulder(110);
        hIntake.pullBack();
        startedTwo = true;
        twoDelay.start();
    }
    public void backDown(){
        back.set(false);
    }
    public void frontDown(){
        front.set(false);
    }
    public void update(){
        if(startedOne){

        }
        if(oneDelay.isFinished()&&startedOne){
            front.set(true);
            
            startedOne = false;
        }
        if(twoDelay.isFinished()&&startedTwo){
            back.set(true);
            startedTwo = false;
        }
        //if we have started second and front is fired retract it
        else if(startedTwo&&front.get()){
            frontDown();
        }
   }
   public boolean oneFinished(){
    return oneDelay.isFinished();
   }
   public boolean twoFinished(){
    return twoDelay.isFinished();
   }
   public boolean first(){
       return first;
   }
   public boolean second(){
       return second;
   }
   /**
    * In case of some random error
    */
   public void reset(){
       first = false;
       second = false;
   }
}

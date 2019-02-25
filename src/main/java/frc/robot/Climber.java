package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class Climber {
    Solenoid front = new Solenoid(4);
    Solenoid back = new Solenoid(5);
    public Climber(){

    }
    public void fireFront(){
        front.set(true);
    }
    public void fireBack(){
        front.set(true);
    }
    public void backDown(){
        back.set(false);
    }
    public void frontDown(){
        front.set(false);
    }

}
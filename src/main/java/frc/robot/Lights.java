package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class Lights{
    Timer timer = new Timer();
    boolean state= false;
    boolean outsideState = false;
    Solenoid light;
    BetterTimer on = new BetterTimer(200);
    public Lights(int port){
        light = new Solenoid(port);
    }
    public void update(boolean state1, boolean state2){
        if(on.isFinished()){
            on.start();
            state = !state;
        }
        if(state1){//if outside state always true
            light.set(state);
        }
        else{
            light.set(state2);
        }
    
    }
    public boolean state(){
        return this.state;
    }
    public void set(boolean set){
        light.set(set);
    }
}
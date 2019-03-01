package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class Lights{
    Timer timer = new Timer();
    boolean state= false;
    boolean outsideState = false;
    Solenoid light;
    double switchTime = 0.5;
    double startTime = 0;
    public Lights(int port){
        light = new Solenoid(port);
    }
    public void update(boolean state1){
        light.set(state1);
    }
    public boolean state(){
        return this.state;
    }
    public void set(boolean set){
        light.set(set);
    }
}
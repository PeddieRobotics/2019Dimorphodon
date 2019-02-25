package frc.robot;
import edu.wpi.first.wpilibj.Timer;
public class BetterTimer{
    double savedTime; // When Timer started
    double totalTime; // How long Timer should last
    int displayDelay = 5;//how long we want to wait before updating our display string
    int currentDelay = 0;//start our delay at zero 
    double delayDisplay = 0;//a double that displays our delay(pay attn. to displayDelay and delayDisplay)
    Timer time;
    BetterTimer(double time) {//instantiate with a time we want it to last in Milliseconds
      totalTime = time;
    }
    // Starting the timer
    void start() {
      savedTime = System.currentTimeMillis();
    }
    /**
    * Checks how much time has passed since our start time 
    * If the passed time is greater than the totalTime(time it should tick for)
    *Then the timer has finished 
    */
    boolean isFinished() {
    // Check out much time has passed
      double passedTime = System.currentTimeMillis() - savedTime;
      if (passedTime >totalTime) {
        return true;
      }else {
        return false;
      }
    }
   } 
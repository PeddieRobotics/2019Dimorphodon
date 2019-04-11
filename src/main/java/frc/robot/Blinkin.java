package frc.robot;

import edu.wpi.first.wpilibj.Spark;

public class Blinkin {

  HatchIntake hatchIntake;
  CargoIntake cargoIntake;
	
  Spark blinkin;

	public Blinkin() {
//    blinkin = new Spark();

//    hatchIntake = new HatchIntake();
//    cargoIntake = new CargoIntake();
  }
  
  /**
   *
   * STROBE BLUE:-0.08
   *      strobe is good, it blinks
   * FIRE, LARGE: -0.57
   *      flashes rainbow colors. Didn't set the robot on fire as hoped,
   *      but functioned objectively well
   *      "Rainbow with Glitter" does the exact same thing, but with a less
   *       exciting name
   * BPM, LAVA PALETTE: -0.63
   *      Make sure no one has epilepsy before turning on tho???
   * 
   * 
   * 
   * BAD ONES:
   *  CHASE - does absolutely nothing, besides un-saturating the color
   *  SPARKLE - no discernable effect
   *  LARSON SCANNER - just stupid
   * 
  */

  public void strobeBlue() {
    blinkin.set(-0.05);
  }

  public void strobeWhite() {
    blinkin.set(-0.05);
  }

  public void fireLarge() { 
    blinkin.set(-0.57);
  }

  public void seizure() {
    blinkin.set(-0.63);
  }
  
  public void blueSeizure() {
    blinkin.set(-0.65);
  }

  public void calmRainbow() {
    blinkin.set(-0.55);
  }

  public void solidBlue() {
    blinkin.set(0.87);
  }

  public void solidWhite() {
    blinkin.set(0.93);
  }

}
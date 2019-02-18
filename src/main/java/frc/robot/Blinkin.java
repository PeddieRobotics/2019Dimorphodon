package frc.robot;

import edu.wpi.first.wpilibj.Spark;

public class Blinkin {

  HatchIntake hatchIntake;
  CargoIntake cargoIntake;
	
  Spark blinkinHatch;
  Spark blinkinCargo;

	public Blinkin() {
    blinkinHatch = new Spark(0);
    blinkinCargo = new Spark(1);

    hatchIntake = new HatchIntake();
    cargoIntake = new CargoIntake();
  }
  
  /**
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
    blinkinHatch.set(-0.05);
    blinkinCargo.set(-0.05);
  }

  public void fireLarge() { 
    blinkinHatch.set(-0.57);
    blinkinCargo.set(-0.57);
  }

  public void seizure() {
    blinkinHatch.set(-0.63);
    blinkinCargo.set(-0.63);
  }
  
  public void blueSeizure() {
    blinkinHatch.set(-0.65);
    blinkinCargo.set(-0.65);
  }

  public void calmRainbow() {
    blinkinHatch.set(-0.55);
    blinkinCargo.set(-0.55);
  }
		
	public void hatchFront() {
    if( hatchIntake.hasHatch() == true ) {
      blinkinHatch.set(-0.55);
    } else {
      blinkinHatch.set(0.67);
    }
    blinkinCargo.set(0.93);
  }
  
  public void cargoFront() {  
    if( cargoIntake.hasCargo() == true ) {
      blinkinCargo.set(-0.57);
    } else {
      blinkinCargo.set(0.61);
    }
    blinkinHatch.set(0.93);
  }
}
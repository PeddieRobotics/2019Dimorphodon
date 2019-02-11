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
   * Hatch front, has hatch: "Rainbow twinkles"
   * Hatch front, does not have hatch: Solid gold
   * Cargo front, has cargo: "Fire, Large"           <--  listen i really just want to know what this does im curious
   * Cargo front, does not have cargo: Solid red
   * 
   * Back of the robot will always be solid white.
  */
		
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
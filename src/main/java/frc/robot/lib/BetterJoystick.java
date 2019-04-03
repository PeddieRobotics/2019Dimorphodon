package frc.robot.lib;

import edu.wpi.first.wpilibj.Joystick;

public class BetterJoystick extends Joystick {

	private boolean[] lastValRising = new boolean[16];
	private boolean[] lastValFalling = new boolean[16];
	
	
	public BetterJoystick(int port) {
		super(port);
	}
	
	public boolean getRisingEdge(int button) {
		boolean cur = this.getRawButton(button);
		boolean ret = !lastValRising[button] && cur;
		lastValRising[button] = cur;
		return ret;
    }
		public boolean triggerPressed(){
			return getRawButton(9);
		}
	public boolean getFallingEdge(int button) {
		boolean cur = this.getRawButton(button);
		boolean ret = lastValFalling[button] && !cur;
		lastValFalling[button] = cur;
		return ret;
	}

}

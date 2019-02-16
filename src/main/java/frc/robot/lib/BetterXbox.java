package frc.robot.lib;

import edu.wpi.first.wpilibj.XboxController;

/**
 * Add your docs here.
 */
public class BetterXbox extends XboxController{
    private boolean[] lastValRising = new boolean[16];
    private boolean[] lastValFalling = new boolean[16];
    
    public BetterXbox(int port) {
		super(port);
	}
	
	public boolean getRisingEdge(int button) {
		boolean cur = this.getRawButton(button);
		boolean ret = !lastValRising[button] && cur;
		lastValRising[button] = cur;
		return ret;
    }
    
	public boolean getFallingEdge(int button) {
		boolean cur = this.getRawButton(button);
		boolean ret = lastValFalling[button] && !cur;
		lastValFalling[button] = cur;
		return ret;
	}
}

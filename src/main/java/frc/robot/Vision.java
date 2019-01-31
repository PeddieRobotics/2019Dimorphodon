package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.CameraServer;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Vision extends Subsystem {
  UsbCamera camera1;
  UsbCamera camera2;

  public Vision() {
    // There are unresolved exposure problems with camera 1.
    camera1 = CameraServer.getInstance().startAutomaticCapture(0);
    camera1.setResolution(640, 360);
    camera1.setExposureManual(0);
    camera1.setBrightness(50);

    camera2 = CameraServer.getInstance().startAutomaticCapture(1);
    camera2.setResolution(640, 360);
    camera2.setExposureAuto();
    // camera2.setExposureManual(0);
  }

  public void initDefaultCommand() {

  }
}

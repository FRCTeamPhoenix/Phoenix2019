package frc.util;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;


public class Vision {

    public static double getVerticalDistance() {
        NetworkTable nt = NetworkTableInstance.getDefault().getTable("Vision");
        return nt.getEntry("DistanceCenter").getDouble(999);
    }

    public static double getHorizontalDistance() {
        NetworkTable nt = NetworkTableInstance.getDefault().getTable("Vision");
        double horizontalDistance = nt.getEntry("HorizontalDistance").getDouble(999);
        nt.getEntry("HorizontalDistance").setDouble(999);
        return horizontalDistance;
    }

    public static double getAngle() {
        NetworkTable nt = NetworkTableInstance.getDefault().getTable("Vision");
        return nt.getEntry("Angle").getDouble(999);
    }
}
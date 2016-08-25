package cg.rayTracingViewer;

import java.util.EventObject;
import rayTracing.math.Point3d;
import rayTracing.math.Vector3d;

/**
 * @author Alexandre Vieira
 */
public class ResetEvent extends EventObject {

    protected CameraInfo cameraInfo;
    protected Resolution resolution;
    protected Vector3d cameraDirection;
    protected Vector3d cameraUp;
    protected Point3d cameraLocation;
    protected double fov;
    protected double distance;

    public ResetEvent(Manager source) {
        super(source);
    }

    public CameraInfo getCameraInfo() {
        return cameraInfo;
    }

    public Vector3d getCameraDirection() {
        return cameraDirection;
    }

    public Point3d getCameraLocation() {
        return cameraLocation;
    }

    public Vector3d getCameraUp() {
        return cameraUp;
    }

    public double getDistance() {
        return distance;
    }

    public double getFOV() {
        return fov;
    }

    public Resolution getResolution() {
        return resolution;
    }
}

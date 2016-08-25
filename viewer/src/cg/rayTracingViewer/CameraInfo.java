package cg.rayTracingViewer;

import rayTracing.math.Point3d;

/**
 * @author Alexandre Vieira
 */
public class CameraInfo {

    protected Point3d location;
    protected Point3d upperLeft;
    protected Point3d upperRight;
    protected Point3d lowerRight;
    protected Point3d lowerLeft;

    public CameraInfo() {
        this.location = new Point3d();
        this.upperLeft = new Point3d();
        this.upperRight = new Point3d();
        this.lowerRight = new Point3d();
        this.lowerLeft = new Point3d();
    }

    public Point3d getLocation() {
        return location;
    }

    public Point3d getLowerLeft() {
        return lowerLeft;
    }

    public Point3d getLowerRight() {
        return lowerRight;
    }

    public Point3d getUpperLeft() {
        return upperLeft;
    }

    public Point3d getUpperRight() {
        return upperRight;
    }
}

package cg.rayTracingViewer.gui;

import cg.rayTracingViewer.CameraInfo;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import rayTracing.math.Point3d;

/**
 * @author Alexandre Vieira
 */
public class CameraShape {

    private CameraInfo cameraInfo;
    private Shape screenShapeAsXY;
    private Shape screenShapeAsXZ;
    private Shape screenShapeAsZY;
    private Shape guidesShapeAsXY;
    private Shape guidesShapeAsXZ;
    private Shape guidesShapeAsZY;

    public CameraShape(CameraInfo info) {
        this.cameraInfo = info;

        this.screenShapeAsXY = createScreenShapeAsXY();
        this.screenShapeAsXZ = createScreenShapeAsXZ();
        this.screenShapeAsZY = createScreenShapeAsZY();

        this.guidesShapeAsXY = createGuidesShapeAsXY();
        this.guidesShapeAsXZ = createGuidesShapeAsXZ();
        this.guidesShapeAsZY = createGuidesShapeAsZY();
    }

    public void setCameraInfo(CameraInfo info) {
        this.cameraInfo = info;

        this.screenShapeAsXY = createScreenShapeAsXY();
        this.screenShapeAsXZ = createScreenShapeAsXZ();
        this.screenShapeAsZY = createScreenShapeAsZY();

        this.guidesShapeAsXY = createGuidesShapeAsXY();
        this.guidesShapeAsXZ = createGuidesShapeAsXZ();
        this.guidesShapeAsZY = createGuidesShapeAsZY();
    }

    public CameraInfo getCameraInfo() {
        return cameraInfo;
    }

    public Shape getScreenShapeAsXY() {
        return screenShapeAsXY;
    }

    public Shape getScreenShapeAsXZ() {
        return screenShapeAsXZ;
    }

    public Shape getScreenShapeAsZY() {
        return screenShapeAsZY;
    }

    public Shape getGuidesShapeAsXY() {
        return guidesShapeAsXY;
    }

    public Shape getGuidesShapeAsXZ() {
        return guidesShapeAsXZ;
    }

    public Shape getGuidesShapeAsZY() {
        return guidesShapeAsZY;
    }

    private Shape createScreenShapeAsXY() {
        GeneralPath path = new GeneralPath();

        Point3d location = cameraInfo.getLocation();
        Point3d upperLeft = cameraInfo.getUpperLeft();
        Point3d upperRight = cameraInfo.getUpperRight();
        Point3d lowerRight = cameraInfo.getLowerRight();
        Point3d lowerLeft = cameraInfo.getLowerLeft();

        path.moveTo(upperLeft.x, upperLeft.y);
        path.lineTo(upperRight.x, upperRight.y);
        path.lineTo(lowerRight.x, lowerRight.y);
        path.lineTo(lowerLeft.x, lowerLeft.y);
        path.closePath();

        return path;
    }

    private Shape createScreenShapeAsXZ() {
        GeneralPath path = new GeneralPath();

        Point3d location = cameraInfo.getLocation();
        Point3d upperLeft = cameraInfo.getUpperLeft();
        Point3d upperRight = cameraInfo.getUpperRight();
        Point3d lowerRight = cameraInfo.getLowerRight();
        Point3d lowerLeft = cameraInfo.getLowerLeft();

        path.moveTo(upperLeft.x, upperLeft.z);
        path.lineTo(upperRight.x, upperRight.z);
        path.lineTo(lowerRight.x, lowerRight.z);
        path.lineTo(lowerLeft.x, lowerLeft.z);
        path.closePath();

        return path;
    }

    private Shape createScreenShapeAsZY() {
        GeneralPath path = new GeneralPath();

        Point3d location = cameraInfo.getLocation();
        Point3d upperLeft = cameraInfo.getUpperLeft();
        Point3d upperRight = cameraInfo.getUpperRight();
        Point3d lowerRight = cameraInfo.getLowerRight();
        Point3d lowerLeft = cameraInfo.getLowerLeft();

        path.moveTo(upperLeft.z, upperLeft.y);
        path.lineTo(upperRight.z, upperRight.y);
        path.lineTo(lowerRight.z, lowerRight.y);
        path.lineTo(lowerLeft.z, lowerLeft.y);
        path.closePath();

        return path;
    }

    private Shape createGuidesShapeAsXY() {
        GeneralPath path = new GeneralPath();

        Point3d location = cameraInfo.getLocation();
        Point3d upperLeft = cameraInfo.getUpperLeft();
        Point3d upperRight = cameraInfo.getUpperRight();
        Point3d lowerRight = cameraInfo.getLowerRight();
        Point3d lowerLeft = cameraInfo.getLowerLeft();

        path.moveTo(location.x, location.y);
        path.lineTo(upperLeft.x, upperLeft.y);

        path.moveTo(location.x, location.y);
        path.lineTo(upperRight.x, upperRight.y);

        path.moveTo(location.x, location.y);
        path.lineTo(lowerRight.x, lowerRight.y);

        path.moveTo(location.x, location.y);
        path.lineTo(lowerLeft.x, lowerLeft.y);

        return path;
    }

    private Shape createGuidesShapeAsXZ() {
        GeneralPath path = new GeneralPath();

        Point3d location = cameraInfo.getLocation();
        Point3d upperLeft = cameraInfo.getUpperLeft();
        Point3d upperRight = cameraInfo.getUpperRight();
        Point3d lowerRight = cameraInfo.getLowerRight();
        Point3d lowerLeft = cameraInfo.getLowerLeft();

        path.moveTo(location.x, location.z);
        path.lineTo(upperLeft.x, upperLeft.z);

        path.moveTo(location.x, location.z);
        path.lineTo(upperRight.x, upperRight.z);

        path.moveTo(location.x, location.z);
        path.lineTo(lowerRight.x, lowerRight.z);

        path.moveTo(location.x, location.z);
        path.lineTo(lowerLeft.x, lowerLeft.z);

        return path;
    }

    private Shape createGuidesShapeAsZY() {
        GeneralPath path = new GeneralPath();

        Point3d location = cameraInfo.getLocation();
        Point3d upperLeft = cameraInfo.getUpperLeft();
        Point3d upperRight = cameraInfo.getUpperRight();
        Point3d lowerRight = cameraInfo.getLowerRight();
        Point3d lowerLeft = cameraInfo.getLowerLeft();

        path.moveTo(location.z, location.y);
        path.lineTo(upperLeft.z, upperLeft.y);

        path.moveTo(location.z, location.y);
        path.lineTo(upperRight.z, upperRight.y);

        path.moveTo(location.z, location.y);
        path.lineTo(lowerRight.z, lowerRight.y);

        path.moveTo(location.z, location.y);
        path.lineTo(lowerLeft.z, lowerLeft.y);

        return path;
    }
}

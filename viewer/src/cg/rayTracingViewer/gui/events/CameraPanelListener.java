package cg.rayTracingViewer.gui.events;

import cg.rayTracingViewer.gui.*;
import cg.rayTracingViewer.Resolution;
import rayTracing.math.Point3d;
import rayTracing.math.Vector3d;

/**
 * @author Alexansdre Vieira
 */
public interface CameraPanelListener {

    public void onAsPreview(Vector3d direction, Vector3d up, Point3d location);

    void onCameraFOVChanged(CameraPanel source, double fov);

    void onCameraDistanceChanged(CameraPanel source, double distance);

    void onCameraResolutionChanged(CameraPanel source, Resolution resolution);

    void onCameraDirectionChanged(CameraPanel source, double x, double y, double z);

    void onCameraUpChanged(CameraPanel source, double x, double y, double z);

    void onCameraLocationChanged(CameraPanel source, double x, double y, double z);
}

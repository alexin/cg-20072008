package cg.rayTracingViewer;

import rayTracing.TracingResult;
import rayTracing.scene.Light;
import rayTracing.scene.Primitive;

/**
 * @author Alexansdre Vieira
 */
public interface ManagerListener {

    void onTracingStarted();

    void onTracingFinished(TracingResult result);

    void onTracingStopped();
    
    void onFrameUpdate(float status);

    void onReset(ResetEvent re);

    void onPrimitiveAdded(Primitive primitive);
    
    void onLightAdded(Light light);

    void onPrimitiveRemoved(Object id);
    
    void onLightRemoved(Object id);

    void onPrimitiveChanged(Primitive primitive);
    
    void onLightChanged(Light light);

    void onCameraInfoChanged(CameraInfo info);
}

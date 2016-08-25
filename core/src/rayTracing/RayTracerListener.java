package rayTracing;

/**
 * @author Alexansdre Vieira
 */
public interface RayTracerListener {

    void onTracingStarted();

    void onTracingFinished(TracingResult result);

    void onTracingStopped();
    
    void onUpdate(UpdateEvent ue);
    
    void onRaySpawned(Ray ray, RayType type);
}

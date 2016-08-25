package cg.rayTracingViewer.gui.events;

import cg.rayTracingViewer.LightType;
import cg.rayTracingViewer.gui.*;
import cg.rayTracingViewer.PrimitiveType;
import java.util.EventListener;
import rayTracing.scene.Light;
import rayTracing.scene.Primitive;

/**
 * @author Alexansdre Vieira
 */
public interface ScenePanelListener extends EventListener {

    boolean onPrimitiveAdded(ScenePanel source, Primitive primitive);

    boolean onPrimitiveAdded(ScenePanel source, Object id, PrimitiveType primitiveType);

    boolean onLightAdded(ScenePanel source, Light light);

    boolean onLightAdded(ScenePanel source, Object id, LightType lightType);

    void onPrimitiveRemoved(ScenePanel source, Object id);

    void onLightRemoved(ScenePanel source, Object id);

    void onPrimitiveSelected(ScenePanel source, Object id);

    void onLightSelected(ScenePanel source, Object id);

    Primitive onPrimitiveProperties(ScenePanel source, Object id);

    Light onLightProperties(ScenePanel source, Object id);

    void onPrimitivePropertiesUpdated(ScenePanel source, Object id);
    
    void onLightPropertiesUpdated(ScenePanel source, Object id);
}

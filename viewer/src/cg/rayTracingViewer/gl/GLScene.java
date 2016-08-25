package cg.rayTracingViewer.gl;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Alexandre Vieira
 */
public class GLScene {

    private ConcurrentHashMap<String, GLObject> primitives;
    private ConcurrentHashMap<String, GLObject> lights;
    private GLCamera camera;


    public GLScene() {
        this.primitives = new ConcurrentHashMap<String, GLObject>(16, 0.75f, 2);
        this.lights = new ConcurrentHashMap<String, GLObject>(16, 0.75f, 2);
        this.camera = new GLCamera("camera");

    }

    public ConcurrentHashMap<String, GLObject> getLights() {
        return lights;
    }

    public ConcurrentHashMap<String, GLObject> getPrimitives() {
        return primitives;
    }

    public GLCamera getCamera() {
        return camera;
    }


    public void addPrimitive(GLObject primitive) {
        primitives.put(primitive.getId(), primitive);
    }

    public void addLight(GLObject light) {
        lights.put(light.getId(), light);
    }

    public GLObject getPrimitive(String id) {
        return primitives.get(id);
    }

    public GLObject getLight(String id) {
        return lights.get(id);
    }

    public void removePrimitive(String id) {
        primitives.remove(id);
    }

    public void removeLight(String id) {
        lights.remove(id);
    }

    public void update(long delta) {
        GLObject object;
        Set<String> primitivesKeys = primitives.keySet();
        Set<String> lightsKeys = lights.keySet();

        synchronized (primitives) {
            Iterator<String> it = primitivesKeys.iterator();

            while (it.hasNext()) {
                object = primitives.get(it.next());
                validateSceneObject(object);
                object.update(delta);
            }

            it = lightsKeys.iterator();
            while (it.hasNext()) {
                object = lights.get(it.next());
                validateSceneObject(object);
                object.update(delta);
            }
        }

        synchronized (camera) {
            validateSceneObject(camera);
            camera.update(delta);
        }

    }

    public void render() {
        GLObject object;
        Set<String> primitivesKeys = primitives.keySet();
        Set<String> lightsKeys = lights.keySet();

        synchronized (primitives) {
            Iterator<String> it = primitivesKeys.iterator();

            while (it.hasNext()) {
                object = primitives.get(it.next());
                validateSceneObject(object);
                object.render();
            }

            it = lightsKeys.iterator();
            while (it.hasNext()) {
                object = lights.get(it.next());
                validateSceneObject(object);
                object.render();
            }
        }
        synchronized (camera) {
            validateSceneObject(camera);
            camera.render();
        }

    }

    private void validateSceneObject(GLObject object) {
        if (!object.isValid()) {
            object.validate();
        }
    }
}

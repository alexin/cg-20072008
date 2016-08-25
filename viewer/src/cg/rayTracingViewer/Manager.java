package cg.rayTracingViewer;

import cg.rayTracingViewer.gui.CameraPanel;
import cg.rayTracingViewer.gui.ScenePanel;
import cg.rayTracingViewer.gui.events.CameraPanelListener;
import cg.rayTracingViewer.gui.events.ControlsPanelListener;
import cg.rayTracingViewer.gui.events.ScenePanelListener;
import cg.rayTracingViewer.gui.events.TransformPanelListener;
import java.util.ArrayList;
import rayTracing.Camera;
import rayTracing.Ray;
import rayTracing.RayTracer;
import rayTracing.RayTracerListener;
import rayTracing.RayType;
import rayTracing.Settings;
import rayTracing.TracingResult;
import rayTracing.UpdateEvent;
import rayTracing.math.Matrix;
import rayTracing.math.Point3d;
import rayTracing.math.Vector3d;
import rayTracing.scene.Light;
import rayTracing.scene.PointLight;
import rayTracing.scene.Primitive;
import rayTracing.scene.Rectangle;
import rayTracing.scene.RectangularPrism;
import rayTracing.scene.Scene;
import rayTracing.scene.Sphere;
import rayTracing.scene.Triangle;

public class Manager implements CameraPanelListener, ControlsPanelListener,
        RayTracerListener, ScenePanelListener, TransformPanelListener {

    public static final PrimitiveType[] PRIMITIVE_TYPES;
    public static final LightType[] LIGHT_TYPES;
    public static final Resolution[] RESOLUTIONS;
    public static final int TRANSLATE = 1;
    public static final int ROTATE_X = 2;
    public static final int ROTATE_Y = 3;
    public static final int ROTATE_Z = 4;
    public static final int SCALE = 5;
    private RayTracer tracer;
    private Scene scene;
    private Camera camera;
    private Matrix matrix;
    private ArrayList<ManagerListener> listeners;

    static {
        PRIMITIVE_TYPES = new PrimitiveType[]{
                    new PrimitiveType(Sphere.class, "Sphere"),
                    new PrimitiveType(Rectangle.class, "Rectangle"),
                    new PrimitiveType(Triangle.class, "Triangle"),
                    new PrimitiveType(RectangularPrism.class, "Rectangular Prism")
                };
        LIGHT_TYPES = new LightType[]{
                    new LightType(PointLight.class, "PointLight")
                };

        RESOLUTIONS = new Resolution[]{
                    new Resolution(64, 64),
                    new Resolution(128, 128),
                    new Resolution(256, 256),
                    new Resolution(512, 512),
                    new Resolution(768, 768),
                    new Resolution(800, 600),
                    new Resolution(1500, 1500)
                };
    }

    public Manager() {
        this.matrix = new Matrix();
        this.listeners = new ArrayList<ManagerListener>();
    }

    public void onCameraDirectionChanged(CameraPanel source, double x, double y, double z) {
        camera.setDirection(x, y, z);
        fireOnCameraInfoChanged(createCameraInfo(matrix, camera));
    }

    public void onCameraDistanceChanged(CameraPanel source, double distance) {
        camera.setScreenDistance(distance);
        fireOnCameraInfoChanged(createCameraInfo(matrix, camera));
    }

    public void onCameraFOVChanged(CameraPanel source, double fov) {
        camera.setFOV(fov);
        fireOnCameraInfoChanged(createCameraInfo(matrix, camera));
    }

    public void onCameraLocationChanged(CameraPanel source, double x, double y, double z) {
        camera.setLocation(x, y, z);
        fireOnCameraInfoChanged(createCameraInfo(matrix, camera));
    }

    public void onCameraResolutionChanged(CameraPanel source, Resolution resolution) {
        camera.setResolution(resolution.getWidth(), resolution.getHeight());
        fireOnCameraInfoChanged(createCameraInfo(matrix, camera));
    }

    public void onCameraUpChanged(CameraPanel source, double x, double y, double z) {
        camera.setUp(x, y, z);
        fireOnCameraInfoChanged(createCameraInfo(matrix, camera));
    }

    public void onAsPreview(Vector3d direction, Vector3d up, Point3d location) {
        onCameraDirectionChanged(null, direction.x, direction.y, direction.z);
        onCameraUpChanged(null, up.x, up.y, up.z);
        onCameraLocationChanged(null, location.x, location.y, location.z);
    }

    public void onStop() {
        tracer.stop();
        tracer.removeRayTracerListener(this);
    }

    public void onTrace(Settings settings) {
        tracer = new RayTracer(settings);
        tracer.addRayTracerListener(this);

        camera.configure();

        Thread thread = new Thread(new Runnable() {

            public void run() {
                tracer.trace(scene, camera);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void onTracingFinished(TracingResult result) {
        System.out.printf("Tracing finished.\nRendering Time: %d\n", result.
                getRenderingTime());
        fireOnTracingFinished(result);
    }

    public void onTracingStarted() {
        System.out.println("Tracing started.");
        fireOnTracingStarted();
    }

    public void onTracingStopped() {
        System.out.println("Tracing stopped.");
        fireOnTracingStopped();
    }

    public void onUpdate(UpdateEvent ue) {
        fireOnFrameUpdate((float) ue.getCurrentPixel() / (camera.getScreenWidth() * camera.
                getScreenHeight()));
    }

    public void onRaySpawned(Ray ray, RayType type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean onPrimitiveAdded(ScenePanel source, Primitive primitive) {
        try {
            boolean added = false;

            if (primitive != null) {
                added = scene.addPrimitive(primitive);
            } else {
                added = false;
            }

            if (added) {
                fireOnPrimitiveAdded(primitive);
            }

            return added;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean onPrimitiveAdded(ScenePanel source, Object id, PrimitiveType primitiveType) {
        try {
            Primitive primitive = null;
            boolean added = false;

            if (primitiveType.getType().equals(Sphere.class)) {
                primitive = new Sphere(id);
            } else if (primitiveType.getType().equals(Rectangle.class)) {
                primitive = new Rectangle(id, 1.0d, 1.0d);
            } else if (primitiveType.getType().equals(Triangle.class)) {
                primitive = new Triangle(id, new Point3d(), new Point3d(1.0d, 0.0d, 0.0d),
                        new Point3d(0.0d, 1.0d, 0.0d));
            } else if (primitiveType.getType().equals(RectangularPrism.class)) {
                primitive = new RectangularPrism(id, 1.0d, 1.0d, 1.0d);
            }

            if (primitive != null) {
                added = scene.addPrimitive(primitive);
            } else {
                added = false;
            }

            if (added) {
                fireOnPrimitiveAdded(primitive);
            }

            return added;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean onLightAdded(ScenePanel source, Light light) {
        try {
            boolean added = false;

            if (light != null) {
                added = scene.addLight(light);
            } else {
                added = false;
            }

            if (added) {
                fireOnLightAdded(light);
            }

            return added;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean onLightAdded(ScenePanel source, Object id, LightType lightType) {
        try {
            Light light = null;
            boolean added = false;

            if (lightType.getType().equals(PointLight.class)) {
                light = new PointLight(id, 0.1d);
            }

            if (light != null) {
                added = scene.addLight(light);
            } else {
                added = false;
            }

            if (added) {
                fireOnLightAdded(light);
            }

            return added;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void onPrimitiveRemoved(ScenePanel source, Object id) {
        removePrimitive(id);
        fireOnPrimitiveRemoved(id);
    }

    public void onLightRemoved(ScenePanel source, Object id) {
        removeLight(id);
        fireOnLightRemoved(id);
    }

    public void onPrimitiveSelected(ScenePanel source, Object id) {
    }

    public void onLightSelected(ScenePanel source, Object id) {
    }

    public Primitive onPrimitiveProperties(ScenePanel source, Object id) {
        return scene.getPrimitive(id);
    }

    public Light onLightProperties(ScenePanel source, Object id) {
        return scene.getLight(id);
    }

    public void onPrimitivePropertiesUpdated(ScenePanel source, Object id) {
        fireOnPrimitiveChanged(scene.getPrimitive(id));
    }

    public void onLightPropertiesUpdated(ScenePanel source, Object id) {
        fireOnLightChanged(scene.getLight(id));
    }

    public void onTransform(Object id, int tag, Object... args) {
        Matrix m = new Matrix();
        if (id != null) {
            Point3d location = scene.getPrimitive(id).getLocation();
            if (tag == TRANSLATE) {
                m.translate((Double) args[0], (Double) args[1], (Double) args[2]);
                scene.getPrimitive(id).transform(m);
            } else if (tag == ROTATE_X) {
                m.translate(-location.x, -location.y, -location.z);
                m.rotateX((Double) args[0]);
                m.translate(location.x, location.y, location.z);
                scene.getPrimitive(id).transform(m);
            } else if (tag == ROTATE_Y) {
                m.translate(-location.x, -location.y, -location.z);
                m.rotateY((Double) args[0]);
                m.translate(location.x, location.y, location.z);
                scene.getPrimitive(id).transform(m);
            } else if (tag == ROTATE_Z) {
                m.translate(-location.x, -location.y, -location.z);
                m.rotateZ((Double) args[0]);
                m.translate(location.x, location.y, location.z);
                scene.getPrimitive(id).transform(m);
            } else if (tag == SCALE) {
                m.scale((Double) args[0], (Double) args[1], (Double) args[2]);
                scene.getPrimitive(id).transform(m);
            }


            fireOnPrimitiveChanged(scene.getPrimitive(id));
        }
    }

    public Scene getScene() {
        return scene;
    }

    public void reset() {
        createScene();
        createCamera();
        fireOnReset();
    }

    public void addManagerListener(ManagerListener listener) {
        listeners.add(listener);
    }

    public void removeManagerListener(ManagerListener listener) {
        listeners.add(listener);
    }

    protected void fireOnReset() {
        ResetEvent re = new ResetEvent(this);

        re.cameraInfo = createCameraInfo(matrix, camera);
        re.fov = camera.getFOV();
        re.distance = camera.getScreenDistance();
        re.resolution = new Resolution(camera.getScreenWidth(), camera.
                getScreenHeight());
        re.cameraDirection = new Vector3d(camera.getUAxis());
        re.cameraUp = new Vector3d(camera.getVAxis());
        re.cameraLocation = new Point3d(camera.getLocation());

        for (ManagerListener listener : listeners) {
            listener.onReset(re);
        }
    }

    protected void fireOnTracingStarted() {
        for (ManagerListener listener : listeners) {
            listener.onTracingStarted();
        }
    }

    protected void fireOnTracingFinished(TracingResult result) {
        for (ManagerListener listener : listeners) {
            listener.onTracingFinished(result);
        }
    }

    protected void fireOnTracingStopped() {
        for (ManagerListener listener : listeners) {
            listener.onTracingStopped();
        }
    }

    protected void fireOnFrameUpdate(float status) {
        for (ManagerListener listener : listeners) {
            listener.onFrameUpdate(status);
        }
    }

    protected void fireOnPrimitiveAdded(Primitive primitive) {
        for (ManagerListener listener : listeners) {
            listener.onPrimitiveAdded(primitive);
        }
    }

    protected void fireOnLightAdded(Light light) {
        for (ManagerListener listener : listeners) {
            listener.onLightAdded(light);
        }
    }

    protected void fireOnPrimitiveRemoved(Object id) {
        for (ManagerListener listener : listeners) {
            listener.onPrimitiveRemoved(id);
        }
    }

    protected void fireOnLightRemoved(Object id) {
        for (ManagerListener listener : listeners) {
            listener.onLightRemoved(id);
        }
    }

    protected void fireOnPrimitiveChanged(Primitive primitive) {
        for (ManagerListener listener : listeners) {
            listener.onPrimitiveChanged(primitive);
        }
    }

    protected void fireOnLightChanged(Light light) {
        for (ManagerListener listener : listeners) {
            listener.onLightChanged(light);
        }
    }

    protected void fireOnCameraInfoChanged(CameraInfo info) {
        for (ManagerListener listener : listeners) {
            listener.onCameraInfoChanged(info);
        }
    }

    protected void removePrimitive(Object id) {
        scene.removePrimitive(id);
    }

    protected void removeLight(Object id) {
        scene.removeLight(id);
    }

    private void createScene() {
        scene = new Scene(1.0d);
    }

    private void createCamera() {
        camera = new Camera(512, 512, 90.0f, 1.0d, new Vector3d(0.0d, 0.0d, 1.0d),
                new Vector3d(0.0d, 1.0d, 0.0d), new Point3d(1.0d, 1.0d, 1.0d));
    }

    protected static void configureMatrix(Matrix matrix, Camera camera) {
        Vector3d vector;

        // X column
        vector = camera.getUAxis();
        matrix.setValueAt(vector.x, 0);
        matrix.setValueAt(vector.y, 1);
        matrix.setValueAt(vector.z, 2);

        // Y column
        vector = camera.getVAxis();
        matrix.setValueAt(vector.x, 4);
        matrix.setValueAt(vector.y, 5);
        matrix.setValueAt(vector.z, 6);

        // Z column
        vector = camera.getNAxis();
        matrix.setValueAt(vector.x, 8);
        matrix.setValueAt(vector.y, 9);
        matrix.setValueAt(vector.z, 10);

        Point3d location = camera.getLocation();
        matrix.setValueAt(location.x, 12);
        matrix.setValueAt(location.y, 13);
        matrix.setValueAt(location.z, 14);
    }

    protected static CameraInfo createCameraInfo(Matrix matrix, Camera camera) {
        CameraInfo info = new CameraInfo();

        camera.configure();
        configureMatrix(matrix, camera);

        info.location = new Point3d(camera.getLocation());
        info.upperLeft = matrix.multiply(camera.createPointForCell(0, 0));
        info.upperRight = matrix.multiply(camera.createPointForCell(0, camera.
                getScreenWidth()));
        info.lowerRight = matrix.multiply(camera.createPointForCell(camera.
                getScreenHeight(), camera.getScreenWidth()));
        info.lowerLeft = matrix.multiply(camera.createPointForCell(camera.
                getScreenHeight(), 0));

//        System.out.println("------------------ Camera Info:");
//        System.out.println(camera);
//        System.out.printf("\nUpper-left = %s\n", info.upperLeft);
//        System.out.printf("Upper-right = %s\n", info.upperRight);
//        System.out.printf("Lower-right = %s\n", info.lowerRight);
//        System.out.printf("Lower-left = %s\n", info.lowerLeft);
//        System.out.printf("\nMatrix = %s\n", matrix);

        return info;
    }

    public void onSave() {
    }
}

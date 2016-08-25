package rayTracing;

import java.awt.Color;
import rayTracing.math.Point3d;
import rayTracing.math.Vector3d;
import rayTracing.scene.IntersectionResult;
import rayTracing.scene.Primitive;
import rayTracing.scene.Scene;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import rayTracing.math.Color3d;
import rayTracing.math.Matrix;
import rayTracing.scene.Light;
import rayTracing.scene.LightPrimitive;

/**
 * @author Alexandre Vieira
 */
public class RayTracer {

    private static final double SHADE_ERROR = 0.000001d;
    /** Settings for controlled ray tracing */
    private Settings settings;
    /** The matrix to change between Coordinates Systems. */
    private Matrix csMatrix;
    /** The scene to process. */
    private Scene scene;
    /** The camera viewing the scene. */
    private Camera camera;
    /** The list of registered listeners. */
    private ArrayList<RayTracerListener> listeners;
    /** Wether the tracing process should stop or not. */
    private boolean trace;

    /**
     * Creates a <code>RayTracer</code>.
     */
    public RayTracer(Settings settings) {
        this.settings = settings;
        this.listeners = new ArrayList<RayTracerListener>();
    }

    /**
     * Processes given the scene with the specified camera.
     * @param scene The scene to process.
     * @param camera the camera viewing the scene.
     */
    public synchronized TracingResult trace(Scene scene, Camera camera) {
        this.scene = scene;
        this.camera = camera;
        setupCSMatrix();

        BufferedImage frame = new BufferedImage(camera.getScreenWidth(),
                camera.getScreenHeight(), BufferedImage.TYPE_INT_ARGB);

        trace = true;
        fireOnTracingStarted();

        long time = System.currentTimeMillis();
        boolean finished = renderFrame(frame, camera);
        time = System.currentTimeMillis() - time;

        if (finished) {
            TracingResult result = new TracingResult(frame, time, TracingResult.SUCCEEDED);
            fireOnTracingFinished(result);
            return result;
        } else {
            fireOnTracingStopped();
            return new TracingResult(null, -1L, TracingResult.FAILED);
        }
    }

    /**
     * Tells this <code>RayTracer</code> to stop tracing.
     */
    public synchronized void stop() {
        trace = false;
    }

    /**
     * Adds the specified <code>RayTracerListener</code> to the list.
     * @param listener The interested listener.
     */
    public void addRayTracerListener(RayTracerListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes the specified <code>RayTracerListener</code> from the list.
     * @param listener The listener to remove.
     */
    public void removeRayTracerListener(RayTracerListener listener) {
        listeners.remove(listener);
    }

    private void fireOnTracingStarted() {
        for (RayTracerListener listener : listeners) {
            listener.onTracingStarted();
        }
    }

    private void fireOnTracingFinished(TracingResult result) {
        for (RayTracerListener listener : listeners) {
            listener.onTracingFinished(result);
        }
    }

    private void fireOnTracingStopped() {
        for (RayTracerListener listener : listeners) {
            listener.onTracingStopped();
        }
    }

    private void fireOnUpdate(UpdateEvent ue) {
        for (RayTracerListener listener : listeners) {
            listener.onUpdate(ue);
        }
    }

    /**
     * Sets up the Coordinate System matrix.
     * The matrix is configured to transform points defined in Camera Coordinate System 
     * into World Coordinate System.
     */
    private void setupCSMatrix() {
        csMatrix = new Matrix();
        Vector3d vector;

        // X column
        vector = camera.getUAxis();
        csMatrix.setValueAt(vector.x, 0);
        csMatrix.setValueAt(vector.y, 1);
        csMatrix.setValueAt(vector.z, 2);

        // Y column
        vector = camera.getVAxis();
        csMatrix.setValueAt(vector.x, 4);
        csMatrix.setValueAt(vector.y, 5);
        csMatrix.setValueAt(vector.z, 6);

        // Z column
        vector = camera.getNAxis();
        csMatrix.setValueAt(vector.x, 8);
        csMatrix.setValueAt(vector.y, 9);
        csMatrix.setValueAt(vector.z, 10);

        Point3d location = camera.getLocation();
        csMatrix.setValueAt(location.x, 12);
        csMatrix.setValueAt(location.y, 13);
        csMatrix.setValueAt(location.z, 14);
    }

    /**
     * Creates and returns a ray from the camera that goes through the 'pixel' at (x, y).
     * @param camera The camera casting the ray.
     * @param x The horizontal coordinate of the 'pixel'.
     * @param y The vertical coordinate of the 'pixel'.
     * @return The ray to cast.
     */
    private Ray castPrimaryRay(Camera camera, int x, int y) {
        Point3d p = csMatrix.multiply(camera.createPointForCell(y, x));

        Vector3d direction = new Vector3d();
        direction.x = p.x - camera.getLocation().x;
        direction.y = p.y - camera.getLocation().y;
        direction.z = p.z - camera.getLocation().z;
        direction.setToNormal();

        Ray ray = new Ray(direction, new Point3d(camera.getLocation()));

        return ray;
    }

    private Color3d trace(Ray ray, int depth) {
        if (depth <= 0) {
            throw new IllegalArgumentException("Tracing depth must be positive.");
        }
        if (depth > settings.getTracingDepth()) {
            return new Color3d(0.0d, 0.0d, 0.0d);
        }

        Primitive primitive = null;
        Primitive temp;
        IntersectionResult result, closestResult = null;

        double minDistance = Double.MAX_VALUE;

        for (Object id : scene.getPrimitives().keySet()) {
            temp = scene.getPrimitive(id);
            result = temp.intersects(ray);

            if (settings.isUseCullFace()) {
                if (result.getWhere() == IntersectionResult.OUTSIDE) {

                    if (result.getDistance() >= 0.0d && result.getDistance() < minDistance) {
                        closestResult = result;
                        minDistance = result.getDistance();
                        primitive = temp;
                    }
                }
            } else {
                if (result.getWhere() != IntersectionResult.MISS) {

                    if (result.getDistance() >= 0.0d && result.getDistance() < minDistance) {
                        closestResult = result;
                        minDistance = result.getDistance();
                        primitive = temp;
                    }
                }
            }
        }

        if (primitive != null) {

            if (primitive instanceof LightPrimitive) {
                return new Color3d(((LightPrimitive) primitive).getLight().getColor());
            }else{
                if(closestResult.getWhere() == IntersectionResult.INSIDE){
                    return new Color3d(0.0d, 0.0d, 0.0d);
                }
            }

            Color3d accumulated = new Color3d(0.0d, 0.0d, 0.0d);
            Point3d pi = ray.getLocation().translate(ray.getDirection().scale(closestResult.getDistance()));
            double shade = 1.0d;
            //accumulated.add(scene.getAmbientColor());

            if (settings.isUseShadows()) {
                shade = getShade(pi, primitive);
            }

            if (shade > 0.0d) {

                if (settings.isUseDiffuse()) {
                    diffuseShading(accumulated, primitive, pi, shade);
                }
                if (settings.isUseSpecular()) {
                    specularShading(accumulated, ray, primitive, pi, shade);
                }
                if (settings.isUseReflection()) {
                    reflection(accumulated, ray, primitive, pi, depth);
                }
                if (settings.isUseRefraction()) {
                    refraction(accumulated, ray, primitive, closestResult, pi, depth);
                }
            }

            return accumulated;
        } else {
            return Scene.DEFAULT_BACKCOLOR;
        }
    }

    /**
     * Returns the shading value at the specified point.
     * @param pi The point of intersection.
     * @return At the moment, 1 if the a light hits the point, 0 otherwise.
     */
    private double getShade(Point3d pi, Primitive primitive) {
        Primitive block;
        Light light;
        Ray shadowRay;
        Vector3d lightVector;
        IntersectionResult result;
        double shade = 1.0d;
        double quote = 1.0d / scene.getLights().size();
        Point3d pi2 = pi.translate(primitive.getNormalAt(pi).scale(SHADE_ERROR));

        for (Object id : scene.getLights().keySet()) {
            light = scene.getLight(id);
            lightVector = light.getLocation().minus(pi2);
            shadowRay = new Ray(lightVector.normalize(), pi2, lightVector.getLength());

            for (Object pid : scene.getPrimitives().keySet()) {
                block = scene.getPrimitive(pid);
                if (!(block instanceof LightPrimitive)) {
                    result = block.intersects(shadowRay);

                    if (result.getWhere() != IntersectionResult.MISS) {
                        shade -= quote;
                        break;
                    }
                }
            }
        }

        return shade;
    }

    /**
     * Returns the color corresponding to the diffuse component.
     * At the moment, the intensity of the light does not decay with distance.
     * @param accumulated The color to add the diffuse component.
     * @param primitive The primitive to apply the diffuse color.
     * @param pi The point of intersection.
     * @param shade The shading value.
     */
    private void diffuseShading(Color3d accumulated, Primitive primitive, Point3d pi, double shade) {
        if (primitive.getMaterial().getDiffuse() > 0.0d) {
            Light light;
            Vector3d lightVector, normal;
            double lightingCoefficent, diffuse;

            normal = primitive.getNormalAt(pi);

            for (Object id : scene.getLights().keySet()) {
                light = scene.getLight(id);
                lightVector = light.getLocation().minus(pi);
                lightVector.setToNormal();
                lightingCoefficent = normal.dot(lightVector);

                if (lightingCoefficent >= 0.0d) {
                    diffuse = lightingCoefficent * primitive.getMaterial().getDiffuse() * shade;
                    accumulated.add(primitive.getMaterial().getColor().multiply(diffuse).multiply(light.getColor()));
                }
            }
        }
    }

    private void specularShading(Color3d accumulated, Ray ray, Primitive primitive, Point3d pi, double shade) {
        Light light;
        Vector3d lightVector, normal, reflected;
        double lightingCoefficent, specular;

        normal = primitive.getNormalAt(pi);

        for (Object id : scene.getLights().keySet()) {
            light = scene.getLight(id);
            lightVector = light.getLocation().minus(pi);
            lightVector.setToNormal();

            if (primitive.getMaterial().getSpecular() > 0.0d) {
                reflected = lightVector.reflect(normal);
                lightingCoefficent = ray.getDirection().dot(reflected);

                if (lightingCoefficent > 0.0d) {
                    specular = Math.pow(lightingCoefficent, 10.0d) * primitive.getMaterial().getSpecular() * shade;
                    accumulated.add(light.getColor().multiply(specular));
                }
            }
        }
    }

    private void reflection(Color3d accumulated, Ray ray, Primitive primitive, Point3d pi, int depth) {
        double reflection = primitive.getMaterial().getReflection();

        if ((reflection > 0.0d) && (depth < settings.getTracingDepth())) {

            Point3d spawnLocation = pi.translate(primitive.getNormalAt(pi).scale(0.00001d));
            Vector3d reflected = ray.getDirection().reflect(primitive.getNormalAt(pi));
            reflected.setToNormal();
            Ray reflectedRay = new Ray(reflected, spawnLocation);

            Color3d reflectedColor = trace(reflectedRay, depth + 1);

            accumulated.add(reflectedColor.multiply(reflection).multiply(primitive.getMaterial().getColor()));
            //accumulated.add(reflectedColor.multiply(reflection));
        }
    }

    private void refraction(Color3d accumulated, Ray ray, Primitive primitive,
            IntersectionResult result, Point3d pi, int depth) {

        double refraction = primitive.getMaterial().getRefraction();

        if ((refraction > 0.0d) && (depth < settings.getTracingDepth())) {

            double refractionIndex = primitive.getMaterial().getRefractionIndex();
            double n = 1.0d / refractionIndex;
            Vector3d normal = primitive.getNormalAt(pi);
            double cosI = -(normal.dot(ray.getDirection()));
            double cosT2 = 1.0d - (n * n * (1.0d - (cosI * cosI)));

            if (cosT2 > 0.0d) {
                Vector3d direction = ray.getDirection().scale(n).plus(normal.scale(n * cosI - Math.sqrt(cosT2)));
                Ray refractedRay = new Ray(direction, pi.translate(primitive.getNormalAt(pi).scale(SHADE_ERROR)));

                Color3d refractedColor = trace(refractedRay, depth + 1);

                accumulated.add(refractedColor);
            }
        }
    }

    /**
     * Renders a single frame of the scene.
     * @param frame The frame to render.
     * @param camera The camera viewing the scene.
     * @return <code>FALSE</code> if the frame rendering is cancelled.
     */
    private boolean renderFrame(BufferedImage frame, Camera camera) {
        Ray ray;
        Color3d color;
        int pixelCount = 0;

        for (int y = 0; y < camera.getScreenHeight(); y++) {
            for (int x = 0; x < camera.getScreenWidth(); x++) {
                pixelCount++;
                if (trace) {
                    ray = castPrimaryRay(camera, x, y);
                    color = trace(ray, 1);
                    color.clamp();
                    frame.setRGB(x, y, new Color((float) color.red, (float) color.green, (float) color.blue).getRGB());
                    fireOnUpdate(new UpdateEvent(pixelCount));
                } else {
                    return false;
                }
            }
        }

        return true;
    }
}

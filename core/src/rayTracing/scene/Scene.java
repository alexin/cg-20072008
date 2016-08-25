package rayTracing.scene;

import java.io.Serializable;
import java.util.HashMap;
import rayTracing.math.Color3d;

/**
 * @author Alexandre Vieira
 */
public class Scene implements Serializable{

    /** The default color of the background. */
    public static final Color3d DEFAULT_BACKCOLOR;
    /** The primitives. */
    private HashMap<Object, Primitive> primitives;
    /** The lights. */
    private HashMap<Object, Light> lights;
    /** The ambient light color. */
    private Color3d ambientColor;
    /** The radius of this <code>Scene</code>. */
    private double radius;

    static {
        DEFAULT_BACKCOLOR = new Color3d(0.25d, 0.25d, 0.25d);
    }

    public Scene(double radius) {
        this.primitives = new HashMap<Object, Primitive>();
        this.lights = new HashMap<Object, Light>();
        this.ambientColor = new Color3d(0.1d, 0.1d, 0.1d);
        this.radius = radius;
    }

    public HashMap<Object, Primitive> getPrimitives() {
        return primitives;
    }

    public HashMap<Object, Light> getLights() {
        return lights;
    }

    /**
     * Returns the color of the ambient light.
     * @return The ambient color.
     */
    public Color3d getAmbientColor() {
        return ambientColor;
    }

    /**
     * Returns the radius of this <code>Scene</code>.
     * @return The radius of the scene.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Returns this <code>Scene</code>'s background color.
     * @return The background color.
     */
    public Color3d getBackColor() {
        return DEFAULT_BACKCOLOR;
    }

    /**
     * Adds the specified primtive to this scene, if it doesn't exists yet.
     * @param primitive The primitive to add.
     * @return TRUE if the primitive was actually added.
     */
    public boolean addPrimitive(Primitive primitive) {
        if (primitive instanceof LightPrimitive) {
           throw new IllegalArgumentException("Can't add a LightPrimitive through Scene.addPrimitive().");
        }
        if (primitives.containsKey(primitive.getId())) {
            return false;
        }

        primitives.put(primitive.getId(), primitive);

        return true;
    }

    /**
     * Adds the specified light to this scene, if it doesn't exists yet.
     * @param primitive The primilighttive to add.
     * @return TRUE if the light was actually added.
     */
    public boolean addLight(Light light) {
        if (lights.containsKey(light.getId()) || primitives.containsKey(light.getPrimitive().getId())) {
            return false;
        }

        lights.put(light.getId(), light);
        primitives.put(light.getPrimitive().getId(), light.getPrimitive());

        return true;
    }

    /**
     * Returns the Primitive identified by the specified ID, or null if there's
     * none. 
     * @param id The ID of the Primitive.
     * @return The Primitive, or null if there's none.
     */
    public Primitive getPrimitive(Object id) {
        return primitives.get(id);
    }

    /**
     * Returns the Light identified by the specified ID, or null if there's
     * none. 
     * @param id The ID of the Light.
     * @return The Light, or null if there's none.
     */
    public Light getLight(Object id) {
        return lights.get(id);
    }

    /**
     * Removes the specified primitive from this scene.
     * @param primitive The primitive to remove.
     */
    public boolean removePrimitive(Primitive primitive) {
        if (primitive instanceof LightPrimitive) {
            throw new IllegalArgumentException("Can't remove a LightPrimitive through Scene.removePrimitive().");
        }
        return (primitives.remove(primitive.getId()) != null) ? true : false;
    }

    /**
     * Removes the specified light from this scene.
     * @param primitive The light to remove.
     */
    public boolean removeLight(Light light) {
        if (lights.remove(light.getId()) != null) {
            primitives.remove(light.getPrimitive().getId());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes the specified primitive from this scene.
     * @param primitive The primitive to remove.
     */
    public boolean removePrimitive(Object id) {
        return (primitives.remove(id) != null) ? true : false;
    }

    /**
     * Removes the specified light from this scene.
     * @param primitive The light to remove.
     */
    public boolean removeLight(Object id) {
        return (lights.remove(id) != null) ? true : false;
    }

    /**
     * Sets the radius of this <code>Scene</code>.
     * @param radius The radius of the scene.
     */
    private void setRadius(double radius) {
        if (radius < 0.0d) {
            throw new IllegalArgumentException("The scene must have a positive radius");
        }
        this.radius = radius;
    }
}

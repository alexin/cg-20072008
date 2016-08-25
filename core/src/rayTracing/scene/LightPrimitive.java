package rayTracing.scene;

import rayTracing.math.Point3d;

/**
 * @author Alexandre Vieira
 */
public abstract class LightPrimitive extends Primitive {
    
    Light light;

    public LightPrimitive(Light owner, Point3d location) {
        super(owner.getId(), location);
        this.light = owner;
    }

    @Override
    public void setMaterial(Material material) {
        throw new UnsupportedOperationException("Can't set Material on LightPrimitive.");
    }

    @Override
    public Material getMaterial() {
        throw new UnsupportedOperationException("Can't get Material on LightPrimitive.");
    }

    public Light getLight() {
        return light;
    }
}

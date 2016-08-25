package rayTracing.scene;

import rayTracing.math.Point3d;

/**
 * @author Alexandre Vieira
 */
public class SphereInfo extends PrimitiveInfo {

    protected float radius;

    public SphereInfo() {
        super();
    }

    @Override
    public void extractInfoTo(Primitive primitive) {
        Sphere sphere = (Sphere) primitive;

        sphere.setLocation(new Point3d(location));
        sphere.setRadius(radius);
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }
}

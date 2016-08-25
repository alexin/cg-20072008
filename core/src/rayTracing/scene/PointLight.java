package rayTracing.scene;

/**
 * @author Alexandre Vieira
 */
public class PointLight extends Light {

    private LightSphere primitive;

    public PointLight(Object id, double radius) {
        super(id);
        primitive = new LightSphere(this, location, radius);
    }

    @Override
    public LightPrimitive getPrimitive() {
        return primitive;
    }
}

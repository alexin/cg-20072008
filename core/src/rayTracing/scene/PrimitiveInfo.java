package rayTracing.scene;

import rayTracing.math.Point3d;

/**
 * @author Alexandre Vieira
 */
public abstract class PrimitiveInfo {

    protected Object id;
    protected Point3d location;
    protected Material material;

    public PrimitiveInfo() {
    }

    public abstract void extractInfoTo(Primitive primitive);

    public void setId(Object id) {
        this.id = id;
    }

    public void setLocation(Point3d location) {
        this.location = location;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Object getId() {
        return id;
    }

    public Point3d getLocation() {
        return location;
    }

    public Material getMaterial() {
        return material;
    }
}

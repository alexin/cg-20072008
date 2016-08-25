package rayTracing.scene;

import java.io.Serializable;
import rayTracing.math.Point3d;
import rayTracing.*;
import rayTracing.math.Matrix;
import rayTracing.math.Vector3d;

/**
 * @author Alexandre Vieira
 */
public abstract class Primitive implements Serializable{

    /** The <code>Material</code>. */
    private Material material;
    /** The location of this <code>Primitive</code>.*/
    private Point3d location;
    /** The ID of this <code>Primitive</code>. */
    private Object id;

    /**
     * Creates a <code>Primitive</code>.
     */
    public Primitive(Object id) {
        this(id, new Point3d());
    }

    /**
     * Creates a <code>Primitive</code> at the specified location.
     */
    public Primitive(Object id, Point3d location) {
        this(id, new Material(), location);
    }

    public Primitive(Object id, Material material, Point3d location) {
        this.material = material;
        this.location = location;
        this.id = id;
    }

    /**
     * Returns the distance between the ray and the intersection point.
     * @param ray The ray to test.
     * @return A positive value if there's an intersection.
     */
    public abstract IntersectionResult intersects(Ray ray);

    /**
     * Returns the normal at the specified point.
     * @param point The point of intersection.
     * @return The normal.
     */
    public abstract Vector3d getNormalAt(Point3d point);

    public abstract void transform(Matrix matrix);

    public abstract PrimitiveInfo createInfo();

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Primitive other = (Primitive) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    /**
     * Sets the material for this primitive.
     * @param material The material to use.
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Sets the location.
     * @param location The location.
     */
    public void setLocation(Point3d location) {
        this.location = location;
    }

    public void setLocation(double x, double y, double z) {
        location.x = x;
        location.y = y;
        location.z = z;
    }

    /**
     * Sets the ID.
     * @param id The object as ID.
     */
    private void setId(Object id) {
        this.id = id;
    }

    /**
     * Returns the material.
     * @return The material.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Returns the location.
     * @return The location.
     */
    public Point3d getLocation() {
        return location;
    }

    /**
     * Returns the ID.
     * @return The object as ID.
     */
    public Object getId() {
        return id;
    }
}

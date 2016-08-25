package rayTracing.scene;

import rayTracing.math.Matrix;
import rayTracing.math.Point3d;
import rayTracing.Ray;
import rayTracing.math.Vector3d;

/**
 * @author Alexandre Vieira
 */
public class Plane extends Primitive {

    /** The vector perpendicular to this <code>Plane</code>. */
    private Vector3d normal;

    /**
     * Creates a <code>Plane</code> defined by the specified <code>Vector3d</code>.
     * @param normal The vector perpendicular to this plane.
     */
    public Plane(Object id, Vector3d normal) {
        this(id, new Point3d(), normal);
    }

    /**
     * Creates a <code>Plane</code> defined by the specified <code>Vector3d</code> 
     * and containing the given point.
     * @param location
     * @param normal
     */
    public Plane(Object id, Point3d location, Vector3d normal) {
        super(id, location);
        this.normal = normal.normalize();
    }

    @Override
    public IntersectionResult intersects(Ray ray) {
        double a, b, c, d;

        c = ray.getDirection().dot(normal);
        if (c == 0) {
            return new IntersectionResult(-1.0d, IntersectionResult.MISS);
        } else {
            a = normal.dot(new Vector3d(getLocation().x, getLocation().y, getLocation().z));
            b = normal.dot(new Vector3d(ray.getLocation().x, ray.getLocation().y, ray.getLocation().z));
            d = -(b - a) / c;

            //if(d > ray.getMaxLength())
            if (d <= ray.getMaxLength() && d > 0.0d) {
                return new IntersectionResult(d, IntersectionResult.OUTSIDE);
            } else {
                return new IntersectionResult(d, IntersectionResult.MISS);
            }
        }
    }

    @Override
    public Vector3d getNormalAt(Point3d point) {
        return new Vector3d(normal);
    }

    @Override
    public void transform(Matrix matrix) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PrimitiveInfo createInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

package rayTracing.scene;

import rayTracing.Ray;
import rayTracing.math.Matrix;
import rayTracing.math.Point3d;
import rayTracing.math.Vector3d;

/**
 * @author Alexandre Vieira
 */
public class LightSphere extends LightPrimitive{
    
    /** The radius of this <code>Sphere</code>. */
    private double radius;
    /** The squared radius of this <code>Sphere</code> (for optimization purposes). */
    private double radius2;

    public LightSphere(Light owner, Point3d location, double radius) {
        super(owner, location);
        setRadius(radius);
    }

    @Override
    public IntersectionResult intersects(Ray ray) {
        Vector3d v = getLocation().minus(ray.getLocation());
        double L2, L2hc, tca, t, d2, thc, b, det, i1, i2;

        tca = v.dot(ray.getDirection());
        if (tca < 0.0d) {
            return new IntersectionResult(-1.0d, IntersectionResult.MISS);
        }

        d2 = v.dot(v) - (tca * tca);
        if (d2 > radius2) {
            return new IntersectionResult(-1.0d, IntersectionResult.MISS);
        }

        thc = Math.sqrt(radius2 - d2);
        t = tca - thc;

        if (t > 0.0d && t <= ray.getMaxLength()) {
            if (getLocation().minus(ray.getLocation()).getLength() <= radius) {
                return new IntersectionResult(t, IntersectionResult.INSIDE);
            } else {
                return new IntersectionResult(t, IntersectionResult.OUTSIDE);
            }
        } else {
            return new IntersectionResult(-1.0d, IntersectionResult.MISS);
        }
    }

    @Override
    public Vector3d getNormalAt(Point3d point) {
        Vector3d normal = point.minus(getLocation());
        normal.setToNormal();

        return normal;
    }

    @Override
    public void transform(Matrix matrix) {
    }

    @Override
    public PrimitiveInfo createInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setRadius(double radius) {
        this.radius = radius;
        this.radius2 = radius * radius;
    }

    public double getRadius() {
        return radius;
    }
}

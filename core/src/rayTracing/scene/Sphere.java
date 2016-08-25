package rayTracing.scene;

import rayTracing.math.Matrix;
import rayTracing.math.Point3d;
import rayTracing.Ray;
import rayTracing.math.Vector3d;

/**
 * @author Alexandre Vieira
 */
public class Sphere extends Primitive {

    /** The radius of this <code>Sphere</code>. */
    private double radius;
    /** The squared radius of this <code>Sphere</code> (for optimization purposes). */
    private double radius2;

    /**
     * Creates a <code>Sphere</code> with a radius of 1.0.
     */
    public Sphere(Object id) {
        this(id, 1.0d);
    }

    /**
     * Creates a <code>Sphere</code> with the specified radius.
     * @param radius The radius of the sphere.
     */
    public Sphere(Object id, double radius) {
        this(id, new Point3d(), radius);
    }

    /**
     * Creates a <code>Sphere</code> with the specified radius, at the given location.
     * @param location The location of the sphere.
     * @param radius The radius of the sphere.
     */
    public Sphere(Object id, Point3d location, double radius) {
        super(id, location);
        this.radius = radius;
        this.radius2 = radius * radius;
    }

    /**
     * Returns the distance between the ray and the intersection point.
     * @param ray The ray to test.
     * @return A positive value if there's an intersection.
     */
    @Override
    public IntersectionResult intersects(Ray ray) {
        Vector3d v = getLocation().minus(ray.getLocation());
        double L2, L2hc, tca, t, d2, thc, b, det, i1, i2;

//        b = -(v.dot(ray.getDirection()));
//        det = (b * b) - v.dot(v) + radius2;
//        
//        if(det > 0.0d){
//            det = Math.sqrt(det);
//            i1 = b - det;
//            i2 = b + det;
//            
//            if(i2 > 0.0d){
//                if(i1 < 0.0d){
//                    if(i2 < ray.getMaxLength()){
//                        return new IntersectionResult(i2, IntersectionResult.INSIDE_PRIMITIVE);
//                    }
//                }else{
//                    if(i1 < ray.getMaxLength()){
//                        return new IntersectionResult(i1, IntersectionResult.OUTSIDE_PRIMTIVE);
//                    }
//                }
//            }
//        }
//        
//        return new IntersectionResult(-1.0f, IntersectionResult.MISS);

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
            if (t < 0.0d && ray.getLocation().minus(getLocation()).getLength() <= radius) {
                return new IntersectionResult(t, IntersectionResult.INSIDE);
            } else {
                return new IntersectionResult(-1.0d, IntersectionResult.MISS);
            }
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
        setLocation(matrix.multiply(getLocation()));
    }

    @Override
    public PrimitiveInfo createInfo() {
        SphereInfo info = new SphereInfo();

        info.setId(getId());
        info.setLocation(new Point3d(getLocation()));
        //info.setRadius(radius);

        return info;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        this.radius2 = radius * radius;
    }

    /**
     * Returns the radius of this sphere.
     * @return The radius.
     */
    public double getRadius() {
        return radius;
    }
}

package rayTracing.scene;

import rayTracing.Ray;
import rayTracing.math.Matrix;
import rayTracing.math.Point3d;
import rayTracing.math.Vector3d;

/**
 * @author Simão Seiça
 */
public class Polygon extends Primitive {

    Point3d[] vertices;
    Vector3d normal;

    public Polygon(Object id, Point3d[] vertices) {
        super(id);
        setVertices(vertices);
    }

    @Override
    public IntersectionResult intersects(Ray ray) {
        double b;
        double c; // Dot product of the plane's normal and ray's direction.
        double d; // Distance between the ray's location and the point of intersection.
        double D; // Closest point (contained in the plane) to the origin.

        c = ray.getDirection().dot(normal);

        if (c == 0.0d) {
            // The ray is parallel to the plane.
            return new IntersectionResult(-1.0d, IntersectionResult.MISS);
        } else {
            b = normal.dot(new Vector3d(ray.getLocation().x, ray.getLocation().y, ray.getLocation().z));
            // A vector to an arbitrary point contained in the plane.
            Vector3d v = new Vector3d(vertices[2].x, vertices[2].y, vertices[2].z);
            D = normal.dot(v);
            d = (-b + D) / c;

            if (d > 0.0d) {
                // There's a hit.

                if (d > ray.getMaxLength()) {
                    //the plane is too far away
                    return new IntersectionResult(-1.0d, IntersectionResult.MISS);
                }

                if (c < 0.0d) {
                    if (intersectsThePolygon(vertices, ray, new IntersectionResult(d, IntersectionResult.OUTSIDE))) {
                        // Hits the front face.
                        return new IntersectionResult(d, IntersectionResult.OUTSIDE);
                    } else {
                        return new IntersectionResult(-1.0d, IntersectionResult.MISS);
                    }
                } else {
                    if (intersectsThePolygon(vertices, ray, new IntersectionResult(d, IntersectionResult.INSIDE))) {
                        // Hits the back face.
                        return new IntersectionResult(d, IntersectionResult.INSIDE);
                    } else {
                        return new IntersectionResult(-1.0d, IntersectionResult.MISS);
                    }

                }
            } else if (d == 0.0d) {
                // The ray was spawned exactly in the plane.
                return new IntersectionResult(0.0d, IntersectionResult.INSIDE);
            } else if (d < 0.0d) {
                // The ray points away from the plane.
                return new IntersectionResult(-1.0d, IntersectionResult.MISS);
            }
        }

        return new IntersectionResult(-1.0d, IntersectionResult.MISS);
    }

    @Override
    public Vector3d getNormalAt(Point3d point) {
        return normal;
    }

    @Override
    public PrimitiveInfo createInfo() {
        return null;
    }

    public void transform(Matrix m) {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = m.multiply(vertices[i]);
        }
        //setLocation(m.multiply(getLocation()));
        computeNormal();
    }

    @Override
    public void setLocation(double x, double y, double z) {
        setLocation(new Point3d(x, y, z));
    }

    @Override
    public void setLocation(Point3d location) {
        Vector3d direction;

        for (int index = 1; index < vertices.length; index++) {
            direction = vertices[index].minus(vertices[0]);
            vertices[index] = location.translate(direction);
        }
        vertices[0] = location;

        computeNormal();
    }

    @Override
    public Point3d getLocation() {
        return vertices[0];
    }

    public void setVertices(Point3d[] vertices) {
        if (vertices.length < 3) {
            throw new IllegalArgumentException("At least three vertices are required.");
        }

        this.vertices = vertices;
        computeNormal();
    }

    public Vector3d getNormal() {
        return normal;
    }

    public Point3d[] getVertices() {
        return vertices;
    }

    public void computeNormal() {
        Vector3d ab = vertices[1].minus(vertices[0]);
        Vector3d ad = vertices[2].minus(vertices[0]);
        ab.setToNormal();
        ad.setToNormal();
        normal = ab.cross(ad);
        normal.setToNormal();
    }

    //if there are intersection in the polygon
    private boolean intersectsThePolygon(Point3d[] vertices, Ray ray, IntersectionResult result) {
        Point3d pi;
        Vector3d api;
        Vector3d bpi;
        Vector3d n;
        double di;

        //point of intersection
        pi = ray.getLocation().translate(ray.getDirection().scale(result.getDistance()));

        if (result.getWhere() == IntersectionResult.OUTSIDE) {
            // Cycles through all the edges.
            for (int i = 0; i < vertices.length; i++) {

                if (i == vertices.length - 1) {
                    api = vertices[i].minus(ray.getLocation());
                    bpi = vertices[0].minus(ray.getLocation());
                } else {
                    api = vertices[i].minus(ray.getLocation());
                    bpi = vertices[i + 1].minus(ray.getLocation());
                }

                n = bpi.cross(api);
                n.setToNormal();


                di = -((ray.getLocation().x * n.x) + (ray.getLocation().y * n.y) + (ray.getLocation().z * n.z));

                if ((pi.x * n.x) + (pi.y * n.y) + (pi.z * n.z) + di < 0.0d) {

                    return false;
                }
            }
            return true;
        } else {
            // Cycles through all the edges.
            for (int i = vertices.length - 1; i > -1; i--) {

                if (i == 0) {
                    api = vertices[i].minus(ray.getLocation());
                    bpi = vertices[vertices.length - 1].minus(ray.getLocation());
                } else {
                    api = vertices[i].minus(ray.getLocation());
                    bpi = vertices[i - 1].minus(ray.getLocation());
                }

                n = bpi.cross(api);
                n.setToNormal();


                di = -((ray.getLocation().x * n.x) + (ray.getLocation().y * n.y) + (ray.getLocation().z * n.z));

                if ((pi.x * n.x) + (pi.y * n.y) + (pi.z * n.z) + di < 0.0d) {
                    return false;
                }
            }

            return true;
        }
    }
}

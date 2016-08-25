package rayTracing.scene;

import rayTracing.Ray;
import rayTracing.math.Matrix;
import rayTracing.math.Point3d;
import rayTracing.math.Vector3d;

/**
 * @author Simão Seiça
 */
public abstract class Solid extends Primitive {

    Polygon[] faces;
    Polygon highlightedFace;
    
    public Solid(Object id) {
        super(id);
    }

    public Solid(Object id, Polygon[] faces) {
        super(id);
        
        setFaces(faces);
    }

    protected abstract IntersectionResult isInside(Ray ray, double distance);

    @Override
    public IntersectionResult intersects(Ray ray) {
        Point3d p = new Point3d(ray.getLocation());
        IntersectionResult temp = new IntersectionResult(ray.getMaxLength(), IntersectionResult.MISS);

        for (int index = 0; index < faces.length; index++) {
            IntersectionResult ir = intersectsPlane(ray, faces[index].normal, faces[index].vertices);

            if (ir.getWhere() != IntersectionResult.MISS) {
                // There's an intersection, so let's see if this one is closer.
                if (ir.getDistance() < temp.getDistance()) {
                    temp = ir;
                    highlightedFace = faces[index];
                }
            }
        }

        return temp;
    }

    @Override
    public Vector3d getNormalAt(Point3d point) {
        return (highlightedFace == null) ? null : highlightedFace.getNormalAt(point);
    }

    @Override
    public void transform(Matrix matrix) {
        for (int index = 0; index < faces.length; index++) {
            faces[index].transform(matrix);
        }
        //setLocation(matrix.multiply(getLocation()));
    }

    @Override
    public Point3d getLocation() {
        return faces[0].getLocation();
    }

    @Override
    public void setLocation(Point3d location) {
        Matrix m = new Matrix();
        m.translate(location.x, location.y, location.z);

        for (int index = 0; index < faces.length; index++) {
            faces[index].transform(m);
        }
    }

    @Override
    public void setLocation(double x, double y, double z) {
        setLocation(new Point3d(x, y, z));
    }

    public void setFaces(Polygon[] faces) {
        if (faces.length < 4) {
            throw new IllegalArgumentException("At least four faces are required.");
        }

        this.faces = faces;
    }

    public Polygon[] getFaces() {
        return faces;
    }

    protected boolean intersectsFace(Point3d[] vertices, Ray ray, IntersectionResult result) {

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

                if ((pi.x * n.x) + (pi.y * n.y) + (pi.z * n.z) + di < 0) {

                    return false;
                }
            }
            return true;
        } else {// Cycles through all the edges.
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

                if ((pi.x * n.x) + (pi.y * n.y) + (pi.z * n.z) + di < 0) {

                    return false;
                }
            }
            return true;
        }
    }

    protected IntersectionResult intersectsPlane(Ray ray, Vector3d normal, Point3d[] vertices) {
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

                if (d >= ray.getMaxLength()) {
                    //the plane is too far away
                    return new IntersectionResult(-1.0d, IntersectionResult.MISS);
                }
                if (c < 0.0d) {
                    if (intersectsFace(vertices, ray, new IntersectionResult(d, IntersectionResult.OUTSIDE))) {
                        // Hits the front face.
                        return new IntersectionResult(d, IntersectionResult.OUTSIDE);
                    } else {
                        return new IntersectionResult(-1.0d, IntersectionResult.MISS);
                    }
                } else {
                    if (intersectsFace(vertices, ray, new IntersectionResult(d, IntersectionResult.INSIDE))) {
                        // Hits the back face.
                        if (intersectsFace(vertices, ray, new IntersectionResult(d, IntersectionResult.INSIDE))) {
                            return new IntersectionResult(d, IntersectionResult.INSIDE);
                        } else {
                            return new IntersectionResult(d, IntersectionResult.OUTSIDE);
                        }

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
}

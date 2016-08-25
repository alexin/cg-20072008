package rayTracing.scene;

import rayTracing.math.Point3d;

public class Triangle extends Polygon {

    public Triangle(Object id, Point3d p1, Point3d p2, Point3d p3) {
        super(id, new Point3d[]{p1, p2, p3});
    }

    @Override
    public void setVertices(Point3d[] vertices) {
        if (vertices.length != 3) {
            throw new IllegalArgumentException("Exactly three vertices are required.");
        }

        super.setVertices(vertices);
    }
}

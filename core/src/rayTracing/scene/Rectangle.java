package rayTracing.scene;

import rayTracing.math.Point3d;

/**
 * @author Simão Seiça
 */
public class Rectangle extends Polygon {

    public Rectangle(Object id, double width, double height) {
        super(id, new Point3d[]{
            new Point3d(0.0d, 0.0d, 0.0d),
            new Point3d(width, 0.0d, 0.0d),
            new Point3d(width, height, 0.0d),
            new Point3d(0.0d, height, 0.0d)
        });
    }

    @Override
    public void setVertices(Point3d[] vertices) {
        if (vertices.length != 4) {
            throw new IllegalArgumentException("Exactly four vertices are required.");
        }

        super.setVertices(vertices);
    }
}

package rayTracing.scene;

import rayTracing.Ray;
import rayTracing.math.Matrix;
import rayTracing.math.Point3d;

/**
 * @author Simão Seiça
 */
public class RectangularPrism extends Solid {

    public RectangularPrism(Object id, double x, double y, double z) {
        super(id);

        Polygon[] facess = new Polygon[6];
        Matrix m = new Matrix();

        Rectangle rect = new Rectangle(id.toString() + "-1", x, y);
        m = new Matrix();
        m.rotateY(180.0d);
        m.translate(x, 0.0d, 0.0d);
        rect.transform(m);
        facess[0] = rect;

        rect = new Rectangle(id.toString() + "-2", x, y);
        m = new Matrix();
        m.translate(0.0d, 0.0d, z);
        rect.transform(m);
        facess[1] = rect;

        rect = new Rectangle(id.toString() + "-3", x, z);
        m = new Matrix();
        m.rotateX(90.0d);
        rect.transform(m);
        facess[2] = rect;

        rect = new Rectangle(id.toString() + "-4", x, z);
        m = new Matrix();
        m.rotateX(-90.0d);
        m.translate(0.0d, y, z);
        rect.transform(m);
        facess[3] = rect;

        rect = new Rectangle(id.toString() + "-5", z, y);
        m = new Matrix();
        m.rotateY(90.0d);
        rect.transform(m);
        facess[4] = rect;

        rect = new Rectangle(id.toString() + "-6", z, y);
        m = new Matrix();
        m.rotateY(-90.0d);
        m.translate(x, 0.0d, z);
        rect.transform(m);
        facess[5] = rect;
        
        setFaces(facess);
    }

    

    protected IntersectionResult isInside(Ray ray, double distance) {
        Point3d p = new Point3d(ray.getLocation());

        if (faces[0].vertices[0].x < p.x && faces[0].vertices[0].y < p.y && faces[0].vertices[0].z < p.z) {

            if (faces[0].vertices[1].x > p.x && faces[0].vertices[1].y < p.y && faces[0].vertices[1].z < p.z) {

                if (faces[0].vertices[2].x > p.x && faces[0].vertices[2].y > p.y && faces[0].vertices[2].z < p.z) {

                    if (faces[0].vertices[3].x < p.x && faces[0].vertices[3].y > p.y && faces[0].vertices[3].z < p.z) {

                        if (faces[1].vertices[0].x < p.x && faces[1].vertices[0].y < p.y && faces[1].vertices[0].z > p.z) {

                            if (faces[1].vertices[1].x > p.x && faces[1].vertices[1].y < p.y && faces[1].vertices[1].z > p.z) {

                                if (faces[1].vertices[2].x > p.x && faces[1].vertices[2].y > p.y && faces[1].vertices[2].z > p.z) {

                                    if (faces[1].vertices[3].x < p.x && faces[1].vertices[3].y > p.y && faces[1].vertices[3].z > p.z) {

                                        return new IntersectionResult(distance, IntersectionResult.INSIDE);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return new IntersectionResult(distance, IntersectionResult.OUTSIDE);
    }

    @Override
    public PrimitiveInfo createInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

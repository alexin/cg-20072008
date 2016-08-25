package cg.rayTracingViewer.gui;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import rayTracing.scene.Primitive;
import rayTracing.scene.Sphere;

/**
 * @author Alexandre Vieira
 */
public class SphereShape extends CanvasShape {

    private Shape shape;
    private double radius;

    public SphereShape() {
    }

    @Override
    public Shape getXYView() {
        return shape;
    }

    @Override
    public Shape getXZView() {
        return shape;
    }

    @Override
    public Shape getZYView() {
        return shape;
    }

    @Override
    public void setInfo(Primitive primitive) {
        super.setInfo(primitive);
        setRadius(((Sphere) primitive).getRadius());
    }

    @Override
    public double getOffset() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        shape = new Ellipse2D.Double(0.0d, 0.0d, radius * 2, radius * 2);
    }

    public double getRadius() {
        return radius;
    }
}

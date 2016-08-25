package cg.rayTracingViewer.gui;

import java.awt.Color;
import java.awt.Shape;
import rayTracing.math.Point3d;
import rayTracing.scene.Primitive;

/**
 * @author Alexandre Vieira
 */
public abstract class CanvasShape {

    Object id;
    Point3d location;
    Color color;

    public CanvasShape() {
        this(null, new Point3d(), Color.RED);
    }

    public CanvasShape(Object id, Point3d location, Color color) {
        this.id = id;
        this.location = location;
        this.color = color;
    }

    public abstract Shape getXYView();

    public abstract Shape getXZView();

    public abstract Shape getZYView();

    public void setId(Object id) {
        this.id = id;
    }

    public void setLocation(Point3d location) {
        this.location = location;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setInfo(Primitive primitive) {
        this.id = primitive.getId();
        this.location = primitive.getLocation();
        //this.color = primitive.getMaterial().getAmbientColor();
    }

    public Object getId() {
        return id;
    }

    public Point3d getLocation() {
        return location;
    }

    public Color getColor() {
        return color;
    }

    public double getOffset() {
        return 0.0d;
    }
}

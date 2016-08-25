package rayTracing.scene;

import java.io.Serializable;
import rayTracing.math.Color3d;
import rayTracing.math.Point3d;

/**
 * @author Alexandre Vieira
 */
public abstract class Light implements Serializable{

    Object id;
    Point3d location;
    Color3d color;

    public Light(Object id) {
        this.id = id;
        this.location = new Point3d();
        this.color = new Color3d();
    }

    public abstract LightPrimitive getPrimitive();

    private void setId(Object id) {
        this.id = id;
    }

    public void setColor(Color3d color) {
        this.color = color;
    }

    public void setColor(double red, double green, double blue) {
        color.red = red;
        color.green = green;
        color.blue = blue;
    }

    public void setLocation(Point3d location) {
        this.location = location;
        getPrimitive().setLocation(location);
    }

    public void setLocation(double x, double y, double z) {
        setLocation(new Point3d(x, y, z));
    }

    public Object getId() {
        return id;
    }

    public Color3d getColor() {
        return color;
    }

    public Point3d getLocation() {
        return location;
    }
}

package rayTracing.math;

import java.io.Serializable;

/**
 * @author Alexandre Vieira
 */
public class Point3d implements Serializable{

    public double x;
    public double y;
    public double z;
    public double w;

    public Point3d() {
        this(0.0d, 0.0d, 0.0d);
    }

    public Point3d(Point3d p) {
        this(p.x, p.y, p.z, p.w);
    }

    public Point3d(double x, double y, double z) {
        this(x, y, z, 1.0d);
    }

    public Point3d(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point3d other = (Point3d) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.z != other.z) {
            return false;
        }
        if (this.w != other.w) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.w) ^ (Double.doubleToLongBits(this.w) >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return String.format("(%f, %f, %f, %f)", x, y, z, w);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getW() {
        return w;
    }

    public Point3d translate(Vector3d direction) {
        return new Point3d(x + direction.x, y + direction.y, z + direction.z);
    }

    public void add(Point3d p) {
        x += p.x;
        y += p.y;
        z += p.z;
    }

    public Vector3d minus(Point3d p) {
        Vector3d result = new Vector3d();
        result.x = x - p.x;
        result.y = y - p.y;
        result.z = z - p.z;

        return result;
    }

    public Vector3d createVectorTo(Point3d p) {
        Vector3d result = new Vector3d();
        result.x = p.x - x;
        result.y = p.y - y;
        result.z = p.z - z;

        return result;
    }
}

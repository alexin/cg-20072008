package rayTracing.math;

import java.io.Serializable;

/**
 * @author Alexandre Vieira
 */
public class Vector3d implements Serializable{

    public double x;
    public double y;
    public double z;

    public Vector3d() {
        this(0.0d, 0.0d, 0.0d);
    }

    public Vector3d(Vector3d vector) {
        this(vector.x, vector.y, vector.z);
    }

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vector3d other = (Vector3d) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.z != other.z) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return String.format("(%f, %f, %f)", x, y, z);
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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public boolean isParallelTo(Vector3d other) {
        return equals(other) || equals(other.negate());
    }

    public Vector3d negate() {
        return new Vector3d(-x, -y, -z);
    }

    public void setToNegation() {
        x = -x;
        y = -y;
        z = -z;
    }

    public Vector3d scale(double value) {
        return new Vector3d(x * value, y * value, z * value);
    }

    public Point3d translate(double distance) {
        return new Point3d(x * distance, y * distance, z * distance);
    }

    public Vector3d plus(Vector3d other) {
        return new Vector3d(x + other.x, y + other.y, z + other.z);
    }

    public Vector3d minus(Vector3d other) {
        return new Vector3d(x - other.x, y - other.y, z - other.z);
    }

    public void setToScale(double value) {
        x *= value;
        y *= value;
        z *= value;
    }

    public Vector3d normalize() {
        Vector3d normal = new Vector3d(x, y, z);
        normal.setToNormal();

        return normal;
    }

    public void setToNormal() {
        double length = getLength();
        x /= length;
        y /= length;
        z /= length;
    }

    public double getLength() {
        return Math.sqrt(getLengthSquared());
    }

    public double getLengthSquared() {
        return (x * x) + (y * y) + (z * z);
    }

    public double dot(Vector3d other) {
        return (x * other.x) + (y * other.y) + (z * other.z);
    }

    public Vector3d cross(Vector3d other) {
        Vector3d result = new Vector3d();
        result.x = (y * other.z) - (z * other.y);
        result.y = -((x * other.z) - (z * other.x));
        result.z = (x * other.y) - (y * other.x);

        return result;
    }

    public Vector3d reflect(Vector3d normal) {
        return this.minus(normal.scale(this.dot(normal) * 2.0f));
    }
    
    /**
     * Returns a refracted ray by the specified normal
     * @param normal
     * @param ni
     * @param nr
     * @return
     */
    public Vector3d refract(Vector3d normal, double ni, double nr){
        Vector3d refracted = new Vector3d();
        
        return refracted;
    }
}

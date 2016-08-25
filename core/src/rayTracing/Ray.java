package rayTracing;

import rayTracing.math.Point3d;
import rayTracing.math.Vector3d;

/**
 * @author Alexandre Vieira
 */
public class Ray {

    /** The direction of this <code>Ray</code>.*/
    private Vector3d direction;
    /** The location where this <code>Ray</code> spawns.*/
    private Point3d location;
    /** The maximum length allowed for this ray.*/
    private double maxLength;

    /**
     * Creates a <code>Ray</code> with the specified direction and spawning location.
     * The direction <code>Vector3d</code> should be normalized.
     * @param direction The normalized direction.
     * @param location The spawning location.
     */
    public Ray(Vector3d direction, Point3d location) {
        this(direction, location, 1000000.0d);
    }
    
    /**
     * Creates a <code>Ray</code> with the specified direction and spawning location.
     * The direction <code>Vector3d</code> should be normalized.
     * @param direction The normalized direction.
     * @param location The spawning location.
     */
    public Ray(Vector3d direction, Point3d location, double maxlength) {
        this.direction = direction;
        this.location = location;
        this.maxLength = maxlength;
    }

    /**
     * Returns the direction.
     * @return The direction.
     */
    public Vector3d getDirection() {
        return direction;
    }

    /**
     * Returns the location.
     * @return The loaction.
     */
    public Point3d getLocation() {
        return location;
    }

    public double getMaxLength() {
        return maxLength;
    }
}

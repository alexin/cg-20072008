package rayTracing.scene;

/**
 * @author Alexandre Vieira
 */
public class IntersectionResult {

    public static final int INSIDE = -1;
    public static final int MISS = 0;
    public static final int OUTSIDE = 1;
    private Object userObject;
    private double distance;
    private int where;

    public IntersectionResult(double distance, int where) {
        this.distance = distance;
        setWhere(where);
    }

    @Override
    public String toString() {
        return String.format("Distance = %f; Where: %d", distance, where);
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    private void setWhere(int where) {
        if (where != INSIDE && where != MISS && where != OUTSIDE) {
            throw new IllegalArgumentException("Illegal state.");
        }
        
        this.where = where;
    }

    public Object getUserObject() {
        return userObject;
    }

    public double getDistance() {
        return distance;
    }

    public int getWhere() {
        return where;
    }
}

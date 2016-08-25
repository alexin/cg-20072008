package rayTracing;

import rayTracing.math.Point3d;
import rayTracing.math.Vector3d;

/**
 * @author Alexandre Vieira
 */
public class Camera {

    /** The location of this <code>Camera</code> in World Coordinates. */
    private Point3d location;
    /** The Projection Plane's point of reference (located at the upper-left corner). */
    private Point3d ppr;
    /** The x-axis of this <code>Camera</code> Coordinate System. */
    private Vector3d uAxis;
    /** The y-axis of this <code>Camera</code> Coordinate System. */
    private Vector3d vAxis;
    /** The z-axis of this <code>Camera</code> Coordinate System. */
    private Vector3d nAxis;
    /** The Field Of View, in degrees. */
    private double fov;
    /** Half the Field Of View, in radians */
    private double alpha;
    /** The tangent of <code>alpha</code> (for optimization purposes). */
    private double tanAlpha;
    /** The distance between this <code>Camera</code>'s and its Projection Plane. */
    private double screenDistance;
    /** The width of the Projection Plane. */
    private double width;
    /** The height of the Projection Plane. */
    private double height;
    /** The width of a single cell. */
    private double cellWidth;
    /** The height of a single cell. */
    private double cellHeight;
    /** The number of horizontal cells on the Projection Plane */
    private int cellColumns;
    /** The number of vertical cells on the Projection Plane */
    private int cellRows;

    /**
     * Creates a <code>Camera</code>.
     */
    public Camera() {
        this(256, 256, 45.0f);
    }

    /**
     * Creates a <code>Camera</code> with the given Field Of View and wgich Projection is 
     * <code>width</code> wide and <code>height</code> tall.
     * @param width The width of the Projection Plane in cells.
     * @param height The height of the Projection Plane in cells.
     * @param fov The Field Of View.
     */
    public Camera(int width, int height, double fov) {
        this(width, height, fov, 1.0d, new Vector3d(1.0d, 0.0d, 1.0d),
                new Vector3d(0.0d, 1.0d, 0.0d), new Point3d(1.0d, 1.0d, 1.0d));
    }

    /**
     * Creates a <code>Camera</code> with the specified direction and up vector, at the given location.
     * @param width The width of the Projection Plane in cells.
     * @param height The height of the Projection Plane in cells.
     * @param fov The Field Of View.
     * @param screenDistance The distance between the location and the Projection Plane.
     * @param direction The direction this <code>Camera</code> is facing.
     * @param up The ??????.
     * @param location The location of this <code>Camera</code> in World Coordinates.
     */
    public Camera(int width, int height, double fov, double screenDistance,
            Vector3d direction, Vector3d up, Point3d location) {
        this.ppr = new Point3d();
        this.screenDistance = screenDistance;
        this.uAxis = direction;
        this.vAxis = up;
        this.nAxis = new Vector3d();
        this.location = location;
        this.cellColumns = width;
        this.cellRows = height;
        this.setFOV(fov);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("Location = %s\n", location));
        builder.append(String.format("uAxis = %s\n", uAxis));
        builder.append(String.format("vAxis = %s\n", vAxis));
        builder.append(String.format("nAxis = %s\n", nAxis));
        builder.append(String.format("PPR = %s\n", ppr));
        builder.append(String.format("FOV = %f\n", fov));
        builder.append(String.format("Alpha = %f\n", alpha));
        builder.append(String.format("TanAlpha = %f\n", tanAlpha));
        builder.append(String.format("Screen Distance = %f\n", screenDistance));
        builder.append(String.format("Screen Width = %f\n", width));
        builder.append(String.format("Screen Height = %f\n", height));
        builder.append(String.format("Horizontal Cells = %d\n", cellColumns));
        builder.append(String.format("Vertical Cells = %d\n", cellRows));
        builder.append(String.format("Cell Width = %f\n", cellWidth));
        builder.append(String.format("Cell Height = %f\n", cellHeight));

        return builder.toString();
    }

    /**
     * Sets the direction this <code>Camera</code> is facing.
     * @param direction The direction vector in World Coordinates.
     */
    public void setDirection(Vector3d direction) {
        uAxis = direction;
        configure();
    }

    public void setDirection(double x, double y, double z) {
        uAxis.x = x;
        uAxis.y = y;
        uAxis.z = z;
    }

    /**
     * Sets the ???????? this <code>Camera</code> is facing.
     * @param up The ???????? vector in World Coordinates.
     */
    public void setUp(Vector3d up) {
        vAxis = up;
        configure();
    }

    public void setUp(double x, double y, double z) {
        vAxis.x = x;
        vAxis.y = y;
        vAxis.z = z;
    }

    /**
     * Sets the Field Of View.
     * @param fov The Field Of View, in degrees.
     */
    public void setFOV(double fov) {
        if (fov < 0.0d || fov > 90.0d) {
            throw new IllegalArgumentException("The Field Of View must" +
                    " range from 0.0 to 90.0, inclusive.");
        }
        this.fov = fov;
        alpha = Math.toRadians(fov / 2.0d);
        tanAlpha = Math.tan(alpha);
        setupProjectionPlane();
    }

    /**
     * Sets the location of this <code>Camera</code>.
     * This <code>Point3d</code> corresponds to the origin of the Camera Coordinates System.
     * @param location The location point in World Coordinates.
     */
    public void setLocation(Point3d location) {
        this.location = location;
    }

    public void setLocation(double x, double y, double z) {
        location.x = x;
        location.y = y;
        location.z = z;
    }

    /**
     * Sets the distance between this <code>Camera</code> and its screen.
     * The screen is no more than the Projection Plane.
     * @param screenDistance The distance between the location point and the 
     * Projection Plane.
     */
    public void setScreenDistance(double screenDistance) {
        if (screenDistance < 0.0d) {
            throw new IllegalArgumentException("The screen must be at a minimum " +
                    "distance of 0.0.");
        }
        this.screenDistance = screenDistance;
        setupProjectionPlane();
    }

    /**
     * Sets the resolution of the Projection Plane.
     * @param width The horizontal number of cells on the Projection Plane.
     * @param height The vertical number of cells on the Projection Plane.
     */
    public void setResolution(int width, int height) {
        if (width <= 0 || width > 2000 || height <= 0 || height > 2000) {
            throw new IllegalArgumentException("The resolution must range " +
                    "from 0 to 2000, inclusive.");
        }
        this.cellColumns = width;
        this.cellRows = height;
        setupProjectionPlane();
    }

    /**
     * Return the x-axis of this <code>Camera</code> Coordinate System.
     * This corresponds to the direction vector.
     * @return the x-axis;
     */
    public Vector3d getUAxis() {
        return uAxis;
    }

    /**
     * Return the y-axis of this <code>Camera</code> Coordinate System.
     * This corresponds to the upward vector.
     * @return the y-axis;
     */
    public Vector3d getVAxis() {
        return vAxis;
    }

    /**
     * Return the z-axis of this <code>Camera</code> Coordinate System.
     * This corresponds to the sideward vector.
     * @return the z-axis;
     */
    public Vector3d getNAxis() {
        return nAxis;
    }

    /**
     * Returns the location of this <code>Camera</code>.
     * @return The location.
     */
    public Point3d getLocation() {
        return location;
    }

    /**
     * Returns the Field Of View.
     * @return The Field Of View, in degrees.
     */
    public double getFOV() {
        return fov;
    }

    /**
     * Returns the distance to the Projection Plane.
     * @return The distance to the Projection Plane.
     */
    public double getScreenDistance() {
        return screenDistance;
    }

    /**
     * Returns the width of the screen.
     * This corresponds to the horizontal number of cells from the Projection Plane.
     * @return The width of the screen.
     */
    public int getScreenWidth() {
        return cellColumns;
    }

    /**
     * Returns the height of the screen.
     * This corresponds to the vertical number of cells from the Projection Plane.
     * @return The height of the screen.
     */
    public int getScreenHeight() {
        return cellRows;
    }

    /**
     * Returns a <code>Point3d</code> contained in the Projection Plane, at the specified row and column.
     * The point represents to the upper-left corner of the corresponding cell.
     * @param row The vertical coordinate of the cell.
     * @param column The horizontal coordinate of the cell.
     * @return The corresponding <code>Point3d</code> of the specified cell. 
     */
    public Point3d createPointForCell(int row, int column) {
        Point3d p = new Point3d();
        p.x = screenDistance;
        p.y = ppr.y - (row * cellHeight);
        p.z = ppr.z + (column * cellWidth);

        return p;
    }

    /**
     * Configures this <code>Camera</code>, particularly its Coordinate System and Projection Plane.
     * This method should be called before any attempt to call <code>createPointForCell</code>.
     */
    public void configure() {
        uAxis.setToNormal();
        vAxis.setToNormal();

        if (uAxis.isParallelTo(vAxis)) {
            throw new IllegalArgumentException("uAxis is parallel to vAxis.");
        }

        nAxis = uAxis.cross(vAxis);
        nAxis.setToNormal();
        vAxis = nAxis.cross(uAxis);
        vAxis.setToNormal();
        setupProjectionPlane();
    }

    /**
     * Sets up the data associated to the Projection Plane.
     */
    private void setupProjectionPlane() {
        ppr.x = screenDistance;
        ppr.y = screenDistance * tanAlpha;
        ppr.z = -(screenDistance * tanAlpha);

        width = 2.0d * screenDistance * tanAlpha;
        height = 2.0d * screenDistance * tanAlpha;
        cellWidth = width / cellColumns;
        cellHeight = height / cellRows;

        ppr.y += (cellHeight / 2.0d);
        ppr.z -= (cellWidth / 2.0d);
    }
}

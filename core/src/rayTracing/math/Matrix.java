package rayTracing.math;

import java.util.Arrays;
import java.util.Stack;

/**
 * @author Alexandre Vieira
 * 
 * Representation of a 4x4 matrix.
 */
public class Matrix {

    private static final Stack<Matrix> MATRICES;
    /** The matrix data. */
    private double[] data;

    static {
        MATRICES = new Stack<Matrix>();
    }

    /**
     * Creates a <code>Matrix</code> as the Identity Matrix.
     */
    public Matrix() {
        data = new double[16];
        identity();
    }

    @Override
    public String toString() {
        return String.format("%s\n", Arrays.toString(data));
    }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    /**
     * Sets this <code>Matrix</code> to the Identity Matrix.
     */
    public void identity() {
        for (double value : data) {
            value = 0.0d;
        }
        data[0] = 1.0d;
        data[5] = 1.0d;
        data[10] = 1.0d;
        data[15] = 1.0d;
    }

    /**
     * Sets the value at the specified index;
     * @param value The value to set.
     * @param index The index of the value.
     */
    public void setValueAt(double value, int index) {
        data[index] = value;
    }

    public void setValueAt(double value, int row, int column) {
        data[(column * 4) + row] = value;
    }

    /**
     * Return the value at the specified index.
     * @param index The index of the value.
     * @return The value at the specified index.
     */
    public double getValueAt(int index) {
        return data[index];
    }

    public double getValueAt(int row, int column) {
        return data[(column * 4) + row];
    }

    /**
     * Multiplies this <code>Matrix</code> with the specified <code>Point3d</code> and returns 
     * a new <code>Point3d</code> as result.
     * @param p The point to multpiply.
     * @return A <code>Point3d</code> resulting from this multiplication.
     */
    public Point3d multiply(Point3d p) {
        Point3d result = new Point3d();
        result.x = (p.x * data[0]) + (p.y * data[4]) + (p.z * data[8]) + (p.w * data[12]);
        result.y = (p.x * data[1]) + (p.y * data[5]) + (p.z * data[9]) + (p.w * data[13]);
        result.z = (p.x * data[2]) + (p.y * data[6]) + (p.z * data[10]) + (p.w * data[14]);
        result.w = (p.x * data[3]) + (p.y * data[7]) + (p.z * data[11]) + (p.w * data[15]);

        return result;
    }

    public Matrix multiply(Matrix m) {
        Matrix result = new Matrix();
        double sum;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sum = 0;

                for (int k = 0; k < 4; k++) {
                    sum += (m.getValueAt(i, k) * getValueAt(k, j));
                }

                result.setValueAt(sum, i, j);
            }
        }

        return result;
    }

    public void translate(double dx, double dy, double dz) {
        Matrix t = createTranslationMatrix(dx, dy, dz);

        data = multiply(t).getData();
    }

    private Matrix createTranslationMatrix(double dx, double dy, double dz) {
        Matrix t = new Matrix();

        t.setValueAt(dx, 12);
        t.setValueAt(dy, 13);
        t.setValueAt(dz, 14);

        return t;
    }

    public void rotateX(double angle) {

        Matrix t = createRotationXMatrix(angle);

        data = multiply(t).getData();
    }

    private Matrix createRotationXMatrix(double angle) {
        Matrix t = new Matrix();

        angle = Math.toRadians(angle);
        t.setValueAt(Math.cos(angle), 5);
        t.setValueAt(-Math.sin(angle), 9);
        t.setValueAt(Math.sin(angle), 6);
        t.setValueAt(Math.cos(angle), 10);

        return t;
    }

    public void rotateY(double angle) {

        Matrix t = createRotationYMatrix(angle);

        data = multiply(t).getData();

    }

    private Matrix createRotationYMatrix(double angle) {
        Matrix t = new Matrix();

        angle = Math.toRadians(angle);
        t.setValueAt(Math.cos(angle), 0);
        t.setValueAt(-Math.sin(angle), 8);
        t.setValueAt(Math.sin(angle), 2);
        t.setValueAt(Math.cos(angle), 10);

        return t;
    }

    public void rotateZ(double angle) {

        Matrix t = createRotationZMatrix(angle);

        data = multiply(t).getData();
    }

    private Matrix createRotationZMatrix(double angle) {
        Matrix t = new Matrix();

        angle = Math.toRadians(angle);
        t.setValueAt(Math.cos(angle), 0);
        t.setValueAt(-Math.sin(angle), 4);
        t.setValueAt(Math.sin(angle), 1);
        t.setValueAt(Math.cos(angle), 5);

        return t;
    }

    public void scale(double x, double y, double z) {

        Matrix t = createScale(x, y, z);

        data = multiply(t).getData();


    }

    private Matrix createScale(double x, double y, double z) {
        Matrix t = new Matrix();

        t.setValueAt(x, 0);
        t.setValueAt(y, 5);
        t.setValueAt(z, 10);

        return t;
    }

    public static void push(Matrix m) {
        MATRICES.push(m);
    }

    public static Matrix pop() {
        return MATRICES.pop();
    }
}

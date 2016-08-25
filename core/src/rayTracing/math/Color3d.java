package rayTracing.math;

import java.io.Serializable;

/**
 * @author Alexandre Vieira
 */
public class Color3d implements Serializable{

    public double red;
    public double green;
    public double blue;

    public Color3d() {
        this(1.0d, 1.0d, 1.0d);
    }

    public Color3d(Color3d color) {
        this(color.red, color.green, color.blue);
    }

    public Color3d(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void setBlue(double blue) {
        this.blue = blue;
    }

    public void setGreen(double green) {
        this.green = green;
    }

    public void setRed(double red) {
        this.red = red;
    }

    public double getBlue() {
        return blue;
    }

    public double getGreen() {
        return green;
    }

    public double getRed() {
        return red;
    }

    public void clamp() {
        if (red > 1.0f) {
            red = 1.0f;
        }
        if (green > 1.0f) {
            green = 1.0f;
        }
        if (blue > 1.0f) {
            blue = 1.0f;
        }
    }

    public void add(Color3d color) {
        red += color.red;
        green += color.green;
        blue += color.blue;
    }

    public Color3d multiply(double value) {
        return new Color3d(red * value, green * value, blue * value);
    }

    public Color3d multiply(Color3d color) {
        return new Color3d(red * color.red, green * color.green, blue * color.blue);
    }
}

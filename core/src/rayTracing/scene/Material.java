package rayTracing.scene;

import java.io.Serializable;
import rayTracing.math.Color3d;

/**
 * @author Alexandre Vieira
 */
public class Material implements Serializable{

    private Color3d color;
    private double emittance;
    private double diffuse;
    private double specular;
    private double reflection;
    private double refraction;
    private double refractionIndex;

    public Material() {
        this(new Color3d(), 0.0d, 0.5d, 0.5d, 0.5d, 0.5d, 0.0d);
    }

    public Material(Color3d color, double emittance, double diffuse, double specular, double reflection, double refraction, double refractionIndex) {
        this.color = color;
        this.emittance = emittance;
        this.diffuse = diffuse;
        this.specular = specular;
        this.reflection = reflection;
        this.refraction = refraction;
        this.refractionIndex = refractionIndex;
    }

    public void setColor(Color3d color) {
        this.color = color;
    }

    public void setEmittance(double emittance) {
        this.emittance = emittance;
    }

    public void setDiffuse(double diffuse) {
        this.diffuse = diffuse;
    }

    public void setReflection(double reflection) {
        this.reflection = reflection;
    }

    public void setRefraction(double refraction) {
        this.refraction = refraction;
    }

    public void setRefractionIndex(double refractionIndex) {
        this.refractionIndex = refractionIndex;
    }

    public void setSpecular(double specular) {
        this.specular = specular;
    }

    public Color3d getColor() {
        return color;
    }

    public double getEmittance() {
        return emittance;
    }

    public double getDiffuse() {
        return diffuse;
    }

    public double getReflection() {
        return reflection;
    }

    public double getRefraction() {
        return refraction;
    }

    public double getRefractionIndex() {
        return refractionIndex;
    }

    public double getSpecular() {
        return specular;
    }
}

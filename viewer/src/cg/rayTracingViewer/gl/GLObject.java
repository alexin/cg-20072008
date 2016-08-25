package cg.rayTracingViewer.gl;

import org.lwjgl.opengl.GL11;
import rayTracing.math.Point3d;

/**
 * @author Alexandre Vieira
 */
public abstract class GLObject {

    private GLMaterial material;
    private Point3d location;
    private String id;
    private boolean valid;

    public GLObject(String id) {
        this.material = new GLMaterial();
        this.location = new Point3d();
        this.id = id;
        setValid(false);
    }

    public abstract void validate();

    public abstract void update(long delta);

    public abstract void render();

    public void setMaterial(GLMaterial material) {
        this.material = material;
        setValid(false);
    }

    public void setLocation(Point3d location) {
        this.location = location;
        setValid(false);
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public GLMaterial getMaterial() {
        return material;
    }

    public Point3d getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public boolean isValid() {
        return valid;
    }

    protected void applyMaterial() {
        GL11.glColor3d(material.getColor().red, material.getColor().green, material.getColor().blue);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, material.getDiffuse());
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, material.getSpecular());
        GL11.glMateriali(GL11.GL_FRONT, GL11.GL_SHININESS, 64);
    }
}

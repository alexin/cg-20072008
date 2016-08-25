package cg.rayTracingViewer.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

/**
 * @author Alexandre Vieira
 */
public class GLSphere extends GLObject {

    private float radius;
    private int list;

    public GLSphere(String id) {
        super(id);
        this.setRadius(1.0f);
    }

    @Override
    public void validate() {
        Sphere sphere = new Sphere();
        sphere.setNormals(GLU.GLU_SMOOTH);
        sphere.setOrientation(GLU.GLU_OUTSIDE);
        sphere.setDrawStyle(GLU.GLU_FILL);
        list = GL11.glGenLists(1);

        GL11.glNewList(list, GL11.GL_COMPILE);
        sphere.draw(radius, 32, 32);
        GL11.glEndList();

        setValid(true);
    }

    @Override
    public void update(long delta) {
    }

    @Override
    public void render() {
        GL11.glPushMatrix();
        GL11.glTranslated(getLocation().x, getLocation().y, getLocation().z);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        applyMaterial();
        GL11.glCallList(list);
        GL11.glDisable(GL11.GL_COLOR_MATERIAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    public void setRadius(float radius) {
        this.radius = radius;
        setValid(false);
    }

    public float getRadius() {
        return radius;
    }
}

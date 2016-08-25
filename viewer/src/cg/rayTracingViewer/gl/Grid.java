package cg.rayTracingViewer.gl;

import org.lwjgl.opengl.GL11;

/**
 * @author Alexandre Vieira
 */
public class Grid extends GLObject {

    private float dimension;
    private int list;

    public Grid(String id, float dimension) {
        super(id);
        setDimension(dimension);
    }

    @Override
    public void validate() {
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void update(long delta) {
    }

    @Override
    public void render() {
        GL11.glDisable(GL11.GL_COLOR_MATERIAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glCallList(list);
    }

    public void setDimension(float dimension) {
        this.dimension = dimension;

        createList();
    }

    public float getDimension() {
        return dimension;
    }

    private void createList() {
        float half = dimension / 2.0f;
        list = GL11.glGenLists(1);

        GL11.glNewList(list, GL11.GL_COMPILE);
        GL11.glBegin(GL11.GL_LINES);
        for (float z = -half; z <= half; z += 1.0f) {
            GL11.glColor3d(1.0f, 1.0f, 1.0f);
            GL11.glVertex3f(-half, 0.0f, z);
            GL11.glColor3d(1.0f, 0.0f, 0.0f);
            GL11.glVertex3f(half, 0.0f, z);
        }
        for (float x = -half; x <= half; x += 1.0f) {
            GL11.glColor3d(1.0f, 1.0f, 1.0f);
            GL11.glVertex3f(x, 0.0f, -half);
            GL11.glColor3d(0.0f, 0.0f, 1.0f);
            GL11.glVertex3f(x, 0.0f, half);
        }
        GL11.glEnd();
        GL11.glEndList();
    }
}

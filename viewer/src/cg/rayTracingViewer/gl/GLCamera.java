package cg.rayTracingViewer.gl;

import org.lwjgl.opengl.GL11;
import rayTracing.math.Point3d;

/**
 * @author Alexandre Vieira
 */
public class GLCamera extends GLObject {

    private Point3d lowerLeft;
    private Point3d lowerRight;
    private Point3d upperLeft;
    private Point3d upperRight;
    private int ppList;
    private int fovList;

    public GLCamera(String id) {
        super(id);

        lowerLeft = new Point3d();
        lowerRight = new Point3d();
        upperLeft = new Point3d();
        upperRight = new Point3d();

    }

    @Override
    public void validate() {
        ppList = GL11.glGenLists(1);

        GL11.glNewList(ppList, GL11.GL_COMPILE);
        GL11.glBegin(GL11.GL_LINES);

        //Aresta 1
        GL11.glVertex3d(lowerLeft.x, lowerLeft.y, lowerLeft.z);
        GL11.glVertex3d(lowerRight.x, lowerRight.y, lowerRight.z);

        //Aresta 2
        GL11.glVertex3d(lowerRight.x, lowerRight.y, lowerRight.z);
        GL11.glVertex3d(upperRight.x, upperRight.y, upperRight.z);

        //Aresta 3
        GL11.glVertex3d(upperRight.x, upperRight.y, upperRight.z);
        GL11.glVertex3d(upperLeft.x, upperLeft.y, upperLeft.z);

        //Aresta 4
        GL11.glVertex3d(upperLeft.x, upperLeft.y, upperLeft.z);
        GL11.glVertex3d(lowerLeft.x, lowerLeft.y, lowerLeft.z);

        GL11.glEnd();
        GL11.glEndList();

        fovList = GL11.glGenLists(1);
        GL11.glNewList(fovList, GL11.GL_COMPILE);
        GL11.glBegin(GL11.GL_LINES);

        //Aresta 5
        GL11.glVertex3d(lowerLeft.x, lowerLeft.y, lowerLeft.z);
        GL11.glVertex3d(getLocation().x, getLocation().y, getLocation().z);

        //Aresta 6
        GL11.glVertex3d(lowerRight.x, lowerRight.y, lowerRight.z);
        GL11.glVertex3d(getLocation().x, getLocation().y, getLocation().z);

        //Aresta 7
        GL11.glVertex3d(upperRight.x, upperRight.y, upperRight.z);
        GL11.glVertex3d(getLocation().x, getLocation().y, getLocation().z);

        //Aresta 8
        GL11.glVertex3d(upperLeft.x, upperLeft.y, upperLeft.z);
        GL11.glVertex3d(getLocation().x, getLocation().y, getLocation().z);

        GL11.glEnd();
        GL11.glEndList();

        setValid(true);
    }

    @Override
    public void update(long delta) {
    }

    @Override
    public void render() {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_COLOR_MATERIAL);
        GL11.glColor3f(1.0f, 0.0f, 0.0f);
        GL11.glCallList(ppList);
        GL11.glColor3f(0.0f, 1.0f, 0.0f);
        GL11.glCallList(fovList);
        GL11.glDisable(GL11.GL_COLOR_MATERIAL);
    }

    public Point3d getLowerLeft() {
        return lowerLeft;
    }

    public void setLowerLeft(Point3d lowerLeft) {
        this.lowerLeft = lowerLeft;
        setValid(false);
    }

    public Point3d getLowerRight() {
        return lowerRight;
    }

    public void setLowerRight(Point3d lowerRight) {
        this.lowerRight = lowerRight;
        setValid(false);
    }

    public Point3d getUpperLeft() {
        return upperLeft;
    }

    public void setUpperLeft(Point3d upperLeft) {
        this.upperLeft = upperLeft;
        setValid(false);
    }

    public Point3d getUpperRight() {
        return upperRight;
    }

    public void setUpperRight(Point3d upperRight) {
        this.upperRight = upperRight;
        setValid(false);
    }
}

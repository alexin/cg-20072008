package cg.rayTracingViewer.gl;

import org.lwjgl.opengl.GL11;
import rayTracing.math.Point3d;
import rayTracing.math.Vector3d;

/**
 * @author Alexandre Vieira
 */
public class GLPolygon extends GLObject {

    private Point3d[] vertices;
    private Vector3d normal;
    private int verticesList;
    private int normalList;

    public GLPolygon(String id) {
        super(id);
    }

    @Override
    public void render() {
        //GL11.glPushMatrix();
        //GL11.glTranslated(getLocation().x, getLocation().y, getLocation().z);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        applyMaterial();
        GL11.glCallList(verticesList);
        GL11.glDisable(GL11.GL_COLOR_MATERIAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glCallList(normalList);
        //GL11.glPopMatrix();
    }

    @Override
    public void update(long delta) {
    }

    @Override
    public void validate() {
        verticesList = GL11.glGenLists(1);

        GL11.glNewList(verticesList, GL11.GL_COMPILE);
        GL11.glBegin(GL11.GL_POLYGON);
        for (Point3d vertex : vertices) {
            GL11.glVertex3d(vertex.x, vertex.y, vertex.z);
        }
        GL11.glEnd();
        GL11.glEndList();

        normalList = GL11.glGenLists(1);

        GL11.glNewList(normalList, GL11.GL_COMPILE);
        GL11.glLineWidth(3.0f);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor3f(0.9f, 0.9f, 0.9f);
        GL11.glVertex3d(getLocation().x, getLocation().y, getLocation().z);
        GL11.glColor3f(0.7f, 0.7f, 0.7f);
        GL11.glVertex3d(getLocation().x + normal.x, getLocation().y + normal.y, getLocation().z + normal.z);
        GL11.glEnd();
        GL11.glLineWidth(1.0f);
        GL11.glEnable(GL11.GL_POINT_SMOOTH);
        GL11.glPointSize(8.0f);
        GL11.glBegin(GL11.GL_POINTS);
        GL11.glColor3f(0.9f, 0.9f, 0.9f);
        GL11.glVertex3d(getLocation().x, getLocation().y, getLocation().z);
        GL11.glEnd();
        GL11.glPointSize(1.0f);
        GL11.glDisable(GL11.GL_POINT_SMOOTH);
        GL11.glEndList();

        setValid(true);
    }

    public void setVertices(Point3d[] vertices) {
        this.vertices = vertices;
        setValid(false);
    }

    public void setNormal(Vector3d normal) {
        this.normal = normal.scale(0.2d);
        setValid(false);
    }

    public Point3d[] getVertices() {
        return vertices;
    }
}

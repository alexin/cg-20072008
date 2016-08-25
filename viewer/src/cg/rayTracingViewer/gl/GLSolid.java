package cg.rayTracingViewer.gl;

import org.lwjgl.opengl.GL11;
import rayTracing.scene.Polygon;

/**
 * @author Alexandre Vieira
 */
public class GLSolid extends GLObject {

    private GLPolygon[] faces;
    private int list;

    public GLSolid(String id) {
        super(id);
    }

    @Override
    public void render() {
        for (GLPolygon face : faces) {
            face.render();
        }

//        if (isValid()) {
//            GL11.glColor3f(1.0f, 0.0f, 0.0f);
//            GL11.glEnable(GL11.GL_POINT_SMOOTH);
//            GL11.glPointSize(8.0f);
//            GL11.glBegin(GL11.GL_POINTS);
//            GL11.glColor3f(0.9f, 0.9f, 0.9f);
//            GL11.glVertex3d(getLocation().x, getLocation().y, getLocation().z);
//            GL11.glEnd();
//            GL11.glPointSize(1.0f);
//            GL11.glDisable(GL11.GL_POINT_SMOOTH);
//            GL11.glEndList();
//        }
    }

    @Override
    public void update(long delta) {
        for (GLPolygon face : faces) {
            face.update(delta);
        }
    }

    @Override
    public void validate() {
        for (int index = 0; index < faces.length; index++) {
            this.faces[index].validate();
        }

        setValid(true);
    }

    public void setFaces(Polygon[] faces) {
        this.faces = new GLPolygon[faces.length];

        for (int index = 0; index < faces.length; index++) {
            this.faces[index] = new GLPolygon(faces[index].getId().toString());
            this.faces[index].setVertices(faces[index].getVertices());
            this.faces[index].setMaterial(new GLMaterial(faces[index].getMaterial().getColor()));
            this.faces[index].setNormal(faces[index].getNormal());
            this.faces[index].setValid(false);
        }

        setValid(false);
    }
}

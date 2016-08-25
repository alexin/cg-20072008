package cg.rayTracingViewer.gl;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import rayTracing.math.Color3d;
import rayTracing.math.Vector3d;

/**
 * @author Alexandre Vieira
 */
public class GLLight extends GLObject {

    private static Texture TEXTURE;
    private Vector3d xAxis;
    private Vector3d yAxis;
    private Vector3d zAxis;
    private FloatBuffer position;
    private Color3d color;
    private int gLId;
    private int list;

    public GLLight(String id) {
        super(id);

        this.position = BufferUtils.createFloatBuffer(4);
    }

    @Override
    public void render() {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
        TEXTURE.bind();
        GL11.glCallList(list);
        Texture.bindNone();
        GL11.glLight(gLId, GL11.GL_POSITION, position);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    @Override
    public void update(long delta) {
    }

    @Override
    public void validate() {
        if (TEXTURE == null) {
            TEXTURE = new Texture(GLPreview.getImage("light.png"));
        }

        position.rewind();
        position.put((float) getLocation().x);
        position.put((float) getLocation().y);
        position.put((float) getLocation().z);
        position.put(1.0f);
        position.flip();

        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);

        buffer.rewind();
        buffer.put((float) color.red);
        buffer.put((float) color.green);
        buffer.put((float) color.blue);
        buffer.put(1.0f);
        buffer.flip();

        GL11.glEnable(gLId);
        GL11.glLight(gLId, GL11.GL_AMBIENT, buffer);
        GL11.glLight(gLId, GL11.GL_DIFFUSE, buffer);
        GL11.glLight(gLId, GL11.GL_SPECULAR, buffer);
        GL11.glLightf(gLId, GL11.GL_CONSTANT_ATTENUATION, 1.0f);
        GL11.glLightf(gLId, GL11.GL_LINEAR_ATTENUATION, 0.01f);
        GL11.glLightf(gLId, GL11.GL_QUADRATIC_ATTENUATION, 0.0f);

        list = GL11.glGenLists(1);
        GL11.glNewList(list, GL11.GL_COMPILE);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(0.0d, 1.0d);
        GL11.glVertex3d(getLocation().x, getLocation().y, getLocation().z);
        GL11.glTexCoord2d(1.0d, 1.0d);
        GL11.glVertex3d(getLocation().x + 0.4d, getLocation().y, getLocation().z);
        GL11.glTexCoord2d(1.0d, 0.0d);
        GL11.glVertex3d(getLocation().x + 0.4d, getLocation().y + 0.4d, getLocation().z);
        GL11.glTexCoord2d(0.0d, 0.0d);
        GL11.glVertex3d(getLocation().x, getLocation().y + 0.4d, getLocation().z);
        GL11.glEnd();
        GL11.glEndList();

        setValid(true);
    }

    public void setColor(Color3d color) {
        this.color = color;
        setValid(false);
    }

    public void setGLId(int gLId) {
        this.gLId = gLId;
        setValid(false);
    }

    public Color3d getColor() {
        return color;
    }

    public int getGLId() {
        return gLId;
    }

    public void orient(FreeCamera camera) {
        zAxis = getLocation().minus(camera.getLocation());
        zAxis.setToNormal();
        xAxis = camera.getVAxis().cross(zAxis);
        xAxis.setToNormal();
        yAxis = zAxis.cross(xAxis);
        yAxis.setToNormal();
    }
}

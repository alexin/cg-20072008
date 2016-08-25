package cg.rayTracingViewer.gl;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import rayTracing.math.Point3d;
import rayTracing.math.Vector3d;

/**
 * @author Alexandre Vieira
 */
public class FreeCamera {

    private FloatBuffer modelViewMatrix;
    private Vector3d uAxis;
    private Vector3d vAxis;
    private Vector3d nAxis;
    private Point3d location;
    private float pitchAngle;
    private float yawAngle;
    private float rollAngle;
    private float velocity;
    private float fov;
    private float previousFOV;
    private float ratio;

    public FreeCamera(float ratio) {
        this.modelViewMatrix = BufferUtils.createFloatBuffer(16);
        this.uAxis = new Vector3d();
        this.vAxis = new Vector3d(0.0f, 1.0f, 0.0f);
        this.vAxis.setToNormal();
        this.nAxis = new Vector3d();
        this.location = new Point3d(0.0d, 0.0d, -5.0d);
        this.pitchAngle = 0.0f;
        this.yawAngle = 0.0f;
        this.rollAngle = 0.0f;
        this.velocity = 0.3f;
        setFov(90.0f);
        this.ratio = ratio;
    }

    public void setFov(float fov) {
        this.previousFOV = this.fov;
        this.fov = fov;
    }

    public Vector3d getNAxis() {
        return nAxis;
    }

    public Vector3d getUAxis() {
        return uAxis;
    }

    public Vector3d getVAxis() {
        return vAxis;
    }

    public Point3d getLocation() {
        return location;
    }

    public float getFov() {
        return fov;
    }

    public void refresh() {
        if (fov != previousFOV) {
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GLU.gluPerspective(fov, ratio, 0.1f, 1000.0f);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
        }

        GL11.glRotatef(yawAngle, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(pitchAngle, 1.0f, 0.0f, 0.0f);

        modelViewMatrix = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelViewMatrix);
        //modelViewMatrix.flip();
        uAxis.x = modelViewMatrix.get(8);
        uAxis.z = modelViewMatrix.get(10);

        GL11.glLoadIdentity();
        GL11.glRotatef(pitchAngle, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(rollAngle, 0.0f, 0.0f, 1.0f);

        modelViewMatrix = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelViewMatrix);
        //modelViewMatrix.flip();
        uAxis.y = modelViewMatrix.get(9);
        uAxis.setToNormal();
        nAxis = vAxis.cross(uAxis);
        nAxis.setToNormal();

        GL11.glRotatef(yawAngle, 0.0f, 1.0f, 0.0f);

        GL11.glTranslatef(-(float) location.x, -(float) location.y, (float) location.z);
    }

    public void addToPitch(float angles) {
        pitchAngle -= angles;

        if (pitchAngle > 360.0f) {
            pitchAngle -= 360.0f;
        } else if (pitchAngle < 0.0f) {
            pitchAngle += 360.0f;
        }
    }

    public void addToYaw(float angles) {
        yawAngle += angles;

        if (yawAngle > 360.0f) {
            yawAngle -= 360.0f;
        } else if (yawAngle < 0.0f) {
            yawAngle += 360.0f;
        }
    }

    public void addToRoll(float angles) {
        rollAngle += angles;

        if (rollAngle > 360.0f) {
            rollAngle -= 360.0f;
        } else if (rollAngle < 0.0f) {
            rollAngle += 360.0f;
        }
    }

    public void forward() {
        location.x += uAxis.x * velocity;
        location.y += uAxis.y * velocity;
        location.z += uAxis.z * velocity;
    }

    public void backward() {
        location.x -= uAxis.x * velocity;
        location.y -= uAxis.y * velocity;
        location.z -= uAxis.z * velocity;
    }

    public void strafeRight() {
        location.x += nAxis.x * velocity;
        location.y += nAxis.y * velocity;
        location.z += nAxis.z * velocity;
    }

    public void strafeLeft() {
        location.x -= nAxis.x * velocity;
        location.y -= nAxis.y * velocity;
        location.z -= nAxis.z * velocity;
    }

    public void up() {
        location.y += velocity;
    }

    public void down() {
        location.y -= velocity;
    }
}

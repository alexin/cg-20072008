package cg.rayTracingViewer.gl;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import rayTracing.math.Color3d;

/**
 * @author Alexandre Vieira
 */
public class GLMaterial {

    private Color3d color;
    private FloatBuffer diffuse;
    private FloatBuffer specular;

    public GLMaterial() {
        this(new Color3d(1.0d, 0.0d, 0.0d));
    }

    public GLMaterial(Color3d color) {
        this.diffuse = BufferUtils.createFloatBuffer(4);
        this.specular = BufferUtils.createFloatBuffer(4);

        setColor(color);
    }

    public void setColor(Color3d color) {
        this.color = color;

        diffuse.rewind();
        diffuse.put((float) color.red);
        diffuse.put((float) color.green);
        diffuse.put((float) color.blue);
        diffuse.put(1.0f);
        diffuse.flip();

        specular.rewind();
        specular.put((float) color.red);
        specular.put((float) color.green);
        specular.put((float) color.blue);
        specular.put(1.0f);
        specular.flip();
    }

    public Color3d getColor() {
        return color;
    }

    public FloatBuffer getDiffuse() {
        diffuse.rewind();

        return diffuse;
    }

    public FloatBuffer getSpecular() {
        specular.rewind();

        return specular;
    }
}

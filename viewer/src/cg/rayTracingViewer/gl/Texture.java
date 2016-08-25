package cg.rayTracingViewer.gl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Hashtable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * @author Alexandre Vieira
 */
public class Texture {

    private int id;

    public Texture(BufferedImage image) {
        IntBuffer buffer = BufferUtils.createIntBuffer(1);
        GL11.glGenTextures(buffer);
        id = buffer.get(0);
        bind();

        int format = image.getColorModel().hasAlpha() ? GL11.GL_RGBA : GL11.GL_RGB;

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,
                0,
                GL11.GL_RGBA,
                image.getWidth(),
                image.getHeight(),
                0,
                format,
                GL11.GL_UNSIGNED_BYTE,
                convertImageData(image));

        bindNone();
    }

    public void bind() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }

    public void release() {
        IntBuffer buffer = BufferUtils.createIntBuffer(1);
        buffer.put(id);
        buffer.flip();

        GL11.glDeleteTextures(buffer);
    }

    public static void bindNone() {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    private static ByteBuffer convertImageData(BufferedImage image) {
        ColorModel glAlphaColorModel = new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{8, 8, 8, 8},
                true, false, ComponentColorModel.BITMASK, DataBuffer.TYPE_BYTE);

        ColorModel glColorModel = new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{8, 8, 8, 0},
                false, false, ComponentColorModel.OPAQUE, DataBuffer.TYPE_BYTE);

        ByteBuffer imageBuffer = null;
        WritableRaster raster;
        BufferedImage texImage;

        if (image.getColorModel().hasAlpha()) {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                    image.getWidth(), image.getHeight(), 4, null);
            texImage = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
        } else {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                    image.getWidth(), image.getHeight(), 3, null);
            texImage = new BufferedImage(glColorModel, raster, false, new Hashtable());
        }

        Graphics g = texImage.getGraphics();
        g.setColor(new Color(0f, 0f, 0f, 0f));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.drawImage(image, 0, 0, null);

        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();

        imageBuffer = ByteBuffer.allocateDirect(data.length);
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(data, 0, data.length);
        imageBuffer.flip();

        return imageBuffer;
    }
}
